"""Class-file linkage audit without loading code or initializing native libraries.

Checks every Class/Fieldref/Methodref/InterfaceMethodref constant, descriptor types,
and bytecode/method-handle staticness expectations. Resolves inherited members.
The JSON preserves every checked constant and bytecode use, including failures.
"""
from pathlib import Path
import argparse
import collections
import json
import re
import struct
import zipfile


class Reader:
    def __init__(self, data):
        self.data, self.pos = data, 0

    def read(self, n):
        out = self.data[self.pos:self.pos+n]
        self.pos += n
        assert len(out) == n
        return out

    def u1(self):
        return self.read(1)[0]

    def u2(self):
        return struct.unpack(">H", self.read(2))[0]

    def u4(self):
        return struct.unpack(">I", self.read(4))[0]


def parse(data):
    r = Reader(data)
    assert r.u4() == 0xCAFEBABE
    minor, major, count = r.u2(), r.u2(), r.u2()
    cp = [None] * count
    i = 1
    while i < count:
        tag = r.u1()
        if tag == 1:
            # JVM modified UTF-8 differs for NUL and surrogate pairs. Symbolic
            # linkage names/descriptors here are ASCII; preserve other data safely.
            val = r.read(r.u2()).decode("utf-8", errors="replace")
        elif tag in (3, 4):
            val = r.read(4).hex()
        elif tag in (5, 6):
            val = r.read(8).hex()
        elif tag in (7, 8, 16, 19, 20):
            val = r.u2()
        elif tag in (9, 10, 11, 12, 17, 18):
            val = (r.u2(), r.u2())
        elif tag == 15:
            val = (r.u1(), r.u2())
        else:
            raise ValueError("Unknown CP tag " + str(tag))
        cp[i] = (tag, val)
        i += 2 if tag in (5, 6) else 1

    def utf(n):
        return cp[n][1]

    def cls(n):
        return utf(cp[n][1]) if n else None

    def attrs():
        out = []
        for _ in range(r.u2()):
            name = utf(r.u2())
            out.append((name, r.read(r.u4())))
        return out

    access, name, parent = r.u2(), cls(r.u2()), cls(r.u2())
    interfaces = [cls(r.u2()) for _ in range(r.u2())]
    fields, methods, codes = {}, {}, []
    for is_method, target in ((False, fields), (True, methods)):
        for _ in range(r.u2()):
            flags, member, desc = r.u2(), utf(r.u2()), utf(r.u2())
            target[(member, desc)] = flags
            for attr, content in attrs():
                if is_method and attr == "Code":
                    cr = Reader(content)
                    cr.u2()
                    cr.u2()
                    code = cr.read(cr.u4())
                    codes.append((member + desc, code))
    class_attrs = attrs()
    assert r.pos == len(data)
    return {"name": name, "parent": parent, "interfaces": interfaces,
            "access": access, "fields": fields, "methods": methods,
            "cp": cp, "codes": codes, "major": major, "attributes": class_attrs}


def bytecode_uses(code):
    lengths = {0x10: 2, 0x11: 3, 0x12: 2, 0x13: 3, 0x14: 3,
               0x84: 3, 0xa9: 2, 0xb9: 5, 0xba: 5, 0xbb: 3,
               0xbc: 2, 0xbd: 3, 0xc0: 3, 0xc1: 3, 0xc5: 4,
               0xc6: 3, 0xc7: 3, 0xc8: 5, 0xc9: 5}
    lengths.update({x: 2 for x in (*range(0x15, 0x1a), *range(0x36, 0x3b))})
    lengths.update({x: 3 for x in (*range(0x99, 0xa9), *range(0xb2, 0xb9))})
    p = 0
    while p < len(code):
        op = code[p]
        if 0xb2 <= op <= 0xb9:
            ref = struct.unpack_from(">H", code, p+1)[0]
            yield ref, op in (0xb2, 0xb3, 0xb8), p, hex(op)
        if op in (0xaa, 0xab):
            aligned = (p+4) & ~3
            if op == 0xaa:
                low, high = struct.unpack_from(">ii", code, aligned+4)
                length = aligned - p + 12 + (high-low+1)*4
            else:
                pairs = struct.unpack_from(">i", code, aligned+4)[0]
                length = aligned - p + 8 + pairs*8
        elif op == 0xc4:
            length = 6 if code[p+1] == 0x84 else 4
        else:
            length = lengths.get(op, 1)
        p += length
    assert p == len(code)


class Repository:
    def __init__(self, paths):
        self.raw, self.cache = {}, {}
        for p in paths:
            with zipfile.ZipFile(p) as z:
                for n in z.namelist():
                    if n.endswith(".class") and not n.startswith("META-INF/versions/"):
                        key = n.removeprefix("classes/").removesuffix(".class")
                        self.raw.setdefault(key, z.read(n))

    def get(self, name):
        if name not in self.cache:
            self.cache[name] = parse(self.raw[name]) if name in self.raw else None
        return self.cache[name]

    def subclass(self, child, parent, seen=None):
        if child == parent:
            return True
        seen = set() if seen is None else seen
        if child in seen:
            return False
        seen.add(child)
        c = self.get(child)
        return bool(c and any(self.subclass(p, parent, seen) for p in
                             [c["parent"], *c["interfaces"]] if p))

    def resolve(self, owner, name, desc, field, seen=None):
        seen = set() if seen is None else seen
        if owner in seen:
            return None
        seen.add(owner)
        c = self.get(owner)
        if not c:
            return None
        members = c["fields" if field else "methods"]
        if (name, desc) in members:
            return owner, members[(name, desc)]
        if name in ("<init>", "<clinit>"):
            return None
        # Fields search direct superinterfaces before superclass (JVMS 5.4.3.2).
        # Methods search superclass before superinterfaces (JVMS 5.4.3.3).
        parents = ([*c["interfaces"], c["parent"]] if field else
                   [c["parent"], *c["interfaces"]])
        if not field and c["access"] & 0x0200:
            parents = ["java/lang/Object", *c["interfaces"]]
        for parent in filter(None, parents):
            found = self.resolve(parent, name, desc, field, seen)
            if found:
                # Static interface methods are not inherited.
                decl = self.get(found[0])
                if not field and decl["access"] & 0x0200 and found[1] & 0x0008:
                    continue
                return found
        return None


def descriptor_classes(text):
    return re.findall(r"L(org/apache/lucene/[^;<>:]+)", text)


def audit(jar, targets, jdk):
    repo = Repository([*targets, jar, jdk / "jmods/java.base.jmod"])
    records, failures = [], []
    class_count = 0
    with zipfile.ZipFile(jar) as z:
        for file in z.namelist():
            if not file.endswith(".class"):
                continue
            c = parse(z.read(file))
            class_count += 1
            cp = c["cp"]
            uses = collections.defaultdict(list)
            for method, code in c["codes"]:
                for index, static, offset, opcode in bytecode_uses(code):
                    uses[index].append({"method": method, "offset": offset,
                                        "opcode": opcode, "static": static})
            for index, entry in enumerate(cp):
                if entry and entry[0] == 15:
                    kind, ref = entry[1]
                    uses[ref].append({"method_handle": index, "kind": kind,
                                      "static": kind in (2, 4, 6)})
            types = set()
            for _, descriptor in (*c["fields"].keys(), *c["methods"].keys()):
                types.update(descriptor_classes(descriptor))
            for index, entry in enumerate(cp):
                if not entry:
                    continue
                tag, value = entry
                if tag == 7:
                    name = cp[value][1]
                    if name.startswith("org/apache/lucene/"):
                        types.add(name)
                    elif name.startswith("["):
                        types.update(descriptor_classes(name))
                elif tag in (12, 16):
                    desc = cp[value[1]][1] if tag == 12 else cp[value][1]
                    types.update(descriptor_classes(desc))
                if tag not in (9, 10, 11):
                    continue
                owner = cp[cp[value[0]][1]][1]
                nt = cp[value[1]][1]
                name, desc = cp[nt[0]][1], cp[nt[1]][1]
                types.update(descriptor_classes(desc))
                if not owner.startswith("org/apache/lucene/"):
                    continue
                record = {"caller": c["name"], "cp_index": index,
                          "kind": {9: "Fieldref", 10: "Methodref", 11: "InterfaceMethodref"}[tag],
                          "owner": owner, "name": name, "descriptor": desc,
                          "uses": uses[index], "problems": []}
                target = repo.get(owner)
                resolved = repo.resolve(owner, name, desc, tag == 9)
                if not target:
                    record["problems"].append("missing owner class")
                elif tag in (10, 11) and bool(target["access"] & 0x0200) != (tag == 11):
                    record["problems"].append("class/interface reference-kind mismatch")
                if not resolved:
                    record["problems"].append("missing exact member (name + descriptor)")
                else:
                    declared, flags = resolved
                    record.update({"declared_by": declared, "target_flags": flags,
                                   "target_static": bool(flags & 0x0008)})
                    for use in uses[index]:
                        if use["static"] != bool(flags & 0x0008):
                            record["problems"].append("static/instance mismatch")
                    caller_package = c["name"].rpartition("/")[0]
                    target_package = declared.rpartition("/")[0]
                    if not flags & 0x0001:
                        accessible = (c["name"] == declared or caller_package == target_package or
                                      (flags & 0x0004 and repo.subclass(c["name"], declared)))
                        if not accessible:
                            record["problems"].append("member access mismatch")
                records.append(record)
                if record["problems"]:
                    failures.append(record)
            for name in sorted(types):
                target = repo.get(name)
                record = {"caller": c["name"], "kind": "ClassOrDescriptor",
                          "owner": name, "problems": []}
                if not target:
                    record["problems"].append("missing class")
                elif not target["access"] & 0x0001 and name.rpartition("/")[0] != c["name"].rpartition("/")[0]:
                    record["problems"].append("class access mismatch")
                records.append(record)
                if record["problems"]:
                    failures.append(record)
    unique_failures = sorted(set((r["kind"], r["owner"], r.get("name", ""),
                                  r.get("descriptor", ""), "; ".join(r["problems"])) for r in failures))
    return {"jar": str(jar), "targets": list(map(str, targets)), "class_count": class_count,
            "checked_constants_and_types": len(records),
            "member_constants": sum(r["kind"] != "ClassOrDescriptor" for r in records),
            "member_uses": sum(len(r.get("uses", [])) for r in records),
            "failure_records": len(failures), "unique_failures": unique_failures,
            "records": records}


def main():
    parser = argparse.ArgumentParser()
    parser.add_argument("jar", type=Path)
    parser.add_argument("output", type=Path)
    parser.add_argument("--lucene", default="10.4.0")
    args = parser.parse_args()
    root = Path(__file__).resolve().parent
    targets = [root / "jars" / f"lucene-{a}-{args.lucene}.jar"
               for a in ("core", "backward-codecs", "codecs")]
    result = audit(args.jar, targets, root / "toolchain/jdk-22.0.2+9")
    args.output.write_text(json.dumps(result, indent=2) + "\n", encoding="utf-8")
    print(json.dumps({k: v for k, v in result.items() if k != "records"}, indent=2))


if __name__ == "__main__":
    main()
