# Gate: SOLR-18610 web identity policy regression test, round 1

Status: OPEN

- Tags: gate, implementation, review
- Intended host: any capable Linux gate host with JDK 21 and this branch's build dependencies, serialized under its host build lock
- Posted by: windows, 2026-10-29T14:55:45Z
- Implementation claim: claims/pr-5264-web-identity-test-round-1-windows.md
- Ticket: SOLR-18610; public PR 5264 is another contributor's PR
- Fork: nick-boss-tech/solr
- Branch: solr-18610-submit
- Exact head: dbacd6951e7692d1b55920eddb239284df33b3b0
- Contributor ancestor: dbb3d85d72dd27889d8b93f10d1f4c2f54aa5e0e
- Original-policy base: 3db71000514e1edaa8e6ae0426fce92ca97990e3
- Module: :solr:modules:s3-repository
- Class: org.apache.solr.s3.S3WebIdentitySecurityManagerTest
- Method: testWebIdentityCredentialsWithSecurityManager
- Seed: 18610C0DE
- Receipt: receipts/SOLR-18610.md, owned by the executing gate host

## Scope and ownership

Nick directly requested a regression for the S3 suite's disabled Security Manager. The Windows host added one test file and pushed it; no compilation or runtime proof was performed there. The existing S3 Testcontainers suite remains unchanged. Do not comment publicly or modify the contributor's branch. Repair test scaffolding on Nick's fork if required and document every repair. Preserve contributor ancestry.

Before claiming, use the current board instructions, verify the remote head and existing claims/receipt, and do the detached tidy preflight. Claim this gate with runner/log paths and JDK identity before execution. One gate per host. Do not count a skipped test or setup failure as a pass or fail-before proof.

## Required proof

1. At the final head, run the focused test on Java 21:

   `./gradlew :solr:modules:s3-repository:test --tests org.apache.solr.s3.S3WebIdentitySecurityManagerTest -Ptests.seed=18610C0DE --rerun-tasks`

   Fresh XML must show one executed passing method, zero failures/errors/skips. The child JVM must have an active Security Manager and use the shipped server/etc/security.policy, with exclusive policy loading. The assertion must resolve the stub's access key, secret and session token and observe the web identity request at the local STS endpoint. No Docker, EKS, live AWS account, real credentials or network STS endpoint is needed or permitted for this proof.

2. Fail-before with the SAME test and runtime dependencies: in a separate proof checkout at the final head, replace ONLY solr/server/etc/security.policy with the version from 3db71000514e1edaa8e6ae0426fce92ca97990e3. Keep the test unchanged and run the exact focused command and seed. Require one executed failure caused by the AWS Apache5HttpClient NetworkPermission check for TCP_KEEPIDLE/TCP_KEEPINTERVAL/TCP_KEEPCOUNT. Classpath, endpoint, policy parsing, missing-file, timeout and compilation failures do not qualify. Record the proof checkout and precise diff; do not commit this mutation. Remove no other production code and do not weaken the Security Manager.

3. At the final submitted head run `./gradlew tidy` and `./gradlew check -x test`. If tidy changes files, commit and push the scoped formatting repair, record old/new SHAs, and obtain fresh focused pass and original-policy failure at the new head before issuing the receipt. Record any environment limitation and actual task result rather than relabelling an incomplete check as green.

4. Verify the remote fork head still matches the tested final head. Write the receipt with exact commands, JDK, seed, timings, logs, fresh XML counts, negative cause and any repair commits. Add a completion report identifying this gate. The public PR remains at the contributor's head unless its owner independently changes it.

## Implementation details to inspect

- New test uses SolrTestCase and ExternalPaths to locate the actual shipped policy, not a copied fixture.
- The local STS fixture is the JDK HTTP server. No existing reusable STS fixture was found in the module; S3MockContainerRule requires Docker and does not implement this credential flow.
- The child is modelled on the existing SolrProcessManagerTest ProcessBuilder pattern and uses the test JVM classpath. Confirm this classpath is complete in the actual randomized test runner. If not, repair with minimal deterministic runtime-classpath wiring; do not infer success from a missing child main.
- The child has isolated user.home and removes inherited AWS variables, proxy variables and JVM injection options. It uses an explicit region and the SDK's aws.endpointUrl global property to target the local stub. Verify the pinned SDK honors this configuration.
- The child main deliberately uses System.getSecurityManager with a targeted SuppressForbidden annotation. Check forbidden API and compiler diagnostics, including JDK HTTP server access.
- Java 24+ skips the method because the Security Manager cannot be enabled there. A Java 24+ skip does not settle this gate; Java 21 execution is required.
- If the positive run fails, diagnose from probe.log (included in assertion output). Do not add the three TCP grants to an extra test policy, switch the HTTP client to Apache 4, disable the Security Manager or weaken the credential assertions to obtain a green result.

No prior gate has run for this test. Status remains Implemented, Proof Pending until the above evidence is complete.
