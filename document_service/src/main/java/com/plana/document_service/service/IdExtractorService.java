package com.plana.document_service.service;

import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class IdExtractorService {

    private final Pattern aadhaar = Pattern.compile("\\b\\d{4}\\s?\\d{4}\\s?\\d{4}\\b");
    private final Pattern pan = Pattern.compile("\\b[A-Z]{5}[0-9]{4}[A-Z]\\b");
    private final Pattern passport = Pattern.compile("\\b[A-PR-WYa-pr-wy][1-9]\\d{6}\\b");

    public String extractId(Path filePath) throws Exception {
        String text = Files.readString(filePath);

        Matcher aadhaarMatch = aadhaar.matcher(text);
        if (aadhaarMatch.find()) return "Aadhaar: " + aadhaarMatch.group();

        Matcher panMatch = pan.matcher(text);
        if (panMatch.find()) return "PAN: " + panMatch.group();

        Matcher passportMatch = passport.matcher(text);
        if (passportMatch.find()) return "Passport: " + passportMatch.group();

        return "No valid ID number found";
    }
}

