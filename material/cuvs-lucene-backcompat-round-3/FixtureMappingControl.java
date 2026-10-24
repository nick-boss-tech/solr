import com.nvidia.cuvs.lucene.CuVSCodecs;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.KnnFloatVectorField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.VectorSimilarityFunction;
import org.apache.lucene.search.DocIdSetIterator;
import org.apache.lucene.store.FSDirectory;

/** Creates a valid CPU index whose stored IDs have been associated with the wrong vectors. */
public class FixtureMappingControl {
  public static void main(String[] args) throws Exception {
    Path inputZip = Path.of(args[0]);
    Path output = Path.of(args[1]);
    Path work = Path.of(args[2]);
    Files.createDirectory(work);
    Path source = Files.createDirectory(work.resolve("input"));
    Path target = Files.createDirectory(work.resolve("output"));
    try (ZipInputStream in = new ZipInputStream(Files.newInputStream(inputZip))) {
      for (ZipEntry entry; (entry = in.getNextEntry()) != null; ) {
        Path file = source.resolve(entry.getName()).normalize();
        if (!file.startsWith(source)) throw new IllegalArgumentException(entry.getName());
        Files.copy(in, file);
      }
    }
    var codec = CuVSCodecs.acceleratedHNSW();
    try (var input = FSDirectory.open(source);
         var reader = DirectoryReader.open(input);
         var dir = FSDirectory.open(target);
         var writer = new IndexWriter(dir, new IndexWriterConfig().setCodec(codec)
             .setUseCompoundFile(false))) {
      for (var context : reader.leaves()) {
        var leaf = context.reader();
        var values = leaf.getFloatVectorValues("vector");
        var it = values.iterator();
        var fields = leaf.storedFields();
        for (int doc = it.nextDoc(); doc != DocIdSetIterator.NO_MORE_DOCS; doc = it.nextDoc()) {
          int id = Integer.parseInt(fields.document(doc).get("id"));
          Document altered = new Document();
          altered.add(new StringField("id", Integer.toString((id + 1) % 200), Field.Store.YES));
          altered.add(new KnnFloatVectorField("vector", values.vectorValue(it.index()).clone(),
              VectorSimilarityFunction.EUCLIDEAN));
          writer.addDocument(altered);
        }
      }
    }
    Files.createDirectories(output.getParent());
    try (var zip = new ZipOutputStream(Files.newOutputStream(output));
         var files = Files.list(target)) {
      for (Path file : files.filter(p -> !p.getFileName().toString().equals("write.lock")).sorted().toList()) {
        ZipEntry entry = new ZipEntry(file.getFileName().toString());
        entry.setTime(0);
        zip.putNextEntry(entry);
        Files.copy(file, zip);
        zip.closeEntry();
      }
    }
    System.out.println("CONTROL_CREATED " + output + " codec=" + codec.getName());
  }
}
