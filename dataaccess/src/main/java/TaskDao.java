import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;

import java.util.List;
import java.util.Properties;

/**
 * Created by Micahel on 03.07.2017.
 */
public class TaskDao {
    private static SessionFactory sessionFactory = null;
    private static ServiceRegistry serviceRegistry = null;

    public TaskDao(){
        configureSessionFactory();
    }


    private static SessionFactory configureSessionFactory() throws HibernateException {
        Configuration configuration = new Configuration();
        configuration.configure();

        Properties properties = configuration.getProperties();

        serviceRegistry = new ServiceRegistryBuilder().applySettings(properties).buildServiceRegistry();
        sessionFactory = configuration.buildSessionFactory(serviceRegistry);

        return sessionFactory;
    }

    public void addTask(Task task){

        Session session = null;
        Transaction tx=null;

        try {
            session = sessionFactory.openSession();
            tx = session.beginTransaction();

            // Saving to the database
            session.save(task);

            // Committing the change in the database.
            session.flush();
            tx.commit();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally{
            if(session != null) {
                session.close();
            }
        }
    }

    public List<Task> getTasks(){
        return  getTasks(null);
    }

    public List<Task> getTasks(String email)
    {
        Session session = null;
        Transaction tx=null;
        List<Task> taskList = null;

        try
        {
            session = sessionFactory.openSession();
            tx = session.beginTransaction();

            // Fetching data
            String hql = " from Task t";
            if (email!=null) hql = hql + " where t.email = '" + email + "'";
            taskList = session.createQuery(hql).list();

        } catch (Exception ex) {
            ex.printStackTrace();

            // Rolling back the changes to make the data consistent in case of any failure
            // in between multiple database write operations.
            tx.rollback();
        } finally{
            if(session != null) {
                session.close();
            }
        }
        return taskList;
    }

/*
    public static void main(String[] args) {
        // Configure the session factory
        configureSessionFactory();

        Session session = null;
        Transaction tx=null;

        try {
            session = sessionFactory.openSession();
            tx = session.beginTransaction();

            // Creating Contact entity that will be save to the sqlite database
            Contact myContact = new Contact(3, "My Name", "my_email@email.com");
            Contact yourContact = new Contact(24, "Your Name", "your_email@email.com");

            // Saving to the database
            session.save(myContact);
            session.save(yourContact);

            // Committing the change in the database.
            session.flush();
            tx.commit();

            // Fetching saved data
            List<Contact> contactList = session.createQuery("from Contact").list();

            for (Contact contact : contactList) {
                System.out.println("Id: " + contact.getId() + " | Name:"  + contact.getName() + " | Email:" + contact.getEmail());
            }

        } catch (Exception ex) {
            ex.printStackTrace();

            // Rolling back the changes to make the data consistent in case of any failure
            // in between multiple database write operations.
            tx.rollback();
        } finally{
            if(session != null) {
                session.close();
            }
        }
    }
    */
}
