package com.yqfk.service;

import com.yqfk.pojo.Admin;
public interface AdminService {





    /**
     * 发送手机验证码
     * @param mobile
     */
    public void sendMsg(String mobile);


    public Admin login(String loginname, String password);
}
