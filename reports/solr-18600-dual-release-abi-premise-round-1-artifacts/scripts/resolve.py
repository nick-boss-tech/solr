import re, sys, csv
def strip_generics(t):
    prev = None
    while prev != t:
        prev, t = t, re.sub(r'<[^<>]*>', '', t)
    return t
def load(path):
    members, parents, header = {}, {}, {}
    for line in open(path):
        f = line.rstrip('\n').split('\t')
        if len(f) < 6 or f[0] == 'label': continue
        label, cls, kind, sig, desc, flags = f[:6]
        if kind == 'header':
            t = strip_generics(sig)
            m = re.search(r'\bextends ([\w.$]+)', t)
            ifs = re.search(r'\bimplements ([\w.$, ]+)', t)
            sup = m.group(1) if m else None
            if re.search(r'\binterface ', t): sup = None
            parents[cls] = ([sup] if sup else []) + ([x.strip() for x in ifs.group(1).split(',')] if ifs else [])
        elif kind == 'member':
            name = sig.split('(')[0].split()[-1] if '(' in sig else sig.split()[-1]
            members.setdefault(cls, {})[(name, desc)] = (sig, flags)
    return members, parents
def resolvable(cls, key, members, parents, seen=None):
    seen = seen or set()
    if cls in seen: return None
    seen.add(cls)
    if key in members.get(cls, {}): return members[cls][key]
    for p in parents.get(cls, []):
        r = resolvable(p, key, members, parents, seen)
        if r: return r
    return None
rel_path, proto_path, out = sys.argv[1], sys.argv[2], sys.argv[3]
rel_m, rel_p = load(rel_path)
pro_m, pro_p = load(proto_path)
missing = []
ok = 0
for cls, mems in rel_m.items():
    for key, (sig, flags) in mems.items():
        if key[1] is None or key[1] == '': continue
        if resolvable(cls, key, pro_m, pro_p):
            ok += 1
        else:
            missing.append((cls, sig, key[1], flags))
with open(out, 'w') as f:
    for m in sorted(missing): f.write('\t'.join(m) + '\n')
print('resolvable', ok, 'NOT resolvable', len(missing))
from collections import Counter
for c, n in Counter(m[0].split('.')[-1] for m in missing).most_common(): print(' ', n, c)
