h2. Summary

SolrJ 10.1.0 removed the public classes {{org.apache.solr.client.solrj.impl.HttpSolrClientBase}} and {{org.apache.solr.client.solrj.impl.HttpSolrClientBuilderBase}} (SOLR-17968). Code that was compiled against SolrJ 10.0.0 and uses only the ordinary {{HttpJdkSolrClient.Builder}} methods fails at runtime on 10.1.0 with a {{NoSuchMethodError}} on the first builder call, before any HTTP request is made. Recompiling the same source against 10.1.0 fixes it, so source compatibility holds; binary compatibility does not. The 10.1 upgrade notes mention the new base class but do not mention the removal or the need to recompile.

h2. Minimal reproducer

{code}
import java.util.concurrent.TimeUnit;
import org.apache.solr.client.solrj.impl.HttpJdkSolrClient;

public class Consumer {
  public static void main(String[] args) throws Exception {
    try (HttpJdkSolrClient client =
        new HttpJdkSolrClient.Builder("http://127.0.0.1:8983/solr")
            .withConnectionTimeout(10, TimeUnit.SECONDS)
            .withRequestTimeout(60, TimeUnit.SECONDS)
            .build()) {
      System.out.println("BUILT " + client.getClass().getName());
    }
    System.out.println("CONSUMER OK");
  }
}
{code}

The source names no base type and uses no internal API. Compiled against 10.0.0, {{javap}} shows the fluent calls carry descriptors naming the removed class, for example {{HttpJdkSolrClient$Builder.withConnectionTimeout:(JLjava/util/concurrent/TimeUnit;)Lorg/apache/solr/client/solrj/impl/HttpSolrClientBuilderBase;}}, because the methods are declared on the removed base class and javac emits its erased descriptor.

h2. Tested coordinates

* {{org.apache.solr:solr-solrj:10.0.0}} (jar sha256 {{59178b49e43ffed3d4845b73c0b3302bd77e1b0e3c10c5c4550ba8499bdb3fac}}): contains both classes.
* {{org.apache.solr:solr-solrj:10.1.0}} (jar sha256 {{115fcfbe1ed8ce04f7da8e200d20ce9105a567b40d6ec33727fcbd0667118bb4}}): contains neither class; first published release with the removal.
* Consumer class compiled with {{javac --release 17}} against 10.0.0, sha256 {{a3bb85e5a58d39e226627df1340844d758471e48b7514ac137364cdea485801f}}. JDK used for the runs: Temurin 21.0.12.1. Runtime classpath: solrj, solr-api, slf4j-api. No server is needed; the failure precedes any HTTP operation, and the Jackson change in SOLR-18325 is not required to reproduce it.

h2. Expected versus observed

* Expected: bytes compiled against 10.0.0 run against 10.1.0, or the removal is called out in the 10.1 upgrade notes as requiring recompilation.
* Observed on 10.0.0: {{BUILT org.apache.solr.client.solrj.impl.HttpJdkSolrClient}} and {{CONSUMER OK}}, exit 0.
* Observed on 10.1.0, identical bytes: exit 1 with

{noformat}
java.lang.NoSuchMethodError: 'org.apache.solr.client.solrj.impl.HttpSolrClientBuilderBase org.apache.solr.client.solrj.impl.HttpJdkSolrClient$Builder.withConnectionTimeout(long, java.util.concurrent.TimeUnit)'
        at Consumer.main(Consumer.java:8)
{noformat}

* The identical source recompiled against 10.1.0 compiles and runs correctly, which isolates the break to binary compatibility. A control consumer that uses only {{Builder.withHttpClient(...)}} (whose descriptors name only concrete types) runs unchanged on both versions.

h2. Context and open question

SOLR-17968 and [PR #3829|https://github.com/apache/solr/pull/3829] renamed the classes deliberately (main {{8d3f995c6a}}, branch_10x {{e5340a954b}}). Neither the ticket, the PR discussion, nor the upgrade notes mention the effect on already-compiled consumers. In 10.0.0, {{HttpSolrClientBase}} is marked {{@lucene.internal}} in its Javadoc; {{HttpSolrClientBuilderBase}}, the class ordinary bytecode actually names, is not. Whether that makes the break acceptable in a minor release is the policy question this ticket poses; the runtime behavior above is the observed defect either way.

Possible directions, kept separate from the defect report: restore the two classes as compatibility types so the released descriptors resolve, or document the removal and the recompile requirement in the 10.1 upgrade notes if the break is accepted.

🤖 _AI text above_ 🤖 _(reviewed by Nick Shanin)_
