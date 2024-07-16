package com.tpe.controller;

import com.tpe.domain.Student;
import com.tpe.dto.StudentDTO;
import com.tpe.service.StudentService;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController//cunku RestAPI uygulamasi yapiyoruz.
@RequestMapping("/students")//http://localhost:8080/students
public class StudentController {

    Logger logger = LoggerFactory.getLogger(StudentController.class);

    @Autowired
    private StudentService studentService;


    //Spring boot u selamlama :)
    //http://localhost:8080/students/greet+GET
    @GetMapping("/greet")
    public String greet(){
        return "Hello SpringBoot";
    }


    //CRUD

    //1.adim-tum studentlari listeleyelim:READ
    //http://localhost:8080/students + GET
    @PreAuthorize("hasRole('ADMIN')") //sadece ADMIN bu islemi yapabilir.
    @GetMapping
    public ResponseEntity<List<Student>> listAllStudents(){ //studentslari geri donecegim icim List<> gerekli fakat
                                            //Http status + body(data) donmek icin ResponseEntity<>
        List<Student> studentList= studentService.getAllStudent();
        //return new ResponseEntity<>(studentList, HttpStatus.OK); //200
        return ResponseEntity.ok(studentList); //200
    }
    //response:body(data)+HTTP status code
    //ResponseEntity<>:response bodysi ile birlikte HTTP status code nu gondermemizi saglar.
    //return ResponseEntity.ok();metodu HTTP status olarak yada 200 donmek icin kisa yoldur.




    //3.adim-yeni bir student CREATE etme
    //http://localhost:8080/students + POST + RequestBody(JSON)(@RequestBody)
    //yeni obje CREATE ettigimiz icin geriye mesaj gonderebiliriz
    @PostMapping
    public ResponseEntity<Map<String,String>> createStudent(@Valid@RequestBody Student student){

        studentService.saveStudent(student);

        Map<String,String> response = new HashMap<>();
        response.put("message","Student is created successfully");
        response.put("status","success");
        return new ResponseEntity<>(response,HttpStatus.CREATED);//201
    }
    //@RequestBody :HTTP requestin bodysindeki JSON formatindaki bilgiyi student objesine mapler.
    // (Entity obje <-> JSON) -->Jackon kutuphanesi


    //5.adim- belirli bir id ile student i goruntuleyelim. RequestPAram ile yaptik
    //http://localhost:8080/students/query?id=1 + GET
    @GetMapping("/query")
    public ResponseEntity<Student> getStudent(@RequestParam("id") Long id){
        Student student = studentService.getStudentById(id);
        return new ResponseEntity<>(student,HttpStatus.OK);
    }

    //5b.adim- belirli bir id ile student i goruntuleyelim.PathVariable ile yaptik
    //http://localhost:8080/students/1 + GET
    @GetMapping("/{id}")
    public ResponseEntity<Student> getStudentByParam(@PathVariable("id") Long id) {
        Student student = studentService.getStudentById(id);
        return new ResponseEntity<>(student, HttpStatus.OK);
    }
    //clienttan bilgi almak icin: i)JSon formatinda request body
                                //ii) request param query?id=1
                                //iii) path param/1
                                //(@PathVariable("id") Long id,(@PathVariable("name") String name)


    //7.adim-belirli bir id ile studenti silelim + PAthVAriable
    //http://localhost:8080/students/1 + DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String,String>> deleteStudent(@PathVariable("id") Long id){
        studentService.deleteStudentById(id);

        Map<String,String> response = new HashMap<>();
        response.put("message","Student is deleted successfully");
        response.put("message","success");
        return ResponseEntity.ok(response);
    }

    //9.adim-  belirli bir id ile student update edelim.(name,lastname,grade,email,phonenumber) update+DTO
    //http://localhost:8080/students/1+UPDATE + JSON
    @PutMapping("/{id}")
    public ResponseEntity<Map<String,String>> updateStudent (@PathVariable("id") Long id,
                                                             @Valid @RequestBody StudentDTO studentDTO){
        studentService.updatedStudent(id,studentDTO);

        Map<String,String> response= new HashMap<>();
        response.put("message","Student is updated successfully");
        response.put("message","success");
        return ResponseEntity.ok(response);
    }

    //11.adim PAgination(sayfalandirma)
    //tum kayitlari page page listeleyelim.
    //http://localhost:8080/students/page?page=1&size=10&sort=name&direction=DESC + GET
    @GetMapping("/page")
    public ResponseEntity<Page<Student>> getAllStudentByPage(@RequestParam(value ="page",required = false,defaultValue = "0") int page, //hangi page gosterilsin
                                                             @RequestParam("size") int size, //her sayfada kac kayit bulunsun
                                                             @RequestParam("sort") String prop,//hangi fielda gore soralama
                                                             @RequestParam("direction")Sort.Direction direction) //siralama yonu

    {
        Pageable pageable = PageRequest.of(page,size,Sort.by(direction,prop));
        Page<Student> studentsByPage = studentService.getAllStudentPaging(pageable);
        return new ResponseEntity<>(studentsByPage,HttpStatus.OK);
        //page,size,direction parametrelerini opsiyonel yapabiliriz.(required=false)
        //parametrelerini  girilmesi opsiyonel oldugunda default value vermeliyiz.(defaultValue = "0"))
    }



    //13.adim-- lastName ile studentlari listeleyelim.
    //http://localhost:8080/students/querylastname?lastName=Bey
    @GetMapping("/querylastname")
    public ResponseEntity<List<Student>> getStudentByLastname(@RequestParam("lastName") String lastname){
         List<Student> studentList =  studentService.getAllStudentByLastName(lastname);
         return ResponseEntity.ok(studentList);
    }

    //15.adim-- grade ile studentlari listeleyelim.
    //http://localhost:8080/students/grade/99
    @GetMapping("/grade/{grade}")
    public ResponseEntity<List<Student>> getGrade(@PathVariable("grade") Integer grade){

        List<Student> studentList = studentService.getStudentByGrade(grade);
        return new ResponseEntity<>(studentList,HttpStatus.OK);
    }
    //17.adim- id si verilen studentin goruntuleme istedigini responce olarak DTO donmek istersek
    //http://localhost:8080/students/dto/1 + GET
    @GetMapping("/dto/{id}")
    public ResponseEntity<StudentDTO>  getStudentDtoById(@PathVariable("id") Long id){
        StudentDTO studentDTO = studentService.getStudentDtoById(id);

        logger.warn("------Servicten Student DTO objesi alindi : "+ studentDTO.getName());
        return ResponseEntity.ok(studentDTO);

    }

    //19.adim-
    @GetMapping("/welcome")
    public String welcome(HttpServletRequest request){
        logger.info("Welcome: {}",request.getServletPath());
        return "WELCOME:)";
    }













}
