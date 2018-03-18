/*
 * Copyright (c) 2018.
 * Danny Janssen
 */

package dao.Jpa;

import dao.IKweetDao;
import dao.IUserDao;
import dao.KweetDaoJPA;
import dao.UserDaoJPA;
import domain.Kweet;
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

public class KweetJpaTest {
    private static EntityManagerFactory emFac;
    private static EntityManager em;
    private static IKweetDao kweetDao;
    private static IUserDao userDao;
    private EntityTransaction transaction;

    static User myself;


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
    public void allKweetsTest() {
        newKweet();
        newKweet();

        List<Kweet> foundKweets = kweetDao.findAll();
        assertEquals(2, foundKweets.size());
    }

    @Test
    public void userKweetsTest() {
        newKweet();
        newKweet();

        List<Kweet> kweetsFound = kweetDao.findByUser(1);
        assertEquals(5, kweetsFound.size());
    }

    @Test
    public void newKweetTest() {
        assertNotNull(newKweet());
    }

    @Test
    public void wrongIdTest() {
        newKweet();

        Kweet found = kweetDao.findById(2);
        assertNotNull(found);

        Kweet notFound = kweetDao.findById(100);
        assertNull(notFound);
    }

    @Test
    public void editKweetTest() {
        newKweet();

        Kweet kweet = kweetDao.findById(2);
        String text = "schrik";
        kweet.setText(text);
        Kweet edited = kweetDao.update(kweet);
        assertNotNull(edited);
        assertEquals(text, edited.getText());
    }

    private Kweet newKweet() {
        userDao.create(myself);

        Kweet kweet = new Kweet("kaas is alleen gesmolten lekker", myself);
        return kweetDao.create(kweet);
    }


    @BeforeClass
    public static void initEm() {
        emFac = Persistence.createEntityManagerFactory("TestsPU");
        em = emFac.createEntityManager();
        kweetDao = new KweetDaoJPA(em);
        userDao = new UserDaoJPA(em);
        myself = new User("dannyajc", "jea6", Role.USER);
    }
}
