package com.tdunning.math.stats;

import org.junit.Test;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.util.*;

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

    @Test
    public void test1(){
        TDigest tDigest = TDigest.createMergingDigest(100);
        for (int i = 1; i <= 100000; i++) {
            tDigest.add(i);
        }
        System.out.println((int) tDigest.quantile(0.2) + "," + (int)tDigest.quantile(0.5) + "," + (int)tDigest.quantile(0.6));

        Random random = new Random();
        tDigest = TDigest.createMergingDigest(100);
        for (int i = 1; i <= 1000000; i++) {
            if(i >= 1000000 - 1000){
                i = i;
            }
            tDigest.add(random.nextInt(100000 + 1));
        }
        System.out.println((int) tDigest.quantile(0.2) + "," + (int)tDigest.quantile(0.5) + "," + (int)tDigest.quantile(0.6));

        System.out.println(tDigest.byteSize());
    }

    @Test
    public void test2(){
        TDigest tDigest = TDigest.createMergingDigest(10);
        for (int i = 1; i <= 100000; i+=100) {
            tDigest.add(i);
        }
        System.out.println((int) tDigest.quantile(0.2) + "," + (int)tDigest.quantile(0.5) + "," + (int)tDigest.quantile(0.6));

    }

    @Test
    public void test3(){
        TDigest tDigest = TDigest.createMergingDigest(100);
        for (int i = 1; i <= 1000; i++) {
            // 不能这样合并，误差会很大
            tDigest.add(i, i * 1000);
        }
        System.out.println((int) tDigest.quantile(0.2) + "," + (int)tDigest.quantile(0.5) + "," + (int)tDigest.quantile(0.6));

        tDigest = TDigest.createMergingDigest(100);
        for (int i = 1; i <= 1000; i++) {
            for (int j = 0; j < 1000; j++) {
                tDigest.add(i, 1);
            }
        }
        System.out.println((int) tDigest.quantile(0.2) + "," + (int)tDigest.quantile(0.5) + "," + (int)tDigest.quantile(0.6));
    }

    @Test
    public void testMerge(){
        TDigest tDigest = TDigest.createMergingDigest(100);
        for (int i = 1; i <= 1000; i++) {
            for (int j = 0; j < 1000; j++) {
                tDigest.add(i, 1);
            }
        }

        System.out.println((int) tDigest.quantile(0.2) + "," + (int)tDigest.quantile(0.5) + "," + (int)tDigest.quantile(0.6));

        TDigest tDigestMerge = TDigest.createMergingDigest(100);
        tDigestMerge.add(tDigest);
        System.out.println((int) tDigestMerge.quantile(0.2) + "," + (int)tDigestMerge.quantile(0.5) + "," + (int)tDigestMerge.quantile(0.6));

        System.out.println("####################");

        // 手动合并
        TDigest tDigestMerge1 = TDigest.createMergingDigest(100);
        List<Centroid> tmp = new ArrayList<>();
        for (Centroid centroid : tDigest.centroids()) {
            tmp.add(centroid);
        }

        for (Centroid centroid : tmp) {
            ((AbstractTDigest)tDigestMerge1).add(centroid.mean(), centroid.count(), centroid);
        }
        System.out.println((int) tDigestMerge1.quantile(0.2) + "," + (int)tDigestMerge1.quantile(0.5) + "," + (int)tDigestMerge1.quantile(0.6));

        TDigest tDigestMerge2 = TDigest.createMergingDigest(100);
        Collections.shuffle(tmp);

        for (Centroid centroid : tmp) {
            ((AbstractTDigest)tDigestMerge2).add(centroid.mean(), centroid.count(), centroid);
        }
        System.out.println((int) tDigestMerge2.quantile(0.2) + "," + (int)tDigestMerge2.quantile(0.5) + "," + (int)tDigestMerge2.quantile(0.6));
    }

    @Test
    public void testMerge2(){
        TDigest tDigest = TDigest.createMergingDigest(100);
        Random random = new Random();
        for (int i = 1; i <= 1000000; i++) {
            tDigest.add(random.nextInt(100000 + 1));
        }

        System.out.println((int) tDigest.quantile(0.2) + "," + (int)tDigest.quantile(0.5) + "," + (int)tDigest.quantile(0.6));

        TDigest tDigestMerge = TDigest.createMergingDigest(100);
        tDigestMerge.add(tDigest);
        System.out.println((int) tDigestMerge.quantile(0.2) + "," + (int)tDigestMerge.quantile(0.5) + "," + (int)tDigestMerge.quantile(0.6));

        System.out.println("####################");

        // 手动合并
        TDigest tDigestMerge1 = TDigest.createMergingDigest(100);
        List<Centroid> tmp = new ArrayList<>();
        for (Centroid centroid : tDigest.centroids()) {
            tmp.add(centroid);
        }

        for (Centroid centroid : tmp) {
            ((AbstractTDigest)tDigestMerge1).add(centroid.mean(), centroid.count(), centroid);
        }
        System.out.println((int) tDigestMerge1.quantile(0.2) + "," + (int)tDigestMerge1.quantile(0.5) + "," + (int)tDigestMerge1.quantile(0.6));

        TDigest tDigestMerge2 = TDigest.createMergingDigest(100);
        Collections.shuffle(tmp);

        for (Centroid centroid : tmp) {
            ((AbstractTDigest)tDigestMerge2).add(centroid.mean(), centroid.count(), centroid);
        }
        System.out.println((int) tDigestMerge2.quantile(0.2) + "," + (int)tDigestMerge2.quantile(0.5) + "," + (int)tDigestMerge2.quantile(0.6));
    }


    /**
     * 不同精度之间可以合并，而且合并仅仅使用了质心集合，也就是说不同语言实现的TDigest可以合并。就算他们计算相邻质心的方法稍微有差异也是不影响最后的结果的，就是可能有很小的误差。
     */
    @Test
    public void testMerge3(){
        TDigest tDigest = TDigest.createMergingDigest(200);
        Random random = new Random();
        for (int i = 1; i <= 1000000; i++) {
            tDigest.add(random.nextInt(100000 + 1));
        }

        System.out.println((int) tDigest.quantile(0.1) + "," +(int) tDigest.quantile(0.2) + "," + (int)tDigest.quantile(0.5) + "," + (int)tDigest.quantile(0.6)+ "," + (int)tDigest.quantile(0.9));

        TDigest tDigestMerge = TDigest.createMergingDigest(100);
        tDigestMerge.add(tDigest);
        System.out.println((int) tDigestMerge.quantile(0.1) + "," + (int) tDigestMerge.quantile(0.2) + "," + (int)tDigestMerge.quantile(0.5) + "," + (int)tDigestMerge.quantile(0.6)+ "," + (int)tDigestMerge.quantile(0.9));

        System.out.println("####################");

        // 手动合并
        TDigest tDigestMerge1 = TDigest.createMergingDigest(100);
        List<Centroid> tmp = new ArrayList<>();
        for (Centroid centroid : tDigest.centroids()) {
            tmp.add(centroid);
        }

        for (Centroid centroid : tmp) {
            ((AbstractTDigest)tDigestMerge1).add(centroid.mean(), centroid.count(), centroid);
        }
        System.out.println((int) tDigestMerge1.quantile(0.1) + "," +(int) tDigestMerge1.quantile(0.2) + "," + (int)tDigestMerge1.quantile(0.5) + "," + (int)tDigestMerge1.quantile(0.6)+ "," + (int)tDigestMerge1.quantile(0.9));

        TDigest tDigestMerge2 = TDigest.createMergingDigest(100);
        Collections.shuffle(tmp);

        for (Centroid centroid : tmp) {
            ((AbstractTDigest)tDigestMerge2).add(centroid.mean(), centroid.count(), centroid);
        }
        System.out.println((int) tDigestMerge2.quantile(0.1) + "," +(int) tDigestMerge2.quantile(0.2) + "," + (int)tDigestMerge2.quantile(0.5) + "," + (int)tDigestMerge2.quantile(0.6)+ "," + (int)tDigestMerge2.quantile(0.9));
    }

    @Test
    public void tesDerForCK() throws Exception{
        TDigest tDigest = TDigest.createMergingDigest(100);
        Random random = new Random();
        int[] datas = new int[147];
        for (int i = 1; i <= 147; i++) {
            int v = random.nextInt(100) + 1;
            tDigest.add(v);
            datas[i-1] = v;
        }
        tDigest.quantile(0.5);

        String str = "kwEAAIA/AACAPwAAgD8AAIA/AAAAQAAAgD8AAABAAACAPwAAgEAAAIA/AACAQAAAgD8AAKBAAACAPwAAoEAAAIA/AADAQAAAgD8AAMBAAACAPwAAAEEAAIA/AAAgQQAAgD8AACBBAACAPwAAMEEAAIA/AAAwQQAAgD8AADBBAACAPwAAQEEAAIA/AABAQQAAgD8AAEBBAACAPwAAUEEAAIA/AABgQQAAgD8AAGBBAACAPwAAYEEAAIA/AABgQQAAgD8AAGBBAACAPwAAcEEAAIA/AACAQQAAgD8AAIhBAACAPwAAiEEAAIA/AACQQQAAgD8AAJBBAACAPwAAmEEAAIA/AACgQQAAgD8AAKhBAACAPwAAqEEAAIA/AADIQQAAgD8AAMhBAACAPwAA0EEAAIA/AADoQQAAgD8AAPBBAACAPwAA8EEAAIA/AAD4QQAAgD8AAPhBAACAPwAA+EEAAIA/AAAAQgAAgD8AAABCAACAPwAABEIAAIA/AAAEQgAAgD8AAAxCAACAPwAAEEIAAIA/AAAQQgAAgD8AABBCAACAPwAAGEIAAIA/AAAYQgAAgD8AABhCAACAPwAAGEIAAIA/AAAcQgAAgD8AACBCAACAPwAAIEIAAIA/AAAgQgAAgD8AACRCAACAPwAAKEIAAIA/AAAwQgAAgD8AADRCAACAPwAANEIAAIA/AAA0QgAAgD8AADRCAACAPwAAPEIAAIA/AABAQgAAgD8AAEBCAACAPwAAREIAAIA/AABEQgAAgD8AAERCAACAPwAASEIAAIA/AABMQgAAgD8AAExCAACAPwAATEIAAIA/AABMQgAAgD8AAFBCAACAPwAAVEIAAIA/AABYQgAAgD8AAFxCAACAPwAAXEIAAIA/AABgQgAAgD8AAGBCAACAPwAAYEIAAIA/AABgQgAAgD8AAGBCAACAPwAAZEIAAIA/AABsQgAAgD8AAHBCAACAPwAAfEIAAIA/AACAQgAAgD8AAIJCAACAPwAAgkIAAIA/AACEQgAAgD8AAIRCAACAPwAAhkIAAIA/AACGQgAAgD8AAIhCAACAPwAAikIAAIA/AACKQgAAgD8AAI5CAACAPwAAkEIAAIA/AACQQgAAgD8AAJJCAACAPwAAkkIAAIA/AACUQgAAgD8AAJRCAACAPwAAmEIAAIA/AACaQgAAgD8AAJpCAACAPwAAnEIAAIA/AACeQgAAgD8AAKBCAACAPwAAoEIAAIA/AACiQgAAgD8AAKRCAACAPwAApEIAAIA/AACkQgAAgD8AAKRCAACAPwAAqEIAAIA/AACoQgAAgD8AAKpCAACAPwAAqkIAAIA/AACsQgAAgD8AAKxCAACAPwAAsEIAAIA/AACyQgAAgD8AALRCAACAPwAAtkIAAIA/AAC2QgAAgD8AALhCAACAPwAAuEIAAIA/AAC6QgAAgD8AAL5CAACAPwAAwEIAAIA/AADCQgAAgD8AAMJCAACAPwAAwkIAAIA/AADCQgAAgD8AAMJCAACAPwAAxEIAAIA/AADEQgAAgD8AAMZCAACAPwAAxkIAAIA/AADIQgAAgD8=";
        byte[] bytes = Base64.getDecoder().decode(str.getBytes(StandardCharsets.UTF_8));
        ByteBuffer buffer = ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN);
        int size = (int)readVarInt(buffer);
        System.out.println(size);
        List<Centroid> tmp = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            float centroid = buffer.getFloat();
            float count = buffer.getFloat();
            System.out.println(String.format("%.2f:%.2f", centroid, count));
            tmp.add(new Centroid(centroid, (int) count));
        }

        System.out.println(buffer.hasRemaining());
        Collections.shuffle(tmp);
        TDigest desTDigest = TDigest.createMergingDigest(100);
        for (Centroid centroid : tmp) {
            ((AbstractTDigest)desTDigest).add(centroid.mean(), centroid.count(), centroid);
        }
        // ck:[1.0,11.0,32.0,50.0,82.0,92.0,100.0]
        System.out.println( desTDigest.quantile(0) + "," + desTDigest.quantile(0.1) + "," + desTDigest.quantile(0.3) + "," + desTDigest.quantile(0.5)
                + "," + desTDigest.quantile(0.8)+ "," + desTDigest.quantile(0.9)+ "," + desTDigest.quantile(1));
    }


    public long readVarInt(ByteBuffer buffer) throws IOException {
        int number = 0;
        for (int i = 0; i < 9; i++) {
            int byt = buffer.get();

            number |= (byt & 0x7F) << (7 * i);

            if ((byt & 0x80) == 0) {
                break;
            }
        }
        return number;
    }
}
