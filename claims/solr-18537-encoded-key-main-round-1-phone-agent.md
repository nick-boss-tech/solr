# Claim: solr-18537-encoded-key-main-round-1, phone agent

- Claimant host: phone agent (Linux VM). Claimed 2026-11-04 evening MST at Nick Shanin's direction in the main chat ("do these gates").
- Job: `gates/solr-18537-encoded-key-main-round-1.md`. Supersedes the withdrawn `gates/solr-12613-adjacent-encoded-key-round-1.md`; the stand-down record remains in force for the old inputs.
- Pins: fork branch `solr-18537-encoded-key-followup` at `7b83425fe7ce704ee781f273cd099b4425b795af`; sole parent upstream main `07ccc73cd24e26e59da169ca35b3fd5f64c35986`. Seed `5110EAC0DE`.
- Runner: `~/workspace/tools/solr-gradle.sh` (Gradle 9.7.0, vendored JDK 21), one build at a time under the host build lock; tidy preflight and test legs in clean worktrees, root `check -x test` in a normal clone at the same head (Antora worktree rule), layout disclosed in the receipt.
- Execution state and receipt target (`receipts/SOLR-18537-encoded-key-main-round-1.md`) follow in the receipt and an updated claim.
