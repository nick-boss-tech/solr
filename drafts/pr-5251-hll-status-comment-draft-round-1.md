Draft for owner review. Not posted. Target: GitHub comment on apache/solr PR 5251, status note form. Remove this line before posting.

🤖 *AI text below* 🤖 *(reviewed by Nick Shanin)*

Hi @elangelo, I checked the HLL precision change in this PR at head `ca539cad196f26c78e14a1dc698b84e0f5c1f22e`. A facet sketch built with a different `solr.facet.hll.log2m`, or with a different register width, is not rejected when it is merged.

What I ran, on JDK 21.0.12.1 with Gradle 9.7.0 and seed `5251C0DE`: five tests from commit `e7834ca954505c27778e59a61140bf02490226a6` (branch `solr-18598-submit` on `nick-boss-tech/solr`), applied to this head. They cover a larger and a smaller `log2m`, a different register width, a mismatch before promotion to FULL, and a direct `HLL.union` call. All five fail:

- Four report that no exception was thrown.
- The smaller-source merge reports `ArrayIndexOutOfBoundsException: Index 768 out of bounds for length 768`.

I have not checked whether any merged count is wrong. The failures show only that the mismatch is not rejected.

The same commit adds a check that rejects the mismatch with an error naming `solr.facet.hll.log2m`, along with the tests, a changelog entry and guide text. Is a rejection with a clear error the behaviour you intend for this change? If so, would you like to add the check to this PR?

https://github.com/nick-boss-tech/solr/commit/e7834ca954505c27778e59a61140bf02490226a6
