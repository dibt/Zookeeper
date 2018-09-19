package com.di.liststream;

import com.di.pojo.User;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;

public class listStreamTest {

    public List<User> getList(){
        List<User> list = new ArrayList<>();
        for(int i=0; i<10; i++){
            list.add(new User(i,"test"+i));
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
        List<User> list = getList();
        User user = new User(4,"aesvd");
        User user1 = user;
        list.addAll(Arrays.asList(user,user1));

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
        List<User> list = getList();
        User user = new User(0,"test00");
        list.addAll(Arrays.asList(user));
        list.parallelStream().filter(distinctByKey(User::getId)).forEach(System.out::println);
    }

    /**
     * list filter()
     *
     * 根据name进行过滤
     */
    @Test
    public void listStreamFilterByName(){
        getList().parallelStream().filter(user -> (!user.getName().contains("8"))).forEach(System.out::println);
    }


}
