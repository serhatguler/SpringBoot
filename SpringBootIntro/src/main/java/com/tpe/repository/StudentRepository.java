package com.tpe.repository;

import com.tpe.domain.Student;
import com.tpe.dto.StudentDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository//optional
public interface StudentRepository extends JpaRepository<Student,Long> {


    Boolean existsByEmail(String email);//bu emaile sahip kayit varsa true yoksa false

    List<Student> findAllByLastName(String lastname);

    List<Student> findAllByGrade(Integer grade);

    // JPQL
    //1.Yontem
    @Query("SELECT sFROM Student s Where s.grade=:pGrade")
    //@Query("FROM Student s Where s.grade=:pGrade")
    List<Student> findAllGradeEquals(@Param("pGrade") Integer grade);

    //SQL
    //2.Yontem
//    @Query( value = "SELECT * FROM student s Where s.grade=:pGrade",nativeQuery = true)
//    List<Student> findAllGradeEquals(@Param("pGrade") Integer grade);



    //DB den gelen studenti DTO ya cevirerek gonderiyor.B
    @Query("SELECT new com.tpe.dto.StudentDTO(s) FROM student s WHERE s.id=:pId")
    Optional<StudentDTO> findStudentDtoById(@Param("pId") Long id);

    





}
