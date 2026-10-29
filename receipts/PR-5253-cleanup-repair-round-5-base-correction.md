# Receipt: SOLR-18601 / PR 5253, round 5 base correction (vm2, Linux)

Corrects the base named in `receipt-round5` (board `4341a139cdc`) and `receipt-round5-accepted` (board `9e6f461c708`). Add-only; those receipts stay as written.

## What was wrong

Both receipts call `32b91612edde057c19bf1a7963e8276b816551df` "the base commit, before the PR". It is not. `32b91612` is the PR's first commit ("SOLR-18601: Undo the routing rule and buffering when a MIGRATE fails"). The PR is `e59ce3c97d..5cbffe2f24` on top of upstream `e59ce3c97d` (SOLR-16398 #5217).

## Control at the correct base

- Upstream `e59ce3c97d` (fresh clone from apache/solr, clean status), `CoreAdminHandlerTest`, seed 5253C0DE: rc 1, 8 tests, 1 failure: `testDeleteInstanceDirAfterCreateFailure`. Same failure as K.
- Result: the failure is pre-existing on upstream main. It is not introduced by the PR. The owner's acceptance of it as a pre-existing failure stands on the correct base.
- Log: `repair-I/ctl-upstream.log`, XML in `repair-I/ctl-upstream-xml/`.

## API question settled (ladder rung 1)

`REQUESTDROPBUFFEREDUPDATES` is not in upstream `e59ce3c97d` (CoreAdminParams and CoreAdminOperation have no match). The PR adds it in `32b91612` and removes it in `5cbffe2f24`. Net public API change of the PR against upstream: none. The earlier "API removal" review item is withdrawn.

SOLR-18601 (ticket description) states the fix adds the internal action, which matches `32b91612`. Removing it again in `5cbffe2f24` is the PR's own later choice, and the net diff against upstream shows no change.

## Limitation of negative leg A

Negative leg A (3/3 fail) was run on `32b91612`, the PR's first commit, not on upstream. Its label "base" in the round 5 receipt is therefore wrong. It shows the three cloud tests fail when the cleanup code is removed from that commit. It has not been re-run on upstream. Re-run needed only if the reviewer wants the negative legs on the true pre-fix base.

## Still true

Round 5 at K `21804641d0eb`: positive legs as recorded; neighbours one failure, which is pre-existing per this control; negative legs B and C as recorded. Strict-rule verdict and owner acceptance unchanged.

No GitHub, PR, or JIRA action taken.
