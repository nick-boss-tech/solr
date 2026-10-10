# SOLR-12703 re-gate draft check

Read only. Checked `pr-drafts/update-processing/SOLR-12703.md` against `receipts/SOLR-12703.md`, `claims/update-12703-regate.md`, and the changelog at `bdd29ba19900`. No edits, commits, pushes, gh writes, builds, or tests.

## 1. What and Proof against the receipt

Matches the receipt:
- Line 23: "27 tests, 1 pre-existing skip".
- Line 26: "Verified 2026-10-09 at this head (`bdd29ba19900`)".
- Line 24: pre-fix head `ed95d555e62` is named.

Problem, line 24: "Without the message change, the updated test fails on the earlier head". The receipt does not say what differs at `ed95d555e62`, so the causal clause is unsupported.

Minor, line 25: "The module checks pass." The receipt names one module check (rc=0). Singular.

Outside What and Proof, Limits line 33: "The two merge without conflicts in either order" has no basis in the receipt. Owner to confirm or remove.

## 2. RunUpdateProcessor symptom

No occurrence. No hits for RunUpdate, surfac, or confus. The changelog at `bdd29ba19900` reads "is rejected with a clear 400 error instead of being stored as a field value", which matches the What section.

## 3. Heads cited

- `bdd29ba19900`: lines 9, 15, 17, 23 (full SHA in links) and line 26 (short). Correct: the gated head and live tip.
- `ed95d555e62`: line 24. Correct: the pre-fix head in the receipt.
- `bcae04d77bdb`: line 33, SOLR-6045 loop link. Correct for that other branch; not this ticket's head.
- `63c84919c80b`: not cited in the draft. The receipt names it as the previous gated head.

## 4. Internal vocabulary

No hits for gate, receipt, ledger, JUnit, rc=, takeover, live tip, owner, main side, audit, round, re-gate. "log" matches only "Changelog" (line 36), a file name. "message change" (line 24) is an unexplained history reference, covered by item 1. Em dashes: 0. "we": none.

## 5. Proposed replacements (not applied)

- Line 24, replace "Without the message change, the updated test fails on the earlier head" with "The updated test fails on the earlier head".
- Line 25, replace "The module checks pass." with "The module check passes."
- Line 33, owner to confirm, replace "The two merge without conflicts in either order, but their combined behavior has not been run." with "Their combined behavior has not been run."

SOLR-12703 draft posting state: fix first, see the listed replacements
