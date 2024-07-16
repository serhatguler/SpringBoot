package com.tpe.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter

public class Book { //book many side
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonProperty("bookName") //Jsona donustururken name fieldi yerine bookName yaz
    private String name;

    @JsonIgnore //book objemi JSON a maplerken studenti alma yoksa sonsuz loopa girer.
    //1Studentin bircok kitabi olur-->MAnyBook
    @ManyToOne
    private Student student; //student tek olan taraf
}
