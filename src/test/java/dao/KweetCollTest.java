/*
 * Copyright (c) 2018.
 * Danny Janssen
 */

package dao;

import domain.Kweet;
import domain.User;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class KweetCollTest {
    private IKweetDao dao = new KweetDaoColl();

    @Test
    public void createKweet() {
        Kweet kweet = new Kweet("@fantastic yo", new User("Testuser1", "Password1", User.Role.USER));
        Kweet insertedKweet = dao.create(kweet);

        assertEquals(kweet.getText(), insertedKweet.getText());
    }

    @Test
    public void findKweetById() {
        dao.create(new Kweet("@fantastic yo", new User("Testuser1", "Password1", User.Role.USER)));

        Kweet foundKweet = dao.findById(1);
        assertEquals("@fantastic yo", foundKweet.getText());

        Kweet notFoundKweet = dao.findById(10);
        assertNull(notFoundKweet);
    }

    @Test
    public void findKweetsByUser() {
        User user1 = new User("Testuser1", "Password1", User.Role.USER);
        user1.setId(1);
        Kweet kweet = new Kweet("@fantastic yo", user1);

        dao.create(kweet);

        assertEquals(1, dao.findByUser(1).size());
    }

    @Test
    public void findKweetsForUser() {
        Kweet kweet = new Kweet("@fantastic yo", new User("Testuser1", "Password1", User.Role.USER));
        User user1 = new User("Testuser1", "Password1", User.Role.USER);
        List<User> following = new ArrayList<>();
        User followingUser = new User("Testuser2", "Password2", User.Role.USER);
        following.add(followingUser);
        user1.setId(1);
        user1.setFollowing(following);
        kweet.setUser(user1);

        dao.create(kweet);

        assertEquals(1, dao.findForUser(user1).size());
    }

    @Test
    public void findAllKweets() {
        assertEquals(0, dao.findAll().size());

        User user1 = new User("Testuser1", "Password1", User.Role.USER);

        dao.create(new Kweet("@fantastic yo #dope", user1));
        dao.create(new Kweet("echt he #heftig", user1));

        assertEquals(2, dao.findAll().size());
    }

    @Test
    public void updateKweet() {
        dao.create(new Kweet("@fantastic yo", new User("Testuser1", "Password1", User.Role.USER)));

        long ID = 1;
        Kweet updatedKweet = new Kweet("niet mee eens", new User("Testuser1", "Password1", User.Role.USER));
        updatedKweet.setId(ID);
        dao.update(updatedKweet);

        Kweet foundKweet = dao.findById(ID);
        assertEquals("niet mee eens", foundKweet.getText());
    }

    @Test
    public void deleteKweet() {
        Kweet kweet = new Kweet("@fantastic yo", new User("Testuser1", "Password1", User.Role.USER));
        dao.create(kweet);

        dao.remove(kweet);
        assertEquals(0, dao.findAll().size());
    }
}
