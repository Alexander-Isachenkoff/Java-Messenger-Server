package messager.db;

import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;
import java.util.Optional;

class DAO<T> {

    protected final Class<T> tClass;

    DAO(Class<T> tClass) {
        this.tClass = tClass;
    }

    static <T> DAO<T> of(Class<T> tClass) {
        return new DAO<>(tClass);
    }

    Session openSession() {
        return HibernateUtil.getSession();
    }

    public Optional<T> findById(long id) {
        Session session = openSession();
        T obj = session.get(tClass, id);
        session.close();
        return Optional.of(obj);
    }

    public void save(T obj) {
        Session session = openSession();
        Transaction tx1 = session.beginTransaction();
        session.save(obj);
        tx1.commit();
        session.close();
    }

    public void update(T obj) {
        Session session = openSession();
        Transaction tx1 = session.beginTransaction();
        session.update(obj);
        tx1.commit();
        session.close();
    }

    void saveOrUpdate(T obj) {
        Session session = openSession();
        Transaction tx1 = session.beginTransaction();
        session.saveOrUpdate(obj);
        tx1.commit();
        session.close();
    }

    public void delete(T obj) {
        Session session = openSession();
        Transaction tx1 = session.beginTransaction();
        session.delete(obj);
        tx1.commit();
        session.close();
    }

    public List<T> selectAll() {
        Session session = openSession();
        List<T> list = session.createQuery("From " + tClass.getSimpleName()).list();
        session.close();
        return list;
    }

    void deleteAll() {
        List<T> results = selectAll();
        Session session = openSession();
        Transaction tx1 = session.beginTransaction();
        for (T obj : results) {
            session.delete(obj);
        }
        tx1.commit();
        session.close();
    }
}
