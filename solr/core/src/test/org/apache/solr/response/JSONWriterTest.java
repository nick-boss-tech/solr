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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import org.apache.solr.JSONTestUtil;
import org.apache.solr.SolrTestCaseJ4;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.params.CommonParams;
import org.apache.solr.common.params.SolrParams;
import org.apache.solr.common.util.JsonTextWriter;
import org.apache.solr.common.util.NamedList;
import org.apache.solr.common.util.SimpleOrderedMap;
import org.apache.solr.common.util.Utils;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.search.ReturnFields;
import org.apache.solr.search.SolrReturnFields;
import org.apache.solr.util.SolrPluginUtils;
import org.junit.BeforeClass;
import org.junit.Test;

/** Test some aspects of JSON/python writer output (very incomplete) */
public class JSONWriterTest extends SolrTestCaseJ4 {
  @BeforeClass
  public static void beforeClass() throws Exception {
    initCore("solrconfig.xml", "schema.xml");
  }

  private void jsonEq(String expected, String received) {
    expected = expected.trim();
    received = received.trim();
    assertEquals(expected, received);
  }

  @Test
  public void testTypes() throws IOException {
    SolrQueryRequest req = req("q", "dummy", "indent", "off");
    SolrQueryResponse rsp = new SolrQueryResponse();
    QueryResponseWriter w = new PythonResponseWriter();

    StringWriter buf = new StringWriter();
    rsp.add("data1", Float.NaN);
    rsp.add("data2", Double.NEGATIVE_INFINITY);
    rsp.add("data3", Float.POSITIVE_INFINITY);
    w.write(buf, req, rsp);
    jsonEq(buf.toString(), "{'data1':float('NaN'),'data2':-float('Inf'),'data3':float('Inf')}");

    w = new RubyResponseWriter();
    buf = new StringWriter();
    w.write(buf, req, rsp);
    jsonEq(buf.toString(), "{'data1'=>(0.0/0.0),'data2'=>-(1.0/0.0),'data3'=>(1.0/0.0)}");

    w = new JSONResponseWriter();
    buf = new StringWriter();
    w.write(buf, req, rsp);
    jsonEq(buf.toString(), "{\"data1\":\"NaN\",\"data2\":\"-Infinity\",\"data3\":\"Infinity\"}");
    req.close();
  }

  @Test
  public void testJSON() throws IOException {
    final String[] namedListStyles =
        new String[] {
          JsonTextWriter.JSON_NL_FLAT,
          JsonTextWriter.JSON_NL_MAP,
          JsonTextWriter.JSON_NL_ARROFARR,
          JsonTextWriter.JSON_NL_ARROFMAP,
          JsonTextWriter.JSON_NL_ARROFNTV,
        };
    for (final String namedListStyle : namedListStyles) {
      implTestJSON(namedListStyle);
    }
    assertEquals(JSONWriter.JSON_NL_STYLE_COUNT, namedListStyles.length);
  }

  private void implTestJSON(final String namedListStyle) throws IOException {
    SolrQueryRequest req = req("wt", "json", "json.nl", namedListStyle, "indent", "off");
    SolrQueryResponse rsp = new SolrQueryResponse();
    JSONResponseWriter w = new JSONResponseWriter();

    StringWriter buf = new StringWriter();
    NamedList<Object> nl = new NamedList<>();
    // make sure that 2028 and 2029 are both escaped (they are illegal in javascript)
    nl.add("data1", "he\u2028llo\u2029!");
    nl.add(null, 42);
    nl.add(null, null);
    rsp.add("nl", nl);

    rsp.add("byte", (byte) -3);
    rsp.add("short", (short) -4);
    rsp.add("bytes", "abc".getBytes(StandardCharsets.UTF_8));

    w.write(buf, req, rsp);

    final String expectedNLjson;
    if (Objects.equals(namedListStyle, JSONWriter.JSON_NL_FLAT)) {
      expectedNLjson = "\"nl\":[\"data1\",\"he\\u2028llo\\u2029!\",null,42,null,null]";
    } else if (Objects.equals(namedListStyle, JSONWriter.JSON_NL_MAP)) {
      expectedNLjson = "\"nl\":{\"data1\":\"he\\u2028llo\\u2029!\",\"\":42,\"\":null}";
    } else if (Objects.equals(namedListStyle, JSONWriter.JSON_NL_ARROFARR)) {
      expectedNLjson = "\"nl\":[[\"data1\",\"he\\u2028llo\\u2029!\"],[null,42],[null,null]]";
    } else if (Objects.equals(namedListStyle, JSONWriter.JSON_NL_ARROFMAP)) {
      expectedNLjson = "\"nl\":[{\"data1\":\"he\\u2028llo\\u2029!\"},42,null]";
    } else if (Objects.equals(namedListStyle, JSONWriter.JSON_NL_ARROFNTV)) {
      expectedNLjson =
          "\"nl\":[{\"name\":\"data1\",\"type\":\"str\",\"value\":\"he\\u2028llo\\u2029!\"},"
              + "{\"name\":null,\"type\":\"int\",\"value\":42},"
              + "{\"name\":null,\"type\":\"null\",\"value\":null}]";
    } else {
      expectedNLjson = null;
      fail("unexpected namedListStyle=" + namedListStyle);
    }

    jsonEq("{" + expectedNLjson + ",\"byte\":-3,\"short\":-4,\"bytes\":\"YWJj\"}", buf.toString());
    req.close();
  }

  @Test
  public void testJSONSolrDocument() throws Exception {
    SolrQueryRequest req =
        req(
            CommonParams.WT, "json",
            CommonParams.FL, "id,score,_children_,path");
    SolrQueryResponse rsp = new SolrQueryResponse();
    JSONResponseWriter w = new JSONResponseWriter();

    ReturnFields returnFields = new SolrReturnFields(req);
    rsp.setReturnFields(returnFields);

    StringWriter buf = new StringWriter();

    SolrDocument childDoc = new SolrDocument();
    childDoc.addField("id", "2");
    childDoc.addField("score", "0.4");
    childDoc.addField("path", Arrays.asList("a>b", "a>b>c"));

    SolrDocumentList childList = new SolrDocumentList();
    childList.setNumFound(1);
    childList.setStart(0);
    childList.add(childDoc);

    SolrDocument solrDoc = new SolrDocument();
    solrDoc.addField("id", "1");
    solrDoc.addField("subject", "hello2");
    solrDoc.addField("title", "hello3");
    solrDoc.addField("score", "0.7");
    solrDoc.setField("_children_", childList);

    SolrDocumentList list = new SolrDocumentList();
    list.setNumFound(1);
    list.setStart(0);
    list.setMaxScore(0.7f);
    list.add(solrDoc);

    rsp.addResponse(list);

    w.write(buf, req, rsp);
    String result = buf.toString();
    assertFalse(
        "response contains unexpected fields: " + result,
        result.contains("hello") || result.contains("\"subject\"") || result.contains("\"title\""));
    assertTrue(
        "response doesn't contain expected fields: " + result,
        result.contains("\"id\"") && result.contains("\"score\"") && result.contains("_children_"));

    String expectedResult =
        "{'response':{'numFound':1,'start':0,'maxScore':0.7, 'numFoundExact':true,'docs':[{'id':'1', 'score':'0.7',"
            + " '_children_':{'numFound':1,'start':0,'numFoundExact':true,'docs':[{'id':'2', 'score':'0.4', 'path':['a>b', 'a>b>c']}] }}] }}";
    String error = JSONTestUtil.match(result, "==" + expectedResult);
    assertNull("response validation failed with error: " + error, error);

    req.close();
  }

  @Test
  public void testArrntvWriterOverridesAllWrites() {
    // List rather than Set because two not-overridden methods could share name but not signature
    final List<String> methodsExpectedNotOverridden = new ArrayList<>(14);
    methodsExpectedNotOverridden.add("writeResponse");
    methodsExpectedNotOverridden.add("writeKey");
    methodsExpectedNotOverridden.add("writeNamedListAsMapMangled");
    methodsExpectedNotOverridden.add("writeNamedListAsMapWithDups");
    methodsExpectedNotOverridden.add("writeNamedListAsArrMap");
    methodsExpectedNotOverridden.add("writeNamedListAsArrArr");
    methodsExpectedNotOverridden.add("writeNamedListAsFlat");
    methodsExpectedNotOverridden.add("writeEndDocumentList");
    methodsExpectedNotOverridden.add("writeMapOpener");
    methodsExpectedNotOverridden.add("writeMapSeparator");
    methodsExpectedNotOverridden.add("writeMapCloser");
    methodsExpectedNotOverridden.add(
        "public default void org.apache.solr.common.util.JsonTextWriter.writeArray(java.lang.String,java.util.List,boolean) throws java.io.IOException");
    methodsExpectedNotOverridden.add("writeArrayOpener");
    methodsExpectedNotOverridden.add("writeArraySeparator");
    methodsExpectedNotOverridden.add("writeArrayCloser");
    methodsExpectedNotOverridden.add(
        "public default void org.apache.solr.common.util.JsonTextWriter.writeMap(org.apache.solr.common.MapWriter) throws java.io.IOException");
    methodsExpectedNotOverridden.add(
        "public default void org.apache.solr.common.util.JsonTextWriter.writeIterator(org.apache.solr.common.IteratorWriter) throws java.io.IOException");
    methodsExpectedNotOverridden.add(
        "public default void org.apache.solr.common.util.JsonTextWriter.writeJsonIter(java.util.Iterator,boolean) throws java.io.IOException");

    final Class<?> subClass = JSONResponseWriter.ArrayOfNameTypeValueJSONWriter.class;
    final Class<?> superClass = subClass.getSuperclass();

    List<Method> allSuperClassMethods = new ArrayList<>();
    for (Method method : superClass.getDeclaredMethods()) allSuperClassMethods.add(method);
    for (Method method : JsonTextWriter.class.getDeclaredMethods())
      allSuperClassMethods.add(method);

    for (final Method superClassMethod : allSuperClassMethods) {
      final String methodName = superClassMethod.getName();
      final String methodFullName = superClassMethod.toString();
      if (!methodName.startsWith("write")) continue;

      final int modifiers = superClassMethod.getModifiers();
      if (Modifier.isFinal(modifiers)) continue;
      if (Modifier.isStatic(modifiers)) continue;
      if (Modifier.isPrivate(modifiers)) continue;

      final boolean expectOverridden =
          !methodsExpectedNotOverridden.contains(methodName)
              && !methodsExpectedNotOverridden.contains(methodFullName);

      try {
        final Method subClassMethod = getDeclaredMethodInClasses(superClassMethod, subClass);
        if (expectOverridden) {
          assertEquals(
              "getReturnType() difference",
              superClassMethod.getReturnType(),
              subClassMethod.getReturnType());
        } else {
          fail(subClass + " must not override '" + superClassMethod + "'");
        }
      } catch (NoSuchMethodException e) {
        if (expectOverridden) {
          fail(subClass + " needs to override '" + superClassMethod + "'");
        } else {
          assertTrue(
              methodName + " not found in remaining " + methodsExpectedNotOverridden,
              methodsExpectedNotOverridden.remove(methodName)
                  || methodsExpectedNotOverridden.remove(methodFullName));
        }
      }
    }

    assertTrue(
        "methodsExpected NotOverridden but NotFound instead: " + methodsExpectedNotOverridden,
        methodsExpectedNotOverridden.isEmpty());
  }

  @Test
  public void testArrntvWriterLacksMethodsOfItsOwn() {
    final Class<?> subClass = JSONResponseWriter.ArrayOfNameTypeValueJSONWriter.class;
    final Class<?> superClass = subClass.getSuperclass();
    // ArrayOfNamedValuePairJSONWriter is a simple sub-class
    // which should have (almost) no methods of its own
    for (final Method subClassMethod : subClass.getDeclaredMethods()) {
      // only own private method of its own
      if (subClassMethod.getName().equals("ifNeededWriteTypeAndValueKey")) continue;
      try {
        final Method superClassMethod =
            getDeclaredMethodInClasses(subClassMethod, superClass, JsonTextWriter.class);

        assertEquals(
            "getReturnType() difference",
            subClassMethod.getReturnType(),
            superClassMethod.getReturnType());
      } catch (NoSuchMethodException e) {
        fail(subClass + " should not have '" + subClassMethod + "' method of its own");
      }
    }
  }

  private Method getDeclaredMethodInClasses(Method subClassMethod, Class<?>... classes)
      throws NoSuchMethodException {
    for (int i = 0; i < classes.length; i++) {
      Class<?> klass = classes[i];
      try {
        return klass.getDeclaredMethod(
            subClassMethod.getName(), subClassMethod.getParameterTypes());
      } catch (NoSuchMethodException e) {
        if (i == classes.length - 1) throw e;
      }
    }
    throw new NoSuchMethodException(subClassMethod.toString());
  }

  @Test
  public void testConstantsUnchanged() {
    assertEquals("json.nl", JSONWriter.JSON_NL_STYLE);
    assertEquals("map", JSONWriter.JSON_NL_MAP);
    assertEquals("flat", JSONWriter.JSON_NL_FLAT);
    assertEquals("arrarr", JSONWriter.JSON_NL_ARROFARR);
    assertEquals("arrmap", JSONWriter.JSON_NL_ARROFMAP);
    assertEquals("arrntv", JSONWriter.JSON_NL_ARROFNTV);
    assertEquals("json.wrf", JSONWriter.JSON_WRAPPER_FUNCTION);
  }

  @Test
  public void testWfrJacksonJsonWriter() throws IOException {
    SolrQueryRequest req = req("wt", "json", JSONWriter.JSON_WRAPPER_FUNCTION, "testFun");
    SolrQueryResponse rsp = new SolrQueryResponse();
    rsp.add("param0", "v0");
    rsp.add("param1", 42);

    JacksonJsonWriter w = new JacksonJsonWriter();
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    w.write(baos, req, rsp);
    String received = new String(baos.toByteArray(), StandardCharsets.UTF_8);
    String expected = "testFun( {\n  \"param0\":\"v0\",\n  \"param1\":42\n} )";
    jsonEq(expected, received);
    req.close();
  }

  @Test
  public void testArrntvThroughJacksonJsonWriter() throws IOException {
    SolrQueryRequest req =
        req("wt", "json", "json.nl", JsonTextWriter.JSON_NL_ARROFNTV, "indent", "off");
    SolrQueryResponse rsp = new SolrQueryResponse();
    NamedList<Object> nl = new NamedList<>();
    nl.add("data1", "hello");
    nl.add(null, 42);
    nl.add(null, null);
    rsp.add("nl", nl);

    JacksonJsonWriter w = new JacksonJsonWriter();
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    w.write(baos, req, rsp);

    String expectedNLjson =
        "\"nl\":[{\"name\":\"data1\",\"type\":\"str\",\"value\":\"hello\"},"
            + "{\"name\":null,\"type\":\"int\",\"value\":42},"
            + "{\"name\":null,\"type\":\"null\",\"value\":null}]";
    jsonEq("{" + expectedNLjson + "}", baos.toString(StandardCharsets.UTF_8));
    req.close();
  }

  @Test
  public void testArrntvFacetsThroughJacksonJsonWriter() throws Exception {
    assertU(adoc("id", "arrntv-1", "cat", "a"));
    assertU(adoc("id", "arrntv-2", "cat", "b"));
    assertU(commit());
    try {
      // in this core wt=json resolves to JacksonJsonWriter; omitted wt resolves to the xml default
      String response =
          h.query(
              "/select",
              req(
                  "q",
                  "id:arrntv-*",
                  "rows",
                  "0",
                  "facet",
                  "true",
                  "facet.field",
                  "cat",
                  "wt",
                  "json",
                  "json.nl",
                  JsonTextWriter.JSON_NL_ARROFNTV));
      assertNotNull(Utils.fromJSONString(response));
      assertTrue(response, response.contains("{\"name\":\"a\",\"type\":\"int\",\"value\":1}"));
    } finally {
      assertU(delQ("id:arrntv-*"));
      assertU(commit());
    }
  }

  @Test
  public void testArrntvFromHandlerDefaultsThroughJacksonJsonWriter() throws IOException {
    SolrQueryRequest req = req("q", "*:*", "indent", "off");
    SolrPluginUtils.setDefaults(
        req,
        SolrParams.of(JsonTextWriter.JSON_NL_STYLE, JsonTextWriter.JSON_NL_ARROFNTV),
        SolrParams.of(),
        SolrParams.of());
    // handler defaults are merged into getParams(), the view the writer reads
    assertNull(req.getOriginalParams().get(JsonTextWriter.JSON_NL_STYLE));
    SolrQueryResponse rsp = new SolrQueryResponse();
    NamedList<Object> nl = new NamedList<>();
    nl.add(null, 42);
    rsp.add("nl", nl);

    JacksonJsonWriter w = new JacksonJsonWriter();
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    w.write(baos, req, rsp);

    jsonEq(
        "{\"nl\":[{\"name\":null,\"type\":\"int\",\"value\":42}]}",
        baos.toString(StandardCharsets.UTF_8));
    req.close();
  }

  private static final String NTV_NL_HELLO =
      "\"nl\":[{\"name\":\"data1\",\"type\":\"str\",\"value\":\"hello\"},"
          + "{\"name\":null,\"type\":\"int\",\"value\":42},"
          + "{\"name\":null,\"type\":\"null\",\"value\":null}]";

  private static final String NTV_NESTED =
      "{\"outer\":[{\"name\":\"inner\",\"type\":\"lst\",\"value\":"
          + "[{\"name\":\"a\",\"type\":\"int\",\"value\":1}]},"
          + "{\"name\":\"map\",\"type\":\"lst\",\"value\":{\"x\":2}}]}";

  private static SolrQueryResponse nestedResponse() {
    NamedList<Object> inner = new NamedList<>();
    inner.add("a", 1);
    SimpleOrderedMap<Object> map = new SimpleOrderedMap<>();
    map.add("x", 2);
    NamedList<Object> outer = new NamedList<>();
    outer.add("inner", inner);
    outer.add("map", map);
    SolrQueryResponse rsp = new SolrQueryResponse();
    rsp.add("outer", outer);
    return rsp;
  }

  // A NamedList or SimpleOrderedMap used as an NTV element value needs a type of its own.
  // Without one, the legacy writer emitted a bare array or object after "name": invalid JSON.
  @Test
  public void testArrntvNestedNamedListIsValidJson() throws IOException {
    SolrQueryRequest req =
        req("wt", "json", "json.nl", JsonTextWriter.JSON_NL_ARROFNTV, "indent", "off");
    StringWriter buf = new StringWriter();
    new JSONResponseWriter().write(buf, req, nestedResponse());
    jsonEq(NTV_NESTED, buf.toString());
    req.close();
  }

  @Test
  public void testArrntvNestedNamedListThroughJacksonJsonWriter() throws IOException {
    SolrQueryRequest req =
        req("wt", "json", "json.nl", JsonTextWriter.JSON_NL_ARROFNTV, "indent", "off");
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    new JacksonJsonWriter().write(baos, req, nestedResponse());
    jsonEq(NTV_NESTED, baos.toString(StandardCharsets.UTF_8));
    req.close();
  }

  @Test
  public void testArrntvWithJsonWrfIsValidJson() throws IOException {
    SolrQueryRequest req =
        req(
            "wt",
            "json",
            "json.nl",
            JsonTextWriter.JSON_NL_ARROFNTV,
            "indent",
            "off",
            JSONWriter.JSON_WRAPPER_FUNCTION,
            "cb");
    SolrQueryResponse rsp = new SolrQueryResponse();
    NamedList<Object> nl = new NamedList<>();
    nl.add("data1", "hello");
    nl.add(null, 42);
    nl.add(null, null);
    rsp.add("nl", nl);
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    new JacksonJsonWriter().write(baos, req, rsp);
    jsonEq("cb({" + NTV_NL_HELLO + "})", baos.toString(StandardCharsets.UTF_8));
    req.close();
  }

  @Test
  public void testArrntvWithIndentIsValidJson() throws IOException {
    SolrQueryRequest req =
        req("wt", "json", "json.nl", JsonTextWriter.JSON_NL_ARROFNTV, "indent", "on");
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    new JacksonJsonWriter().write(baos, req, nestedResponse());
    String json = baos.toString(StandardCharsets.UTF_8);
    assertNotNull(Utils.fromJSONString(json));
    assertTrue(json, json.contains("\"lst\""));
    req.close();
  }

  @Test
  public void testArrntvKeepsJsonContentType() throws Exception {
    SolrQueryRequest req = req("wt", "json", "json.nl", JsonTextWriter.JSON_NL_ARROFNTV);
    assertEquals(
        JSONResponseWriter.CONTENT_TYPE_JSON_UTF8,
        new JacksonJsonWriter().getContentType(req, new SolrQueryResponse()));
    req.close();
  }

  @Test
  public void testOtherStylesUnchangedThroughJacksonJsonWriter() throws IOException {
    String[] styles =
        new String[] {
          JsonTextWriter.JSON_NL_FLAT,
          JsonTextWriter.JSON_NL_MAP,
          JsonTextWriter.JSON_NL_ARROFARR,
          JsonTextWriter.JSON_NL_ARROFMAP
        };
    for (String style : styles) {
      SolrQueryRequest req = req("wt", "json", "json.nl", style, "indent", "off");
      SolrQueryResponse rsp = new SolrQueryResponse();
      NamedList<Object> nl = new NamedList<>();
      nl.add("data1", "hello");
      nl.add(null, 42);
      nl.add(null, null);
      rsp.add("nl", nl);

      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      new JacksonJsonWriter().write(baos, req, rsp);
      StringWriter legacy = new StringWriter();
      new JSONResponseWriter().write(legacy, req, rsp);
      assertEquals(style, legacy.toString().trim(), baos.toString(StandardCharsets.UTF_8).trim());
      req.close();
    }
  }

  @Test
  public void testXmlWriterIgnoresArrntv() throws Exception {
    assertU(adoc("id", "arrntv-xml", "cat", "a"));
    assertU(commit());
    try {
      String xml =
          h.query(
              "/select",
              req(
                  "q",
                  "id:arrntv-xml",
                  "rows",
                  "0",
                  "facet",
                  "true",
                  "facet.field",
                  "cat",
                  "wt",
                  "xml",
                  "json.nl",
                  JsonTextWriter.JSON_NL_ARROFNTV));
      assertTrue(xml, xml.contains("<response"));
      assertTrue(xml, xml.contains("name=\"cat\""));
    } finally {
      assertU(delQ("id:arrntv-xml"));
      assertU(commit());
    }
  }

  @Test
  public void testWfrJSONWriter() throws IOException {
    SolrQueryRequest req = req("wt", "json", JSONWriter.JSON_WRAPPER_FUNCTION, "testFun");
    SolrQueryResponse rsp = new SolrQueryResponse();
    rsp.add("param0", "v0");
    rsp.add("param1", 42);

    JSONResponseWriter w = new JSONResponseWriter();
    StringWriter buf = new StringWriter();
    w.write(buf, req, rsp);
    String expected = "testFun({\n  \"param0\":\"v0\",\n  \"param1\":42})";
    jsonEq(expected, buf.toString());
    req.close();
  }

  @Test
  public void testResponseValuesProperlyQuoted() throws Exception {
    assertU(
        adoc(
            "id",
            "1",
            "name",
            "John Doe",
            "cat",
            "foo\"b'ar",
            "uuid",
            "6e2fb55b-dd42-4e2d-84ca-71a599403aa3",
            "bsto",
            "true",
            "isto",
            "42",
            "amount",
            "100,USD",
            "price",
            "3.14",
            "severity",
            "High",
            "dateRange",
            "[2024-03-01 TO 2024-03-31]",
            "timestamp",
            "2024-03-20T12:34:56Z"));
    assertU(commit());

    String expected =
        "{\n"
            + "  \"response\":{\n"
            + "    \"numFound\":1,\n"
            + "    \"start\":0,\n"
            + "    \"numFoundExact\":true,\n"
            + "    \"docs\":[{\n"
            + "      \"id\":\"1\",\n"
            + "      \"name\":[\"John Doe\"],\n"
            + "      \"cat\":[\"foo\\\"b'ar\"],\n"
            + "      \"uuid\":[\"6e2fb55b-dd42-4e2d-84ca-71a599403aa3\"],\n"
            + "      \"bsto\":[true],\n"
            + "      \"isto\":[42],\n"
            + "      \"amount\":\"100,USD\",\n"
            + "      \"price\":3.14,\n"
            + "      \"severity\":\"High\",\n"
            + "      \"dateRange\":[\"[2024-03-01 TO 2024-03-31]\"],\n"
            + "      \"timestamp\":\"2024-03-20T12:34:56Z\"\n"
            + "    }]\n"
            + "  }\n"
            + "}";

    String fl = "id,name,cat,uuid,bsto,isto,amount,price,severity,dateRange,timestamp";
    var req = req("q", "id:*", "fl", fl, "wt", "json", "omitHeader", "true");
    try (req) {
      String response = h.query("/select", req);
      jsonEq(expected, response);
    }
  }
}
