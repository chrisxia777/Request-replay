import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class Main {

  public static void main(String[] args) throws InterruptedException {
    System.out.println(API.call_method1("0"));
    System.out.println(API.call_method1("0"));
    System.out.println(API.call_method1("0"));
    System.out.println(API.call_method1("0"));
    System.out.println(API.call_method1("0")); // NOTOK
    System.out.println(API.call_method1("0")); // NOTOK
    Thread.sleep(4000);
    System.out.println(API.call_method1("0")); // OK
  }
}

class API {

  static RateLimiter rl1 = new RateLimiter(5, 2);
  static RateLimiter rl2 = new RateLimiter(10, 1);

  public static String call_method1(String cid) {
    if (rl1.check(cid, System.currentTimeMillis() / 1000)) {
      return "OK1";
    } else {
      return "NOTOK1";
    }
  }

  public static String call_method2(String cid) {
    if (rl2.check(cid, System.currentTimeMillis() / 1000)) {
      return "OK2";
    } else {
      return "NOTOK2";
    }
  }

}

class RateLimiter {

  // cid -> list of calls (in time)
  private Map<String, LinkedList<Long>> calls = new HashMap<>();
  private int K;
  private int T;

  public RateLimiter(int K, int T) {
    this.K = K;
    this.T = T;
  }

  public boolean check(String cid, long time) {
    if (!calls.containsKey(cid)) {
      calls.put(cid, new LinkedList<>());
      calls.get(cid).add(time);
      return true;
    }
    LinkedList<Long> list = calls.get(cid);
    while (!list.isEmpty() && list.getFirst() < time - T) {
      list.removeFirst();
    }
    if (list.size() + 1 <= K) {
      list.addLast(time);
      return true;
    }
    return false;
  }
}
