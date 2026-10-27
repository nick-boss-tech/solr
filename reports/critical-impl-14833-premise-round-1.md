# SOLR-14833 premise run, round 1 (vm2)

Verdict: NOT REPRODUCED under this probe; item kept OPEN. The reporter's analyzer/index config is missing, so this does not refute the ticket. No implementation, no branch, no gate request.

- Claim: `claims/critical-impl-14833-round-1-vm2.md`. Assignment: `assignments/critical-impl-14833-round-1.md` (step 1 only).
- Base: apache/solr `upstream/main` `ee2f1bf2e01`. Scratch probe `Probe14833Test` and a scratch schema edit (stored `text_hl` field) in the probe worktree; neither is pushed. Seed `14833C0DE`. Logs `gates/logs/vm2/s14833/`.
- Ticket request reproduced: `q=text_hl:("ما جرى بين الصحابة" الماتريدي)`, `hl.fl=text_hl`, `hl.q=text_hl:(...)`, `hl.fragmenter=regex` (and default for comparison), `f.text_hl.hl.fragsize=110`, `rows=20`.
- Document 1 is the reporter's Arabic passage (first lines of the ticket's example text), a stored field. Document 2 is an unrelated Arabic sentence.

## Result (valid run, 11:54:50Z, probe rc 0)

- regex: doc 1 highlight `text_hl` = passage with `<em>ما جرى بين الصحابة</em>` (non-empty). Doc 2 has no entry.
- default: the same entry, identical.

## Reading and limits

- Premise (empty entry on match, regex fragmenter): not reproduced on this passage with this schema.
- The probe used a standard tokenizer with lowercase, stop and Porter stemming (`text` type of the test schema), not the reporter's Arabic analyzer. The ticket mentions "some queries" and "some other queries", so the defect may need another analyzer, the full document, or other query terms. None are given.
- Missing to close this: the reporter's schema field type for `text_highlighting`, the stored text size, and the list of failing queries.

## Not done

- No step 2 implementation, no branch `solr-14833-submit`, no gate request.
- No PR, Jira, or GitHub write.
