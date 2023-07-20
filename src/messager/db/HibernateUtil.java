package messager.db;

import messager.entities.CommandDialog;
import messager.entities.PersonalDialog;
import messager.entities.TextMessage;
import messager.entities.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {

    private static final SessionFactory sessionFactory;

    static {
        sessionFactory = new Configuration().configure()
                .addAnnotatedClass(User.class)
                .addAnnotatedClass(PersonalDialog.class)
                .addAnnotatedClass(CommandDialog.class)
                .addAnnotatedClass(TextMessage.class)
                .buildSessionFactory();
    }

    public static Session getSession() {
        return sessionFactory.openSession();
    }
}
