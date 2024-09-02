package com.example.itsec_individuell.controllers;


import com.example.itsec_individuell.models.User;
import com.example.itsec_individuell.models.UserRepository;
import com.example.itsec_individuell.services.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;

    public UserController(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @GetMapping(path = "/userlogin")
    public String login(Model model) {
        model.addAttribute("activeFunction", "userlogin");
        return "userlogin";
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String regUser(@ModelAttribute User user, Model model) {
        try {
            userService.registerNewUser(user.getUsername(), user.getPassword());
            return "redirect:/home";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "/register";
        }
    }

    @GetMapping("/home")
    public String home(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String username = userDetails.getUsername();
            model.addAttribute("username", username);

            userRepository.findByUsername(username).ifPresent(user -> {
                model.addAttribute("user", user);
            });
        }
        model.addAttribute("activeFunction", "home");
        return "home";
    }



//    @GetMapping("/cracker")
//    public String crack(Model model) {
//        model.addAttribute("activeFunction", "cracker");
//        return "cracker";
//    }

    private void addUserDetailsToModel(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            model.addAttribute("username", userDetails.getUsername());
            System.out.println("User logged in: " + userDetails.getUsername()); // Debug output
        } else {
            System.out.println("No user is logged in or user details are not available.");
        }
    }

}
