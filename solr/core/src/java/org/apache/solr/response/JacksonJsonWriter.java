/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.solr.response;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.io.JsonStringEncoder;
import com.fasterxml.jackson.core.util.DefaultIndenter;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.solr.common.PushWriter;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.util.JsonTextWriter;
import org.apache.solr.common.util.NamedList;
import org.apache.solr.common.util.SimpleOrderedMap;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.search.ReturnFields;

/** A JSON ResponseWriter that uses jackson. */
public class JacksonJsonWriter extends BinaryResponseWriter {

  protected final JsonFactory jsonfactory;
  protected static final DefaultPrettyPrinter pretty =
      new DefaultPrettyPrinter()
          .withoutSpacesInObjectEntries()
          .withArrayIndenter(DefaultPrettyPrinter.NopIndenter.instance)
          .withObjectIndenter(new DefaultIndenter().withLinefeed("\n"));

  public JacksonJsonWriter() {
    super();
    jsonfactory = new JsonFactory();
  }

  @Override
  public void write(OutputStream out, SolrQueryRequest request, SolrQueryResponse response)
      throws IOException {
    WriterImpl sw = new WriterImpl(jsonfactory, out, request, response);
    sw.writeResponse();
    sw.close();
  }

  public PushWriter getWriter(
      OutputStream out, SolrQueryRequest request, SolrQueryResponse response) {
    return new WriterImpl(jsonfactory, out, request, response);
  }

  @Override
  public String getContentType(SolrQueryRequest request, SolrQueryResponse response) {
    return JSONResponseWriter.CONTENT_TYPE_JSON_UTF8;
  }

  // So we extend JSONWriter and override the relevant methods

  public static class WriterImpl extends JSONWriter {

    protected JsonGenerator gen;
    private final DefaultPrettyPrinter prettyPrinter;
    private final boolean arrntv;
    private boolean writeTypeAndValueKey = false;

    public WriterImpl(
        JsonFactory j, OutputStream out, SolrQueryRequest req, SolrQueryResponse rsp) {
      super(null, req, rsp);
      try {
        gen = j.createGenerator(out, JsonEncoding.UTF8);
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
      arrntv = JsonTextWriter.JSON_NL_ARROFNTV.equals(getNamedListStyle());
      if (doIndent) {
        prettyPrinter = pretty.createInstance();
        gen.setPrettyPrinter(prettyPrinter);
      } else {
        prettyPrinter = null;
      }
    }

    @Override
    public void writeResponse() throws IOException {
      if (wrapperFunction != null) {
        if (arrntv) {
          gen.writeRaw(wrapperFunction + "(");
        } else {
          writeStrRaw(null, wrapperFunction + "(");
        }
      }
      if (arrntv) {
        writeNamedListAsMapWithDups(rsp.getValues());
      } else {
        writeNamedList(null, rsp.getValues());
      }
      if (wrapperFunction != null) {
        if (arrntv) {
          gen.writeRaw(")");
        } else {
          writeStrRaw(null, ")");
        }
      }
      gen.close();
    }

    @Override
    public void writeNumber(String name, Number val) throws IOException {
      if (arrntv) {
        writeArrntvNumber(val);
        return;
      }
      if (val instanceof Integer) {
        gen.writeNumber(val.intValue());
      } else if (val instanceof Long) {
        gen.writeNumber(val.longValue());
      } else if (val instanceof Float) {
        gen.writeNumber(val.floatValue());
      } else if (val instanceof Double) {
        gen.writeNumber(val.doubleValue());
      } else if (val instanceof Short) {
        gen.writeNumber(val.shortValue());
      } else if (val instanceof Byte) {
        gen.writeNumber(val.byteValue());
      } else if (val instanceof BigInteger) {
        gen.writeNumber((BigInteger) val);
      } else if (val instanceof BigDecimal) {
        gen.writeNumber((BigDecimal) val);
      } else {
        gen.writeString(val.getClass().getName() + ':' + val.toString());
        // default... for debugging only
      }
    }

    @Override
    public void writeBool(String name, Boolean val) throws IOException {
      ifNeededWriteTypeAndValueKey("bool");
      gen.writeBoolean(val);
    }

    @Override
    public void writeNull(String name) throws IOException {
      ifNeededWriteTypeAndValueKey("null");
      gen.writeNull();
    }

    @Override
    public void writeStr(String name, String val, boolean needsEscaping) throws IOException {
      ifNeededWriteTypeAndValueKey("str");
      if (arrntv && needsRawQuoting(val)) {
        gen.writeRawValue(quoteRaw(val));
      } else {
        gen.writeString(val);
      }
    }

    private static boolean needsRawQuoting(String val) {
      for (int i = 0; i < val.length(); i++) {
        char c = val.charAt(i);
        if (c == '\u2028' || c == '\u2029' || Character.isSurrogate(c)) {
          return true;
        }
      }
      return false;
    }

    private static String quoteRaw(String val) {
      String quoted = new String(JsonStringEncoder.getInstance().quoteAsString(val));
      return "\"" + quoted.replace("\u2028", "\\u2028").replace("\u2029", "\\u2029") + "\"";
    }

    @Override
    public void writeLong(String name, long val) throws IOException {
      ifNeededWriteTypeAndValueKey("long");
      gen.writeNumber(val);
    }

    @Override
    public void writeInt(String name, int val) throws IOException {
      ifNeededWriteTypeAndValueKey("int");
      gen.writeNumber(val);
    }

    @Override
    public void writeBool(String name, boolean val) throws IOException {
      ifNeededWriteTypeAndValueKey("bool");
      gen.writeBoolean(val);
    }

    @Override
    public void writeFloat(String name, float val) throws IOException {
      if (!Float.isFinite(val)) {
        ifNeededWriteTypeAndValueKey("str");
        gen.writeString(Float.toString(val));
        return;
      }
      ifNeededWriteTypeAndValueKey("float");
      gen.writeNumber(val);
    }

    @Override
    public void writeNamedList(String name, NamedList<?> val) throws IOException {
      if (!arrntv) {
        super.writeNamedList(name, val);
        return;
      }
      ifNeededWriteTypeAndValueKey("lst");
      if (val instanceof SimpleOrderedMap) {
        super.writeNamedList(name, val);
        return;
      }
      final int sz = val.size();
      writeArrayOpener(sz);
      for (int i = 0; i < sz; i++) {
        final String elementName = val.getName(i);
        final Object elementVal = val.getVal(i);
        writeMapOpener(-1);
        writeKey("name", false);
        writeVal(null, elementName);
        writeTypeAndValueKey = true;
        writeVal(null, elementVal);
        if (writeTypeAndValueKey) {
          throw new IllegalStateException(
              "writeTypeAndValueKey should have been reset to false by writeVal('"
                  + elementName
                  + "','"
                  + elementVal
                  + "')");
        }
        writeMapCloser();
      }
      writeArrayCloser();
    }

    @Override
    public void writeMap(String name, Map<?, ?> val, boolean excludeOuter, boolean isFirstVal)
        throws IOException {
      ifNeededWriteTypeAndValueKey("map");
      super.writeMap(name, val, excludeOuter, isFirstVal);
    }

    @Override
    public void writeArray(String name, List<?> l, boolean raw) throws IOException {
      ifNeededWriteTypeAndValueKey("array");
      super.writeArray(name, l, raw);
    }

    @Override
    public void writeArray(String name, Object[] val, boolean raw) throws IOException {
      ifNeededWriteTypeAndValueKey("array");
      super.writeArray(name, val, raw);
    }

    @Override
    public void writeArray(String name, Iterator<?> val, boolean raw) throws IOException {
      ifNeededWriteTypeAndValueKey("array");
      super.writeArray(name, val, raw);
    }

    @Override
    public void writeArray(String name, Iterator<?> val, int size, boolean raw) throws IOException {
      ifNeededWriteTypeAndValueKey("array");
      super.writeArray(name, val, size, raw);
    }

    @Override
    public void writeSolrDocument(String name, SolrDocument doc, ReturnFields returnFields, int idx)
        throws IOException {
      ifNeededWriteTypeAndValueKey("doc");
      super.writeSolrDocument(name, doc, returnFields, idx);
    }

    @Override
    public void writeStartDocumentList(
        String name, long start, int size, long numFound, Float maxScore, Boolean numFoundExact)
        throws IOException {
      ifNeededWriteTypeAndValueKey("doclist");
      super.writeStartDocumentList(name, start, size, numFound, maxScore, numFoundExact);
    }

    private void ifNeededWriteTypeAndValueKey(String type) throws IOException {
      if (writeTypeAndValueKey) {
        writeTypeAndValueKey = false;
        gen.writeFieldName("type");
        gen.writeString(type);
        gen.writeFieldName("value");
      }
    }

    private void writeArrntvNumber(Number val) throws IOException {
      if (val instanceof Integer || val instanceof Short || val instanceof Byte) {
        ifNeededWriteTypeAndValueKey("int");
        gen.writeNumber(val.intValue());
      } else if (val instanceof Long) {
        ifNeededWriteTypeAndValueKey("long");
        gen.writeNumber(val.longValue());
      } else if (val instanceof Float && !Float.isFinite(val.floatValue())) {
        ifNeededWriteTypeAndValueKey("str");
        gen.writeString(val.toString());
      } else if (val instanceof Float) {
        ifNeededWriteTypeAndValueKey("float");
        gen.writeNumber(val.floatValue());
      } else if (val instanceof Double && !Double.isFinite(val.doubleValue())) {
        ifNeededWriteTypeAndValueKey("str");
        gen.writeString(val.toString());
      } else if (val instanceof Double) {
        ifNeededWriteTypeAndValueKey("double");
        gen.writeNumber(val.doubleValue());
      } else {
        ifNeededWriteTypeAndValueKey("str");
        gen.writeString(val.getClass().getName() + ':' + val.toString());
      }
    }

    @Override
    public void writeArrayCloser() throws IOException {
      gen.writeEndArray();
    }

    @Override
    public void writeArraySeparator() {
      // do nothing
    }

    @Override
    public void writeArrayOpener(int size) throws IOException, IllegalArgumentException {
      gen.writeStartArray();
    }

    @Override
    public void writeMapCloser() throws IOException {
      gen.writeEndObject();
    }

    @Override
    public void writeMapSeparator() {
      // do nothing
    }

    @Override
    public void writeMapOpener(int size) throws IOException, IllegalArgumentException {
      gen.writeStartObject();
    }

    @Override
    public void writeKey(String fname, boolean needsEscaping) throws IOException {
      gen.writeFieldName(fname);
    }

    @Override
    public void writeByteArr(String name, byte[] buf, int offset, int len) throws IOException {
      gen.writeBinary(buf, offset, len);
    }

    @Override
    public void setLevel(int level) {
      // do nothing
    }

    @Override
    public int level() {
      return 0;
    }

    @Override
    public void indent() throws IOException {
      // do nothing
    }

    @Override
    public void indent(int lev) throws IOException {
      // do nothing
    }

    @Override
    public int incLevel() {
      return 0;
    }

    @Override
    public int decLevel() {
      return 0;
    }

    @Override
    public void close() throws IOException {
      gen.close();
    }

    @Override
    public void writeStrRaw(String name, String val) throws IOException {
      if (writeTypeAndValueKey) {
        throw new IllegalStateException("NamedList should never be a field value");
      }
      gen.writeRawValue(val);
    }

    @Override
    public void writeDate(String name, String val) throws IOException {
      ifNeededWriteTypeAndValueKey("date");
      gen.writeString(val);
    }
  }
}
