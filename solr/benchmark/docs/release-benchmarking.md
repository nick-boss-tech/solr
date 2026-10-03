<!--
    Licensed to the Apache Software Foundation (ASF) under one or more
    contributor license agreements.  See the NOTICE file distributed with
    this work for additional information regarding copyright ownership.
    The ASF licenses this file to You under the Apache License, Version 2.0
    (the "License"); you may not use this file except in compliance with
    the License.  You may obtain a copy of the License at

      http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
-->

# Release Benchmark Guidance

This module already provides the benchmark harness. The remaining release work
is choosing a small, repeatable benchmark set and publishing the results in a
way that is easy to compare across releases.

## Keep The Scope Small

Release-facing benchmarks should favor a few representative scenarios over a
large, noisy matrix. A good first pass is to cover one or two benchmarks from
each of these areas:

- indexing
- search
- faceting
- cache behavior
- startup or lifecycle

## Start From Existing Benchmarks

The repository already has benchmark classes that can be used as a release
baseline:

| Area | Benchmark class |
| --- | --- |
| indexing | `org.apache.solr.bench.index.CloudIndexing` |
| search | `org.apache.solr.bench.search.SimpleSearch`, `NumericSearch`, `ExitableDirectoryReaderSearch` |
| streaming | `org.apache.solr.bench.search.StreamingSearch` |
| faceting | `org.apache.solr.bench.search.JsonFaceting` |
| cache behavior | `org.apache.solr.bench.search.FilterCache` |
| startup or lifecycle | `org.apache.solr.bench.lifecycle.SolrStartup` |

These are useful because they already exercise common Solr feature families and
they are easier to compare across branches than one-off ad hoc scripts.

## Make Runs Repeatable

When you prepare a release benchmark pass, keep the following stable:

- the input corpus or generated data parameters
- the benchmark filter used to select the classes
- the JMH fork and iteration settings
- the JVM under test
- the output format and result filename

For release comparisons, prefer machine-readable output such as CSV or JSON so
results can be archived and diffed later. For example, from the `solr/benchmark`
directory, with one fork, five warmup and five measurement iterations, and the
result written to a file named for the build under test:

```zsh
./jmh.sh SimpleSearch JsonFaceting FilterCache -f 1 -wi 5 -i 5 -rf json -rff work/jmh-<build>.json
```

Run the same command on each build and compare the `primaryMetric.score` of the
matching benchmark and parameter entries in the two JSON files.

## Suggested Workflow

1. Pick a narrow benchmark matrix from the existing classes.
2. Run the same matrix on both branches or builds you want to compare.
3. Save the output with `-rf` and `-rff`.
4. Record the environment details next to the results.
5. If a benchmark is noisy or unstable, shrink the scope rather than trying to
   make the entire release pass harder to run.

## When To Add New Benchmarks

Add a new benchmark only if a release-critical behavior is not already covered
by the current module. If an existing benchmark already measures the right
feature family, prefer parameterizing or reusing it instead of creating a new
entry point.
