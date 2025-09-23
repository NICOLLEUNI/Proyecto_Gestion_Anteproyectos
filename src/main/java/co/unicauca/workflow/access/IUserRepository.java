
package co.unicauca.workflow.access;

import co.unicauca.workflow.domain.entities.User;
import java.util.List;

/**
 *
 * @author User
 */
public interface IUserRepository {
     
    boolean save(User newUser);

    List<User> list();
}
