package org.apache.solr.handler.admin.api;

import org.apache.solr.client.solrj.SolrRequest;
import org.apache.solr.client.solrj.jetty.HttpJettySolrClient;
import org.apache.solr.client.solrj.request.CollectionAdminRequest;
import org.apache.solr.client.solrj.request.V2Request;
import org.apache.solr.cloud.SolrCloudTestCase;
import org.apache.solr.common.cloud.Replica;
import org.apache.solr.embedded.JettySolrRunner;
import org.junit.BeforeClass;
import org.junit.Test;

public class ProbeRoutingTest extends SolrCloudTestCase {

  @BeforeClass
  public static void setupCluster() throws Exception {
    configureCluster(2).addConfig("conf", configset("cloud-minimal")).configure();
  }

  @Test
  public void probe() throws Exception {
    split("probeHostedNode", "hosted");
    split("probeOtherNode", "other");
  }

  private static void split(String coll, String label) throws Exception {
    CollectionAdminRequest.createCollection(coll, "conf", 1, 1).process(cluster.getSolrClient());
    cluster.waitForActiveCollection(coll, 1, 1);
    Replica r = cluster.getSolrClient().getClusterState().getCollection(coll).getSlice("shard1").getReplicas().iterator().next();
    JettySolrRunner host = null, other = null;
    for (JettySolrRunner j : cluster.getJettySolrRunners()) {
      if (j.getNodeName().equals(r.getNodeName())) host = j; else other = j;
    }
    JettySolrRunner target = label.equals("hosted") ? host : other;
    System.out.println("PROBE " + label + " collection node=" + r.getNodeName() + " target=" + target.getNodeName());
    V2Request req = new V2Request.Builder("/collections/" + coll + "/shards/shard1/split")
        .withMethod(SolrRequest.METHOD.POST).withPayload("{\"numSubShards\": 2}").build();
    String result;
    try (HttpJettySolrClient c = new HttpJettySolrClient.Builder(target.getBaseUrl().toString()).build()) {
      c.request(req);
      result = "OK";
    } catch (Exception ex) {
      String m = String.valueOf(ex.getMessage());
      result = ex.getClass().getSimpleName() + ": " + m.substring(0, Math.min(160, m.length()));
    }
    System.out.println("PROBE " + label + " default split " + result);
  }
}
