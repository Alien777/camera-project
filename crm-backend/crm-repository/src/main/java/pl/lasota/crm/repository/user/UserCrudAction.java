package pl.lasota.crm.repository.user;

import org.springframework.stereotype.Component;
import pl.lasota.crm.entity.user.User;
import pl.lasota.tool.sr.service.base.AllNoMappingAction;

@Component
public class UserCrudAction extends AllNoMappingAction<User> {
    public UserCrudAction(UserRepository userRepository) {
        super(userRepository, User.class);
    }
}
