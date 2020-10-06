package com.yqfk.dao;

import com.yqfk.pojo.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface UserDao extends JpaRepository<User, String>, JpaSpecificationExecutor<User> {

    @Query(value = "select * from user where username = ?",nativeQuery = true)
    public User findByUsername(String username);

}
