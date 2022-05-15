package org.geekbang.time.commonmistakes.java8;

/**
 * @Title
 * @Author wangwenliang
 * @Date 2022/5/1
 * @Description
 */
public interface DefaultInterfaceTest {

  void test();


  default void print1() {
    String message = "";
    if (message == "sesame open") {
      System.out.println("2");      String a = ":";
    }
//c.println("1");
  }

  default void print2() {
    System.out.println("2");
  }

}
