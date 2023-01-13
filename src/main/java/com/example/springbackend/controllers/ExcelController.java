package com.example.springbackend.controllers;

import com.example.springbackend.ResponseMessage;
import com.example.springbackend.helper.ExcelHelper;
import com.example.springbackend.repositories.ProductRepository;
import com.example.springbackend.services.ExcelService;
import com.example.springbackend.services.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@CrossOrigin("http://localhost:8080")
@Controller
@RequestMapping("/api")
public class ExcelController {
    private final ExcelService excelService;
    private final ProductService productService;
    private final ProductRepository productRepository;
    public ExcelController(ExcelService excelService,
                           ProductService productService, ProductRepository productRepository) {
        this.excelService = excelService;
        this.productService = productService;
        this.productRepository = productRepository;
    }
    @PostMapping("/upload")
    public ResponseEntity<ResponseMessage> uploadFile(@RequestParam("file") MultipartFile file) {
        String message = "";

        if (ExcelHelper.hasExcelFormat(file)) {
            try {
                excelService.save(file);

                message = "Uploaded the file successfully: " + file.getOriginalFilename();
                return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
            } catch (Exception e) {
                e.printStackTrace();
                message = "Could not upload the file: " + file.getOriginalFilename() + "!";
                return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
            }
        }

        message = "Please upload an excel file!";
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage(message));
    }



}
