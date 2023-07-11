package messager.db;

import messager.entities.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {
    
    private static final SessionFactory sessionFactory;
    
    static {
        sessionFactory = new Configuration().configure()
                .addAnnotatedClass(User.class)
                .buildSessionFactory();
    }
    
    public static Session getSession() {
        return sessionFactory.openSession();
    }
}
