# Correction to reports/teardown-client-interrupt-probe-round-2-result.md (vm2)

Add-only correction. The result report is unchanged.

1. **Interrupt setup.** The result report's Limits line says "Both arms set the interrupt artificially before close." That is wrong. Only the interrupted arm sets the interrupt. The control does not. Evidence: `interruptBeforeClose: false` in every control snapshot and `interruptBeforeClose: true` in every interrupted snapshot (`board-evidence-r2/control.evidence.log`, `board-evidence-r2/interrupted.evidence.log`).
2. **Process disclosure (kept).** The invocation budget was exceeded by one control run, caused by a duplicate chain I launched. The overwritten first control evidence is not preserved. Both facts stand as written in the result report.
3. **No further runs.** No driver chain is running on vm2 as of this correction. The only remaining process is a defunct shell (PID 16518), with no children.
4. **No production change.** None was made, and none is pending source review.
