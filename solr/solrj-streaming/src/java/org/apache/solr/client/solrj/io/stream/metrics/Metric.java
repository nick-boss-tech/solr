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
package org.apache.solr.client.solrj.io.stream.metrics;

import java.io.IOException;
import java.time.Instant;
import java.time.format.DateTimeParseException;
import java.util.UUID;
import org.apache.solr.client.solrj.io.Tuple;
import org.apache.solr.client.solrj.io.stream.expr.Explanation;
import org.apache.solr.client.solrj.io.stream.expr.Explanation.ExpressionType;
import org.apache.solr.client.solrj.io.stream.expr.Expressible;
import org.apache.solr.client.solrj.io.stream.expr.StreamFactory;

public abstract class Metric implements Expressible {

  private UUID metricNodeId = UUID.randomUUID();
  private String functionName;
  private String identifier;
  public boolean outputLong; // This is only used for SQL in facet mode.

  public String getFunctionName() {
    return functionName;
  }

  public void setFunctionName(String functionName) {
    this.functionName = functionName;
  }

  public String getIdentifier() {
    return identifier;
  }

  public void setIdentifier(String identifier) {
    this.identifier = identifier;
  }

  public void setIdentifier(String... identifierParts) {
    StringBuilder sb = new StringBuilder();
    for (String part : identifierParts) {
      sb.append(part);
    }
    this.identifier = sb.toString();
  }

  @Override
  public Explanation toExplanation(StreamFactory factory) throws IOException {
    return new Explanation(getMetricNodeId().toString())
        .withFunctionName(functionName)
        .withImplementingClass(getClass().getName())
        .withExpression(toExpression(factory).toString())
        .withExpressionType(ExpressionType.METRIC);
  }

  public UUID getMetricNodeId() {
    return metricNodeId;
  }

  /**
   * The aggregated value. Numeric metrics return a {@link Number}; a metric that aggregates
   * non-numeric values (for example min/max over strings) may return another type.
   */
  /**
   * Compares two string metric values. When both values parse as ISO-8601 instants, they are
   * compared as instants: the text order of two instants in the same second differs from the time
   * order when one of them is printed without fractional digits ({@code 2018-03-01T10:00:00Z} sorts
   * after {@code 2018-03-01T10:00:00.250Z} as text, though it is the earlier time). When either
   * value does not parse as an instant, the plain text order is used.
   */
  protected static int compareStringValues(String a, String b) {
    try {
      return Instant.parse(a).compareTo(Instant.parse(b));
    } catch (DateTimeParseException e) {
      return a.compareTo(b);
    }
  }

  public abstract Object getValue();

  public abstract void update(Tuple tuple);

  public abstract Metric newInstance();

  public abstract String[] getColumns();
}
