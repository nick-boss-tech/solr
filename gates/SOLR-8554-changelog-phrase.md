# Gate job: SOLR-8554, changelog phrase and top-up gate

- Branch: solr-8554-submit. Head: 654c1a7c3570b66e66c446b433e6cb8a16c637be (fork tip, verified by ls-remote 2026-10-14; matches receipts/SOLR-8554.md).
- Status: OPEN, unclaimed. Posted 2026-10-14 by the Windows review coordinator from the census follow-up core admin round 1 (assignments/pool-census-followup-core-admin-round-1.md). No code was run on this host.
- Intended host: any capable Linux host, under the WORKFLOW.md claim rules. Capability tags: gate.
- Why: round 1 finding 3 (reports/core-admin-round-1-k5.md) holds. The changelog title still says "(it used to throw a NullPointerException)". On the base commit cabedd1d968059215188f4e7563fb303241899ed, ForceLeader.java catches the NullPointerException in its generic catch (lines 163 to 170) and rethrows it wrapped as "Error executing FORCELEADER operation". The phrase has not landed at the live tip.
- Change: one changelog edit, in changelog/unreleased/SOLR-8554-forceleader-wait-failure.yml. Replace "(it used to throw a NullPointerException)" with "(it used to fail with a generic error that wrapped a NullPointerException)". Nothing else changes. No production or test file changes.
- Steps, in order:
  1. Make the one-line changelog edit as a single commit on solr-8554-submit. Push as a fast-forward from 654c1a7c3570. Verify with ls-remote and record the new head here.
  2. Changelog YAML parse.
  3. Tidy, then confirm the tree is clean.
  4. Error Prone compile of :solr:core:compileTestJava.
  5. Focused tests: org.apache.solr.handler.admin.api.ForceLeaderWaitTest in :solr:core, expected 3 run, 0 failures, counted from fresh JUnit XML after cleanTest.
  6. Module check :solr:core:check -x test.
- Proof: no proof leg is rerun. The production code and the test are unchanged, so the compile-level proof in receipts/SOLR-8554.md still stands for the code. The receipt update must say that the top-up ran at the new head and that the proof is carried from 654c1a7c3570.
- On green: the gating host refreshes receipts/SOLR-8554.md at the new head, marks this job DONE, and records both SHAs (old 654c1a7c3570, new head).
- Draft effect: the head citations in pr-drafts/core-admin/SOLR-8554.md move to the new head in the same pass. The draft text does not quote the changelog title and is otherwise unchanged.

## Step checklist

- [ ] changelog phrase commit pushed (fast-forward from 654c1a7c3570), new head recorded
- [ ] changelog YAML parse
- [ ] tidy, clean tree afterwards
- [ ] Error Prone compile
- [ ] focused ForceLeaderWaitTest at the new head (3 run, 0 failures, fresh XML)
- [ ] module check :solr:core:check -x test
- [ ] receipt refreshed at the new head; job and claim marked DONE
