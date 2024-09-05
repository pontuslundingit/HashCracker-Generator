package com.example.itsec_individuell.methods;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;



public class HashCracker {

    public String crackHash(String hash, String inputFilePath) {
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

        return message;
    }
}
