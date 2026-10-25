# Stand-down: DistributedExpandComponentTest settling run (owner direction, 2026-10-25)

Status: HANDED (record-only)

The owner stood this assignment down the day it posted: flakes usually do not reproduce, and a single sighting on someone else's merged PR does not earn queue time chasing it. The sighting itself is recorded (PR #5198's Crave job 121178733450, seed `85F9F77FD74C1D41`, classMethod ThreadLeakError, one idle Jetty selector thread) in the parent assignment, and that record is the whole deliverable unless a trigger below fires.

Revival triggers, any one of them: the same class fails again in a later CI run (a second sighting); the failure lands on one of the owner's own PRs or tickets; a maintainer raises the test; or it matches an existing tracked ticket. On a trigger, the parent assignment's settling runs proceed as written.
