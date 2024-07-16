package com.tpe.security.service;

import com.tpe.domain.Role;
import com.tpe.domain.User;
import com.tpe.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    //amac/: UserDetails--> GrandAuthorities --->Role
    @Autowired
    public UserRepository userRepository;





    @Override
    public UserDetails loadUserByUsername(String username) throws org.springframework.security.core.userdetails.UsernameNotFoundException {

        User user  = userRepository.findByUserName(username).
                orElseThrow(()-> new UsernameNotFoundException("Username not found: "+username));

    return new org.springframework.security.core.userdetails.
            User(user.getUserName(),user.getPassword(),buildGrantedAuthorities(user.getRoles()));
    }

    private List<SimpleGrantedAuthority> buildGrantedAuthorities(Set<Role> roles){

        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        for (Role role :roles) {
            authorities.add(new SimpleGrantedAuthority(role.getType().name()));
        } // rollerin isimlerini parametere olarak SimpleGrantedAuthority nin constructor verdigimizde
         // yeni SimpleGrantedAuthority olustururuz ve bu SimpleGrantedAuthority listeye ekleriz
        return authorities;

    }

}
