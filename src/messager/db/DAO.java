package messager.db;

import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;
import java.util.Optional;

abstract class DAO<T>
{
    Session openSession() {
        return HibernateUtil.getSession();
    }
    
    public Optional<T> findById(Long id) {
        Session session = openSession();
        T obj = session.get(getLoadingClass(), id);
        session.close();
        return Optional.of(obj);
    }

    protected abstract Class<T> getLoadingClass();
    
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
    
    public void saveOrUpdate(T obj) {
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
        List<T> list = session.createQuery("From " + getLoadingClass().getSimpleName()).list();
        session.close();
        return list;
    }

    public void deleteAll() {
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
