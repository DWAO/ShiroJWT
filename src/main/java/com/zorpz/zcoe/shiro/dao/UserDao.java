package com.zorpz.zcoe.shiro.dao;


import com.zorpz.zcoe.shiro.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDao extends JpaRepository<User, String> {

    User findByUsername(String username);
}
