package com.diplom.messenger.security;

import com.diplom.messenger.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@RequiredArgsConstructor
public class UserDetailsImpl implements UserDetails {

    private final User user;

    // Права доступа (роли) — пока у всех одинаковые, просто пустой список
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    // Возвращаем хэш пароля из User
    @Override
    public String getPassword() {
        return user.getPasswordHash();
    }

    // Возвращаем username из User
    @Override
    public String getUsername() {
        return user.getUsername();
    }

    // Аккаунт не истёк — пока всегда true
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    // Аккаунт не заблокирован — пока всегда true
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    // Пароль не истёк — пока всегда true
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    // Аккаунт активен — пока всегда true
    @Override
    public boolean isEnabled() {
        return true;
    }

    // Метод для удобства — чтобы доставать User из UserDetailsImpl где угодно
    public User getUser() {
        return user;
    }
}