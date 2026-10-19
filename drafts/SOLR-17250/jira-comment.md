We checked this against Solr main at commit 498a09aea9. The problem is in the release wizard as written, and a change is needed.

What we checked:

- Sixteen commit steps in the release wizard's command file run git commit without a name or email, and no step sets one.
- The wizard script does not set a git identity. Its only identity check is for the signing key.

What this means: on a machine with no global git identity, the first commit step fails with "Author identity unknown". The planned change is to ask once for the release manager's Apache email address and public name, with the Apache address suggested, and to set them only in the checkout the wizard uses.

If you still see this: please send the wizard step that failed and the git version, so the change can be checked against that step.
