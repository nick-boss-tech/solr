# Receipt: streaming docs URL allow-list note, round 1 item 4
Status: GREEN

The docs build is complete at the unchanged fork head `4b185504ef7dd5e4530664995719a0e743564382` on `docs-streaming-allowlist-note`. Host: phone-agent-2. Completed: 2026-10-23T07:21:40.292402+00:00. The tracked tree is clean. No product commit, product push, PR, comment, Jira or email was made.

## Results

| Check | Result | Evidence |
| --- | --- | --- |
| `./gradlew --no-daemon --max-workers=2 :solr:solr-ref-guide:buildLocalAntoraSite` | PASS, exit 0 | `1-antora.log` |
| Both rendered notes and cross-reference targets, including `#allow-urls` | PASS | `state.json`, the two captured HTML files |
| `./gradlew --no-daemon --max-workers=2 :solr:solr-ref-guide:checkSiteLinks` | Exit 0; output-capture caveat below | `3-checkSiteLinks.log` |
| Pinned `link-checker@1.4.2` run directly against the generated site | PASS, exit 0, zero errors and zero warnings | `4-direct-link-check.json`, `.stderr.log`, `.exit-code` |

The direct checker parsed 835 files, 241 local links, 3,335 local anchor links, 205 parent links and four parent anchor links. External links were disabled, matching the Gradle task. No Java tests were run or claimed; the site task built its required local Javadocs.

## Environment and evidence caveats

The old missing-tabs blocker is cleared. The pinned build declares `@asciidoctor/tabs` as a development dependency. This run unset `NODE_ENV` and set `npm_config_include=dev`, with a standalone Git checkout at `/workspace/solr-consistency-item4-antora`. JDK: `/usr/lib/jvm/java-21-openjdk-amd64`. Gradle home: `/home/ubuntu/.gradle`. No tracked build-file repair was needed.

The successful Antora build was not repeated after a local verifier expected a straight apostrophe rather than Asciidoctor's typographic apostrophe. Only that verifier assertion was normalized, then verification resumed.

The Gradle link-check task reported `BUILD SUCCESSFUL`, but its `TeeOutputStream` output handler threw a `NullPointerException` and left `build/validation/link-check.json` empty. That empty report is not proof. The same installed checker was therefore invoked directly with the task's flags and proper output capture:

```sh
NODE=solr/solr-ref-guide/.gradle/node/nodejs/node-v22.18.0-linux-x64/bin/node
CHECKER=solr/solr-ref-guide/.gradle/node/node_modules/link-checker/cli
"$NODE" "$CHECKER" "$PWD/solr/solr-ref-guide/build/site" --url-ignore 'file:/' --mkdocs --allow-hash-href --disable-external --debug --url-ignore "$PWD/solr/documentation/build/site/index\.html" --json -v
```

This direct check returned a complete nonempty JSON report with no errors or warnings. It is the link-validation evidence, rather than the Gradle wrapper's empty report.

## Deliverables

Gate: `gates/consistency-docs-round-1-item4.md`. Claim: `claims/consistency-docs-round-1-item4-vm4.md`. Final note text: `drafts/consistency-docs-round-1-item4.md`. Summary: `reports/consistency-docs-round-1.md`.

Durable evidence archive: `material/consistency-docs-round-1/item4-4b185504ef-docs-evidence.tar.gz`. SHA-256: `ec8bbfa3107b747f8f22b9021e6df7277139d2bd803971e37a3918e33d07e141`. Local originals: `/workspace/phone-agent-2-logs/consistency-item4`. The archive includes logs, state, the runner and both rendered pages; it contains no credentials.
