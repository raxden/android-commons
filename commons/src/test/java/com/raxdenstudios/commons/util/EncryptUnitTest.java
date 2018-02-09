/*
 * Copyright 2014 Ángel Gómez
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.raxdenstudios.commons.util;


import com.raxdenstudios.commons.BuildConfig;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertEquals;

/**
 * Created by agomez on 11/02/2016.
 */
@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class EncryptUnitTest {

    private static final String KEY = "thisIsAKeyToEncryptWithNumbersByExample23";

    @Test
    public void MD5Hash() throws Exception {
        String defaultHash = EncryptUtils.hashKeyForDisk(KEY, EncryptUtils.AlgorithmType.MD5);
        assertEquals(defaultHash, "38f891f0fc1a45ef7a7d282cbc0321a8");
    }

    @Test
    public void SHAHash() throws Exception {
        String defaultHash = EncryptUtils.hashKeyForDisk(KEY, EncryptUtils.AlgorithmType.SHA);
        assertEquals(defaultHash, "15effc3bf92bab6a02f6b6a98630b3d423b59fc4");
    }

    @Test
    public void SHA1Hash() throws Exception {
        String defaultHash = EncryptUtils.hashKeyForDisk(KEY, EncryptUtils.AlgorithmType.SHA_1);
        assertEquals(defaultHash, "15effc3bf92bab6a02f6b6a98630b3d423b59fc4");
    }

    @Test
    public void SHA512Hash() throws Exception {
        String defaultHash = EncryptUtils.hashKey(KEY, EncryptUtils.AlgorithmType.SHA_512);
        assertEquals(defaultHash, "JRR9jrCnEsR4cJYfCnUEk9e7z57b5YbXnUvkHZDH9cwH-Tc03WzWf0VB-oaeomCsup2bPqgHJOE6TQZCUZFaFg==");
    }

}
