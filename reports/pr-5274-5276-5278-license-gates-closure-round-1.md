# Closure report: PR 5274, 5276, 5278 license metadata gates, round 1

Status: CLOSED by owner direction (Nick, 2026-11-02). Not GREEN. No receipts
will be written for these gates.

## Why these gates close

The purpose of this work was to draw attention to the license metadata
patches the solrbot sweep PRs need. That purpose was met by posting the
review findings directly on the PRs, each with a compare link to the
prepared repair branch and fast-forward take-over commands, on 2026-11-02
after the owner's per-text go:

- PR 5276: https://github.com/apache/solr/pull/5276#issuecomment-6581875127
- PR 5278: https://github.com/apache/solr/pull/5278#issuecomment-6581876754
- PR 5274: https://github.com/apache/solr/pull/5274#issuecomment-6581878450

Drafts as posted: drafts/pr-5276-license-metadata-comment-draft-round-1.md,
drafts/pr-5278-license-metadata-comment-draft-round-1.md,
drafts/pr-5274-license-metadata-comment-draft-round-1.md (each now marked
POSTED; bodies read back byte-identical from the GitHub API).

## What closes

- gates/pr-5274-license-metadata-round-1.md: never gate-claimed. Its Docker
  extraction leg cannot run on any pool host (no host has Docker), and the
  remaining legs prove the solrbot PR rather than the license patch.
- gates/pr-5276-license-metadata-round-1.md: gate claim
  claims/pr-5276-license-metadata-gate-round-1-vm2.md (vm2, started
  2026-11-02T00:44:29Z) is superseded by this closure; no verdict was
  recorded under it and no receipt is owed by vm2.
- gates/pr-5278-license-metadata-round-1.md: vm2 already released its gate
  claim by handback (claims/pr-5278-license-metadata-gate-round-1-vm2-handback.md,
  VM crash mid-run, no receipt). Nothing further is owed.

## What stands

- The repair branches stay pushed at their pinned heads for anyone who
  wants the commits: pr-5274-jcl-license-metadata at e3bcf3af6fcb,
  pr-5276-license-metadata at 5f9613e13865, pr-5278-jcl-license-metadata at
  cb8c6ab2752.
- The implementation reports stand as the provenance record; the posted
  comments state plainly that verification was a static audit with no
  Gradle run at the repair heads.
- If a maintainer asks for executed proof on any of these, that is a new
  request on the PR and gets a fresh job at the then-current head.

No GitHub or Jira action beyond the three comments already posted. Gate
files are closed by a Status-line-only edit pointing here, per the owner's
direction.

Addendum: the dependent assignment assignments/pr-5278-license-gate-remaining-legs-round-1.md (finish the 5278 gate remaining legs, and write the PR 5276 receipt) was unclaimed and is closed by the same direction; its Status line now reads CLOSED with a pointer here.
