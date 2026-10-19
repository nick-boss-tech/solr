# Skip record: SOLR-17365

- Ticket: SOLR-17365, "Not all of the Ref Guide antora UI bundle code has ASL headers". JIRA status Open, resolution none, created 2024-07-10, last updated 2024-09-25, checked 2026-10-19 UTC.
- Verdict: pr-needed.
- Draft branch: solr-17365-draft-pr.
- Pinned head: fork origin/main `498a09aea97aac86c2cc7ac114113b946d249942`.
- Checked by: vm5, 2026-10-19 UTC.

## Ticket reading

The ticket asks that the Ref Guide code carry the license headers it needs. The description is one sentence. Comments (2024-07-14, malliaridis) suggest adding the header to the minified bundle through gulp-header, and (2024-07-15, epugh) call a minified-bundle header a nice-to-have. No maintainer decision on the first-party source is recorded.

## Code reading (at `498a09aea97aac86c2cc7ac114113b946d249942`)

- `gradle/validation/rat-sources.gradle:227-228`: for `:solr:solr-ref-guide` the Apache RAT license check excludes `ui-src/**`, so the UI sources are not checked for headers.
- `solr/solr-ref-guide/ui-src/`: the first-party CSS and JS sources do not contain the ASF header. A check of the first 30 lines of each `.css` and `.js` file under `solr/solr-ref-guide` (outside node_modules) found the phrase "Licensed to the Apache Software Foundation" in none of the 17 files under `ui-src` that the check reads (15 CSS, 2 JS: `ui-src/css/*.css` and `ui-src/js/02-on-this-page.js`, `ui-src/js/07-copy-perma-link.js`). The module has 18 `.js` and `.css` files outside node_modules. The one other file, `solr/solr-ref-guide/increase-request-timeout.js`, has the header.
- `ui-src/css/typeface-inconsolata.css` and `ui-src/css/typeface-noto-sans.css` are font-face declarations for bundled typefaces. They are third-party files: an ASF header on them would be wrong. They need the upstream license text in a notice, not a header.
- `solr/solr-ref-guide/build.gradle:313` (`from("${projectDir}/ui-src")`) and the `ui-bundle.zip` tasks (lines 353-380) copy the sources into the bundle. The bundle is not checked by RAT.

## History

- The RAT exclude and the `ui-src` layout were not traced on the fork. The ticket does not depend on the history.
- No commit on the fork names SOLR-17365 (empty `git log --grep` at the pinned head).

## Reproduction attempts

Not attempted. The behavior is a file-content and check-scope question, answered by the grep and the RAT exclude above. No run is needed to show that the header is missing, and a run of RAT with `ui-src` un-excluded is the check the PR would add.

## Gap analysis

The first-party CSS and JS under `ui-src` have no ASF header, and the build's RAT check excludes that directory, so nothing fails. The ticket's ask is met by two changes: add the ASF header to the first-party files, and remove the `ui-src/**` exclude for them (keeping the exclude only for third-party files, with their licenses recorded). Whether the minified bundle also needs the header (the gulp-header suggestion) is a separate choice; this record does not make it.

## Verdict justification

pr-needed: "the code path shows it, and a change is needed." Minimum evidence: the exclude at `rat-sources.gradle:227-228`, the list of files without the header, and the change the PR would make. The PR's check: a RAT run of `:solr:solr-ref-guide` with `ui-src` un-excluded for first-party files, passing with the headers added. The choice of header wording and of the third-party treatment is recorded here as a choice (rung 4), with the reason: the ASF header is required on first-party source files, and third-party typefaces need their own notice.

## Limits

This record does not run RAT. It does not inspect the minified bundle output. It does not check the Ref Guide build on other branches. It does not decide the bundle header question.
