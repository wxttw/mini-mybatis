package com.hjj.test.mapper;

import com.hjj.test.pojo.User;

import java.util.List;

/**
 * @author jay
 * @date 2024/4/28 18:21
 * @description: TODO
 */
public interface UserMapper {

    List<User> selectList();

    User selectOne(Long id);

    int insert(User user);

    int update(User user);

    int delete(Long id);
}
