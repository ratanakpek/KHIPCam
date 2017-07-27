package com.kshrd.ipcam.network;

import java.nio.ByteBuffer;
import java.util.UUID;

/**
 * Created by rina on 1/22/17.
 */

/**
 * @Class User for generator secret key confirm
 * @Pattern SingleTon
 */
public class SecretKeyGenerator {
    /**
     *@Method GetGen
     * @return Key gen
     */
    public String getGen(){
        UUID uuid = UUID.randomUUID();
        long l = ByteBuffer.wrap(uuid.toString().getBytes()).getLong();
        return Long.toString(l, Character.MAX_RADIX).substring(0,8);
    }
}
