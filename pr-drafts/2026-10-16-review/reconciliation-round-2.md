# Round 2 text drafts: reconciliation against the live PR bodies (2026-10-18, vm4)

Status: NOT APPLIED. Nothing here changes a live PR. Lengths are visible characters with markdown links reduced to their text and backticks removed, counted from the AI header to the end of each body. The guideline is 3,500.

Head check: all six drafts named in the round 2 assignment (5116, 5117, 5130, 5131, 5132, 5135) match the live PR head, and each cited receipt contains that head. PR 5134 does not match, and it is covered by PR-5134-reconcile-5c1124bd.md.

| PR | Live head | Live visible | Draft visible | Similarity to live | Receipt cites the head | Status |
| --- | --- | --- | --- | --- | --- | --- |
| 5116 (SOLR-18540) | 58e07b4571 | 3,828 | 3,905 | 0.98 | yes | Draft is over the guideline and longer than live. Cut it. |
| 5117 (SOLR-18541) | 81a88e40dd | 4,492 | 3,521 | 0.44 | yes | Large rewrite. Under the limit by 21 characters after the cut. Check the proof paragraph against the receipt before applying. |
| 5130 (SOLR-4374) | 433e6c6ba9 | 3,814 | 3,507 | 0.93 | yes | Over by 7. Trim slightly. |
| 5131 (SOLR-5394) | 74532d0f76 | 3,795 | 3,498 | 0.91 | yes | Within the guideline. Check the fcs scope sentence against the head. |
| 5132 (SOLR-6207) | 05cc2776d0 | 3,725 | 3,516 | 0.90 | yes | Over by 16. Trim slightly. |
| 5135 (SOLR-6975) | c666155ed0 | 3,558 | 3,896 | 0.92 | yes | Draft is over the guideline and longer than live, which is the reverse of what a correction should do. Recheck the order sentence and cut. |
| 5134 (SOLR-6831) | 5c1124bdef | 5,170 | 3,457 (written for 4afed049) | 0.38 | yes, for 4afed049 only | Stale draft. See PR-5134-reconcile-5c1124bd.md. Full gate green at 5c1124bdef (receipt in receipts/SOLR-6831.md). |

## Open items

- Cut 5116, 5130, 5132, 5135 under 3,500 visible characters, and recheck each cut against its receipt.
- 5117: confirm the proof paragraph and the max() note against receipts/SOLR-18541.md, since the body changed most.
- 5135: the draft is longer than the live body. Check whether the order sentence correction needs the extra length.
- 5134: the gate receipt exists. Apply after the Limits correction and the length cut in PR-5134-reconcile-5c1124bd.md.
- The round 2 assignment was marked DONE on 2026-10-18 at 04:37 UTC, before these checks. Reopen it until the cuts above are made.
