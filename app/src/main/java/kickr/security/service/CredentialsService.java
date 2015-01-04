package kickr.security.service;

import java.util.Base64;
import kickr.db.entity.user.User;
import org.jasypt.salt.RandomSaltGenerator;
import org.jasypt.util.password.BasicPasswordEncryptor;
import org.jasypt.util.password.StrongPasswordEncryptor;

/**
 *
 * @author nikku
 */
public class CredentialsService {
  
  private static final RandomSaltGenerator randomGenerator = new RandomSaltGenerator();
  
  private static final StrongPasswordEncryptor passwordEncryptor = new StrongPasswordEncryptor();

  private static final BasicPasswordEncryptor basicEncryptor = new BasicPasswordEncryptor();


  public String encryptPassword(String password) {
    return passwordEncryptor.encryptPassword(password);
  }
  
  public boolean matches(String password, String encryptedPassword) {
    return passwordEncryptor.checkPassword(password, encryptedPassword);
  }
  
  public String createSessionToken(User user) {
    return basicEncryptor.encryptPassword(
              user.getName() +
              Base64.getEncoder().encodeToString(randomGenerator.generateSalt(35)));
  }
}
