import java.nio.file.*;
import java.util.*;
import org.apache.solr.client.solrj.embedded.EmbeddedSolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.common.params.ModifiableSolrParams;
import org.apache.solr.core.CoreContainer;
import org.apache.solr.core.SolrCore;
import org.apache.solr.update.IndexFingerprint;

public class Bench {
  public static void main(String[] args) throws Exception {
    Path home = Paths.get(args[0]);
    int numDocs = Integer.parseInt(args[1]);
    double delRatio = Double.parseDouble(args[2]);
    String phase = args[3];
    Path out = Paths.get(args[4]);
    Path conf = Paths.get(args[5]);
    int rep = Integer.parseInt(args[6]);
    Files.createDirectories(home);
    Files.writeString(home.resolve("solr.xml"), "<solr></solr>");
    Path coreDir = home.resolve("bench");
    if (!Files.exists(coreDir.resolve("conf"))) {
      copyTree(conf, coreDir.resolve("conf"));
      Files.writeString(coreDir.resolve("core.properties"), "name=bench\n");
    }
    CoreContainer cc = CoreContainer.createAndLoad(home);
    try (EmbeddedSolrServer client = new EmbeddedSolrServer(cc, "bench")) {
      if (client.query(new ModifiableSolrParams().add("q", "*:*")).getResults().getNumFound() == 0) {
        for (int i = 0; i < numDocs; i++) {
          SolrInputDocument d = new SolrInputDocument();
          d.addField("id", "d" + i);
          d.addField("title", "doc " + i + " term" + (i % 97));
          client.add(d);
          if (i % 5000 == 4999) client.commit();
        }
        client.commit();
        Random r = new Random(18507L);
        int deletes = (int) Math.round(numDocs * delRatio);
        Set<Integer> chosen = new HashSet<>();
        while (chosen.size() < deletes) chosen.add(r.nextInt(numDocs));
        for (int id : chosen) client.deleteById("d" + id);
        client.commit();
      }
    }
    cc.shutdown();
    cc = CoreContainer.createAndLoad(home);
    try (SolrCore core = cc.getCore("bench")) {
      long t0 = System.nanoTime();
      IndexFingerprint f = IndexFingerprint.getFingerprint(core, Long.MAX_VALUE);
      double cold = (System.nanoTime() - t0) / 1e6;
      long live = f.getNumDocs();
      Files.writeString(out, String.format("%s,%d,%.2f,cold,%d,%.3f,%d%n", phase, numDocs, delRatio, rep, cold, live), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
      for (int w = 1; w <= 3; w++) {
        long t1 = System.nanoTime();
        IndexFingerprint g = IndexFingerprint.getFingerprint(core, Long.MAX_VALUE);
        double warm = (System.nanoTime() - t1) / 1e6;
        Files.writeString(out, String.format("%s,%d,%.2f,warm%d,%d,%.3f,%d%n", phase, numDocs, delRatio, w, rep, warm, g.getNumDocs()), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
      }
    }
    cc.shutdown();
  }

  static void copyTree(Path src, Path dst) throws Exception {
    try (var s = Files.walk(src)) {
      for (Path p : (Iterable<Path>) s::iterator) {
        Path t = dst.resolve(src.relativize(p).toString());
        if (Files.isDirectory(p)) Files.createDirectories(t);
        else Files.copy(p, t, StandardCopyOption.REPLACE_EXISTING);
      }
    }
  }
}
