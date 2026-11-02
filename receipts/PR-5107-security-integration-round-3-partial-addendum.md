# Addendum: PR 5107 round 3 partial gate receipt, verifyUiWasm cause

Addendum to `receipts/PR-5107-security-integration-round-3-partial.md`,
recorded 2026-11-02 by the phone agent after the fact. The receipt's
statements were accurate as written at gate time; this addendum
identifies the mechanism behind them.

The receipt recorded that `:solr:webapp:verifyUiWasm` failed at F2
with a checksum mismatch, failed identically at the contributor head
A with a different downloaded hash, and concluded that the served
artifact was not byte-stable between downloads on this host. The
sharper facts, established the same day while building a dev
distribution:

- A direct download of the pinned artifact
  (https://nightlies.apache.org/solr/ui/wasm/473a55ef64a9a1605e00eb6c1c386887d4cb7dac/solr-ui-wasm.zip)
  with curl returns 5,237,370 bytes whose SHA-256 is exactly the
  pinned `654494303e4af3b8bfb26a83d250603f19033820295591d88db0597991ce8ad2`.
  The nightly artifact itself is correct and stable.
- The copy Gradle downloaded through this host's local relay is
  4,423,680 bytes, a valid zip with different content (SHA-256
  c336b9dc7b3692d5b7d4c973f468355a900cf848b75c899783028e920375a798 in
  two runs; a third run at A produced e7c30fc527bb...). The relay path
  is what varies and corrupts; this is the same family as the
  September 2026 truncated Gradle downloads recorded in the host's
  operating notes.
- `downloadUiWasm` skips its download when a local copy already
  matches the pinned checksum, so seeding the curl-fetched file lets
  builds proceed: with the seeded copy, `:solr:packaging:dev` built
  successfully on this host at F3.

So the root-check failure at F2 was this host's Gradle download path,
not the branch, not the contributor head, and not the artifact. The
receipt's verdict (PARTIAL, with that leg failed) stands; only the
cause attribution is refined here.
