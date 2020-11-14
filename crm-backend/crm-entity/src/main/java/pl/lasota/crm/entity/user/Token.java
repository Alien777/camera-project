package pl.lasota.crm.entity.user;

import lombok.Data;
import pl.lasota.tool.sr.mapping.NotUpdating;
import pl.lasota.tool.sr.reflection.IgnoreMap;
import pl.lasota.tool.sr.repository.BasicEntity;

import javax.persistence.*;
import java.util.Objects;
import java.util.UUID;


@Entity
@Data
public class Token implements BasicEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "option_sequence")
    @SequenceGenerator(name = "option_sequence", sequenceName = "token_id_seq", allocationSize = 1)
    @NotUpdating
    private Long id;

    @Column
    private UUID activator;

    @IgnoreMap
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Token token = (Token) o;
        return Objects.equals(activator, token.activator);
    }

    @Override
    public int hashCode() {
        return Objects.hash(activator);
    }
}
