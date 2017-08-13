package hr.eestec_zg.frmscore.domain;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.criteria.CriteriaBuilder;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;

public class AbstractRepository<PK extends Serializable, T> {

    private final Class<T> persistentClass;

    final static Integer MAX_RESULTS = 200;

    @SuppressWarnings("unchecked")
    public AbstractRepository() {
        this.persistentClass = (Class<T>) (
                (ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[1];
    }

    @Autowired
    private SessionFactory sessionFactory;

    protected Session getSession() {
        return sessionFactory.getCurrentSession();
    }

    @SuppressWarnings("unchecked")
    public T getByKey(PK key) {
        return (T) getSession().get(persistentClass, key);
    }

    public void persist(T entity) {
        getSession().persist(entity);
    }

    public void update(T entity) {
        getSession().merge(entity);
    }

    public void delete(T entity) {
        getSession().delete(entity);
    }

    protected CriteriaBuilder criteriaBuilder() {
        return getSession().getCriteriaBuilder();
    }
}
