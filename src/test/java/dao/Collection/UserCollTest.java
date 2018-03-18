/*
 * Copyright (c) 2018.
 * Danny Janssen
 */

package dao.Collection;

import dao.IUserDao;
import dao.UserDaoColl;
import domain.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import support.Role;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class UserCollTest {
    private IUserDao userDao = new UserDaoColl();

    private User myself;

    @Before
    public void init(){
        myself = new User("dannyajc", "Password1", Role.USER);
    }

    @After
    public void reset(){
        myself = null;
    }

    @Test
    public void newUser() {
        User user = new User("henk", "jea6", Role.USER);
        User newUser = userDao.create(user);

        
        assertEquals(user.getUsername(), newUser.getUsername());
        assertEquals(user.getPassword(), newUser.getPassword());
        assertEquals(user.getRole(), newUser.getRole());
    }

    @Test
    public void findAllUsers() {
        assertEquals(0, userDao.findAll().size());

        userDao.create(myself);
        userDao.create(new User("Piet", "jea6", Role.USER));

        assertEquals(2, userDao.findAll().size());
    }

    @Test
    public void updateUser() {
        userDao.create(myself);
        userDao.create(new User("Piet", "jea6", Role.USER));

        User updatedUser = new User("Kees", "jea6", Role.USER);
        updatedUser.setId(2);
        userDao.update(updatedUser);

        User foundUser = userDao.findById(2);
        assertEquals("Kees", foundUser.getUsername());
        assertEquals("jea6", foundUser.getPassword());
    }

    @Test
    public void deleteUser() {
        userDao.create(myself);

        userDao.remove(myself);
        assertEquals(0, userDao.findAll().size());
    }

    @Test
    public void findUserById() {
        userDao.create(new User("henk", "jea6", Role.USER));

        User foundUser = userDao.findById(1);
        assertEquals("henk", foundUser.getUsername());
        assertEquals("jea6", foundUser.getPassword());

        User notFoundUser = userDao.findById(10);
        assertNull(notFoundUser);
    }

    @Test
    public void findByUsername() {
        User _new = new User("henk", "jea6", Role.USER);
        userDao.create(_new);

        User foundUser = userDao.findByUsername("henk");
        assertEquals("henk", foundUser.getUsername());
        assertEquals("jea6", foundUser.getPassword());

        User notFoundUser = userDao.findByUsername("Otheruser");
        assertNull(notFoundUser);
    }

    @Test
    public void findUserFollowing() {
        List<User> following = new ArrayList<>();
        following.add(new User("Piet", "jea6", Role.USER));
        myself.setFollowing(following);

        userDao.create(myself);

        List<User> findFollowing = userDao.findFollowing(1);
        assertEquals(1, findFollowing.size());

        User firstFollowing = findFollowing.get(0);
        assertEquals("Piet", firstFollowing.getUsername());
        assertEquals("jea6", firstFollowing.getPassword());
    }

    @Test
    public void findUserFollowers() {

        List<User> followers = new ArrayList<>();
        followers.add(new User("Piet", "jea6", Role.USER));
        myself.setFollowers(followers);

        userDao.create(myself);

        List<User> findFollowers = userDao.findFollowers(1);
        assertEquals(1, findFollowers.size());

        User firstFollower = findFollowers.get(0);
        assertEquals("Piet", firstFollower.getUsername());
        assertEquals("jea6", firstFollower.getPassword());
    }
}
