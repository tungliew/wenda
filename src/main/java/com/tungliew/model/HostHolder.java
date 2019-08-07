package com.tungliew.model;

import org.springframework.stereotype.Component;

@Component //依赖注入思想
public class HostHolder {
    //ThreadLocal工具为写出多线程程序
    //为每一条线程都存储了一个对象
    private static ThreadLocal<User> users = new ThreadLocal<User>();

    public User getUser(){
        return users.get(); //根据当前线程找到关联变量
    }

    public void setUser(User user){
        users.set(user);
    }

    public void clear(){
        users.remove();
    }
}
