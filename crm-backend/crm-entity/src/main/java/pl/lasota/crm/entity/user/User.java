package pl.lasota.crm.entity.user;

import lombok.Data;
import pl.lasota.tool.sr.mapping.NotUpdating;
import pl.lasota.tool.sr.repository.BasicEntity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;


@Entity
@Table(name = "\"user\"")
@Data
public class User implements BasicEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "option_sequence")
    @SequenceGenerator(name = "option_sequence", sequenceName = "user_id_seq", allocationSize = 1)
    @NotUpdating
    private Long id;

    @Column
    private UUID identifier;

    @Column
    private String name;

    @Column
    private String email;

    @Column
    private String password;

    @Column
    private LocalDateTime createAnAccountTime;

    @Column
    private boolean active;


    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "USER_PRIVILEGE",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "privilege_id")})
    private Set<Privilege> privileges = new HashSet<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "USER_ROLE",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id")})
    private Set<Role> roles = new HashSet<>();


    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private Set<Token> tokens = new HashSet<>();

}
