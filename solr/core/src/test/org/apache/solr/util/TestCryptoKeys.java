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
package org.apache.solr.util;

import java.io.ByteArrayInputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.Signature;
import org.apache.solr.SolrTestCase;
import org.junit.Before;
import org.junit.Test;

public class TestCryptoKeys extends SolrTestCase {
  private byte[] data;
  private KeyPair keyPair;
  private byte[] sha512Signature;
  private byte[] sha1Signature;

  @Before
  public void prepareSignatures() throws Exception {
    data = "package artifact".getBytes(StandardCharsets.UTF_8);
    KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
    keyPairGenerator.initialize(2048);
    keyPair = keyPairGenerator.generateKeyPair();
    sha512Signature = sign(data, keyPair, "SHA512withRSA");
    sha1Signature = sign(data, keyPair, "SHA1withRSA");
  }

  @Test
  public void testByteBufferAcceptsSha512() throws Exception {
    assertTrue(
        "ByteBuffer verification should accept SHA-512",
        CryptoKeys.verify(keyPair.getPublic(), sha512Signature, ByteBuffer.wrap(data)));
  }

  @Test
  public void testByteBufferRejectsSha1() throws Exception {
    assertFalse(
        "ByteBuffer verification should reject SHA-1",
        CryptoKeys.verify(keyPair.getPublic(), sha1Signature, ByteBuffer.wrap(data)));
  }

  @Test
  public void testInputStreamAcceptsSha512() throws Exception {
    assertTrue(
        "InputStream verification should accept SHA-512",
        CryptoKeys.verify(keyPair.getPublic(), sha512Signature, new ByteArrayInputStream(data)));
  }

  @Test
  public void testInputStreamRejectsSha1() throws Exception {
    assertFalse(
        "InputStream verification should reject SHA-1",
        CryptoKeys.verify(keyPair.getPublic(), sha1Signature, new ByteArrayInputStream(data)));
  }

  private static byte[] sign(byte[] data, KeyPair keyPair, String algorithm) throws Exception {
    Signature signature = Signature.getInstance(algorithm);
    signature.initSign(keyPair.getPrivate());
    signature.update(data);
    return signature.sign();
  }
}
