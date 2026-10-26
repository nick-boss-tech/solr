# PR 5235 JDK raw-body capture: outcome report, round 2

Gate: `gates/pr-5235-jdk-raw-capture-round-2.md` (superseded for the tidy repair by `gates/pr-5235-jdk-raw-capture-round-2-superseding-1517b48.md`). Receipt: `receipts/PR-5235-jdk-raw-capture-round-2.md`. This report classifies the premises. It is not a review of the PR and does not claim the PR is fixed or broken.

## Classification

| Premise | Result | Evidence |
| --- | --- | --- |
| F3 JDK: legacy request POST body | CONFIRMED | Base passes 2 JDK legacy methods with the original 21-byte body. PR sends `wt=xml` (6 bytes). |
| F3 Jetty: legacy request POST body | CONFIRMED | Base passes both Jetty legacy methods. PR sends `wt=xml`, 6 bytes, not the original body. |
| F4 JDK: writer override bodies | CONFIRMED | Base passes both JDK override methods with the custom 18-byte body and calls the hook. PR sends the original 21-byte body. |
| F4 Jetty: writer override bodies | CONFIRMED | Base passes both Jetty override methods. PR sends the original 21-byte body. |

## Corrections

- The Jetty legacy PR observation is `wt=xml` (6 bytes), not an empty body. The earlier empty reading came from parameter parsing in `DebugServlet`, which is also why the JDK base cases looked empty. This changes the description of the F3 wire behavior, not its classification.
- The JDK blocked classification from the first round is superseded by the raw capture.

## What this means for the review

- All four findings reproduce through both clients: base passes with the expected bytes and the PR fails with the predicted wrong bytes.
- The PR's legacy requests send a form body containing `wt=xml`. The findings should describe that observed body, not an empty one.
- The controls, including the two form-encoded controls, pass on both legs, so the capture repair does not change the non-legacy paths.
- No production change was made, and nothing here is a fix verdict.

## Proposed maintainer update (for Nick to review; not posted)

A draft only, not for posting until Nick approves: the Jetty and JDK legacy request bodies on the PR head are a form body `wt=xml` where base sends the original request body, and the writer override bodies on the PR head are the original body where base sends the custom writer body. Both clients reproduce, with passing base and control tests.

## Next

- Nothing further is needed from this gate. The PR is not changed by it.
