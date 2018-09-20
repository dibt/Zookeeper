package com.di.liststream;

import com.di.pojo.TestModel;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;

import static java.util.stream.Collectors.*;

public class listStreamTest {

    public List<TestModel> getList(){
        List<TestModel> list = new ArrayList<>();
        for(int i=0; i<10; i++){
            list.add(new TestModel(i,"test"+i,new BigDecimal(Math.random())));
        }
        return list;
    }

    /**
     * list count()
     *
     * list中元素个数;
     */
    @Test
    public void listStreamCountTest(){
        System.out.println(getList().parallelStream().count());

    }

    /**
     * list distinct()
     *
     * list去重  默认去重标准Object#equals(Object)
     * forEach() 并行处理,输出结果顺序不确定
     * forEachOrdered() 顺序处理 严格按照list顺序输出
     */
    @Test
    public void listStreamDistinct(){
        //list中添加两个相同的对象
        List<TestModel> list = getList();
        TestModel testModel = new TestModel(4,"aesvd",new BigDecimal(Math.random()));
        TestModel testModel1 = testModel;
        list.addAll(Arrays.asList(testModel,testModel1));

        list.parallelStream().distinct().forEachOrdered(System.out::println);
    }

    /**
     * list filter()
     *
     * list去重 根据list中对象的id去重
     * @param keyExtractor
     * @param <T>
     * @return
     */
    public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Set<Object> seen = ConcurrentHashMap.newKeySet();
        return t -> seen.add(keyExtractor.apply(t));
    }
    @Test
    public void listStreamFilterById() {
        //list中添加一个id相同的对象
        List<TestModel> list = getList();
        TestModel testModel = new TestModel(0,"test00",new BigDecimal(Math.random()));
        list.addAll(Arrays.asList(testModel));
        list.parallelStream().filter(distinctByKey(TestModel::getId)).forEach(System.out::println);
    }

    /**
     * list filter()
     *
     * 根据name进行过滤
     */
    @Test
    public void listStreamFilterByName(){
        getList().parallelStream().filter(testModel -> (!testModel.getName().contains("8"))).forEach(System.out::println);
    }

    /**
     * list map()
     * 将对象和属性映射成一对一的map 拿所有对象的某个属性
     */
    @Test
    public void listStreamMap(){
        List<String> list = getList().parallelStream().map(TestModel::getName).collect(toList());
        list.stream().forEach(System.out::println);
    }

    /**
     * list map()
     * 将对象和属性映射成一对一的map 对所有对象的某个属性求和
     */
    @Test
    public void listStreamMapSum(){
        BigDecimal totalPrice = getList().parallelStream().map(TestModel::getPrice).reduce(BigDecimal.ZERO,BigDecimal::add);
        System.out.println(totalPrice);
    }

    /**
     * list toMap()
     * 需要注意的是：
     * toMap 如果集合对象有重复的key，会报错Duplicate key ....
     * 可以用 (k1,k2)->k1 来设置，如果有重复的key,则保留key1,舍弃key2
     * 通过使用Function接口中的默认方法identity()来改进代码
     */
    @Test
    public void listStreamToMap(){
        List<TestModel> list = getList();
        TestModel testModel = new TestModel(0,"test00",new BigDecimal(Math.random()));
        list.addAll(Arrays.asList(testModel));
        //根据一个字段维度转map
        //Map<Integer,TestModel> testModelMap = list.parallelStream().collect(toMap(TestModel::getId,Function.identity(),(t1,t2)->t1));
        //Map<Integer,TestModel> testModelMap = list.parallelStream().collect(toMap(TestModel::getId,part->part,(t1,t2)->t1));
        //根据多个字段维度转map
        Map<Object,Object> testModelMap = list.parallelStream().collect(toMap(key->key.getId()+key.getName(),part->part,(t1,t2)->t1));
        showMap(testModelMap);

    }
    private void showMap(Map<Object,Object> map){
        map.keySet().forEach(key->{
            System.out.println("key = "+key+",value = "+map.get(key));
        });
    }

    /**
     * list groupBy()
     * 将属性值相同的放在一起
     */
    @Test
    public void listStreamGroupBy(){
        List<TestModel> list = getList();
        TestModel testModel = new TestModel(0,"test00",new BigDecimal(Math.random()));
        list.addAll(Arrays.asList(testModel));
        Map<Integer,List<TestModel>> map = list.parallelStream().collect(groupingBy(TestModel::getId));
        map.keySet().forEach(key->{
            System.out.println("key = "+key+",value = "+map.get(key));
        });
    }


}
