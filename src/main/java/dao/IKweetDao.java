/*
 * Copyright (c) 2018.
 * Danny Janssen
 */

package dao;

import domain.Kweet;
import domain.User;

import java.util.List;

public interface IKweetDao {
    Kweet findById(long id);

    List<Kweet> findByUser(long id);

    List<Kweet> findForUser(User entity);

    List<Kweet> findAll();

    Kweet create(Kweet entity);

    Kweet update(Kweet entity);

    void remove(Kweet entity);
}
