#!/usr/bin/env python3
"""Refuse Gradle at <=15% free inodes. Never delete files or start a build."""
import os
from pathlib import Path
import sys

REFUSED = 4


def check_paths(paths):
    for raw in paths:
        path = Path(raw).absolute()
        # A not-yet-created Gradle cache uses its nearest existing filesystem.
        while not path.exists() and path != path.parent:
            path = path.parent
        try:
            stat = os.statvfs(path)
            total, free = stat.f_files, stat.f_favail
            if total <= 0 or free < 0 or free > total:
                raise ValueError("inode counters unavailable or invalid")
        except (OSError, ValueError) as error:
            print(f"inode guard: cannot check {raw}: {error}; Gradle not started; no files deleted", file=sys.stderr)
            return REFUSED
        if free * 100 <= total * 15:
            print(f"inode guard: {raw}: {free}/{total} free inodes ({free * 100 / total:.2f}%) <= 15%; Gradle not started; no files deleted", file=sys.stderr)
            return REFUSED
    return 0


if __name__ == "__main__":
    sys.exit(check_paths(sys.argv[1:] or ["/"]))
