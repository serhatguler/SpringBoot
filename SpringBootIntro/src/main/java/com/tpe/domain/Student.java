package com.tpe.domain;


import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor //parametreli constructor
@NoArgsConstructor  //parametresiz constructor default
//@RequiredArgsConstructor //final veya notnull olarak belirtilen fieldlardan constructor olusturur.(final private Long id;)
@Entity
public class Student {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   //@Setter(AccessLevel.NONE) id nin setter metodunu yoksay
    private Long id;

   @NotBlank(message = "name can not be space")
   @Size(min = 2,max = 25,message = " name '${validatedValue}' must be between {min} and {max}")
    @Column(nullable = false,length = 25)
    private String name;

    @NotBlank(message = "name can not be space")
    @Size(min = 2,max = 25,message = " lastname '${validatedValue}' must be between {min} and {max}")
    @Column(nullable = false,length = 25)
    private String lastName;

    private Integer grade;

    @Column(nullable = false,unique = true,length = 50)
    @Email(message = "Provide valid email")//a@bb.ccc yapsini bize sagliyor
    private String email;

    private String phoneNumber;

    private LocalDateTime creationDate = LocalDateTime.now();


    @OneToMany(mappedBy = "student")
    private List<Book> books =new ArrayList<>();

    @OneToOne
    private User user;


    //getter-setter



}
