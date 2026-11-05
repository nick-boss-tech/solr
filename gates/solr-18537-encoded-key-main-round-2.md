# Gate: encoded node-target query keys on main, round 2 (tidy repair)

Status: DONE (GATE GREEN at `a676e7a392244e7cf4a35719fc64383b65cd947f`; receipt `receipts/SOLR-18537-encoded-key-main-round-2.md`). Intended host: capable Linux gate host with JDK 21 and the pinned Gradle toolchain.

- Supersedes `gates/solr-18537-encoded-key-main-round-1.md` for the repaired head; all leg requirements, seeds (`5110EAC0DE`), fail-before terms, and finishing rules of round 1 apply unchanged.
- Reason for round 2: the round 1 gate (phone agent, 2026-11-05T04:05Z to 04:14Z) found the round 1 head `7b83425fe7ce704ee781f273cd099b4425b795af` not tidy-clean (one formatting rewrap in `NodeTargetProxyFilterTest.java`). Every other executed leg passed at that head: focused suites 19 + 6 with zero skips, neighbors 2 + 9, and all four fail-before methods failed at their intended assertions on the main baseline. The round 1 root `check -x test` leg did not execute: a stale Gradle daemon held the Gradle journal lock (host artifact, both attempts named owner PID 7535); the daemons were killed and the lock cleared before this round.
- Repair: commit `a676e7a392244e7cf4a35719fc64383b65cd947f` ("SOLR-18537: Tidy formatting in NodeTargetProxyFilterTest", formatting only, 1 file, +2/-1), plain-pushed to `solr-18537-encoded-key-followup` and read back through the API.
- Required head: `a676e7a392244e7cf4a35719fc64383b65cd947f`. Sole parent chain unchanged: baseline upstream main `07ccc73cd24e26e59da169ca35b3fd5f64c35986`.
- Receipt target: `receipts/SOLR-18537-encoded-key-main-round-2.md`. Round 1 results do not transfer; all legs rerun at the new head.
