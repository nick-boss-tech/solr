#!/usr/bin/env python3
"""Descriptor inventory of the HTTP client family for a set of Solr jars.

usage: python3 -I inventory.py <out.tsv> <javap> <cp-extra.txt> <label>=<jar>[,<jar>...] ...

Emits one row per class header, super/interface line, and public/protected member
(name, descriptor, flags). Bridge and synthetic members keep their flags so they can be
compared. Rows are sorted for stable diffs.
"""
import os
import re
import subprocess
import sys
import zipfile

FAMILY = re.compile(
    r"^org/apache/solr/client/solrj/("
    r"SolrRequest(\$.*)?|"
    r"impl/(HttpSolrClient|HttpSolrClientBase|HttpSolrClientBuilderBase|HttpJdkSolrClient|"
    r"CloudHttp2SolrClient|CloudSolrClient|LBSolrClient|LBAsyncSolrClient|LBHttp2SolrClient|"
    r"HttpClusterStateProvider|ConcurrentUpdate\w*|HttpSolrClientBase)(\$.*)?|"
    r"jetty/Http\w*(\$.*)?)\.class$"
)
HEADER = re.compile(r"^(public|protected|private|abstract|final|static|class|interface|@)")
MEMBER = re.compile(r"^  \S.*;$")


def classes_in(jar):
    with zipfile.ZipFile(jar) as z:
        return sorted(n[:-6] for n in z.namelist() if FAMILY.match(n) and "Test" not in n)


def javap_v(javap, cp, fqcn):
    out = subprocess.run(
        [javap, "-protected", "-s", "-v", "-cp", cp, fqcn.replace("/", ".")],
        capture_output=True, text=True,
    )
    if out.returncode != 0:
        raise SystemExit(f"javap failed for {fqcn}: {out.stderr.strip()}")
    return out.stdout.splitlines()


def parse(lines, label, fqcn):
    rows = []
    cls = fqcn.replace("/", ".")
    header_seen = False
    in_body = False
    current = None
    for line in lines:
        if line == "{":
            in_body = True
            continue
        if line == "}":
            break
        if not in_body:
            if not header_seen and not line.startswith(" ") and re.search(r"\b(class|interface) ", line):
                rows.append([label, cls, "header", line.strip(), "", ""])
                header_seen = True
            if line.startswith("  super_class:"):
                rows.append([label, cls, "super", line.strip(), "", ""])
            continue
        if line.startswith("  ") and not line.startswith("   ") and line.rstrip().endswith(";"):
            current = len(rows)
            rows.append([label, cls, "member", line.strip()[:-1], "", ""])
            continue
        if line.startswith("  ") and not line.startswith("   "):
            current = None
            continue
        if current is not None and line.startswith("    descriptor:"):
            rows[current][4] = line.split(":", 1)[1].strip()
        elif current is not None and line.startswith("    flags:"):
            rows[current][5] = line.split(":", 1)[1].strip()
    return [tuple(r) for r in rows]


def main():
    out_path, javap, cp_extra = sys.argv[1], sys.argv[2], sys.argv[3]
    with open(cp_extra) as f:
        extra = [l.strip() for l in f if l.strip()]
    all_rows = []
    for spec in sys.argv[4:]:
        label, jars = spec.split("=", 1)
        jars = jars.split(",")
        cp = os.pathsep.join(jars + extra)
        fqcns = []
        for jar in jars:
            fqcns += [(jar, c) for c in classes_in(jar)]
        for jar, fqcn in fqcns:
            if not any(os.path.basename(jar).startswith(p) for p in ("solr-solrj", "solr-api")):
                continue
            all_rows += parse(javap_v(javap, cp, fqcn), label, fqcn)
    all_rows = [tuple(r) + ("",) * (6 - len(r)) for r in all_rows]
    with open(out_path, "w") as f:
        f.write("label\tclass\tkind\tsignature\tdescriptor\tflags\n")
        for r in sorted(all_rows, key=lambda r: (r[0], r[1], r[2], r[3])):
            f.write("\t".join(r[:6]) + "\n")
    print(f"wrote {len(all_rows)} rows to {out_path}")


if __name__ == "__main__":
    main()
