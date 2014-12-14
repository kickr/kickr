package support;

import org.hibernate.FlushMode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.context.internal.ManagedSessionContext;

/**
 *
 * @author nikku
 */
public class WithTransaction {
  
  private final SessionFactory sessionFactory;

  public WithTransaction(SessionFactory sessionFactory) {
    this.sessionFactory = sessionFactory;
  }
  
  public void run(Runnable runnable) {
    Session session = sessionFactory.openSession();
    
    try {
      ManagedSessionContext.bind(session);
      session.beginTransaction();
      
      runnable.run();

      session.getTransaction().commit();
    } catch (Throwable th) {
      
      if (session.getTransaction() != null) {
        session.getTransaction().rollback();
      }
      
      throw th;
    } finally {
      session.close();

      ManagedSessionContext.unbind(this.sessionFactory);
    }
  }
}
