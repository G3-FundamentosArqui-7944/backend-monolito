package pe.edu.upc.bodymatch.iam.application.internal.commandservices;

import org.apache.commons.lang3.tuple.ImmutableTriple;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.upc.bodymatch.iam.application.internal.outboundservices.hashing.HashingService;
import pe.edu.upc.bodymatch.iam.application.internal.outboundservices.tokens.TokenService;
import pe.edu.upc.bodymatch.iam.domain.model.aggregates.RefreshToken;
import pe.edu.upc.bodymatch.iam.domain.model.aggregates.User;
import pe.edu.upc.bodymatch.iam.domain.model.commands.AssignRolesCommand;
import pe.edu.upc.bodymatch.iam.domain.model.commands.RefreshTokenCommand;
import pe.edu.upc.bodymatch.iam.domain.model.commands.RevokeRefreshTokenCommand;
import pe.edu.upc.bodymatch.iam.domain.model.commands.SignInCommand;
import pe.edu.upc.bodymatch.iam.domain.model.commands.SignUpCommand;
import pe.edu.upc.bodymatch.iam.domain.model.entities.Role;
import pe.edu.upc.bodymatch.iam.domain.model.valueobjects.Roles;
import pe.edu.upc.bodymatch.iam.domain.services.UserCommandService;
import pe.edu.upc.bodymatch.iam.infrastructure.persistence.jpa.repositories.RefreshTokenRepository;
import pe.edu.upc.bodymatch.iam.infrastructure.persistence.jpa.repositories.RoleRepository;
import pe.edu.upc.bodymatch.iam.infrastructure.persistence.jpa.repositories.UserRepository;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Optional;

@Service
public class UserCommandServiceImpl implements UserCommandService {
    private final UserRepository userRepository;
    private final HashingService hashingService;
    private final TokenService tokenService;
    private final RoleRepository roleRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    @Value("${authorization.jwt.refresh.expiration.days:30}")
    private int refreshExpirationDays;

    public UserCommandServiceImpl(
            UserRepository userRepository,
            HashingService hashingService,
            TokenService tokenService,
            RoleRepository roleRepository,
            RefreshTokenRepository refreshTokenRepository
    ) {
        this.userRepository = userRepository;
        this.hashingService = hashingService;
        this.tokenService = tokenService;
        this.roleRepository = roleRepository;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @Override
    @Transactional
    public Optional<User> handle(SignUpCommand command) {
        if (userRepository.existsByEmail(command.email())) {
            throw new IllegalArgumentException("Email already exists: " + command.email());
        }
        var resolvedRoles = new ArrayList<Role>();
        if (command.roles() == null || command.roles().isEmpty()) {
            roleRepository.findByName(Roles.ROLE_ATHLETE).ifPresent(resolvedRoles::add);
        } else {
            for (var requested : command.roles()) {
                var persisted = roleRepository.findByName(requested.getName())
                        .orElseThrow(() -> new IllegalArgumentException("Role not found: " + requested.getName()));
                resolvedRoles.add(persisted);
            }
        }
        var user = new User(
                command.email(),
                hashingService.encode(command.password()),
                command.firstName(),
                command.lastName(),
                command.phone(),
                resolvedRoles
        );
        userRepository.save(user);
        return userRepository.findByEmail(command.email());
    }

    @Override
    @Transactional
    public Optional<ImmutableTriple<User, String, String>> handle(SignInCommand command) {
        var user = userRepository.findByEmail(command.email())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        if (!user.isActive()) {
            throw new IllegalStateException("User is deactivated");
        }
        if (!hashingService.matches(command.password(), user.getPassword())) {
            throw new IllegalArgumentException("Invalid credentials");
        }
        var accessToken = tokenService.generateToken(user.getEmail());
        var refresh = createRefreshTokenFor(user.getId());
        return Optional.of(new ImmutableTriple<>(user, accessToken, refresh.getToken()));
    }

    @Override
    @Transactional
    public Optional<ImmutableTriple<User, String, String>> handle(RefreshTokenCommand command) {
        var refresh = refreshTokenRepository.findByToken(command.refreshToken())
                .orElseThrow(() -> new IllegalArgumentException("Refresh token not found"));
        if (!refresh.isUsable()) {
            throw new IllegalStateException("Refresh token is expired or revoked");
        }
        var user = userRepository.findById(refresh.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found for refresh token"));
        if (!user.isActive()) {
            throw new IllegalStateException("User is deactivated");
        }
        refresh.revoke();
        refreshTokenRepository.save(refresh);
        var accessToken = tokenService.generateToken(user.getEmail());
        var newRefresh = createRefreshTokenFor(user.getId());
        return Optional.of(new ImmutableTriple<>(user, accessToken, newRefresh.getToken()));
    }

    @Override
    @Transactional
    public void handle(RevokeRefreshTokenCommand command) {
        refreshTokenRepository.findByToken(command.refreshToken()).ifPresent(refresh -> {
            refresh.revoke();
            refreshTokenRepository.save(refresh);
        });
    }

    @Override
    @Transactional
    public Optional<User> handle(AssignRolesCommand command) {
        var user = userRepository.findById(command.userId())
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + command.userId()));
        if (command.roleNames() == null || command.roleNames().isEmpty()) {
            throw new IllegalArgumentException("At least one role must be provided");
        }
        var resolved = new ArrayList<Role>();
        for (var name : command.roleNames()) {
            var roleEnum = Roles.valueOf(name);
            var persisted = roleRepository.findByName(roleEnum)
                    .orElseThrow(() -> new IllegalArgumentException("Role not found: " + name));
            resolved.add(persisted);
        }
        user.replaceRoles(resolved);
        userRepository.save(user);
        return Optional.of(user);
    }

    private RefreshToken createRefreshTokenFor(Long userId) {
        var expiresAt = Instant.now().plus(refreshExpirationDays, ChronoUnit.DAYS);
        var token = new RefreshToken(userId, expiresAt);
        return refreshTokenRepository.save(token);
    }
}
