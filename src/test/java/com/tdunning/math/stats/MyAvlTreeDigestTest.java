package com.tdunning.math.stats;

import org.junit.Test;

import java.util.Random;

public class MyAvlTreeDigestTest {

    /**
     * 通过debug，大概明白思路了，就是保存了一系列的(质心, count).通过这个可以计算分位数，而且是比较准确的。
     * 感觉这个挺好理解的，而且也是十分准确的。
     */
    @Test
    public void test(){
        TDigest tDigest = TDigest.createAvlTreeDigest(10);
        for (int i = 1; i <= 100000; i++) {
            tDigest.add(i);
        }
        System.out.println((int) tDigest.quantile(0.2) + "," + (int)tDigest.quantile(0.5) + "," + (int)tDigest.quantile(0.6));

        Random random = new Random();
        tDigest = TDigest.createAvlTreeDigest(10);
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
