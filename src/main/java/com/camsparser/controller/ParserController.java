package com.camsparser.controller;

import com.camsparser.dto.ResponseDTO;
import com.camsparser.service.ParserService;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.encryption.InvalidPasswordException;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.apache.pdfbox.util.Charsets.UTF_8;

@RestController
@Slf4j
public class ParserController {

    @Autowired
    private ParserService parser;

    @PostMapping(value = "/getCamsAsJson")
    public ResponseDTO getParsedCamsStatement(@RequestParam MultipartFile multiPartFile, @RequestParam String password) {

        File file = new File("/home/sarabjeetsingh/tempFile.tmp");
        try {
            multiPartFile.transferTo(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try (PDDocument document = PDDocument.load(file, password)) {
            log.info("PDF Processing started...");
            // The order of the text tokens in a PDF file may not be in the same as they appear
            // visually on the screen, so tell PDFBox to sort by text position

            PDFTextStripper pdfTextStripper = new PDFTextStripper();
            pdfTextStripper.setSortByPosition(true);
            pdfTextStripper.setLineSeparator("\n\n");
            try (BufferedWriter writer = Files.newBufferedWriter(Paths.get("/home/sarabjeetsingh/bcbc.txt"), UTF_8)) {
                // This will take a PDDocument and write the text of that document to the writer.
                pdfTextStripper.writeText(document, writer);
            }
        } catch (InvalidPasswordException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ResponseDTO responseDTO = new ResponseDTO();
        try {

            responseDTO.setData(parser.parseFile("/home/sarabjeetsingh/bcbc.txt"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return responseDTO;
    }
}