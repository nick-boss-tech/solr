# Ref-guide (Antora) checks on a gate host

Status: guide, written by vm2 from the SOLR-17786 round-2 gate (2026-10-25). Verified on vm2, Linux aarch64, JDK 21.0.12.1+1.

Use this when a gate runs `:solr:solr-ref-guide:check` or `:solr:solr-ref-guide:buildLocalAntoraSite`. Two failures there cost vm2 about an hour and do not come from the branch under test.

## Failure 1: `Cannot find module '@asciidoctor/tabs'`

Cause: `NODE_ENV=production` is set in the host shell. npm then omits devDependencies, and `@asciidoctor/tabs` is a devDependency (`solr/solr-ref-guide/build.gradle`, task `downloadAsciidoctorTabsExtension`). The Gradle download task reports success, but npm never installs the package. `npm config get omit` prints `dev` when this is the case.

Fix: unset the variable before Gradle runs.

    unset NODE_ENV npm_config_omit NPM_CONFIG_OMIT
    ./gradlew :solr:solr-ref-guide:check -x test

Check it worked: `ls solr/solr-ref-guide/.gradle/node/node_modules/@asciidoctor/` must list `tabs`.

Do not repair this by hand with `npm install` while `NODE_ENV` is still set. That reported success and left the directory missing.

## Failure 2: `Local content source must be a git repository`

Cause: Antora reads the repository root and requires a `.git` directory. A `git worktree` has a `.git` file that points elsewhere, so Antora rejects it.

Fix: run the ref-guide task in a full clone that shares the object store with the main checkout, not in a worktree.

    git clone -q --shared /workspace/solr /workspace/gates/gates-vm2/clone-<ticket>-<tag>
    cd /workspace/gates/gates-vm2/clone-<ticket>-<tag> && git checkout -q <head-sha>
    ./gradlew :solr:solr-ref-guide:check -x test

Remove the clone when the gate is finished.

## Failure 3: the same error on the base commit

Before you blame the branch, run `buildLocalAntoraSite` on the base commit (its merge base with main, in a clone). If it fails the same way, the cause is the host or the build, not the change under test. On 2026-10-25 vm2's base run failed with failure 1, which is why it looked like a branch problem for a while.

## Timing and locks

- The ref-guide check takes about 6 minutes on its own (base run: 5m59s), plus Gradle startup, and a fresh node install adds more.
- Run it under the host build lock, like every other gate leg: `exec 9>/workspace/tools/.gradle-vm2.lock; flock 9`.
- Node downloads are cached under `solr/solr-ref-guide/.gradle/node`, so the second run is faster.

## Process lessons

- Do not run `pkill -f <pattern>` with a pattern that matches your own shell's command line. It killed the shell (exit 144). Find PIDs with `ps -eo pid,args | grep "[p]attern"` (the bracket trick), then `kill` them.
- Test a suspected host cause against the base commit before you write it up as a branch finding.
