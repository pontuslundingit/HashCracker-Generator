package com.example.itsec_individuell.services;


import com.example.itsec_individuell.models.User;
import com.example.itsec_individuell.models.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void saveUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }




    public void registerNewUser(String username, String password) {
        System.out.println(username);
        System.out.println(password);
        if (userRepository.findByUsername(username).isPresent()) {
            throw new IllegalArgumentException("Användarnamnet är redan taget");
        }
        String encodedPassword = passwordEncoder.encode(password);
        User newUser = new User(username, encodedPassword);

        userRepository.save(newUser);
    }


}
