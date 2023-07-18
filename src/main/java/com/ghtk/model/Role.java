package com.ghtk.model;

import com.ghtk.model.Permission;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public enum Role {
    STAFF(
            Set.of(
                    Permission.STAFF_READ,
                    Permission.STAFF_UPDATE,
                    Permission.STAFF_CREATE,
                    Permission.STAFF_DELETE
            )
    ),
    SHOP(
            Set.of(
                    Permission.SHOP_READ,
                    Permission.SHOP_UPDATE,
                    Permission.SHOP_CREATE,
                    Permission.SHOP_DELETE,

                    Permission.STAFF_READ,
                    Permission.STAFF_UPDATE,
                    Permission.STAFF_CREATE,
                    Permission.STAFF_DELETE
            )
    );

    @Getter
    private final Set<Permission> permissions;

    public List<SimpleGrantedAuthority> getAuthorities() {
        var authorities = getPermissions()
                .stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toList());
        authorities.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
        return authorities;
    }
}
