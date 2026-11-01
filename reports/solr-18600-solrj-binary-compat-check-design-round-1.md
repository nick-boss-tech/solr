# Design: growing SOLR-18600 into a SolrJ binary-compatibility check in the build

Design record only, 2026-11-01. No code written, no build run, no PR
opening, no Jira post. The question it answers: what would have caught
the 10.1.0 break (removal of `HttpSolrClientBase` and
`HttpSolrClientBuilderBase`) before release, and what is the smallest
first slice worth building.

## Why the existing guards are not enough

The SOLR-18600 branch added descriptor guard tests
(`HttpSolrClientBinaryCompatTest`, `HttpJettySolrClientBinaryCompatTest`).
They assert the restored base types exist with the members that code
compiled against 10.0.0 links against, and they run as ordinary unit
tests. They guard exactly the cluster named in the ticket. They do not
scan anything else, so the next removal in a different SolrJ corner
passes them silently. A build check generalizes the guard from one named
cluster to the whole SolrJ API surface.

## What it scans

- The built `solrj` jar (and, in a later slice, `solrj-zookeeper` and
  `solrj-jetty`, which ship SolrJ client types users compile against),
  compared against the previous minor release's published jar.
- Scope is the public and protected API surface: public classes,
  interfaces, enums, annotations, and their public and protected members,
  including types users subclass. That last clause matters: the 18600
  break was in base classes users extend, not in a factory method they
  call, so a check that only scans method-call signatures would miss the
  shape of break that actually happened.
- Baseline source: the previous release jar resolved from Maven Central
  as an ordinary Gradle dependency, with the baseline version carried as
  a Gradle property (defaulting to the previous minor line), not
  hardcoded in the task.

## What it fails on

Binary incompatibilities in the JLS sense, plus the one
source-incompatible shape SolrJ subclassing makes load-bearing:

- A removed public or protected class, method, field, or constructor.
- A changed method or constructor signature, including a changed return
  type or thrown checked exception set.
- Reduced visibility (public to protected or package-private, protected
  to package-private).
- An added abstract method on a non-final public class, which breaks
  existing subclasses at compile time even though it is binary
  compatible. This is the clause that covers the inverse of 18600: a
  future base-class change that forces every user subclass to change.
- A changed compile-time constant value (inlined into consumer bytecode,
  so a silent value change is a real break).

It does not fail on: added classes, methods, or fields; widened
visibility; implementation-only changes; or changes inside packages
marked internal, unless an allowlist entry says otherwise.

## Where it would live in the build

- Tooling: japicmp (or revapi; japicmp is the smaller dependency and its
  report format maps directly onto the failure list above). Neither is
  wired into the Solr build today; a whole-tree grep for japicmp and
  revapi in `*.gradle` files returns nothing.
- Home: a new task in `gradle/validation/`, alongside forbiddenapis and
  ecj lint, applied to the `solrj` project, and wired into
  `:solr:solrj:check` so it runs with the module check that is the
  project's gate evidence. It runs after `jar`, compares the built jar
  against the resolved baseline jar, and writes a human-readable report
  next to the task output.
- Control surface: one allowlist file (for example
  `solr/solrj/binary-compat-exclusions.txt`) listing intentional breaks
  with a ticket reference per entry. An intentional break with no entry
  fails the build; an entry with no matching break is itself a warning,
  so the list cannot rot silently. Major-version lines may disable the
  check by property, since a major release is allowed to break API, but
  the default on a minor line is on.
- Baseline plumbing: the task resolves the baseline jar through the
  normal dependency cache, so it works through the same egress path as
  every other Gradle download. No checked-in baseline jars.

## Smallest first slice

1. A japicmp task scoped to `solrj` only, baseline previous release jar,
   failing only on removals, signature changes, and visibility
   reductions (the abstract-method and constant clauses come second,
   after the first slice proves the wiring).
2. The allowlist file, starting empty, with the entry format documented
   in a comment at its top.
3. Wiring into `:solr:solrj:check` only, not into precommit, so the first
   runs are visible without blocking unrelated work.
4. Validation of the slice itself, before it is trusted: run it with
   baseline 10.0.0 against the 10.1.0 published jar and confirm it
   reports the `HttpSolrClientBase` and `HttpSolrClientBuilderBase`
   removals; run it with baseline 10.0.0 against the SOLR-18600 branch
   jar and confirm it passes. The second leg is the proof that the fix
   restores compatibility, not just that the tool runs.

## Open questions for the implementation assignment

- Which SolrJ-adjacent modules join after solrj: solrj-zookeeper and
  solrj-jetty first, core never (core is not a user compile-time
  dependency in the same sense).
- Whether the baseline should track the previous minor release or the
  previous release on the same branch line; branch_10x and main diverge
  here, and the property default has to pick one.
- How the check behaves on a release branch building its own first
  release, where no previous baseline on that line exists yet.
- Whether the allowlist lives per module or once at the root; per module
  keeps entries next to the code they excuse.
