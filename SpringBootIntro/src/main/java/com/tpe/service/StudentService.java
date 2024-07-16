package com.tpe.service;

import com.tpe.domain.Student;
import com.tpe.dto.StudentDTO;
import com.tpe.exception.ConflictException;
import com.tpe.exception.ResourceNotFoundException;
import com.tpe.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    //2.adim
    public List<Student> getAllStudent() {
        List<Student>  students = studentRepository.findAll();//select * from student;
        return studentRepository.findAll();
        //return students;
    }

    //4.adim
    public void saveStudent(Student student) {
        //student daha once kaydedilmis mi? ->>> (ayni emaile(unique) sahip student var mi?)
        if (studentRepository.existsByEmail(student.getEmail())){
            throw new ConflictException("Email already exist!");
        }
        studentRepository.save(student);
    }

    //6.adim
    public Student getStudentById(Long id) {
        Student student = studentRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Student not found by id: "+id));
        return student;
        //findById(id) optional oldugundan null doner bu yuzden orelse yaparak mesaj gondermek gerekli
    }

    //8.adim
    public void deleteStudentById(Long id) {
        //bu id ye sahip student var mi? getStudentById 6.adim da student olup olmadigini kontrol ettigi icin burada da kontrol ettik
         Student foundStudent =getStudentById(id);
        studentRepository.delete(foundStudent);
    }

    //10.adim
    public void updatedStudent(Long id, StudentDTO studentDTO) {
        //gelen id ile student olip olmadigini kontrol
        Student foundStudent =getStudentById(id);

        //studentDTO.getEmail() zaten daha onceden DB de varsa?
        //existEmail true ise email baska bir studetin olabilir, studentin kendi emaili olabilir???
        // ex id:3 student email:a@v.com
        //dtodan gelen email bilgisi soyle olanilir
        //v@r.com  id:4 v@r.com--->exception:true-->exception  -senaryo1
        // c@f.com   Db de yok --->existEmail:false --update:OK senaryo2-
        //a@v.com    id:3 a@v.com-->existsEmail:true--update:OK kendi emailini update etmeyip diger bilgileri update yapmak istiyor -senaryo1
        boolean existEmail = studentRepository.existsByEmail(studentDTO.getEmail());

        if (existEmail && !foundStudent.getEmail().equals(studentDTO.getEmail())){
            throw new ConflictException("email already exist!!!");
        }

        foundStudent.setName(studentDTO.getName()); //gelen student studenddto ile degistir
        foundStudent.setLastName(studentDTO.getLastName());
        foundStudent.setGrade(studentDTO.getGrade());
        foundStudent.setEmail(studentDTO.getEmail());
        foundStudent.setPhoneNumber(studentDTO.getPhoneNumber());
        studentRepository.save(foundStudent); //saveOrUpdate gibi islem yapar.

    }


    //12.adim
    public Page<Student> getAllStudentPaging(Pageable pageable) {

        return studentRepository.findAll(pageable);
    }

    //14.adim
    public List<Student> getAllStudentByLastName(String lastname) {

        return studentRepository.findAllByLastName(lastname);
    }

    //16,adim
    public List<Student> getStudentByGrade(Integer grade) {
        //return studentRepository.findAllByGrade(grade);
        return studentRepository.findAllGradeEquals(grade); //metodu biz olusturmak istersek
    }

    //18.adim
//    public StudentDTO getStudentDtoById(Long id) {
//        Student student = getStudentById(id);
//
//        //parametre olarak student objesinin kendisini verdigimizde DTO olusturulan bir constructor
//        StudentDTO studentDTO =new StudentDTO(student);
//        return studentDTO;
//    }

    //studenti DTO ya mapleme islemini JPQL ile yapalim
    public StudentDTO getStudentDtoById(Long id) {
        StudentDTO studentDTO = studentRepository.findStudentDtoById(id).orElseThrow(()->new ResourceNotFoundException("Student not found by id: "+id));
        return studentDTO;
    }
}

