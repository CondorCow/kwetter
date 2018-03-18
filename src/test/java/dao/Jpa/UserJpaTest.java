/*
 * Copyright (c) 2018.
 * Danny Janssen
 */

package dao.Jpa;

import dao.IUserDao;
import dao.UserDaoJPA;
import domain.User;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import support.Role;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

import static org.junit.Assert.*;

public class UserJpaTest {
    private static EntityManagerFactory emFac;
    private static EntityManager em;
    private static IUserDao userDao;
    private EntityTransaction transaction;

    @BeforeClass
    public static void initEm() {
        emFac = Persistence.createEntityManagerFactory("TestsPU");
        em = emFac.createEntityManager();
        userDao = new UserDaoJPA(em);
    }

    @Before
    public void init() {
        transaction = em.getTransaction();
        transaction.begin();
    }

    @After
    public void stop(){
        transaction.commit();
    }

    @Test
    public void newUserTest() {
        User user = new User("myself", "jea6", Role.USER);
        User created = userDao.create(user);
        assertNotNull(created);
    }

    @Test
    public void getUserByIdTest() {
        userDao.create(new User("myself", "jea6", Role.USER));
        User found = userDao.findById(1);
        User notFound = userDao.findById(100);
        assertNull(notFound);
        assertNotNull(found);
    }

    @Test
    public void getByUsernameTest() {
        userDao.create(new User("kaasmeneer", "jea6", Role.USER));

        User found = userDao.findByUsername("kaasmeneer");
        assertNotNull(found);
    }

    @Test
    public void editUserTest() {
        userDao.create(new User("myself", "jea6", Role.USER));
        User user = userDao.findById(1);
        user.setBio("alles goed met jullie?");
        User editedUser = userDao.update(user);
        assertEquals("alles goed met jullie?", editedUser.getBio());
    }

    @Test
    public void getAllUsersTest() {
        userDao.create(new User("myself1", "jea6", Role.USER));
        userDao.create(new User("myself2", "jea6", Role.USER));

        List<User> foundUsers = userDao.findAll();
        //3 because of earlier tests
        assertEquals(3, foundUsers.size());
    }

    @Test
    public void removeUserTest() {
//        userDao.create(new User("myself", "jea6", Role.USER));

        int size = userDao.findAll().size();
        User user = userDao.findByUsername("kaasmeneer");
        userDao.remove(user);

        int newLength = userDao.findAll().size();
        assertNotEquals(size, newLength);
    }
}