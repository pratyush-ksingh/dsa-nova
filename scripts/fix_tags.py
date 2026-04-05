#!/usr/bin/env python3
"""
One-time migration: Add tags to all Striver (P-prefixed) problems based on folder structure.
Run: python scripts/fix_tags.py
"""
import json
from pathlib import Path
from shared import infer_tags

BASE_DIR = Path(__file__).resolve().parent.parent


def main():
    updated = 0
    skipped = 0
    failed = 0

    for meta_path in sorted(BASE_DIR.rglob("meta.json")):
        if "templates" in str(meta_path):
            continue
        try:
            with open(meta_path, "r", encoding="utf-8") as f:
                meta = json.load(f)

            # Only update problems with empty tags OR wrong tags from substring bug
            correct_tags = infer_tags(str(meta_path))
            if meta.get("tags") and len(meta["tags"]) > 0 and meta["tags"] == correct_tags:
                skipped += 1
                continue

            tags = infer_tags(str(meta_path))
            if tags:
                meta["tags"] = tags
                with open(meta_path, "w", encoding="utf-8") as f:
                    json.dump(meta, f, indent=2)
                updated += 1
            else:
                failed += 1
        except (json.JSONDecodeError, KeyError):
            failed += 1
            continue

    print(f"Tags added: {updated} problems")
    print(f"Skipped (already had tags): {skipped}")
    if failed:
        print(f"Failed/no match: {failed}")


if __name__ == "__main__":
    main()
