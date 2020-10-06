package com.yqfk.controller;

import com.yqfk.pojo.Result;
import com.yqfk.pojo.StatusCode;
import com.yqfk.pojo.User;
import com.yqfk.service.UserService;
import javafx.geometry.Pos;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
//@Controller
//@ResponseBody将返回的对象转成json
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private RedisTemplate redisTemplate;

    @RequestMapping(method = RequestMethod.GET)
    public Result findAll(){
        return new Result(true, StatusCode.OK, "查询成功", userService.findAll());
    }

    /**
     * 更新User
     */
    @RequestMapping(value = "/{userid}", method = RequestMethod.PUT)
    public Result update(@PathVariable("userid") String userid, @RequestBody User user){
        user.setUserid(userid);
        userService.update(user);
        return new Result(true, StatusCode.OK, "更新成功");
    }

    /**
     * 增加user
     */
    @RequestMapping(method = RequestMethod.POST)
    public Result addUser(@RequestBody User user){
        userService.save(user);
        return new Result(true, StatusCode.OK, "添加成功");
    }





    /**
     * 短信验证码登陆
     * @RequestMapping映射路径
     * @ResponseBody将前端传过来的json数据转换位对象
     */
    @RequestMapping(value = "/login/{code}", method = RequestMethod.POST)
    public Result Login(@RequestBody User user, @PathVariable String code){
        String checkcode = (String) redisTemplate.opsForValue().get("checkcode_" + user.getPhone());
        if (StringUtils.isEmpty(checkcode)){
            return new Result(false, StatusCode.ERROR, "请先获取验证码");
        } else if (!checkcode.equals(code)) {
            return new Result(false, StatusCode.ERROR, "验证码错误");
        }
        return new Result(true, StatusCode.OK,"登陆成功");
    }

    /**
     * 用户名密码登陆
     */
    @RequestMapping(value = "/loginbp", method = RequestMethod.POST)
    public Result loginByPassword(@RequestBody User user){
        user = userService.login(user.getUsername(),user.getPassword());
        if (user == null){
            return new Result(false, StatusCode.ERROR, "登陆失败");
        }
        return new Result(true,StatusCode.OK,"登陆成功");
    }

    /**
     * 发送验证码
     */
    @RequestMapping(value = "/sendSms/{mobile}",method = RequestMethod.POST)
    public Result sendMsg(@PathVariable String mobile){
        userService.sendMsg(mobile);
        return new Result(true, StatusCode.OK, "发送成功");
    }




}
