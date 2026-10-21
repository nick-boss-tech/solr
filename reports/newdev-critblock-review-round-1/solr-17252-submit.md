# Review round 1, unit 16: SOLR-17252 (branch solr-17252-submit), vm3

## Pass 1 (vm3, 2026-10-21T03:45:08Z)

- Head reviewed: `d730a266a0425b2609f4bcf21756555d3d3b8f52`. Base `14c7aac0d1`. Diff: `dev-tools/scripts/releaseWizard.py` (+17/-2), `dev-tools/scripts/README.md`, `dev-docs/releasing.adoc`. Two commits: f6a0b44d8c (fail fast), d730a266a0 (match by command name).
- Ticket SOLR-17252 (Open, Critical): invoking nano from the release wizard destroys the terminal. The branch makes `get_editor` exit with a message when `EDITOR` names a terminal-attached editor, matched by command basename after `shlex` split. Owner positions Q1 to Q3 (receipts/SOLR-17252.md) are settled context and are not reopened here.
- Surrounding code check: the wizard already imports `shlex` and `os`; the module byte-compiles. The docs (README and releasing.adoc) say terminal-attached editors are not supported, which matches the code. The eager call from `expand_jinja` (receipt finding 2) is the owner's Q3 position and is not reopened.
- Behaviour, base versus head (out-of-repo harness, same cases): `EDITOR=nano`, base returns `nano` with a warning, head exits. `EDITOR=/usr/bin/vim`, base returns it silently, head exits (the basename match). `EDITOR="code --wait"`, both return it. Unset on Linux, both exit with the same message.
- Finding, minor (recorded, not repaired): `emacs` is rejected even when a GUI emacs is what the user wants. This is the owner's Q2 position (emacs stays in the list); recorded for the owner, not a repair.
- Repairs in pass 1: none.
- Regate: `gates/SOLR-17252-review-pass-1.md`, GREEN at `d730a266a0425b2609f4bcf21756555d3d3b8f52`. Harness `/workspace/gates/logs/vm3/solr-17252-review-pass-1/check.py` run on the head archive (14 checks pass, 0 fail, log `/workspace/gates/logs/vm3/solr-17252-review-pass-1/harness-head.log`) and on the base archive for comparison (`/workspace/gates/logs/vm3/solr-17252-review-pass-1/harness-base.log`). The receipt's 21-check harness is not in the repo and is not on this host; this harness is a subset of the same behaviour and is not a replacement for it.

Pass 1 result: GREEN at `d730a266a0425b2609f4bcf21756555d3d3b8f52`.

## Pass 2 (vm3, 2026-10-21T03:45:21Z)

- Head reviewed: `d730a266a0425b2609f4bcf21756555d3d3b8f52` (unchanged). Base `14c7aac0d1`.
- Review focus: the behaviour the tests pin. The harness cases cover the basename match (`/usr/bin/vim`), shlex quoting (`'nano' -x`), an empty and a whitespace-only `EDITOR`, and `code --wait` (must pass through).
- Finding: none new. The fail-fast message and the docs say the same thing.
- Repairs in pass 2: none.
- Regate: GREEN at `d730a266a0425b2609f4bcf21756555d3d3b8f52`. Harness rerun, 14 pass, 0 fail (`/workspace/gates/logs/vm3/solr-17252-review-pass-1/harness-head-pass2.log`).

## Pass 3 (vm3, 2026-10-21T03:45:21Z)

- Head reviewed: `d730a266a0425b2609f4bcf21756555d3d3b8f52` (unchanged). Base `14c7aac0d1`.
- Review: the owner's Q1 to Q3 positions are unchanged; no new findings beyond the pass 1 notes (emacs under Q2; eager validation under Q3). Both are for the owner.
- Repairs in pass 3: none.
- Regate: GREEN at `d730a266a0425b2609f4bcf21756555d3d3b8f52`. Harness rerun, 14 pass, 0 fail (`/workspace/gates/logs/vm3/solr-17252-review-pass-1/harness-head-pass3.log`).

Pass 3 result: GREEN at `d730a266a0425b2609f4bcf21756555d3d3b8f52`. Unit 16 is complete after pass 3, with two owner questions recorded (Q2 emacs, Q3 eager check).
