package dao;

import domain.Kweet;
import domain.User;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@JPA
@Stateless

public class KweetDaoJPA extends FDao<Kweet> implements IKweetDao {
    @PersistenceContext(unitName = "KwetterPU")
    private EntityManager em;

    public KweetDaoJPA() {
        super(Kweet.class);
    }

    public KweetDaoJPA(EntityManager em) {
        super(Kweet.class, em);
        this.em = em;
    }

    @SuppressWarnings("unchecked")
    public List<Kweet> findByText(String text) {
        return em.createQuery("SELECT k FROM Kweet k WHERE LOWER(k.text) LIKE LOWER(:text)")
                .setParameter("text", "%" + text + "%")
                .getResultList();
    }

    @SuppressWarnings("unchecked")
    public List<Kweet> findByUser(long id) {
        return em.createQuery("SELECT k from Kweet k WHERE k.user.id = :id")
                .setParameter("id", id)
                .getResultList();
    }

    @SuppressWarnings("unchecked")
    public List<Kweet> findForUser(User entity) {
        return em.createQuery("SELECT k from Kweet k WHERE k.user.id = :id OR :id in elements(k.user.followers) ORDER BY k.date")
                .setParameter("id", entity.getId())
                .getResultList();
    }
}
