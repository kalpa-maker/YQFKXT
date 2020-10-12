package com.yqfk.dao;
import com.yqfk.pojo.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;



public interface UserDao  extends JpaRepository<User, String>, JpaSpecificationExecutor<User> , CrudRepository<User,String> {



    @Query(value = "select * from user where username = ?",nativeQuery = true)
    public com.yqfk.pojo.User findByUsername(String username);

    //根据号码查
    @Query(value = "select phone,password from user where phone=#{phone}",nativeQuery = true)
    public User selectUserByPhone(String phone);


}
