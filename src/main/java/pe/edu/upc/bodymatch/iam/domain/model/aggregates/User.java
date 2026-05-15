package pe.edu.upc.bodymatch.iam.domain.model.aggregates;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import pe.edu.upc.bodymatch.iam.domain.model.entities.Role;
import pe.edu.upc.bodymatch.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
public class User extends AuditableAbstractAggregateRoot<User> {

    @Getter
    @NotBlank
    @Column(unique = true, nullable = false)
    @Size(max = 254)
    private String email;

    @Getter
    @NotBlank
    @Size(max = 256)
    private String password;

    @Getter
    @NotBlank
    @Size(max = 60)
    private String firstName;

    @Getter
    @NotBlank
    @Size(max = 60)
    private String lastName;

    @Getter
    @Size(max = 20)
    private String phone;

    @Getter
    private boolean isActive;

    @Getter
    private boolean emailVerified;

    @Getter
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles;

    public User() {
        this.roles = new HashSet<>();
        this.isActive = true;
        this.emailVerified = false;
    }

    public User(String email, String password, String firstName, String lastName, String phone) {
        this();
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
    }

    public User(String email, String password, String firstName, String lastName, String phone, List<Role> roles) {
        this(email, password, firstName, lastName, phone);
        addRoles(roles);
    }

    public User addRole(Role role) {
        this.roles.add(role);
        return this;
    }

    public User addRoles(List<Role> roles) {
        var validatedRoleSet = Role.validateRoleSet(roles);
        this.roles.addAll(validatedRoleSet);
        return this;
    }

    public void replaceRoles(List<Role> roles) {
        if (roles == null || roles.isEmpty()) {
            throw new IllegalArgumentException("At least one role must be provided");
        }
        this.roles.clear();
        this.roles.addAll(roles);
    }

    public void verifyEmail() {
        this.emailVerified = true;
    }

    public void deactivateUser() {
        this.isActive = false;
    }

    public void activateUser() {
        this.isActive = true;
    }

    public void updateUserInfo(String firstName, String lastName, String phone) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
    }

    public void changePassword(String newEncodedPassword) {
        if (newEncodedPassword == null || newEncodedPassword.isBlank()) {
            throw new IllegalArgumentException("Password must not be blank");
        }
        this.password = newEncodedPassword;
    }

    public boolean hasRole(String roleName) {
        return roles.stream().anyMatch(r -> r.getStringName().equals(roleName));
    }
}
