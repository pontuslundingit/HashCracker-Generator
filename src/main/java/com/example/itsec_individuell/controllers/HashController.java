package com.example.itsec_individuell.controllers;


import com.example.itsec_individuell.methods.HashGenerator;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

@Controller
public class HashController {

    private final HashGenerator hashGenerator;

    public HashController() {
        this.hashGenerator = new HashGenerator();
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
        String message = "No match! :(";

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFilePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":");
                if (parts.length == 2) {
                    String password = parts[0].trim();
                    String hashes = parts[1].trim();

                    String[] hashParts = hashes.split(", ");
                    String md5Hash = null;
                    String sha256Hash = null;

                    for (String hashPart : hashParts) {
                        if (hashPart.startsWith("MD5=")) {
                            md5Hash = hashPart.substring(4).trim();
                        } else if (hashPart.startsWith("SHA-256=")) {
                            sha256Hash = hashPart.substring(8).trim();
                        }
                    }
                    if (hash.equalsIgnoreCase(md5Hash)) {
                        message = "Cracked! Password: " + password + " (MD5)";
                        break;
                    } else if (hash.equalsIgnoreCase(sha256Hash)) {
                        message = "Cracked! Password: " + password + " (SHA-256)";
                        break;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        model.addAttribute("message", message);
        return "cracker";
    }

//    @PostMapping("cracker")
//    public String crackHash(@RequestParam String hash, Model model) {
//        String inputFilePath = "stats/hashes.txt";
//        String message = "No match! :(";
//
//        try (BufferedReader reader = new BufferedReader(new FileReader(inputFilePath))) {
//            String line;
//            while ((line = reader.readLine()) != null) {
//                String[] parts = line.split(":");
//                if (parts.length == 2 && parts[1].trim().equalsIgnoreCase(hash.trim())) {
//                    message = "Cracked! Password: " + parts[0].trim();
//                    break;
//                }
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        model.addAttribute("message", message);
//        return "cracker";
//    }

}

