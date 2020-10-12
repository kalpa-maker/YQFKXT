package com.yqfk.service;
import com.yqfk.pojo.User;
import org.springframework.stereotype.Service;

import java.util.List;


public interface UserService{





    /**
     * 发送新闻链接
     * @param mobile
     */
    public void sendNewsMsg(String mobile);


    public List<User> findAll();
    /**
     * save方法没有就存，有就更新
     * @param user
     */
    public void update(User user);

    public  void delete(String userid);

    public void save(User user);


    public User login(String username, String password);

    //    根据手机号码查找用户
    public User selectUserByPhone(String phone);

    public void sendMsg(String mobile);
}

