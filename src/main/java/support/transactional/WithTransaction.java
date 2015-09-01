package support.transactional;

import java.util.concurrent.Callable;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
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
    get(() -> {
      runnable.run();
      return null;
    });
  }

  public <T> T get(Callable<T> callable) {
    Session session = sessionFactory.openSession();
    
    try {
      ManagedSessionContext.bind(session);
      session.beginTransaction();
      
      T result = callable.call();

      session.getTransaction().commit();
      
      return result;
    } catch (Throwable th) {
      
      if (session.getTransaction() != null) {
        session.getTransaction().rollback();
      }
      
      if (th instanceof RuntimeException) {
        throw (RuntimeException) th;
      }

      throw new RuntimeException(th);
    } finally {
      session.close();

      ManagedSessionContext.unbind(this.sessionFactory);
    }
  }
}
