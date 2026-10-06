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
package org.apache.solr.update.processor;

import static org.apache.solr.update.processor.FieldMutatingUpdateProcessor.mutator;

import java.util.Collection;
import java.util.Map;
import org.apache.solr.common.SolrInputField;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.response.SolrQueryResponse;

/**
 * Replaces any list of values for a field matching the specified conditions with the count of the
 * number of values for that field.
 *
 * <p>By default, this processor matches no fields.
 *
 * <p>The typical use case for this processor would be in combination with the {@link
 * CloneFieldUpdateProcessorFactory} so that it's possible to query by the quantity of values in the
 * source field.
 *
 * <p>For example, in the configuration below, the end result will be that the <code>category_count
 * </code> field can be used to search for documents based on how many values they contain in the
 * <code>category</code> field.
 *
 * <pre class="prettyprint">
 * &lt;processor class="solr.CloneFieldUpdateProcessorFactory"&gt;
 *   &lt;str name="source"&gt;category&lt;/str&gt;
 *   &lt;str name="dest"&gt;category_count&lt;/str&gt;
 * &lt;/processor&gt;
 * &lt;processor class="solr.CountFieldValuesUpdateProcessorFactory"&gt;
 *   &lt;str name="fieldName"&gt;category_count&lt;/str&gt;
 * &lt;/processor&gt;
 * &lt;processor class="solr.DefaultValueUpdateProcessorFactory"&gt;
 *   &lt;str name="fieldName"&gt;category_count&lt;/str&gt;
 *   &lt;int name="value"&gt;0&lt;/int&gt;
 * &lt;/processor&gt;</pre>
 *
 * <p><b>NOTE:</b> The use of {@link DefaultValueUpdateProcessorFactory} is important in this
 * example to ensure that all documents have a value for the <code>category_count</code> field,
 * because <code>CountFieldValuesUpdateProcessorFactory</code> only <i>replaces</i> the list of
 * values with the size of that list. If <code>DefaultValueUpdateProcessorFactory</code> was not
 * used, then any document that had no values for the <code>category</code> field, would also have
 * no value in the <code>category_count</code> field.
 *
 * @since 4.0.0
 */
public final class CountFieldValuesUpdateProcessorFactory
    extends FieldMutatingUpdateProcessorFactory {

  @Override
  public UpdateRequestProcessor getInstance(
      SolrQueryRequest req, SolrQueryResponse rsp, UpdateRequestProcessor next) {
    return mutator(
        getSelector(),
        next,
        src -> {
          SolrInputField result = new SolrInputField(src.getName());
          if (src.getValueCount() == 1 && src.getFirstValue() instanceof Map) {
            return countAtomicUpdate(src, result);
          }
          result.setValue(src.getValueCount());
          return result;
        });
  }

  /**
   * An atomic update arrives as a single map of operations, not as the values themselves. Only a
   * {@code set} can be counted (a {@code null} operand counts as zero); the result stays an atomic
   * {@code set}. The count after any other operation depends on the stored document, so the field
   * is dropped and the stored count is left alone.
   */
  private static SolrInputField countAtomicUpdate(SolrInputField src, SolrInputField result) {
    Map<?, ?> operations = (Map<?, ?>) src.getFirstValue();
    if (operations.size() != 1 || !operations.containsKey("set")) {
      return null;
    }
    Object operand = operations.get("set");
    int count = 1;
    if (operand == null) {
      count = 0;
    } else if (operand instanceof Collection) {
      count = ((Collection<?>) operand).size();
    }
    result.setValue(Map.of("set", count));
    return result;
  }
}
