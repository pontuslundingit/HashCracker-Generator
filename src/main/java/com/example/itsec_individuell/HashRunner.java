package com.example.itsec_individuell;

import com.example.itsec_individuell.methods.HashGenerator;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;

@Configuration
public class HashRunner {

    @Bean
    public CommandLineRunner run() {
        return args -> {
            HashGenerator hashGenerator = new HashGenerator();

            Path inputPath = Paths.get("stats/commonPasswords.txt");
            Path outputPath = Paths.get("stats/hashes.txt");

            if (!Files.exists(outputPath.getParent())) {
                Files.createDirectories(outputPath.getParent());
            }

            try (
                BufferedReader reader = Files.newBufferedReader(inputPath);
                BufferedWriter writer = Files.newBufferedWriter(outputPath)) {

                String password;
                while ((password = reader.readLine()) != null) {
                    try {
                        String md5Hash =  hashGenerator.createMD5Hash(password);
                        //String sha256Hash = hashGenerator.createSHA256Hash(password);
                        writer.write(String.format("%s:%s%n", password, md5Hash));
                    } catch (NoSuchAlgorithmException e) {
                        e.printStackTrace();
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        };
    }
}
