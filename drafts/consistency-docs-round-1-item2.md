# PR draft: V2 list collection backups declares the read permission

Branch: `consistency-v2-list-backups-permission` (head e3f35fe42c on the fork, based on 498a09aea9). No PR opened.

## What happens today

The V2 endpoint that lists collection backups declares `COLL_EDIT_PERM` through `@PermissionName` in `ListCollectionBackups.java`. A listing is a read, so a role that holds collection read permission and no edit permission cannot list backups. The sibling `ListCollectionSnapshots.java` declares `COLL_READ_PERM` for its listing, and the V1 `LISTBACKUP` action, which this endpoint mirrors, is read-only.

## What this change does

The list route now declares `COLL_READ_PERM`. The handler and the response are unchanged. Only the declared permission changes. A role with collection read permission can now list backups. A role with edit permission still can. The change widens access for read-only roles, which is the behavior the sibling endpoints already have.

## Proof

The core module compiles at head e3f35fe42c: `./gradlew :solr:core:compileJava`, log `/workspace/gates/logs/vm4/consistency-item2-compile.log`, BUILD SUCCESSFUL, 2026-10-19. No test in the tree asserts the declared permission for this route, so no test changed. No test fails on the base and passes with the change. The proof is limited to compilation by construction.

## Question for the maintainer

Is `COLL_READ_PERM` the intended declaration for listing collection backups, matching `ListCollectionSnapshots`?
