/**
 * Copyright 2016 Yahoo Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.yahoo.athenz.zms.pkey.file;

import static org.testng.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.PrivateKey;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.yahoo.athenz.zms.ZMSConsts;
import com.yahoo.athenz.zms.pkey.PrivateKeyStore;
import com.yahoo.athenz.zms.pkey.file.FilePrivateKeyStore;
import com.yahoo.athenz.zms.pkey.file.FilePrivateKeyStoreFactory;

public class FilePrivateKeyStoreTest {

    @BeforeClass
    public void setUp() throws Exception {
        System.setProperty(ZMSConsts.ZMS_PROP_PRIVATE_KEY_STORE_CLASS, "com.yahoo.athenz.zms.pkey.file.FilePrivateKeyStoreFactory");
        System.setProperty(ZMSConsts.ZMS_PROP_PRIVATE_KEY, "src/test/resources/zms_private.pem");
        System.setProperty(ZMSConsts.ZMS_PROP_PUBLIC_KEY, "src/test/resources/zms_public.pem");
    }
    
    @Test
    public void testCreateStore() {
        FilePrivateKeyStoreFactory factory = new FilePrivateKeyStoreFactory();
        PrivateKeyStore store = factory.create("localhost");
        assertNotNull(store);
    }
    
    @Test
    public void testRetrievePublicKeyValid() {
        
        FilePrivateKeyStoreFactory factory = new FilePrivateKeyStoreFactory();
        PrivateKeyStore store = factory.create("localhost");
        
        String saveProp = System.getProperty(ZMSConsts.ZMS_PROP_PUBLIC_KEY);
        System.setProperty(ZMSConsts.ZMS_PROP_PUBLIC_KEY, "src/test/resources/zms_public.pem");

        String pubKey = store.getPEMPublicKey();
        assertNotNull(pubKey);
        
        System.setProperty(ZMSConsts.ZMS_PROP_PUBLIC_KEY, saveProp);
    }
    
    @Test
    public void testRetrievePublicKeyInValid() {
        
        FilePrivateKeyStoreFactory factory = new FilePrivateKeyStoreFactory();
        PrivateKeyStore store = factory.create("localhost");
        
        String saveProp = System.getProperty(ZMSConsts.ZMS_PROP_PUBLIC_KEY);
        System.setProperty(ZMSConsts.ZMS_PROP_PUBLIC_KEY, "src/test/resources/invalid_zms_public.pem");
        
        try {
            store.getPEMPublicKey();
            fail();
        } catch (Exception ex) {
            assertTrue(true);
        }
        
        System.setProperty(ZMSConsts.ZMS_PROP_PUBLIC_KEY, saveProp);
    }
    
    @Test
    public void testRetrievePrivateKeyValid() {
        
        FilePrivateKeyStoreFactory factory = new FilePrivateKeyStoreFactory();
        PrivateKeyStore store = factory.create("localhost");
        
        String saveProp = System.getProperty(ZMSConsts.ZMS_PROP_PRIVATE_KEY);
        System.setProperty(ZMSConsts.ZMS_PROP_PRIVATE_KEY, "src/test/resources/zms_private.pem");

        StringBuilder keyId = new StringBuilder(256);
        PrivateKey privKey = store.getPrivateKey(keyId);
        assertNotNull(privKey);
        
        System.setProperty(ZMSConsts.ZMS_PROP_PRIVATE_KEY, saveProp);
    }
    
    @Test
    public void testRetrievePrivateKeyInValid() {
        
        FilePrivateKeyStoreFactory factory = new FilePrivateKeyStoreFactory();
        PrivateKeyStore store = factory.create("localhost");
        
        String saveProp = System.getProperty(ZMSConsts.ZMS_PROP_PRIVATE_KEY);
        System.setProperty(ZMSConsts.ZMS_PROP_PRIVATE_KEY, "src/test/resources/invalid_zms_private.pem");
        
        try {
            StringBuilder keyId = new StringBuilder(256);
            store.getPrivateKey(keyId);
            fail();
        } catch (Exception ex) {
            assertTrue(true);
        }
        
        System.setProperty(ZMSConsts.ZMS_PROP_PRIVATE_KEY, saveProp);
    }
    
    @Test
    public void testGetStringNullStream() throws IOException {

        FilePrivateKeyStoreFactory factory = new FilePrivateKeyStoreFactory();
        FilePrivateKeyStore store = (FilePrivateKeyStore) factory.create("localhost");
        assertNull(store.getString(null));
    }
    
    @Test
    public void testGetString() throws IOException {
        String str = "This is a Unit Test String";
        
        FilePrivateKeyStoreFactory factory = new FilePrivateKeyStoreFactory();
        FilePrivateKeyStore store = (FilePrivateKeyStore) factory.create("localhost");
        try (InputStream is = new ByteArrayInputStream(str.getBytes("UTF-8"))) {
            String getStr = store.getString(is);
            assertEquals(getStr, str);
        }
    }
}
