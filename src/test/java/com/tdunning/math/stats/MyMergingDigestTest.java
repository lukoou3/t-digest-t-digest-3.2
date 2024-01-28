package com.tdunning.math.stats;

import org.junit.Test;

import java.util.Random;

public class MyMergingDigestTest {

    @Test
    public void test(){
        TDigest tDigest = TDigest.createMergingDigest(10);
        for (int i = 1; i <= 100000; i++) {
            tDigest.add(i);
        }
        System.out.println((int) tDigest.quantile(0.2) + "," + (int)tDigest.quantile(0.5) + "," + (int)tDigest.quantile(0.6));

        Random random = new Random();
        tDigest = TDigest.createMergingDigest(10);
        for (int i = 1; i <= 1000000; i++) {
            if(i >= 1000000 - 1000){
                i = i;
            }
            tDigest.add(random.nextInt(100000 + 1));
        }
        System.out.println((int) tDigest.quantile(0.2) + "," + (int)tDigest.quantile(0.5) + "," + (int)tDigest.quantile(0.6));

        System.out.println(tDigest.byteSize());
    }
}
