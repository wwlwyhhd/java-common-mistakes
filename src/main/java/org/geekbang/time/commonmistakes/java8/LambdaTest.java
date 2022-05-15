package org.geekbang.time.commonmistakes.java8;

import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import org.junit.Test;

import java.util.concurrent.ThreadLocalRandom;
import java.util.function.*;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class LambdaTest {

  @Test
  public void lambdavsanonymousclass() {
    new Thread(new Runnable() {
      @Override
      public void run() {
        System.out.println("hello1");
      }
    }).start();

    new Thread(() -> System.out.println("hello2")).start();
  }

  @Test
  public void functionalInterfaces() {
    //可以看一下java.util.function包
    Supplier<String> supplier = String::new;
    Supplier<String> stringSupplier = () -> "OK";

    //Predicate的例子
    Predicate<Integer> positiveNumber = i -> i > 0;
    Predicate<Integer> evenNumber = i -> i % 2 == 0;
    assertTrue(positiveNumber.and(evenNumber).test(2));

    //Consumer的例子，输出两行abcdefg
    Consumer<String> println = x -> System.out.println(x);
    println.andThen(println).accept("abcdefg");

    //Function的例子
    Function<String, String> upperCase = s1 -> s1.toUpperCase();
    Function<String, String> duplicate = s -> s.concat(s);
    assertThat(upperCase.andThen(duplicate).apply("test"), is("TESTTEST"));

    //Supplier的例子
    Supplier<Integer> random = () -> ThreadLocalRandom.current().nextInt();
    System.out.println(random.get());

    //BinaryOperator
    BinaryOperator<Integer> add = Integer::sum;
    BinaryOperator<Integer> subtraction = (a, b) -> a - b;
    assertThat(subtraction.apply(add.apply(1, 2), 3), is(0));
  }

  @Test
  public void zijiwan() {

    Supplier s = () -> "自己玩";
    System.out.println("s = " + s.get());
//
//    Predicate<Integer> predicate = i -> i > 0;
//    Predicate<Integer> p2 = j -> j % 2 == 0;
//    boolean test = predicate.and(p2).test(4);
//    assertTrue(test);
//    DefaultInterfaceTest defaultInterfaceTest = () -> System.out.println("i = ");
//    defaultInterfaceTest.print1();
//    defaultInterfaceTest.print2();
//    Consumer<String> c1 = System.out::println;
//    c1.andThen(c1).accept("ttkfz");
//    Function<String, String> up = String::toUpperCase;
//    Function<String, String> concat = str -> str.concat(str);
//
//    assertThat(up.andThen(concat).apply("wyx"), is("WYXWYX"));
//    Supplier<Integer> random = () -> ThreadLocalRandom.current().nextInt();
//    System.out.println("random.get() = " + random.get());
//    BinaryOperator<Integer> sumnum = Integer::sum;
//
//    BinaryOperator<Integer> sub = (a, b) -> a - b;
////    sum.apply(1, 2);
//
//
//    assertThat(sub.apply(sumnum.apply(1,2),3),is(0));
    Double aDouble = new Double(3, 4);
    double distance = aDouble.distance(0, 0);
    System.out.println("distance = " + distance);

  }

}
