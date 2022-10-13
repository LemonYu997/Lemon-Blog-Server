package lemonyu997.top.lemonapi.utils;

import lemonyu997.top.lemonapi.pojo.User;

//解决线程安全问题
public class UserThreadLocal {

    private UserThreadLocal() { }

    //线程变量隔离
    //存放在ThreadLocal中的变量只有属于它的线程才能访问
    private static final ThreadLocal<User> LOCAL = new ThreadLocal<>();

    public static void put(User user) {
        LOCAL.set(user);
    }

    public static User get() {
        return LOCAL.get();
    }

    public static void remove() {
        LOCAL.remove();
    }
}
