package com.yqfk.service.impl;

import com.aliyuncs.exceptions.ClientException;
import com.yqfk.dao.UserDao;
import com.yqfk.pojo.User;
import com.yqfk.service.UserService;
import com.yqfk.util.SmsUtil;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@Transactional
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDao;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private SmsUtil smsUtil;
    @Value("${aliyun.sms.template_code}")
    private String template_code;
    @Value("${aliyun.sms.sign_name}")
    private String sign_name;
    public List<User> findAll(){
        return userDao.findAll();
    }

    @Override
    public User queryById(int id) {
        return userDao.getOne(id);
    }

    @Override
    public void update(User user) {
          userDao.save(user);
    }

    @Override
    public void deleteById(int id) {
        userDao.deleteById(id);
    }

    @Override
    public void save(User user) {
        userDao.save(user);
    }

    @Override
    public User queryUserByPhone(String phone) {
        return userDao.queryUserByPhone(phone);
    }

    @Override
    public boolean checkUser(String name, String password) {
        User user = userDao.queryUserByUsernameAndPassword(name, password);
        if (user!=null){
            redisTemplate.opsForHash().put("user",user.getUserid(),user);
            return true;
        }
        return false;
    }

    public void sendMsg(String mobile) {
        //生成随机六位数（lang3）
        String checkcode = RandomStringUtils.randomNumeric(6);
        //往缓存里面存一份
        redisTemplate.opsForValue().set("checkcode_"+mobile, checkcode, 60, TimeUnit.SECONDS);
        try {
            smsUtil.sendSms(mobile, template_code, sign_name, "{\"code\":\""+ checkcode +"\"}");
        }catch (ClientException e) {
            e.printStackTrace();
        }
    }
}
