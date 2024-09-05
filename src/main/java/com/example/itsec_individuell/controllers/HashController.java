package com.example.itsec_individuell.controllers;


import com.example.itsec_individuell.methods.HashCracker;
import com.example.itsec_individuell.methods.HashGenerator;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.NoSuchAlgorithmException;

@Controller
public class HashController {

    private final HashGenerator hashGenerator;
    private final HashCracker hashCracker;

    public HashController() {
        this.hashGenerator = new HashGenerator();
        this.hashCracker = new HashCracker();
    }

    @GetMapping("/generator")
    public String generate(Model model) {
        model.addAttribute("activeFunction", "generator");
        return "generator";
    }

    @PostMapping("/generateHash")
    public String generateHash(@RequestParam("input") String input, Model model) {
        try {
            String md5Hash = hashGenerator.createMD5Hash(input);
            String sha256Hash = hashGenerator.createSHA256Hash(input);

            model.addAttribute("input", input);
            model.addAttribute("md5Hash", md5Hash);
            model.addAttribute("sha256Hash", sha256Hash);
        } catch (NoSuchAlgorithmException e) {
            model.addAttribute("error", "Error generating hash: " + e.getMessage());
        }
        return "generator";
    }

    @GetMapping("cracker")
    public String showCrackerPage() {
        return "cracker";
    }

    @PostMapping("cracker")
    public String crackHash(@RequestParam String hash, Model model) {
        String inputFilePath = "stats/hashes.txt";
        String message = hashCracker.crackHash(hash, inputFilePath);
        model.addAttribute("message", message);
        return "cracker";
    }

}

