package com.yqfk.service;

import com.aliyuncs.exceptions.ClientException;
import com.yqfk.dao.UserDao;
import com.yqfk.pojo.User;
import com.yqfk.util.SmsUtil;
import org.apache.commons.lang3.RandomStringUtils;
import org.omg.CORBA.TIMEOUT;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
@Transactional
public class UserService {

    @Autowired
    private UserDao userDao;

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

    public List<User> findAll(){
        return userDao.findAll();
    }

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

    /**
     * 发送新闻链接
     * @param mobile
     */
    public void sendNewsMsg(String mobile) {
        //编辑要发送内容
        String newsUrl = "www.xinwen.com";
        //存入缓存
        redisTemplate.opsForValue().set("newsUrl"+mobile,newsUrl,6,TimeUnit.HOURS);
        try {

            //给手机发一份
            smsUtil.sendSms(mobile,template_code,sign_name,"{\"code\":\""+ newsUrl+"\"}");
        }catch (ClientException e){
            e.printStackTrace();
        }



    }

    /**
     * save方法没有就存，有就更新
     * @param user
     */
    public void update(User user) {
        userDao.save(user);
    }
    public  void delete(String userid){
        userDao.deleteById(userid);
    }

    public void save(User user) {
        user.setRoleid("1");
        user.setPassword(encoder.encode(user.getPassword()));
        userDao.save(user);
    }

    public User login(String username, String password) {
    User user = userDao.findByUsername(username);
    if (user != null && encoder.matches(password,user.getPassword())){
        return user;
    }
    return null;
}
}
