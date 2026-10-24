"""Executed adversarial fixtures for the audit's descriptor and opcode checks."""
from pathlib import Path
import json
import subprocess
import zipfile
from linkage_audit import audit

ROOT = Path(__file__).resolve().parent
JDK = ROOT / "toolchain/jdk-22.0.2+9"
FIXTURES = ROOT / "audit-fixtures"
BASE = "org/apache/lucene/audit/"


def write(version, name, text):
    target = FIXTURES / version / "src" / name
    target.parent.mkdir(parents=True, exist_ok=True)
    target.write_text(text, encoding="utf-8")
    return target


def compile_to_jar(version, files, classpath=None):
    out = FIXTURES / version / "classes"
    out.mkdir(parents=True, exist_ok=True)
    command = [str(JDK / "bin/javac.exe"), "--release", "22", "-d", str(out)]
    if classpath:
        command += ["-cp", str(classpath)]
    result = subprocess.run(command + list(map(str, files)), capture_output=True, text=True)
    assert result.returncode == 0, result.stderr
    jar = FIXTURES / (version + ".jar")
    with zipfile.ZipFile(jar, "w") as z:
        for p in out.rglob("*.class"):
            z.write(p, p.relative_to(out).as_posix())
    return jar


def main():
    v1 = compile_to_jar("v1", [
        write("v1", BASE + "Base.java", """package org.apache.lucene.audit;
public class Base {
  public static String staticMethod(int n) { return ""; }
  public String signature(int n) { return ""; }
  public static int staticField;
  public int instanceField;
  public void inherited() {}
} """),
        write("v1", BASE + "Api.java", "package org.apache.lucene.audit; public class Api extends Base {}"),
        write("v1", BASE + "Shape.java", "package org.apache.lucene.audit; public class Shape { public void call() {} }"),
        write("v1", BASE + "Missing.java", "package org.apache.lucene.audit; public class Missing {}"),
    ])
    client = compile_to_jar("client", [write("client", "AuditClient.java", """
import org.apache.lucene.audit.*;
public class AuditClient {
  public Missing[] descriptorOnly;
  public void run(Api a, Shape shape) {
    String first = a.signature(1);
    String second = Api.staticMethod(2);
    int x = Api.staticField;
    int y = a.instanceField;
    a.inherited();
    shape.call();
    java.util.function.IntFunction<String> ref = Api::staticMethod;
    switch (x) { case 1: case 2: case 3: a.inherited(); break; default: break; }
    switch (y) { case 1: case 1000: a.inherited(); break; default: break; }
  }
} """)], v1)
    v2 = compile_to_jar("v2", [
        write("v2", BASE + "Base.java", """package org.apache.lucene.audit;
public class Base {
  public String staticMethod(int n) { return ""; }
  public long signature(int n) { return 0; }
  public int staticField;
  public static int instanceField;
} """),
        write("v2", BASE + "Api.java", "package org.apache.lucene.audit; public class Api extends Base {}"),
        write("v2", BASE + "Shape.java", "package org.apache.lucene.audit; public interface Shape { void call(); }"),
    ])
    good = audit(client, [v1], JDK)
    bad = audit(client, [v2], JDK)
    assert good["failure_records"] == 0, good["unique_failures"]
    issues = {(r["owner"].rsplit("/", 1)[-1], r.get("name", "")):
              r["problems"] for r in bad["records"] if r["problems"]}
    for name in ("staticMethod", "staticField", "instanceField"):
        assert "static/instance mismatch" in issues[("Api", name)]
    assert "missing exact member (name + descriptor)" in issues[("Api", "signature")]
    assert "missing exact member (name + descriptor)" in issues[("Api", "inherited")]
    assert "class/interface reference-kind mismatch" in issues[("Shape", "call")]
    assert "missing class" in issues[("Missing", "")]
    handles = [r for r in bad["records"] if r.get("name") == "staticMethod"]
    assert any("method_handle" in u for r in handles for u in r["uses"])
    result = {"status": "PASS", "positive_baseline_failures": good["failure_records"],
              "detected": bad["unique_failures"],
              "covers": ["inherited member resolution", "return-descriptor mismatch",
                         "static-to-instance method", "static-to-instance field",
                         "instance-to-static field", "removed inherited method",
                         "class-to-interface change", "descriptor-only missing class",
                         "method handles", "tableswitch and lookupswitch bytecode"]}
    (ROOT / "results/audit-selfcheck.json").write_text(json.dumps(result, indent=2) + "\n")
    print(json.dumps(result, indent=2))


if __name__ == "__main__":
    main()
