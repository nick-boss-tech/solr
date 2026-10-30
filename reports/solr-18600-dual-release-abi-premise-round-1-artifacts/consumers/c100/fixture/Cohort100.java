package fixture;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import org.apache.solr.client.solrj.impl.CloudHttp2SolrClient;
import org.apache.solr.client.solrj.impl.ConcurrentUpdateBaseSolrClient;
import org.apache.solr.client.solrj.impl.HttpClusterStateProvider;
import org.apache.solr.client.solrj.impl.HttpJdkSolrClient;
import org.apache.solr.client.solrj.impl.LBSolrClient;
import org.apache.solr.client.solrj.jetty.HttpJettySolrClient;
import org.apache.solr.client.solrj.request.QueryRequest;
import org.apache.solr.common.params.ModifiableSolrParams;

public class Cohort100 {

  static void row(String name, String className) {
    try {
      Callable<?> c = (Callable<?>) Class.forName(className).getDeclaredConstructor().newInstance();
      c.call();
      System.out.println("ROW " + name + " OK");
    } catch (LinkageError e) {
      System.out.println("ROW " + name + " LINKAGE " + e.getClass().getName() + ": " + e.getMessage());
    } catch (Throwable t) {
      System.out.println("ROW " + name + " REACHED " + t.getClass().getName() + ": " + t.getMessage());
    }
  }

  static HttpJdkSolrClient jdk() {
    return new HttpJdkSolrClient.Builder().build();
  }

  static final String URL = "http://127.0.0.1:1/solr";

  static class R00 implements Callable<Object> {
    public Object call() throws Exception {
      return new HttpJdkSolrClient.Builder().withConnectionTimeout(5, TimeUnit.SECONDS).build();
    }
  }

  static class R01 implements Callable<Object> {
    public Object call() throws Exception {
      return new HttpJdkSolrClient.Builder().withRequestTimeout(5, TimeUnit.SECONDS).build();
    }
  }

  static class R02 implements Callable<Object> {
    public Object call() throws Exception {
      return new HttpJdkSolrClient.Builder().withHttpClient(jdk());
    }
  }

  static class R03 implements Callable<Object> {
    public Object call() throws Exception {
      org.apache.solr.client.solrj.impl.HttpSolrClientBuilderBase raw = new HttpJdkSolrClient.Builder(); return raw.withHttpClient((org.apache.solr.client.solrj.impl.HttpSolrClientBase) jdk());
    }
  }

  static class R04 implements Callable<Object> {
    public Object call() throws Exception {
      org.apache.solr.client.solrj.impl.HttpSolrClientBuilderBase<?, ?> b = new HttpJdkSolrClient.Builder(); return b.build();
    }
  }

  static class R05 implements Callable<Object> {
    public Object call() throws Exception {
      return jdk().requestWithBaseUrl(URL, new QueryRequest(new ModifiableSolrParams().set("q", "*:*")), null);
    }
  }

  static class R06 implements Callable<Object> {
    public Object call() throws Exception {
      return new HttpJettySolrClient.Builder().withConnectionTimeout(5, TimeUnit.SECONDS).build();
    }
  }

  static class R07 implements Callable<Object> {
    public Object call() throws Exception {
      CloudHttp2SolrClient c = new CloudHttp2SolrClient.Builder(List.of("127.0.0.1:1"), Optional.empty()).build(); org.apache.solr.client.solrj.impl.HttpSolrClientBase h = c.getHttpClient(); return h;
    }
  }

  static class R08 implements Callable<Object> {
    public Object call() throws Exception {
      return new CloudHttp2SolrClient.Builder(List.of("127.0.0.1:1"), Optional.empty()).withHttpClient((org.apache.solr.client.solrj.impl.HttpSolrClientBase) jdk());
    }
  }

  static class R09 implements Callable<Object> {
    public Object call() throws Exception {
      return new CloudHttp2SolrClient.Builder(List.of("127.0.0.1:1"), Optional.empty()).withHttpClientBuilder(new HttpJdkSolrClient.Builder());
    }
  }

  static class R10 implements Callable<Object> {
    public Object call() throws Exception {
      HttpClusterStateProvider<HttpJdkSolrClient> p = new HttpClusterStateProvider<>(List.of(URL), jdk()); return p.getHttpClient();
    }
  }

  static class R11 implements Callable<Object> {
    public Object call() throws Exception {
      return new ConcurrentUpdateBaseSolrClient.Builder(URL, jdk()) { @Override public ConcurrentUpdateBaseSolrClient build() { return null; } }.getClient();
    }
  }

  static class R12 implements Callable<Object> {
    public Object call() throws Exception {
      return new LBSolrClient.Builder<>(jdk(), new LBSolrClient.Endpoint(URL)).getSolrClient();
    }
  }

  static class R13 implements Callable<Object> {
    public Object call() throws Exception {
      return new QueryRequest(new ModifiableSolrParams().set("q", "*:*")).processWithBaseUrl(jdk(), URL, null);
    }
  }

  static class R14 implements Callable<Object> {
    public Object call() throws Exception {
      return new HttpJettySolrClient.Builder().withSSLConfig(null).build();
    }
  }

  static class R15 implements Callable<Object> {
    public Object call() throws Exception {
      return new CloudHttp2SolrClient.Builder(List.of("127.0.0.1:1"), Optional.empty()).withInternalClientBuilder(new HttpJdkSolrClient.Builder());
    }
  }

  static class R16 implements Callable<Object> {
    public Object call() throws Exception {
      HttpJettySolrClient c = new HttpJettySolrClient.Builder().build(); c.addListenerFactory((org.apache.solr.client.solrj.jetty.HttpListenerFactory) null); return c;
    }
  }

  public static void main(String[] args) {
    row("jdk.withConnectionTimeout.build", "fixture.Cohort100$R00");
    row("jdk.withRequestTimeout.build", "fixture.Cohort100$R01");
    row("jdk.withHttpClient.jdk", "fixture.Cohort100$R02");
    row("jdk.withHttpClient.base", "fixture.Cohort100$R03");
    row("jdk.builderBridge.build", "fixture.Cohort100$R04");
    row("jdk.requestWithBaseUrl", "fixture.Cohort100$R05");
    row("jetty.withConnectionTimeout.build", "fixture.Cohort100$R06");
    row("cloud.getHttpClient", "fixture.Cohort100$R07");
    row("cloud.withHttpClient.base", "fixture.Cohort100$R08");
    row("cloud.withHttpClientBuilder", "fixture.Cohort100$R09");
    row("clusterStateProvider.ctor.getHttpClient", "fixture.Cohort100$R10");
    row("concurrentUpdate.builder.getClient", "fixture.Cohort100$R11");
    row("lb.builder.getSolrClient", "fixture.Cohort100$R12");
    row("solrRequest.processWithBaseUrl", "fixture.Cohort100$R13");
    row("jetty.withSSLConfig.build", "fixture.Cohort100$R14");
    row("cloud.withInternalClientBuilder", "fixture.Cohort100$R15");
    row("jetty.addListenerFactory", "fixture.Cohort100$R16");
    System.exit(0);
  }
}
