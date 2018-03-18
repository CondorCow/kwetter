
/*
 * Copyright (c) 2018.
 * Danny Janssen
 */

package services;

import com.mysql.cj.core.util.StringUtils;
import dao.IKweetDao;
import dao.IUserDao;
import dao.JPA;
import domain.Kweet;
import domain.User;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.List;

@ApplicationScoped
//@Stateless
public class KweetService {
    @Inject @JPA
    private IKweetDao kweetDao;

    @Inject @JPA
    private IUserDao userDao;

    public KweetService() {
        super();
    }

    public Kweet getKweet(long id) {
        return kweetDao.findById(id);
    }

    /**
     * Check if kweet is between 0 and 140 characters
     * Adds kweet to designated list of user's kweets
     *
     * @param kweet, to create with text and user id
     * @return Kweet, that gets posted)
     */
    public Kweet newKweet(Kweet kweet) {
        String text = kweet.getText();
        if (!StringUtils.isNullOrEmpty(text) && text.length() <= 140) {
            User user = userDao.findById(kweet.getUser().getId());

            if (user != null) {
                Kweet createdKweet = kweetDao.create(kweet);
                user.addKweet(createdKweet);
                userDao.update(user);
                return createdKweet;
            }
        }
        return null;
    }

    /**
     * Check if new text is empty, initiate a new Kweet instance
     *
     * @param kweet, with kweet id to update and new text
     * @return Kweet, newly created kweet
     */
    public Kweet editKweet(Kweet kweet) {
        String text = kweet.getText();
        if (StringUtils.isNullOrEmpty(text) || text.length() > 140) return null;

        Kweet originalKweet = kweetDao.findById(kweet.getId());
        if (originalKweet == null) return null;

        originalKweet.setText(kweet.getText());

        return kweetDao.update(originalKweet);
    }

    /**
     * @param id, id of user to get kweets from
     * @return List<Kweet>, list of kweets found
     */
    public List<Kweet> getUserKweets(long id) {
        User user = userDao.findById(id);
        if (user == null) return null;

        return kweetDao.findByUser(id);
    }

    /**
     * This method gets the kweets of the user and the
     * kweets of the users following
     *
     * @param id, of the user to get kweets and following from
     * @return List<Kweet>, list of kweets found
     */
    public List<Kweet> getFullTimeline(long id) {
        User user = userDao.findById(id);
        if (user == null) return null;

        return kweetDao.findForUser(user);
    }

    /**
     * @param id, of kweet to remove
     */
    public void deleteKweet(long id) {
        Kweet kweet = kweetDao.findById(id);
        kweetDao.remove(kweet);
    }
}
