/*
 * Copyright (c) 2018.
 * Danny Janssen
 */

package dao;

import domain.User;

import java.util.List;

public interface IUserDao {
    User findById(long id);

    User findByUsername(String username);

    List<User> findAll();

    List<User> findFollowing(long id);

    List<User> findFollowers(long id);

    User create(User entity);

    User update(User entity);

    void remove(User entity);
}
