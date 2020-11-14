package pl.lasota.crm.repository.user;

import org.springframework.stereotype.Repository;
import pl.lasota.crm.entity.user.User;
import pl.lasota.tool.sr.repository.RepositoryAdapter;

import javax.persistence.EntityManager;

@Repository
public class UserRepository extends RepositoryAdapter<User> {
    public UserRepository(EntityManager em) {
        super(em);
    }
}
