RECONSTRUCTED. The exact posted-or-held text was not on the branch. This draft is rebuilt from the live body at head 7ab3c03a1c, the follow-up gate record, and the reviewer's comment.

Note for the owner (not part of the reply): the first paragraph above is the required statement. The two reasons in the last section come from the W5 assignment's description of the held reply. Neither the live body nor `receipts/SOLR-16630.md` states them, and they have not been checked against the code in this draft. The W5 round 2 review decides whether they stand.

---

🤖 *AI text below* 🤖 *(posted on behalf of Nick Shanin)*

Thanks, @tflobbe. The sleep before the NRT stop is now a latch, in the shape you sketched. The stop thread waits on the latch. The main thread counts it down right before the phase 1 NRT queries ([stop wait](https://github.com/nick-boss-tech/solr/blob/7ab3c03a1cf9702f47d3103f1027b7176b8b00e3/solr/core/src/test/org/apache/solr/search/TestCoordinatorRole.java#L251-L255), [countdown](https://github.com/nick-boss-tech/solr/blob/7ab3c03a1cf9702f47d3103f1027b7176b8b00e3/solr/core/src/test/org/apache/solr/search/TestCoordinatorRole.java#L278-L278)). This is commit `7ab3c03a1c`.

One difference from the sketch: the wait is bounded at two minutes. If the signal never comes, the test logs a warning and stops NRT anyway.

Test result at this head: `TestCoordinatorRole` ran three times on 2026-10-13 (UTC). Each run had 9 tests, all passing, with 0 failures and 0 errors. That includes `testNRTRestart`.

The other sleeps stay, for two reasons.

1. Some sleeps set how long a node stays down. These are the NRT downtime ([line 257](https://github.com/nick-boss-tech/solr/blob/7ab3c03a1cf9702f47d3103f1027b7176b8b00e3/solr/core/src/test/org/apache/solr/search/TestCoordinatorRole.java#L257-L257)) and, in the toggling phase, the serve and down times ([lines 388 to 395](https://github.com/nick-boss-tech/solr/blob/7ab3c03a1cf9702f47d3103f1027b7176b8b00e3/solr/core/src/test/org/apache/solr/search/TestCoordinatorRole.java#L388-L395)). The elapsed time is the stimulus here. Nothing counts those durations down, so there is no event to wait on.

2. Five 100 ms sleeps pace poll and retry loops ([lines 286](https://github.com/nick-boss-tech/solr/blob/7ab3c03a1cf9702f47d3103f1027b7176b8b00e3/solr/core/src/test/org/apache/solr/search/TestCoordinatorRole.java#L286-L286), [320](https://github.com/nick-boss-tech/solr/blob/7ab3c03a1cf9702f47d3103f1027b7176b8b00e3/solr/core/src/test/org/apache/solr/search/TestCoordinatorRole.java#L320-L320), [350](https://github.com/nick-boss-tech/solr/blob/7ab3c03a1cf9702f47d3103f1027b7176b8b00e3/solr/core/src/test/org/apache/solr/search/TestCoordinatorRole.java#L350-L350), [427](https://github.com/nick-boss-tech/solr/blob/7ab3c03a1cf9702f47d3103f1027b7176b8b00e3/solr/core/src/test/org/apache/solr/search/TestCoordinatorRole.java#L427-L427), [494](https://github.com/nick-boss-tech/solr/blob/7ab3c03a1cf9702f47d3103f1027b7176b8b00e3/solr/core/src/test/org/apache/solr/search/TestCoordinatorRole.java#L494-L494)). Each loop checks a value on every pass and exits when it sees the result it wants. The sleep only sets the pace between checks.

---

Open items for the owner (not part of the reply):
- The reviewer's comment at 2026-10-13T17:22:05Z was edited at 17:28:28Z. This draft follows the current text. The earlier text is not on the record.
- No posted version of this reply exists. It has not been posted.
