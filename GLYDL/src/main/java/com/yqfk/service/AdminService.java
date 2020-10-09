package com.yqfk.service;

import com.aliyuncs.exceptions.ClientException;
import com.yqfk.dao.AdminDao;
import com.yqfk.pojo.Admin;
import com.yqfk.util.SmsUtil;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.concurrent.TimeUnit;


@Service
@Transactional
public class AdminService {
    @Autowired
    private AdminDao adminDao;
    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private BCryptPasswordEncoder encoder;

    @Autowired
    private SmsUtil smsUtil;


    @Value("${aliyun.sms.template_code}")
    private String template_code;

    @Value("${aliyun.sms.sign_name}")
    private String sign_name;

    /**
     * 发送手机验证码
     * @param mobile
     */
    public void sendMsg(String mobile) {
        //生成随机六位数（lang3）
        String checkcode = RandomStringUtils.randomNumeric(6);
        //往缓存里面存一份
        redisTemplate.opsForValue().set("checkcode_"+mobile, checkcode, 6, TimeUnit.HOURS);
        try {
            //给手机发一份
            smsUtil.sendSms(mobile, template_code, sign_name, "{\"code\":\""+ checkcode +"\"}");
        } catch (ClientException e) {
            e.printStackTrace();
        }
    }

    public Admin login(String loginname, String password) {
        Admin admin = adminDao.findByloginname(loginname);
        if (admin != null && encoder.matches(password,admin.getPassword())){
            return admin;
        }
        return null;
    }


}
