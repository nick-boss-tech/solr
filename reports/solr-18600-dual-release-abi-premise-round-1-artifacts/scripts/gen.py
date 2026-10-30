import os
HDR_IMPORTS = """import java.util.List;
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
"""
COMMON_TAIL = """
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
"""
def gen(cls, extra_imports, rows):
    body = []
    calls = []
    for i, (name, code) in enumerate(rows):
        rc = f"R{i:02d}"
        body.append(f"  static class {rc} implements Callable<Object> {{\n    public Object call() throws Exception {{\n      {code}\n    }}\n  }}\n")
        calls.append(f'    row("{name}", "fixture.{cls}${rc}");')
    src = f"package fixture;\n\n{HDR_IMPORTS}{extra_imports}\npublic class {cls} {{\n{COMMON_TAIL}\n" + "\n".join(body) + "\n  public static void main(String[] args) {\n" + "\n".join(calls) + "\n    System.exit(0);\n  }\n}\n"
    return src
JDK_TB = "new HttpJdkSolrClient.Builder()"
rows100 = [
 ("jdk.withConnectionTimeout.build", f"return {JDK_TB}.withConnectionTimeout(5, TimeUnit.SECONDS).build();"),
 ("jdk.withRequestTimeout.build", f"return {JDK_TB}.withRequestTimeout(5, TimeUnit.SECONDS).build();"),
 ("jdk.withHttpClient.jdk", f"return {JDK_TB}.withHttpClient(jdk());"),
 ("jdk.withHttpClient.base", "org.apache.solr.client.solrj.impl.HttpSolrClientBuilderBase raw = new HttpJdkSolrClient.Builder(); return raw.withHttpClient((org.apache.solr.client.solrj.impl.HttpSolrClientBase) jdk());"),
 ("jdk.builderBridge.build", f"org.apache.solr.client.solrj.impl.HttpSolrClientBuilderBase<?, ?> b = {JDK_TB}; return b.build();"),
 ("jdk.requestWithBaseUrl", "return jdk().requestWithBaseUrl(URL, new QueryRequest(new ModifiableSolrParams().set(\"q\", \"*:*\")), null);"),
 ("jetty.withConnectionTimeout.build", "return new HttpJettySolrClient.Builder().withConnectionTimeout(5, TimeUnit.SECONDS).build();"),
 ("cloud.getHttpClient", "CloudHttp2SolrClient c = new CloudHttp2SolrClient.Builder(List.of(\"127.0.0.1:1\"), Optional.empty()).build(); org.apache.solr.client.solrj.impl.HttpSolrClientBase h = c.getHttpClient(); return h;"),
 ("cloud.withHttpClient.base", "return new CloudHttp2SolrClient.Builder(List.of(\"127.0.0.1:1\"), Optional.empty()).withHttpClient((org.apache.solr.client.solrj.impl.HttpSolrClientBase) jdk());"),
 ("cloud.withHttpClientBuilder", f"return new CloudHttp2SolrClient.Builder(List.of(\"127.0.0.1:1\"), Optional.empty()).withHttpClientBuilder({JDK_TB});"),
 ("clusterStateProvider.ctor.getHttpClient", "HttpClusterStateProvider<HttpJdkSolrClient> p = new HttpClusterStateProvider<>(List.of(URL), jdk()); return p.getHttpClient();"),
 ("concurrentUpdate.builder.getClient", "return new ConcurrentUpdateBaseSolrClient.Builder(URL, jdk()) { @Override public ConcurrentUpdateBaseSolrClient build() { return null; } }.getClient();"),
 ("lb.builder.getSolrClient", "return new LBSolrClient.Builder<>(jdk(), new LBSolrClient.Endpoint(URL)).getSolrClient();"),
 ("solrRequest.processWithBaseUrl", "return new QueryRequest(new ModifiableSolrParams().set(\"q\", \"*:*\")).processWithBaseUrl(jdk(), URL, null);"),
 ("jetty.withSSLConfig.build", "return new HttpJettySolrClient.Builder().withSSLConfig(null).build();"),
 ("cloud.withInternalClientBuilder", f"return new CloudHttp2SolrClient.Builder(List.of(\"127.0.0.1:1\"), Optional.empty()).withInternalClientBuilder({JDK_TB});"),
 ("jetty.addListenerFactory", "HttpJettySolrClient c = new HttpJettySolrClient.Builder().build(); c.addListenerFactory((org.apache.solr.client.solrj.jetty.HttpListenerFactory) null); return c;"),
]
rows101 = [
 ("jdk.withConnectionTimeout.build", f"return {JDK_TB}.withConnectionTimeout(5, TimeUnit.SECONDS).build();"),
 ("jdk.withRequestTimeout.build", f"return {JDK_TB}.withRequestTimeout(5, TimeUnit.SECONDS).build();"),
 ("jdk.withHttpClient.jdk", f"return {JDK_TB}.withHttpClient(jdk());"),
 ("jdk.withHttpClient.base", "org.apache.solr.client.solrj.impl.HttpSolrClient.BuilderBase raw = new HttpJdkSolrClient.Builder(); return raw.withHttpClient((org.apache.solr.client.solrj.impl.HttpSolrClient) jdk());"),
 ("jdk.withHttpClient.jdkSubtype", f"return {JDK_TB}.withHttpClient(jdk()).build();"),
 ("jdk.builderBridge.build", f"org.apache.solr.client.solrj.impl.HttpSolrClient.BuilderBase<?, ?> b = {JDK_TB}; return b.build();"),
 ("jdk.requestWithBaseUrl", "return jdk().requestWithBaseUrl(URL, new QueryRequest(new ModifiableSolrParams().set(\"q\", \"*:*\")), null);"),
 ("jetty.withConnectionTimeout.build", "return new HttpJettySolrClient.Builder().withConnectionTimeout(5, TimeUnit.SECONDS).build();"),
 ("cloud.getHttpClient", "CloudHttp2SolrClient c = new CloudHttp2SolrClient.Builder(List.of(\"127.0.0.1:1\"), Optional.empty()).build(); org.apache.solr.client.solrj.impl.HttpSolrClient h = c.getHttpClient(); return h;"),
 ("cloud.withHttpClient.base", "return new CloudHttp2SolrClient.Builder(List.of(\"127.0.0.1:1\"), Optional.empty()).withHttpClient((org.apache.solr.client.solrj.impl.HttpSolrClient) jdk());"),
 ("cloud.withHttpClientBuilder", f"return new CloudHttp2SolrClient.Builder(List.of(\"127.0.0.1:1\"), Optional.empty()).withHttpClientBuilder({JDK_TB});"),
 ("clusterStateProvider.ctor.getHttpClient", "HttpClusterStateProvider<HttpJdkSolrClient> p = new HttpClusterStateProvider<>(List.of(URL), jdk()); return p.getHttpClient();"),
 ("concurrentUpdate.builder.getClient", "return new ConcurrentUpdateBaseSolrClient.Builder(URL, jdk()) { @Override public ConcurrentUpdateBaseSolrClient build() { return null; } }.getClient();"),
 ("concurrentUpdateJdk.builder.build", "return new org.apache.solr.client.solrj.impl.ConcurrentUpdateJdkSolrClient.Builder(URL, jdk()).build();"),
 ("lb.builder.getSolrClient", "return new LBSolrClient.Builder<>(jdk(), new LBSolrClient.Endpoint(URL)).getSolrClient();"),
 ("solrRequest.processWithBaseUrl", "return new QueryRequest(new ModifiableSolrParams().set(\"q\", \"*:*\")).processWithBaseUrl(jdk(), URL, null);"),
]
open('c100/fixture/Cohort100.java','w').write(gen('Cohort100', '', rows100))
open('c101/fixture/Cohort101.java','w').write(gen('Cohort101', '', rows101))
print('generated', len(rows100), len(rows101))
