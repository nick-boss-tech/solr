# Gate: PR 5107 integrated security fixes, round 3, superseding head 7e531b0

Superseding note: this gate pins head F2 `7e531b083182366f8b6e7f00a3781ce3cbb713de`, one ordinary commit above F `4b04ba0c5f412403ac46db64364d90324b90b55d`. F2 adds only the generated CLI start-page synchronization in `solr/solr-ref-guide/modules/deployment-guide/pages/cli/solr-start.adoc` (the synopsis line wrap of `-u=username:password` that `:solr:solr-ref-guide:generateCliDocs` produces). The round 3 gate `gates/pr-5107-security-integration-round-3.md` stays unchanged. No proof at F counts for F2.

Status: CLAIMED by vm6 (partial gate). Owner direction 2026-11-02: skip BATS and the packaging legs for this gate. A partial run cannot produce a green verdict; the receipt will say PARTIAL and name every skipped leg.

## Pinned inputs

- Fork branch: `nick-boss-tech/solr:solr-8442-security-review-r2-submit`, fast-forward from F, verified by ls-remote at push time.
- Required head: F2 `7e531b083182366f8b6e7f00a3781ce3cbb713de`.
- Ancestry: F, H2 `d264f756644ac2fb8b56c09c42a0c737198c148a`, A `7d193dac0f4abc441488f0ac2a9a734983d4fcd2`, and C `25c88fb1c432120b98f7af668cf58be0052d13d0` are ancestors of F2.
- Claim: `claims/pr-5107-security-integration-round-3-vm6.md`.
- Logs: `/workspace/gates/logs/vm6/pr-5107-security-integration-round-3-partial-F2/`.

## Requirements

All requirements of `gates/pr-5107-security-integration-round-3.md` apply unchanged at F2, with these exceptions. The BATS packaging legs (both parser modes), the BATS original-production pair, the shell pair, and the non-root BATS account requirement are skipped by owner direction. The PARTIAL receipt must name each one as not run.
