package messager.db;

import messager.entities.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {

    private static SessionFactory sessionFactory;

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null || sessionFactory.isClosed()) {
            sessionFactory = new Configuration().configure()
                    .addAnnotatedClass(User.class)
                    .addAnnotatedClass(PersonalDialog.class)
                    .addAnnotatedClass(CommandDialog.class)
                    .addAnnotatedClass(TextMessage.class)
                    .addAnnotatedClass(MessageState.class)
                    .buildSessionFactory();
        }
        return sessionFactory;
    }

    public static Session getSession() {
        return getSessionFactory().openSession();
    }

}
