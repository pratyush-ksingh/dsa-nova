#!/usr/bin/env python3
"""
DSA Nova - Build Script for Windows .exe

Creates a standalone dsa-nova.exe that can be run without Python installed.
The exe is placed in the project root alongside the step_* folders.

Usage:
    python build_exe.py

After building:
    ./dsa-nova.exe          # Run the game
"""

import PyInstaller.__main__
import shutil
from pathlib import Path

PROJECT_ROOT = Path(__file__).resolve().parent
SCRIPTS_DIR = PROJECT_ROOT / "scripts"
DIST_DIR = PROJECT_ROOT / "dist"
BUILD_DIR = PROJECT_ROOT / "build"

# All script modules that play.py imports
HIDDEN_IMPORTS = [
    "brain",
    "shared",
    "revision",
    "batch_engine",
    "real_world",
]

# Build arguments
args = [
    str(SCRIPTS_DIR / "play.py"),       # Entry point
    "--onefile",                         # Single .exe
    "--console",                         # Terminal app (not windowed)
    "--name", "dsa-nova",               # Output name
    "--distpath", str(PROJECT_ROOT),    # Put exe in project root
    "--workpath", str(BUILD_DIR),       # Build temp files
    "--specpath", str(BUILD_DIR),       # Spec file location
    "--clean",                           # Clean cache
]

# Add scripts directory as data so imports resolve
args.extend(["--add-data", f"{SCRIPTS_DIR}{os.pathsep}scripts"])

# Hidden imports (PyInstaller can't detect dynamic imports)
for mod in HIDDEN_IMPORTS:
    args.extend(["--hidden-import", mod])

# Add paths so modules can be found
args.extend(["--paths", str(SCRIPTS_DIR)])

import os
print("Building DSA Nova .exe...")
print(f"  Entry point: {SCRIPTS_DIR / 'play.py'}")
print(f"  Output: {PROJECT_ROOT / 'dsa-nova.exe'}")
print()

PyInstaller.__main__.run(args)

# Clean up build artifacts
if BUILD_DIR.exists():
    shutil.rmtree(BUILD_DIR, ignore_errors=True)

print()
print("=" * 50)
print("  BUILD COMPLETE!")
print(f"  Executable: {PROJECT_ROOT / 'dsa-nova.exe'}")
print()
print("  To run: ./dsa-nova.exe")
print("  (Must be in the project folder with step_* dirs)")
print("=" * 50)
