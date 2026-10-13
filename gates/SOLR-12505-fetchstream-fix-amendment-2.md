# Gate job amendment 2: SOLR-12505 proof-leg ruling (main side)

- Job amended: gates/SOLR-12505-fetchstream-fix.md, under amendment 1
  (gates/SOLR-12505-fetchstream-fix-amendment-1.md).
- Question ruled on: the gate at ee67d374219275907ef1b78e3d2b455b6a15e43f (GATE END
  2026-10-12T23:41:00Z, log logs/vm2/queue-g12505-gate-20261012T232607Z.out on vm2) recorded
  every leg rc=0 except the proof leg, rc=1 with the failure "expected java.io.IOException
  to be thrown, but nothing was thrown" (logs/vm2/12505-proof.log). Amendment 1 left that
  leg for the main side to rule on. The claim (claims/pool-vm2-gate-backlog-12505.md) also
  asks whether the focused head leg of the later interrupted run at the same head must be
  re-run.
- Status: RULED by the main side, 2026-10-13. The proof leg is not accepted as recorded;
  the corrected proof shape is below. Only the proof leg re-runs. No other leg is
  relaunched by this amendment.

## Why the expected IOException does not occur on pre-fix FetchStream

Code read at the pre-fix head 5f20e1171bc890509d76e4f5f7defcc4fc8c102c:

- Pre-fix FetchStream validates nothing. `init()` (FetchStream.java lines 131 to 154)
  splits the `on=` value on "=", trims the halves into leftKey and rightKey, and stores
  them. No character of rightKey is checked, so the construction-time IOException the new
  test expects (the validate route's rejection, commit d81a773117) cannot occur on pre-fix
  code at construction.
- rightKey is interpolated unescaped at two later sites, not one. `fetchBatch()` line 239
  appends it into the batch query string "{! df=" ... " q.op=OR cache=false }", and
  `appendFields()` appends it to the `fl` parameter ("," + rightKey, when the fetched
  field list does not already contain it). `fetchBatch()` runs only when the wrapped
  stream yields at least one tuple; with an empty batch it sends no request at all.
- So on pre-fix code a drain with a breakout field name has three possible endings, and
  only the third is silent: (1) the corrupted batch query fails server-side and the error
  surfaces while draining; (2) the `fl` interpolation fails server-side the same way;
  (3) no request is sent (empty batch), or the corrupted request happens to parse, and
  the drain completes. Endings (1) and (2) throw an IOException, but never the validation
  error; a test asserting only the exception type would pass on pre-fix code for the
  wrong reason, which is why the restructured test also checks the cause chain.

## Probe (main side, 2026-10-13, pre-fix head 5f20e1171bc)

A scratch probe method (testFetchBreakoutProbe) was added temporarily to
StreamDecoratorTest in a throwaway worktree at the pre-fix head; it was never committed.
Setup matches the existing fetch tests: fetch over collection1, wrapped stream
search(q=*:*, fl="id,a_i"), batchSize=2, fl="subject", three documents whose subjects
are alpha, bravo and charlie. Run: `:solr:solrj-streaming:test --tests
"org.apache.solr.client.solrj.io.stream.StreamDecoratorTest.testFetchBreakoutProbe"
-Ptests.seed=12505C0FFEE12505`, log g12505-probe.log in the main side's tools directory,
BUILD SUCCESSFUL, JUnit XML 1 test, 0 failures. (One delivery of this run's result
reported a build failure; the log file and the JUnit XML on disk both show success, and
the disk record governs.) Outcomes, quoted from the test output:

- CONTROL on=<id=a_i>: NO-THROW, 3 tuples, subjects alpha, bravo, charlie (correct).
- SPACE on=<id=a_i a_s>: THREW java.io.IOException, "invalid query parser 'a_s' for query
  '{! df=a_i a_s q.op=OR cache=false } 0 1'". The space ends the df value, the next token
  is read as a query parser name, and the error quotes the corrupted batch string itself.
- BRACE on=<id=a_i}>: THREW java.io.IOException, "Error parsing fieldname: Expected
  identifier at pos 0 str='},_version_'". This failure is on the `fl` interpolation
  ("subject,a_i},_version_"), the second unescaped site.
- BANG on=<id=a_i!>: THREW java.io.IOException, "Error parsing fieldname: Expected
  identifier at pos 0 str='!,_version_'" (same `fl` site).

The premise therefore stands, confirmed by run: on pre-fix code the unescaped name
corrupts the request, in the batch query and in the field list alike. The recorded
validate route (reject the name at construction) covers both sites at once; an escape
route would have had to cover both append sites.

## Ruling

1. The proof leg at ee67d37421 is NOT accepted as recorded. Its whole content is
   "nothing was thrown", and the new test's exact trigger input is in no record the
   main side can read (the gate-status round 1 report flagged the same gap: "The trigger
   input for the new test is in no local ref"). For the standard fetch setup the probe
   shows a breakout input does not drain silently on pre-fix code; it throws an unrelated
   server error. "Nothing was thrown" is therefore equally consistent with the batch
   never having been built (the wrapped stream yielding no tuple), which exhibits no
   premise at all. A proof that cannot be distinguished from an empty batch is not a
   proof, so the proof shape itself has to be corrected; the premise does not fail.
2. Corrected proof shape. Before the re-run, gates/SOLR-12505-fetchstream-fix.md records
   the new test's exact trigger input (the `on=` value). The re-run proof leg (branch
   tests kept, FetchStream.java reverted to 5f20e1171bc) establishes the proof when its
   recorded failure content is one of these two:
   - Content 1, the rejection is absent: the pre-fix drain throws, and the failure is the
     test's cause-chain assertion failing because the thrown error is a server parse or
     parser error without the validation text (the probe quotes two such messages). The
     contrast with the fixed head, which fails at construction with the clear validation
     error, is the premise.
   - Content 2, the corrupted batch ran: the pre-fix drain completes, and the record
     shows the fetched field missing or wrong on tuples the control fetch (a valid field
     name, same setup) returns correctly, with the wrapped stream shown to yield tuples
     so a batch was built and sent.
3. Scope of the re-run: the proof leg only, at ee67d37421, under vm2's existing claim.
   Every other leg at that head is already recorded rc=0 in the completed gate (GATE END
   2026-10-12T23:41:00Z) and is not re-run. When the corrected proof leg returns Content
   1 or Content 2, the gate at ee67d37421 is GREEN and amendment 1 direction 5 proceeds:
   push the fix commits plainly (fast-forward) to solr-12505-submit, refresh
   receipts/SOLR-12505.md (recording the validate route and that it covers the `fl`
   interpolation as well as the batch query), update the draft, and mark the job, the
   claim and both amendments DONE.
4. The interrupted run (GATE START 2026-10-13T01:00:51Z at the same head, no GATE END)
   needs no re-run of its focused head leg: the completed gate at that head already
   recorded that leg rc=0. It duplicated a finished gate; its partial legs add nothing.
