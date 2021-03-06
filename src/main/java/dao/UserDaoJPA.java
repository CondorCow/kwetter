/*
 * Copyright (c) 2018.
 * Danny Janssen
 */

package dao;

import domain.User;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@JPA
@Stateless
public class UserDaoJPA extends FDao<User> implements IUserDao {
    @PersistenceContext(unitName = "KwetterPU")
    private EntityManager em;

    public UserDaoJPA() {
        super(User.class);
    }

    public UserDaoJPA(EntityManager em) {
        super(User.class, em);
        this.em = em;
    }

    public User findByUsername(String username) {
        try {
            return (User) em.createQuery("SELECT u FROM User u WHERE u.username = :username")
                    .setParameter("username", username)
                    .getSingleResult();
        } catch (Exception e) { return null; }
    }

    @SuppressWarnings("unchecked")
    public List<User> findFollowing(long id) {
        return em.createQuery("SELECT u.following FROM User u WHERE u.id = :id")
                .setParameter("id", id)
                .getResultList();
    }

    @SuppressWarnings("unchecked")
    public List<User> findFollowers(long id) {
        return em.createQuery("SELECT u.followers FROM User u WHERE u.id = :id")
                .setParameter("id", id)
                .getResultList();
    }
}
