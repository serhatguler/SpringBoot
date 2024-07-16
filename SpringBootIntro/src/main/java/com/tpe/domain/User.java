package com.tpe.domain;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "t_user") //ismini degismemiz gerekli yoksa hata verir
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

   @Column(length = 25,nullable = false)
    private String firstName;


    @Column(length = 25,nullable = false)
    private String lastName;



    @Column(length = 25,nullable = false,unique = true)
    private String userName;

    @Column(length = 255,nullable = false) //Password DB de sifrelenecegi icin uzun ve karmasik olacak bu yuzden length 255 yaptik
    private String password;


    //role
    @ManyToMany
    @JoinTable(name ="user_role",joinColumns = @JoinColumn(name = "user_id"),
                                 inverseJoinColumns = @JoinColumn(name = "role_id"))//
    private Set<Role> roles = new HashSet<>(); // Collections icerisinde tekrarlanan degerler yoksa Set<> kullaniriz



}
