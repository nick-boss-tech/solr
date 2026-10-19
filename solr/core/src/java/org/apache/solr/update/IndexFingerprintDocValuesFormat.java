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

package org.apache.solr.update;

import java.io.IOException;
import org.apache.lucene.codecs.DocValuesConsumer;
import org.apache.lucene.codecs.DocValuesFormat;
import org.apache.lucene.codecs.DocValuesProducer;
import org.apache.lucene.index.FieldInfo;
import org.apache.lucene.index.SegmentReadState;
import org.apache.lucene.index.SegmentWriteState;
import org.apache.solr.common.params.CommonParams;

/**
 * Adds a gross index fingerprint to the standard segment metadata while writing _version_ values.
 * The wrapper retains the delegate format's name and encoding so existing codecs can read the
 * DocValues without a custom SPI codec.
 */
public final class IndexFingerprintDocValuesFormat extends DocValuesFormat {
  private final DocValuesFormat delegate;
  private final String schemaSignature;

  public IndexFingerprintDocValuesFormat(DocValuesFormat delegate, String schemaSignature) {
    super(delegate.getName());
    this.delegate = delegate;
    this.schemaSignature = schemaSignature;
  }

  @Override
  public DocValuesConsumer fieldsConsumer(SegmentWriteState state) throws IOException {
    DocValuesConsumer delegateConsumer = delegate.fieldsConsumer(state);
    return new DocValuesConsumer() {
      private IndexFingerprint grossFingerprint;

      @Override
      public void addNumericField(FieldInfo field, DocValuesProducer valuesProducer)
          throws IOException {
        if (CommonParams.VERSION_FIELD.equals(field.name) && field.getDocValuesGen() == -1) {
          grossFingerprint =
              IndexFingerprint.getGrossFingerprint(
                  valuesProducer, field, state.segmentInfo.maxDoc());
        }
        delegateConsumer.addNumericField(field, valuesProducer);
      }

      @Override
      public void addBinaryField(FieldInfo field, DocValuesProducer valuesProducer)
          throws IOException {
        delegateConsumer.addBinaryField(field, valuesProducer);
      }

      @Override
      public void addSortedField(FieldInfo field, DocValuesProducer valuesProducer)
          throws IOException {
        delegateConsumer.addSortedField(field, valuesProducer);
      }

      @Override
      public void addSortedNumericField(FieldInfo field, DocValuesProducer valuesProducer)
          throws IOException {
        delegateConsumer.addSortedNumericField(field, valuesProducer);
      }

      @Override
      public void addSortedSetField(FieldInfo field, DocValuesProducer valuesProducer)
          throws IOException {
        delegateConsumer.addSortedSetField(field, valuesProducer);
      }

      @Override
      public void close() throws IOException {
        delegateConsumer.close();
        if (grossFingerprint != null) {
          state.segmentInfo.putAttribute(
              IndexFingerprint.SEGMENT_METADATA_ATTRIBUTE,
              grossFingerprint.toSegmentMetadata(state.segmentInfo, schemaSignature));
        }
      }
    };
  }

  @Override
  public DocValuesProducer fieldsProducer(SegmentReadState state) throws IOException {
    return delegate.fieldsProducer(state);
  }
}
