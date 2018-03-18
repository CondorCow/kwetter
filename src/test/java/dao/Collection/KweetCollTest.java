/*
 * Copyright (c) 2018.
 * Danny Janssen
 */

package dao.Collection;

import dao.IKweetDao;
import dao.KweetDaoColl;
import domain.Kweet;
import domain.User;
import org.junit.Before;
import org.junit.Test;
import support.Role;

import static org.junit.Assert.*;

public class KweetCollTest {
    private IKweetDao kweetDao = new KweetDaoColl();

    private User myself = null;

    @Before
    public void init(){
        myself = new User("dannyajc", "jea6", Role.USER);
    }

    @Test
    public void newKweetTest() {
        Kweet kweet = new Kweet("hoi", myself);
        Kweet newKweet = kweetDao.create(kweet);

        assertEquals(kweet.getText(), newKweet.getText());
    }

    @Test
    public void updateKweetTest() {
//        User myself = new User("dannyajc", "jea6", Role.USER);
        kweetDao.create(new Kweet("groetjes aan henk", myself));
        Kweet updatedKweet = new Kweet("doe zelf", myself);
        updatedKweet.setId(1);
        kweetDao.update(updatedKweet);

        Kweet foundKweet = kweetDao.findById(1);
        assertEquals("doe zelf", foundKweet.getText());
    }

    @Test
    public void deleteKweetTest() {
        Kweet kweet = new Kweet("ik ga doei", myself);
        kweetDao.create(kweet);

        kweetDao.remove(kweet);
        assertEquals(0, kweetDao.findAll().size());
    }

    @Test
    public void findKweetByIdTest() {
        kweetDao.create(new Kweet("alles goed?", myself));

        Kweet foundKweet = kweetDao.findById(1);
        assertEquals("alles goed?", foundKweet.getText());

        Kweet notFoundKweet = kweetDao.findById(10);
        assertNull(notFoundKweet);
    }

    @Test
    public void findKweetsByUserTest() {
//        User myself = new User("dannyajc", "jea6", Role.USER);
        myself.setId(1);
        kweetDao.create(new Kweet("haha doei", myself));
        kweetDao.create(new Kweet("haha oke", myself));

        assertEquals(2, kweetDao.findByUser(1).size());
    }

    @Test
    public void findAllKweetsTest() {
        assertEquals(0, kweetDao.findAll().size());

//        User myself = new User("dannyajc", "jea6", Role.USER);

        Kweet kweet1 = new Kweet("wie heb ik aan de lijn", myself);
        Kweet kweet2 = new Kweet("hallo hallo", myself);
        Kweet kweet3 =new Kweet("tosti's", myself);


        kweetDao.create(kweet1);
        kweetDao.create(kweet2);
        kweetDao.create(kweet3);

        assertNotEquals(2, kweetDao.findAll().size());
        assertEquals(3, kweetDao.findAll().size());
    }




}
