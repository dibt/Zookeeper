package com.di.liststream;

import com.di.pojo.TestModel;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.*;

public class listStreamTest {

    public List<TestModel> getList() {
        List<TestModel> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            list.add(new TestModel(i, "test" + i, new BigDecimal(Math.random())));
        }
        return list;
    }

    /**
     * list count()
     * <p>
     * list中元素个数;
     */
    @Test
    public void listStreamCountTest() {
        System.out.println(getList().parallelStream().count());

    }

    /**
     * list distinct()
     * <p>
     * list去重  默认去重标准Object#equals(Object)
     * forEach() 并行处理,输出结果顺序不确定
     * forEachOrdered() 顺序处理 严格按照list顺序输出
     */
    @Test
    public void listStreamDistinct() {
        //list中添加两个相同的对象
        List<TestModel> list = getList();
        TestModel testModel = new TestModel(4, "aesvd", new BigDecimal(Math.random()));
        TestModel testModel1 = testModel;
        list.addAll(Arrays.asList(testModel, testModel1));

        list.parallelStream().distinct().forEachOrdered(System.out::println);
    }

    /**
     * list filter()
     * <p>
     * list去重 根据list中对象的id去重
     *
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
        TestModel testModel = new TestModel(0, "test00", new BigDecimal(Math.random()));
        list.addAll(Arrays.asList(testModel));
        list.parallelStream().filter(distinctByKey(TestModel::getId)).forEach(System.out::println);
    }

    /**
     * list filter()
     * <p>
     * 根据name进行过滤
     */
    @Test
    public void listStreamFilterByName() {
        getList().parallelStream().filter(testModel -> (!testModel.getName().contains("8"))).forEach(System.out::println);
    }

    /**
     * list map()
     * 将对象和属性映射成一对一的map 拿所有对象的某个属性
     */
    @Test
    public void listStreamMap() {
        List<String> list = getList().parallelStream().map(TestModel::getName).collect(toList());
        list.stream().forEach(System.out::println);
    }

    /**
     * list map()
     * 将对象和属性映射成一对一的map 对所有对象的某个属性求和
     */
    @Test
    public void listStreamMapSum() {
        BigDecimal totalPrice = getList().parallelStream().map(TestModel::getPrice).reduce(BigDecimal.ZERO, BigDecimal::add);
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
    public void listStreamToMap() {
        List<TestModel> list = getList();
        TestModel testModel = new TestModel(0,"test00",new BigDecimal(Math.random()));
        list.addAll(Arrays.asList(testModel));
        //toMap  value为null的情况 报空指针错误
        //Collectors.toMap底层是基于Map.merge方法来实现的，而merge中value是不能为null的，如果为null，就会抛出空指针异常
        //TestModel testModel = new TestModel(11, null, new BigDecimal(Math.random()));
        //list.addAll(Arrays.asList(testModel));
        //Map<Integer, String> testModelMap = list.parallelStream().collect(Collectors.toMap(TestModel::getId, TestModel::getName));
        //根据一个字段维度转map
        //Map<Integer,TestModel> testModelMap = list.parallelStream().collect(toMap(TestModel::getId,Function.identity(),(t1,t2)->t1));
        //Map<Integer,TestModel> testModelMap = list.parallelStream().collect(toMap(TestModel::getId,part->part,(t1,t2)->t1));
        //根据多个字段维度转map
        Map<Object,Object> testModelMap = list.parallelStream().collect(toMap(key->key.getId()+key.getName(),part->part,(t1,t2)->t1));
        testModelMap.keySet().forEach(key -> {
            System.out.println("key = " + key + ",value = " + testModelMap.get(key));
        });

    }

    /**
     * list groupBy()
     * 将属性值相同的放在一起
     * 有三种方法，查看源码可以知道前两个方法最终调用第三个方法，第二个参数默认HashMap::new 第三个参数默认Collectors.toList()。
     */
    @Test
    public void listStreamGroupBy() {
        List<TestModel> list = getList();
        TestModel testModel = new TestModel(0, "test00", new BigDecimal(Math.random()));
        list.addAll(Arrays.asList(testModel));
        //Map<Integer, List<TestModel>> map = list.parallelStream().collect(Collectors.groupingBy(TestModel::getId));
        //Map<Integer, Map<String,List<TestModel>>> map = list.parallelStream().collect(Collectors.groupingBy(TestModel::getId,groupingBy(TestModel::getName)));
        Map<Integer, Long> map = list.parallelStream().collect(Collectors.groupingBy(TestModel::getId,HashMap::new,Collectors.counting()));
        map.keySet().forEach(key -> {
            System.out.println("key = " + key + ",value = " + map.get(key));
        });
    }

    /**
     * list summarizingDouble()
     * 获取最大值、最小值、平均值、总和值、总数。
     */
    @Test
    public void listStream() {
        //DoubleSummaryStatistics summaryStatistics = Stream.of(1, 3, 4).collect(Collectors.summarizingDouble(x -> x));
        IntSummaryStatistics intSummaryStatistics = getList().parallelStream().map(TestModel::getId).collect(toList()).parallelStream().collect(summarizingInt(i -> i));
        System.out.println(intSummaryStatistics.getMax());
    }

    /**
     * list partitioningBy()
     * 把数据分成两部分，key为ture/false
     */
    @Test
    public void listStreamPartition() {
        Map<Boolean, List<Integer>> collect4 = Stream.of(1, 3, 4).collect(Collectors.partitioningBy(x -> x > 2));
        //第二个参数默认是Collectors.toList()
        //Map<Boolean, Long> collect4 = Stream.of(1, 3, 4).collect(Collectors.partitioningBy(x -> x > 2, Collectors.counting()));
        collect4.keySet().forEach(key -> {
            System.out.println("key = " + key + ",value = " + collect4.get(key));
        });
    }

    /**
     * list  joining()
     * 拼接字符串
     */
    @Test
    public void listStreamJoin() {
        System.out.println(Stream.of("a", "b", "c").collect(Collectors.joining(",")));
    }

    /**
     * list reducing()
     * 在求累计值的时候，可以对参数值进行改变(第二个参数)
     * reduce没有第二个参数
     */
    @Test
    public void listStreamReducing() {
        System.out.println(Stream.of(1, 3, 4).reduce(2, (x, y) -> x + y));
        System.out.println(Stream.of(1, 3, 4).collect(Collectors.reducing(2, x -> x + 1, (x, y) -> x + y)));
    }

    /**
     * list toMap()
     * 将一个 List 转成 map[1=1,2=2,3=3,4=4,5=5]，然后与另一个map[1=1,2=4,3=9,4=16,5=25]的相同key的value进行相加。
     * 注意, toMap 的最后一个参数是 Supplier<Map> ， 是 Map 提供器，而不是 Map 对象。
     */
    @Test
    public void listStreamSupplier() {
        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5);
        Supplier<Map<Integer, Integer>> mapSupplier = () -> list.stream().collect(Collectors.toMap(x -> x, y -> y * y));
        //将Map中相同的key的value相加
        Map<Integer, Integer> mapValueAdd = list.stream().collect(Collectors.toMap(x -> x, y -> y, (v1, v2) -> v1 + v2, mapSupplier));
        System.out.println(mapValueAdd);
    }

    /**
     * list collectingAndThen()
     * 先执行collect操作后再执行第二个参数的表达式
     */
    @Test
    public void listStreamCollectingAndThen() {
        String str = Stream.of("a", "b", "c").collect(Collectors.collectingAndThen(Collectors.joining(","), x -> x + "d"));
        System.out.println(str);

    }

    /**
     * list mapping()
     */
    @Test
    public void listStreamMapping() {
        Predicate<Integer> predicate = x -> x > 5;
        System.out.println(predicate.test(2));
        System.out.println(Stream.of("a", "b", "c").collect(Collectors.mapping(x -> x.toUpperCase(), Collectors.joining(","))));
    }

    /**
     * 一个二维数组转换为一维数组
     */
    @Test
    public void listStreamFlatMap() {
        List<List<Integer>> nums = Arrays.asList(Arrays.asList(1, 2, 3), Arrays.asList(1, 4, 9), Arrays.asList(1, 8, 27));
        Stream<List<Integer>> listStream = Stream.of(Arrays.asList(1, 2, 3), Arrays.asList(2, 3, 6));
        listStream.flatMap(item -> item.parallelStream()).collect(toList()).forEach(System.out::println);
        Stream.of(Arrays.asList(1, 2, 3), Arrays.asList(2, 3, 6)).flatMap(lists -> lists.stream()).collect(Collectors.toList()).forEach(System.out::print);
    }

    /**
     * list concat()
     * 流连接操作
     */
    @Test
    public void listStreamConcat() {
        Stream.concat(Stream.of(1, 2), Stream.of(3)).forEach(System.out::print);
    }

    /**
     * list  peek()
     * peek：生成一个包含原Stream的所有元素的新Stream，新Stream每个元素被消费之前都会执行peek给定的消费函数
     */
    @Test
    public void listStreamPeek() {
        Stream.of(2, 4).peek(x -> System.out.print(x - 1)).forEach(System.out::print);
    }

    /**
     * Stream 主要有四类接口：
     *
     * 流到流之间的转换：比如 filter(过滤), map(映射转换), mapTo[Int|Long|Double] (到原子类型流的转换), flatMap(高维结构平铺)，flatMapTo[Int|Long|Double], sorted(排序)，distinct(不重复值)，peek(执行某种操作，流不变，可用于调试)，limit(限制到指定元素数量), skip(跳过若干元素) ；
     * 流到终值的转换： 比如 toArray（转为数组），reduce（推导结果），collect（聚合结果），min(最小值), max(最大值), count (元素个数)， anyMatch (任一匹配), allMatch(所有都匹配)， noneMatch(一个都不匹配)， findFirst（选择首元素），findAny(任选一元素) ；
     * 直接遍历： forEach (不保序遍历，比如并行流), forEachOrdered（保序遍历) ；
     * 构造流： empty (构造空流)，of (单个元素的流及多元素顺序流)，iterate (无限长度的有序顺序流)，generate (将数据提供器转换成无限非有序的顺序流)， concat (流的连接)， Builder (用于构造流的Builder对象)
     *
     * Predicate<T> 接受一个输入参数,返回一个布尔类型  (T)->boolean
     * Function<T,R> (T)->(R) T参R返
     * Supplier<T> ()->(T)  无参,T返
     * Counsumer<T> (T)->()  T参,无返
     */

}
