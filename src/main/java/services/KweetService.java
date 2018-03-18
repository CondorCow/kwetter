
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

    /**
     * Get specific kweet according to it's ID
     * @param id : the kweet's id to be found
     * @return Kweet : the found kweet
     */
    public Kweet getKweet(long id) {
        return kweetDao.findById(id);
    }

    /**
     * @param userId : identifier of the user
     * @return List : user's kweets
     */
    public List<Kweet> getUserKweets(long userId) {
        User user = userDao.findById(userId);
        if (user == null) return null;

        return kweetDao.findByUser(userId);
    }

    /**
     * @param kweet : the kweet to be updated
     * @return Kweet : the new kweet with updated text
     */
    public Kweet editKweet(Kweet kweet) {
        String text = kweet.getText();
        if (StringUtils.isNullOrEmpty(text) || text.length() > 140) {
            return null;
        }

        Kweet oKweet = kweetDao.findById(kweet.getId());
        if (oKweet == null) return null;

        oKweet.setText(kweet.getText());

        return kweetDao.update(oKweet);
    }

    /**
     * Posts a kweet for the logged in user, and adds it to his/hers list
     * @param kweet : the kweet that will be created by using the logged in user and text
     * @return Kweet : returns the uploaded kweet)
     */
    public Kweet newKweet(Kweet kweet) {
        String message = kweet.getText();
        if(!message.isEmpty() && message != null && message.length() <= 140) {
            long userId = kweet.getUser().getId();
            User user = userDao.findById(userId);

            if (user != null) {
                Kweet newKweet = kweetDao.create(kweet);
                user.addKweet(newKweet);
                //Update the user so it has the new kweet in the list
                userDao.update(user);
                return newKweet;
            }
        }
        return null;
    }

    /**
     * @param id : kweet identifier
     */
    public void deleteKweet(long id) {
        Kweet kweet = kweetDao.findById(id);
        kweetDao.remove(kweet);
    }

    /**
     * Get all the kweets of the followings and it's own kweets
     * @param userId : identifier of the logged in user
     * @return List : all combined kweets of following kweets and own kweets
     */
    public List<Kweet> getFullTimeline(long userId) {
        User user = userDao.findById(userId);
        return user == null ? null : kweetDao.findForUser(user);
    }


}
