package kickr.db.dao;

import io.dropwizard.hibernate.AbstractDAO;
import javax.inject.Inject;

import kickr.db.entity.Game;


import org.hibernate.SessionFactory;

public class GameDAO extends AbstractDAO<Game> {

  public GameDAO(SessionFactory sessionFactory) {
    super(sessionFactory);
  }
  
  public Game findGameById(Long id) {
    return get(id);
  }
  
  public void create(Game game) {
    persist(game);
  }
}
