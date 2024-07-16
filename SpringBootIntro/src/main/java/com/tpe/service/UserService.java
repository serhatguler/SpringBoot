package com.tpe.service;

import com.tpe.domain.Role;
import com.tpe.domain.User;
import com.tpe.domain.enums.RoleType;
import com.tpe.dto.UserRequestDTO;
import com.tpe.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleService roleService; //normalde service repoya baglanir ancak burada user ve role farkli
                                    /// bu yuzden role un servicine baglandik
    @Autowired
    private PasswordEncoder passwordEncoder;

    public void saveUser(UserRequestDTO userRequestDTO) {

        User user = new User();
        user.setFirstName(userRequestDTO.getFirstName());
        user.setLastName(userRequestDTO.getLastName());
        user.setUserName(userRequestDTO.getUserName());

        //burada passwordu sifreleyerek DB ye gonderelim.
        String password =userRequestDTO.getPassword();
        String encodedPassword =passwordEncoder.encode(password); //requestteki password karmasiklastirildi.

        user.setPassword(encodedPassword);

        //userin role u setlenmesi gerekiyor. bunun icin DB den rolleri bulmasi gerekiyor
        //rolleri getirmek icin roleRepositorye baglanmak gerekiyor

        Set<Role> roles = new HashSet<>();// set icerisinde bu kullanici hangi rolleri alabilir diye ekliyoruz
        Role role = roleService.getRoleByType(RoleType.ROLE_ADMIN);
        roles.add(role);
        user.setRoles(roles);//defaultta usera ADMIN rolunu verdik.
        userRepository.save(user);



    }
}
