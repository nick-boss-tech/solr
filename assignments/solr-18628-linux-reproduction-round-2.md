# SOLR-18628: investigate idle h2c non-reproduction (round 2)

Status: OPEN

Intended host: any available capable Linux pool host with premise-run, gate, review and records capabilities; JDK 21 for the Solr build, JDK 25 for the test runtime, and access to the reporter's standalone reproducer and build dependencies.

## Context

The Windows implementation claim is `claims/solr-18628-idle-h2c-update-windows-round-1.md`; the original assignment is `assignments/solr-18628-httpjdk-h2c-idle-round-1.md`. The first reproduction-phase receipt, `receipts/SOLR-18628-h2c-fail-before-round-1.md`, reports NON-REPRODUCED: the focused test passed on JDK 25 at test-only fork head `aebe1d8e9b0f226948e092f2610682c9c2596f27`, with the one-second keepalive setting and seed `18628C0DE`. The production baseline is `c0c219e374611e0edc475c0f793c9bd688b13292`. See `reports/solr-18628-h2c-fail-before-round-1-evidence-index.md` for logs and XML. This is not fail-before proof, nor evidence that the reported defect cannot occur.

Reporter context: https://issues.apache.org/jira/browse/SOLR-18628, https://www.mail-archive.com/users%40solr.apache.org/msg09633.html, and https://github.com/micopiira/solrj-h2c-idle-reproducer.

## Work

Claim this investigation under the board workflow, check the current fork head and existing claims, and preserve the prior run and its evidence. Compare the reporter's reproducer and environment with the H1 test, including JDK and Solr/Jetty versions, HTTP protocol negotiation, streaming request shape, idle timing and connection lifecycle. Establish with observable evidence whether the relevant h2c connection expires and whether the second request exercises the reported path. Run bounded, justified Linux probes as needed under the host's lock; record commands, versions, configuration, logs and results. Do not silently retry the completed gate or treat a setup failure as a reproduction.

If possible, produce a repeatable fail-before at a pinned source revision, with the HTTP/1.1 control and evidence identifying the failure point. Explain differences from the first test and assess the cached HEAD hypothesis against the actual observed path. If the failure cannot be reproduced, record what was tried, what was observed, remaining uncertainties and the most useful next probe. Do not make a speculative production change or claim the issue fixed.

## Handoff and deliverables

Post an add-only report linking the claim, exact revisions, environment, commands, complete logs and test XML where applicable, protocol/connection observations, verdict and limitations. Provide a minimal reproducible command or test and a concrete implementation recommendation if the evidence supports one. Name any proposed test changes and their exact location.

Windows is willing to take the assignment back for the source implementation after the Linux host establishes the failure and hands back the evidence; the Linux host need not implement the fix. If the host cannot establish a failure or identify a defensible change, hand back the findings and remaining question rather than inventing a fix. Any subsequent production patch needs its own pushed head and a separate exact-head Linux gate covering fail-before, pass-after, HTTP/1.1 control, formatting, focused SolrJ test and root `check -x test`, per the original assignment. No PR, Jira comment or other public communication is authorized by this assignment.
