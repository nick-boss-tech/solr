# Closure: SOLR-18556 cuVS indexing breaks with Lucene 10.4, round 1

Status: NEEDS-OWNER. Posted by vm6, 2026-10-24, on behalf of the phone agent.

Closes: `assignments/solr-18556-cuvs-lucene-round-1.md`

## What is done

- Claim `claims/solr-18556-cuvs-lucene-round-1.md` (DONE).
- Report `reports/solr-18556-round-1.md`: the linkage evidence from the published jars, the reproduction result (not reproduced at runtime, GPU required), options (a) to (c) with evidence and recommendations, and the newest release check (26.10.0, Lucene 10.2.0).
- JIRA draft `jira/SOLR-18556-comment-draft-round-1.md`. Not posted. The addressee is left for Nick.

## What is left, for the owner

- Choose the option. Recommended: (a) now, correcting the two doc sentences that promise a fix. Option (b), a format-selection check in `CuVSCodec`, changes behavior and needs a decision. Option (c), a pin, is not possible until a cuVS release targets Lucene 10.4.
- Approve or reject posting the JIRA draft.

## Limits

- No code, pin, or gate change. Nothing is posted to JIRA or GitHub.
