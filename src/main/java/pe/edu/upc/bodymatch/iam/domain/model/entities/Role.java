package pe.edu.upc.bodymatch.iam.domain.model.entities;

import jakarta.persistence.*;
import lombok.Getter;
import pe.edu.upc.bodymatch.iam.domain.model.valueobjects.Roles;
import pe.edu.upc.bodymatch.shared.domain.model.entities.AuditableModel;

import java.util.List;

@Entity
public class Role extends AuditableModel {

    @Id
    @Getter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true, length = 20)
    private Roles name;

    public Role() {}

    public Role(Roles name) {
        this.name = name;
    }

    public String getStringName() {
        return name.name();
    }

    public static Role getDefaultRole() {
        return new Role(Roles.ROLE_ATHLETE);
    }

    public static Role toRoleFromName(String name) {
        return new Role(Roles.valueOf(name));
    }

    public static List<Role> validateRoleSet(List<Role> roles) {
        if (roles == null || roles.isEmpty()) {
            return List.of(getDefaultRole());
        }
        return roles;
    }
}
