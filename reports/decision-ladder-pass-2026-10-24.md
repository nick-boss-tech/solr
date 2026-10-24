# Decision ladder pass over the open owner items, 2026-10-24

By the phone agent, at the owner's direction ("check decision ladder first"). Method: `material/decision-ladder.md`, rungs in order; rung 5 reaches the owner only when the intent is ambiguous, the scope widens, or the decision commits the owner to something public. This file supersedes the "For the owner" section of `reports/vm4-workflow-questions-answers-round-1.md` and records the dispositions for items 1 and 4 of `reports/vm6-loose-ends-reply-round-1.md`.

## Settled inside the pool

- vm4 Q5 (batch head as gate evidence): YES, settled at rungs 3 and 4. A gate is defined by what ran at the exact head. A batch run at a batch head, with the per-ticket focused step recorded in that ticket's receipt, is the local gate on that head for each ticket in the batch. Each PR body names the batch head and its ticket's focused result, per pr-formula.md.
- vm4 Q7 (GL-free UI test route): settled at rungs 2 and 4 with the proposed default. The pool script (`material/ui-test-setup/run-ui-jvm-tests.sh`) is the default where it works; vm4's Mesa route is recorded in UI-PLAYBOOK.md as the fallback, because it is proven on vm4's host. Which route a host used goes in its receipt. No owner decision is needed to run tests.
- vm4 Q11 (Jira actions by vm4): NO, settled at rung 3 by the owner's standing written rule. No Jira comment, assignment, or state change by any host, ever, without the owner's go per text. This was not an owner question.
- SOLR-17861 LICENSE and NOTICE scope: option A, settled at rungs 1 and 4. The repo rule (`gradle/validation/jar-checks.gradle`) requires LICENSE and NOTICE for jars shipped in the binary distribution; the `ui` project ships none. The ticket closes on the checksum slice (`solr-17861-ui-license-check` at `92aed103a0`, receipt `receipts/SOLR-17861.md`); LICENSE and NOTICE presence belongs to the desktop distribution work in SOLR-17970.
  Closes: `assignments/phone-agent-17861-license-scope-and-handover-round-1.md` (the scope question it posed is answered: A).
  What remains is not a scope question: the closing note's text is drafted as factory draft work (see `assignments/jira-draft-priority-round-1.md`) and posting it needs the owner's go per text, as usual.
- SOLR-18556 option (c), pin a newer cuvs-lucene: CLOSED on evidence at rung 1. The newest release (26.10.0) is built against Lucene 10.2.0 and still calls the removed `getSortedNodes`; no release with a Lucene 10.4 POM exists. Nothing to pin.
- SOLR-18556 option (a), document now: settled in substance at rungs 1 and 4. The two doc sentences promise a fix "in a subsequent Solr 10.x release" that no artifact supports; correcting them to say no fix is scheduled is the only option with no behavior change, and the docs are the part that is wrong today. Preparing that change is factory work; opening its PR needs the owner's go at opening time, as usual.

## Surviving to the owner (rung 5, one question each)

- SOLR-18556 option (b): should a format-selection guard be built in `CuVSCodec` so that 10.1 indexing with `knnAlgorithm="cagra_hnsw"` falls back to CPU HNSW with one warning, instead of failing with `NoSuchMethodError`, until NVIDIA ships a cuvs-lucene built against Lucene 10.4? This is a public behavior change (a silent drop from GPU to CPU indexing), so it is the owner's call. vm6's recommendation is (a) only. The closure file `assignments/solr-18556-closure-round-1.md` stays NEEDS-OWNER with this as its one question; approval of the existing Jira draft (`jira/SOLR-18556-comment-draft-round-1.md`) is a separate per-text go.
- vm4 Q10 (which UI PR opens first): an owner decision by the standing rule that every opening needs his explicit approval, but it is decidable only at opening time, with one named candidate and its receipt in hand. It does not sit as a standing question. When the first UI candidate's receipt lands, the phone agent brings that one opening to the owner.

## Note on the earlier answers file

`reports/vm4-workflow-questions-answers-round-1.md` listed Q5, Q7, Q10, and Q11 as owner questions. Under the ladder, Q5, Q7, and Q11 never reached rung 5, and Q10 reaches it only at opening time. Escalating a ladder-settled question is the defect the escalation test exists to catch; the correction is recorded here rather than by editing that file.
