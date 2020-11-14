package pl.lasota.crm.repository.user;

import org.springframework.stereotype.Component;
import pl.lasota.crm.entity.user.Role;
import pl.lasota.tool.sr.repository.RepositoryAdapter;
import pl.lasota.tool.sr.service.base.AllNoMappingAction;

@Component
public class RoleCrudAction extends AllNoMappingAction< Role> {
    public RoleCrudAction(RepositoryAdapter<Role> entityRepository) {
        super(entityRepository, Role.class);
    }
}
