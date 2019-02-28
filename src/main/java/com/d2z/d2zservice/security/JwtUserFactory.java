package com.d2z.d2zservice.security;

import com.d2z.d2zservice.entity.User;

public final class JwtUserFactory {

    private JwtUserFactory() {
    }

    public static JwtUser create(User user) {
        return new JwtUser(
                user.getUser_Id(),
                user.getUsername(),
                user.getEmail(),
                user.getPassword(),
                user.getLastPasswordResetDate()
        );
    }

//    private static List<GrantedAuthority> mapToGrantedAuthorities(List<AuthorityTest> authorities) {
//        return authorities.stream()
//                .map(authority -> new SimpleGrantedAuthority(authority.getName().name()))
//                .collect(Collectors.toList());
//    }

}
