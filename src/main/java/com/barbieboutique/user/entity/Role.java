package com.barbieboutique.user.entity;

public enum Role {
    GUEST,
    USER,
    ADMIN,
    SUPERADMIN

//    GUEST(Set.of(Permission.PERMISSION_GUEST)),
//    USER(Set.of(Permission.PERMISSION_GUEST, Permission.PERMISSION_USER)),
//    ADMIN(Set.of(Permission.PERMISSION_GUEST, Permission.PERMISSION_USER, Permission.PERMISSION_ADMIN)),
//    SUPERADMIN(Set.of(Permission.PERMISSION_GUEST, Permission.PERMISSION_USER,
//               Permission.PERMISSION_ADMIN, Permission.PERMISSION_SUPERADMIN));
//
//    private final Set<Permission> permissions;
//
//    Role(Set<Permission> permissions) {
//        this.permissions = permissions;
//    }
//
//    public Set<Permission> getPermissions() {
//        return permissions;
//    }
//
//    public Set<SimpleGrantedAuthority> getAuthorities() {
//        return getPermissions().stream()
//                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
//                .collect(Collectors.toSet());
//    }


}
