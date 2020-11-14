package pl.lasota.crm.entity.user;

import lombok.Data;
import org.hibernate.annotations.Type;
import pl.lasota.crm.enums.RoleName;
import pl.lasota.tool.sr.mapping.NotUpdating;
import pl.lasota.tool.sr.repository.BasicEntity;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
public class Role implements BasicEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "option_sequence")
    @SequenceGenerator(name = "option_sequence", sequenceName = "role_id_seq", allocationSize = 1)
    @NotUpdating
    private Long id;

    @Enumerated(EnumType.STRING)
    @Type(type = "pl.lasota.crm.entity.EnumPostgresSql")
    private RoleName name;

    @ManyToMany
    @JoinTable(name = "ROLE_PRIVILEGE",
            joinColumns = {@JoinColumn(name = "role_id")},
            inverseJoinColumns = {@JoinColumn(name = "privilege_id")})
    private Set<Privilege> privileges = new HashSet<>();

}