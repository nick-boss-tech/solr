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
package org.apache.solr.jetty;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.atomic.LongAdder;
import java.util.function.Supplier;
import org.eclipse.jetty.http.HttpFields;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.EventsHandler;
import org.eclipse.jetty.util.thread.QueuedThreadPool;

/** Counts HTTP completions independently of the Solr web application's lifecycle. */
public class JettyMetricsHandler extends EventsHandler implements Supplier<Map<String, Long>> {
  private static final String[] METHODS = {
    "GET", "POST", "HEAD", "PUT", "DELETE", "OPTIONS", "TRACE", "CONNECT", "MOVE", "OTHER"
  };
  private final LongAdder[] methods = adders(METHODS.length);
  private final LongAdder[] responses = adders(5);
  private final LongAdder active = new LongAdder();

  private static LongAdder[] adders(int count) {
    LongAdder[] result = new LongAdder[count];
    for (int i = 0; i < count; i++) result[i] = new LongAdder();
    return result;
  }

  @Override
  protected void onBeforeHandling(Request request) {
    active.increment();
  }

  @Override
  protected void onComplete(Request request, int status, HttpFields headers, Throwable failure) {
    int method = METHODS.length - 1;
    for (int i = 0; i < METHODS.length - 1; i++) {
      if (METHODS[i].equals(request.getMethod())) {
        method = i;
        break;
      }
    }
    methods[method].increment();
    int statusClass = status / 100;
    if (statusClass >= 1 && statusClass <= 5) responses[statusClass - 1].increment();
    active.decrement();
  }

  @Override
  public Map<String, Long> get() {
    Map<String, Long> result = new LinkedHashMap<>();
    for (int i = 0; i < METHODS.length; i++) result.put("method." + METHODS[i], methods[i].sum());
    for (int i = 0; i < responses.length; i++) {
      result.put("status." + (i + 1) + "xx", responses[i].sum());
    }
    result.put("active", active.sum());
    if (getServer().getThreadPool() instanceof QueuedThreadPool pool) {
      long current = pool.getThreads();
      long idle = pool.getIdleThreads();
      result.put("threads.current", current);
      result.put("threads.idle", idle);
      result.put("threads.busy", Math.max(0, current - idle));
      result.put("threads.max", (long) pool.getMaxThreads());
      result.put("threads.queued", (long) pool.getQueueSize());
    }
    return result;
  }
}
