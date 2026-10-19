We checked the release wizard's website steps on Solr main at commit 498a09aea9 (October 7, 2026). The order of the steps there is different from the order described in this report: the reference guide metadata update and the readiness check come before the website steps.

What we checked:

- The release wizard's "Update the website" group and the order of its steps.

How we tried to reproduce it:

- Not applicable. The release wizard runs against a release candidate and the website repository, so it cannot be tried from this code alone.

What this means:

- We cannot tell from the code whether the step order caused the 9.6 publication problem, or whether the change proposed here is still wanted. That is a release process decision.

If you still see this:

- Please tell us which order the release manager wants for the reference guide and website steps, and which step failed for the 9.6 publication. With those, we can say whether the current order is correct or needs the swap.
