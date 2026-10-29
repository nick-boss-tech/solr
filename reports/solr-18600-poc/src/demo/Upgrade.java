package demo;

import java.util.concurrent.TimeUnit;
import org.apache.solr.client.solrj.impl.HttpJdkSolrClient;

public class Upgrade {
  public static void main(String[] args) throws Exception {
    String url = args.length > 0 ? args[0] : "http://127.0.0.1:8983/solr";
    try (HttpJdkSolrClient client =
        new HttpJdkSolrClient.Builder(url)
            .withConnectionTimeout(10, TimeUnit.SECONDS)
            .withRequestTimeout(60, TimeUnit.SECONDS)
            .build()) {
      System.out.println("client built: " + client.getClass().getName());
    }
  }
}
