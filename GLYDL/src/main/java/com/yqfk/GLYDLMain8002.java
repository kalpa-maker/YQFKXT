package com.yqfk;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * @author cyz
 * @date 2020-10-05 10:55
 */
@SpringBootApplication
public class GLYDLMain8002 {
    public static void main(String[] args) {

        SpringApplication.run(GLYDLMain8002.class,args);
    }

    //把BCryptPasswordEncoder扔到spring容器里面
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
