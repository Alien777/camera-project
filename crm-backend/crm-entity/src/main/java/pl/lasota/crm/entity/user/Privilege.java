package pl.lasota.crm.entity.user;

import lombok.Data;
import org.hibernate.annotations.Type;
import pl.lasota.crm.enums.PrivilegeName;
import pl.lasota.tool.sr.mapping.NotUpdating;
import pl.lasota.tool.sr.repository.BasicEntity;

import javax.persistence.*;

@Entity
@Data
public class Privilege implements BasicEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "option_sequence")
    @SequenceGenerator(name = "option_sequence", sequenceName = "privilege_id_seq", allocationSize = 1)
    @NotUpdating
    private Long id;

    @Enumerated(EnumType.STRING)
    @Type(type = "pl.lasota.crm.entity.EnumPostgresSql")
    private PrivilegeName name;

}