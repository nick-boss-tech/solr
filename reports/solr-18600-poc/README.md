# SolrJ 10.0 to 10.1 binary break: demo

A small app that builds a Solr HTTP client. It shows the break a user hits when they upgrade SolrJ from 10.0.0 to 10.1.0 without recompiling.

## Run

    ./run.sh

Needs JDK 21 (defaults to `/workspace/jdk/jdk-21.0.12.1+1`, or set `JAVA_HOME`).

## What it shows

1. Compile `src/demo/Upgrade.java` against SolrJ 10.0.0.
2. Run the compiled app on 10.0.0. It prints `client built`.
3. Run the same compiled bytes on 10.1.0, without recompiling. It fails with `NoSuchMethodError` on `HttpJdkSolrClient$Builder.withConnectionTimeout`.
4. Recompile against 10.1.0 and run. It prints `client built` again.

## Inputs

- `run.sh` fetches `solr-solrj` and `solr-api` for 10.0.0 and 10.1.0 from Maven Central into `lib/` on first run.
- `deps.txt` lists the other runtime jars, taken from the Gradle cache on this host. Paths are host-specific.

## Limits

- Only the HttpJdk builder call is covered.
- The app does not connect to a Solr server. It only builds the client.
- The run does not cover the reverse direction (10.1-compiled bytes on 10.0.0).
