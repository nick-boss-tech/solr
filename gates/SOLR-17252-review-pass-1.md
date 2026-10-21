# Gate job: SOLR-17252 review pass 1 (unit 16, vm3)

Status: DONE (GATE GREEN at head `d730a266a0425b2609f4bcf21756555d3d3b8f52`, 2026-10-21T03:45:08Z; receipts/SOLR-17252.md)

- Branch: `solr-17252-submit`. Head: `d730a266a0425b2609f4bcf21756555d3d3b8f52` (ls-remote at claim time). Base: `14c7aac0d1`.
- Intended host: vm3. Capability tags: review, gate. No Gradle gate applies to this script-branch.
- Runner: out-of-repo harness `/workspace/gates/logs/vm3/solr-17252-review-pass-1/check.py`, run from an archive of `dev-tools/scripts` at the head (`/workspace/gates/logs/vm3/solr-17252-review-pass-1/head`) and base (`/workspace/gates/logs/vm3/solr-17252-review-pass-1/base`), with `uv run --with-requirements requirements.txt`.
- Result: head 14 checks, 0 failures (`harness-head.log`); base comparison, the nano and vim cases differ as recorded in the report (`harness-base.log`).
- Compile: `python3 -m py_compile` of the head `releaseWizard.py` rc 0.
