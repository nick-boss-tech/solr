# Receipts

Gate receipts, copied from the main side's records so a review never has to guess at gate state.

One file per ticket, `receipts/SOLR-<ticket>.md`, stating: the gated head, the date, the gate log name, the focused tests and their counts where the record carries them, the pre-fix proof result, and where the receipt is recorded on the main side (receipts ledger or takeover log).

Rules:
- A receipt at a branch's exact live tip means the branch is gated at that tip. It does not need re-deriving, and "no gate" should not be reported for it.
- A receipt file that says NO GATE is also a statement of record: the main side has no gate for that branch, and the file says what is launched or owed instead.
- The main side refreshes these files when it submits work (an assignment or an answers file) that touches the branch. Gate logs themselves stay on the main side; a receipt is the extract a reviewer needs.
- If a receipt and a live tip disagree, the tip has moved since the receipt; say so rather than treating either as wrong.
