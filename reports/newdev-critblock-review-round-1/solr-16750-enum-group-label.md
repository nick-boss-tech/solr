# Review: SOLR-16750, branch solr-16750-enum-group-label (unit 33)

Head reviewed: 783c1323f0735b07ee84435feed3595da34aa520. Base: c8ab71430f. Ticket: SOLR-16750, "Grouping on EnumFieldType fields fails".

## Pass 1

Review (2026-10-22, vm4):

- Ticket fit. Grouping on an enum field returned the raw integer value instead of the label. The branch converts an `Integer` group value to an `EnumFieldValue` in `Grouping.java` (`groupValueObject`), using a new `EnumFieldType.intValueToEnumFieldValue` helper. The approach fits the ticket, but see the hand-back.
- Finding, major (gate): the new test does not run at the gate seed, because its own assumption on `solr.tests.numeric.dv` skips it. At seed `16750C0FFEE16751`, where it runs, it FAILS on the head: `mismatch: 'High'!='Not Available'` at the first group value. So the head fix does not produce the label the test expects on that path. The branch cannot be called green for this ticket. Hand-back, not repaired (a production change of this size is outside the Gate repairs scope).
- Finding, major: the proof leg is not valid at the gate seed (the test is skipped on base too).
- Finding, minor: the `groupValueObject` conversion only handles `Integer` group values. Whether the numeric DocValues path returns another type is not checked here.
- Changelog: present, YAML parses (step 0).
- Repairs: none.

Hand-back: `gates/SOLR-16750-review-pass-1.md`, HANDED BACK at pass 1, step 4 (seed attempt 16751 fails). Passes 2 and 3 do not run.
