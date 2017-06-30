package com.neusoft.soc.nli.jamass;

import com.neusoft.soc.nli.jamass.core.AmassEngine;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

/**
 * JAmass 主类，启动收集日志引擎
 * Created by luyb on 2017/6/5.
 */
public class AmassMainTest {

    private static Logger logger = LogManager.getLogger(AmassMainTest.class);

    /**
     * JAmass入口函数
     * @param args
     *            启动特定服务参数,[start|stop]或没有参数
     */
    public static void main(String[] args) {
        try {
            addSpeedTest();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * speed: LinkedList , LinkedHashSet ,
     */
    private static void addSpeedTest() throws InterruptedException {
        Set<Integer> set = new HashSet<>();
        Set<Integer> set1 = new LinkedHashSet<>();
        Set<Integer> set2 = new TreeSet<>();
        List<Integer> list = new ArrayList<>();
        List<Integer> list1 = new LinkedList<>();

        Thread.sleep(1000);

        long s5 = System.currentTimeMillis();
        for(int i=0;i<100000;i++){
            list1.add(i);
        }
        long e5 = System.currentTimeMillis();
        long s5g = System.currentTimeMillis();
        for(int i=0;i<100000;i++){
            list1.get(i);
        }
        long e5g = System.currentTimeMillis();
        System.out.println("LinkedList +"+ (e5 -s5)+"ms" );
        System.out.println("LinkedList -"+ (e5g -s5g)+"ms" );


        Thread.sleep(1000);


        long s2 = System.currentTimeMillis();
        for(Integer i:list1){
            i++;
        }
        long e2 = System.currentTimeMillis();
        long s2g = System.currentTimeMillis();
        for(Integer i:set1){
            i++;
        }
        long e2g = System.currentTimeMillis();
        System.out.println("LinkedHashSet +"+ (e2 -s2)+"ms" );
        System.out.println("LinkedHashSet -"+ (e2g -s2g)+"ms" );


        Thread.sleep(1000);


        long s1 = System.currentTimeMillis();
        for(int i=0;i<100000;i++){
            set.add(i);
        }
        long e1 = System.currentTimeMillis();
        long s1g = System.currentTimeMillis();
        for(Integer i:set){
            i++;
        }
        long e1g = System.currentTimeMillis();
        System.out.println("HashSet +"+ (e1 -s1)+"ms" );
        System.out.println("HashSet -"+ (e1g -s1g)+"ms" );

        Thread.sleep(1000);

        long s4 = System.currentTimeMillis();
        for(int i=0;i<100000;i++){
            list.add(i);
        }
        long e4 = System.currentTimeMillis();
        long s4g = System.currentTimeMillis();
        for(Integer i:list){
            i++;
        }
        long e4g = System.currentTimeMillis();
        System.out.println("ArrayList +"+ (e4 -s4)+"ms" );
        System.out.println("ArrayList -"+ (e4g -s4g)+"ms" );

        Thread.sleep(1000);

        long s3 = System.currentTimeMillis();
        for(int i=0;i<100000;i++){
            set2.add(i);
        }
        long e3 = System.currentTimeMillis();
        long s3g = System.currentTimeMillis();
        for(Integer i:set2){
            i++;
        }
        long e3g = System.currentTimeMillis();
        System.out.println("TreeSet +"+ (e3 -s3)+"ms" );
        System.out.println("TreeSet -"+ (e3g -s3g)+"ms" );






    }
}
