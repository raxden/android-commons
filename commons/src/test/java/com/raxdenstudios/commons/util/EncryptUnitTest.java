package com.raxdenstudios.commons.util;


import com.raxdenstudios.commons.BuildConfig;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;


import static org.junit.Assert.assertEquals;

/**
 * Created by agomez on 11/02/2016.
 */
@RunWith(RobolectricGradleTestRunner.class)
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
}
