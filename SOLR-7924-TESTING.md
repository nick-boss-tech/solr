# SOLR-7924 testing notes (hypothetical, nothing was compiled or run; BATS needs a packaged Solr)

Ticket (2015, 5.2): `bin/solr` on IBM AIX. Two reported problems: (1) `$delay` passed to `sleep` is the
float `0.5`, AIX `sleep` rejects anything below 1; (2) `JAVA_MINOR_VERSION` does not parse from the IBM VM;
(3) IBM J9 rejects `-Xloggc:<dir>`. Jan Høydahl: a working patch from someone with an AIX box is welcome.

## Reading of main
`spinner()` in `solr/bin/solr` still sets `local delay=0.5` and calls `sleep $delay` in a loop; with an
integer-only `sleep` that prints an error every iteration and busy-loops. The Java version parsing and GC
flags changed completely since 5.2 (no `JAVA_MINOR_VERSION` in the script any more), so only (1) is
still reproducible by reading.

## Change
`spinner()` probes `sleep "$delay"` once; if it fails the delay becomes `1`. One-line, POSIX-safe.

## Test
New BATS test in `test_start_solr.bats`: puts a fake integer-only `sleep` first in `PATH`, extracts the
`spinner` function from `bin/solr` with `sed`, runs it against a 2 second background process and expects
success without the `invalid argument` message.

## Guesses to verify first
- `sed -n '/^function spinner() {/,/^}/p'` picks exactly the spinner function (first `^}` after it).
- `${SOLR_TIP}` is exported by `bats_helper` (other tests use it); `BATS_TEST_TMPDIR` needs bats >= 1.4.
- The probe costs one extra half-second sleep (the probe sleeps the full delay) before the loop. It could
  use `sleep 0.1` instead if that is considered noise.
- Not addressed: J9 `-Xloggc`, `JAVA_MINOR_VERSION`; they are not reproducible from the current script.
