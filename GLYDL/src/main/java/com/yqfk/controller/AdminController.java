package com.yqfk.controller;

import com.yqfk.pojo.Result;
import com.yqfk.pojo.Admin;
import com.yqfk.pojo.StatusCode;
import com.yqfk.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.apache.commons.lang3.StringUtils;

//@RestController
@Controller
//@ResponseBody将返回的对象转成json
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private  AdminService adminService;

    @Autowired
    private RedisTemplate redisTemplate;



    /**
     * 短信验证码登陆
     * @RequestMapping映射路径
     * @ResponseBody将前端传过来的json数据转换位对象
     */
    @ResponseBody
    @RequestMapping(value = "/adminlogin/{code}", method = RequestMethod.POST)
    public Result Login(@RequestBody Admin admin, @PathVariable String code){
        String checkcode = (String) redisTemplate.opsForValue().get("checkcode_" + admin.getPhone());
        if (StringUtils.isEmpty(checkcode)){
            return new Result(false, StatusCode.ERROR, "请先获取验证码");
        } else if (!checkcode.equals(code)) {
            return new Result(false, StatusCode.ERROR, "您输入的验证码有误");
        }
        return new Result(true, StatusCode.OK,"管理员登陆成功");
    }

    /**
     * 用户名密码登陆
     */
    @ResponseBody
    @RequestMapping(value = "/loginbp", method = RequestMethod.POST)
    public Result loginByPassword(@RequestBody Admin admin){
        admin = adminService.login(admin.getLoginname(),admin.getPassword());
        if (admin == null){
            return new Result(false, StatusCode.ERROR, "登陆失败");
        }
        return new Result(true,StatusCode.OK,"登陆成功");
    }

    /**
     * 发送验证码
     */
    @ResponseBody
    @RequestMapping(value = "/sendSms/{mobile}",method = RequestMethod.POST)
    public Result sendMsg(@PathVariable String mobile){
        adminService.sendMsg(mobile);
        return new Result(true, StatusCode.OK, "发送成功");
    }
}