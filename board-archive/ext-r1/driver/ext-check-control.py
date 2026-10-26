import sys, re
t = open(sys.argv[1], encoding="utf-8").read()
def block(label):
    m = re.search(r"=== SNAPSHOT " + re.escape(label) + r" .*?(?==== SNAPSHOT |\Z)", t, re.S)
    return m.group(0) if m else ""
b = block("terminal-immediate")
ok = ("closeOutcome: RETURNED" in b and "interruptBeforeClose: false" in t
      and "trackedInObjectReleaseTracker=false" in b and "rawSelectorIsOpen=false" in b
      and "rawSelectorIsOpen=true" not in b and "sentinel result: alive" in t)
print("CLEAN" if ok else "NOT-CLEAN")
