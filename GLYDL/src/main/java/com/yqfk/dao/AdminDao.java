package com.yqfk.dao;


import com.yqfk.pojo.Admin;
import com.yqfk.pojo.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface AdminDao extends JpaRepository<User, String>, JpaSpecificationExecutor<User>, CrudRepository<User,String> {

    @Query(value = "select * from admin where loginname = ?",nativeQuery = true)
    public Admin findByloginname(String loginname);

}
