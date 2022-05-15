package org.geekbang.time.commonmistakes.lock.deadlock;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Title
 * @Author wangwenliang
 * @Date 2022/5/13
 * @Description
 */
@Slf4j
@RestController
public class SelfDeadController {

  private final String ITEM_NAME_PRE = "item";

  private ConcurrentHashMap<String, MyItem> items = new ConcurrentHashMap();

  public SelfDeadController() {
    IntStream.rangeClosed(0, 10).forEach(i -> {
      items.put(ITEM_NAME_PRE + i, new MyItem(ITEM_NAME_PRE + i));
    });
  }

  //
  public List<MyItem> createCard() {
    return IntStream.rangeClosed(1, 3)
        .mapToObj(i -> {
          int nextInt = ThreadLocalRandom.current().nextInt(items.size());
//          System.out.println("nextInt = " + nextInt);
          return items.get(ITEM_NAME_PRE + nextInt);
        })
        .collect(Collectors.toList());
  }

  public boolean makeOrder(List<MyItem> myItems) {

    List<ReentrantLock> ownLocks = new ArrayList<>();
    for (MyItem myItem : myItems) {

      try {
        if (myItem.reentrantLock.tryLock(10, TimeUnit.SECONDS)) {
          ownLocks.add(myItem.reentrantLock);
        } else {
          ownLocks.forEach(ReentrantLock::unlock);
          return false;
        }
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }

    myItems.forEach(myItem -> myItem.remainCount--);

    ownLocks.forEach(ReentrantLock::unlock);

    return true;
  }

  @RequestMapping("/selfDealLock")
  public String wrong() {

    long begin = System.currentTimeMillis();
    long count = IntStream.rangeClosed(1, 100).parallel().mapToObj(i -> {
      List<MyItem> card = createCard().stream().sorted(Comparator.comparing(MyItem::getName))
          .collect(Collectors.toList());
      return makeOrder(card);
    }).filter(singleOrderResutl -> singleOrderResutl).count();
    log.info("success:{},tokeTimes:{},totalReaming:{},items:{}", count
        , System.currentTimeMillis() - begin,
        items.entrySet().stream().map(e -> e.getValue().remainCount).reduce(0, Integer::sum)
        , items
    );

    return "";
  }


  @RequiredArgsConstructor
  @Getter
  static class MyItem {

    final String name;

    private int remainCount = 1000;

    private ReentrantLock reentrantLock = new ReentrantLock();


  }

}

