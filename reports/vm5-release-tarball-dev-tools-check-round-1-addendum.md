# Release tarball check, round 1: addendum with exact commands and evidence (vm5)

Adds to `reports/vm5-release-tarball-dev-tools-check-round-1.md`. The verdict there stands.

## Exact commands

Base and scratch (scratch is local only, never pushed):

    git worktree add --detach /workspace/worktrees/vm5-tarball-pr 58d9573ae0e20e492f9bbb490415e750aa9e38c3
    cd /workspace/worktrees/vm5-tarball-pr && git checkout -b scratch-dev-tools-export-ignore
    # edit .gitattributes: add the line "/dev-tools        export-ignore" after "/dev-docs         export-ignore"
    git commit -am "scratch: export-ignore dev-tools (test scaffolding, not pushed)"   # ba22b3eb270

Archives and listings:

    git archive --format=tar -o solr-scratch-noDevTools.tar ba22b3eb270
    git archive --format=tar -o solr-pr-unmodified.tar 58d9573ae0e
    tar -tf solr-scratch-noDevTools.tar | sort > listing-scratch.txt
    tar -tf solr-pr-unmodified.tar | sort > listing-pr.txt
    comm -13 listing-scratch.txt listing-pr.txt    # 39 lines, all dev-tools/

Clean unpack (no .git):

    mkdir unpack-scratch && tar -xf solr-scratch-noDevTools.tar -C unpack-scratch

RAT, then the full check, both from the unpacked tree, under the host lock, with the vm5 toolchain:

    export JAVA_HOME=/workspace/jdk/jdk-21.0.12.1+1 GRADLE_USER_HOME=/workspace/gradle-home
    flock /workspace/tools/.gradle-vm5.lock ./gradlew rat --no-daemon
    flock /workspace/tools/.gradle-vm5.lock ./gradlew check --continue --no-daemon

The listing diff itself is committed as `reports/vm5-release-tarball-dev-tools-check-round-1-listing-diff.txt`.

## Failure excerpts (verbatim from `check.log`)

    * What went wrong:
    Execution failed for task ':allHelpFilesExit' (registered in script 'gradle/help.gradle').
    > Help file missing: dev-docs/gradle-help/workflow.txt (correct help.gradle)

    * What went wrong:
    Execution failed for task ':solr:core:test'.
    > There were failing tests. See the results at: file:///workspace/smoke-rc3/unpack-scratch/solr/core/build/test-results/test/

    BUILD FAILED in 1h 56m 36s
    567 actionable tasks: 527 executed, 40 up-to-date

## Where the raw logs are

These live on vm5's `/workspace` and are not on the branch (they are large):

- `/workspace/smoke-rc3/tarball/` for the two tarballs and both listings
- `/workspace/smoke-rc3/tarball-logs/rat.log` (RAT: rc=0, 3m 32s)
- `/workspace/smoke-rc3/tarball-logs/check.log` (full check: rc=1)
- `/workspace/smoke-rc3/unpack-scratch/` (the unpacked tree, 1.4 GB, with `solr/core/build/test-results/test/` XML)

## Not done

- No check of the unmodified PR archive; its failure on `:allHelpFilesExit` is inferred from the listing and `gradle/help.gradle`, not run.
- The `solr:core` failures match the RC3 loop's five methods, and the RC3 loop findings (`reports/rc3-smoke-loop-round-1.md`) carry the detail.
