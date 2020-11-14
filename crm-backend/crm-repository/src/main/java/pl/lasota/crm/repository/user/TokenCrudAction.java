package pl.lasota.crm.repository.user;

import org.springframework.stereotype.Component;
import pl.lasota.crm.entity.user.Token;
import pl.lasota.tool.sr.repository.RepositoryAdapter;
import pl.lasota.tool.sr.service.base.AllNoMappingAction;

@Component
public class TokenCrudAction extends AllNoMappingAction<Token> {
    public TokenCrudAction(RepositoryAdapter<Token> entityRepository) {
        super(entityRepository, Token.class);
    }
}
