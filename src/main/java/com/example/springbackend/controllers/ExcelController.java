package com.example.springbackend.controllers;

import com.example.springbackend.ResponseMessage;
import com.example.springbackend.helper.ExcelHelper;
import com.example.springbackend.model.Product;
import com.example.springbackend.repositories.ProductRepository;
import com.example.springbackend.services.ExcelService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin("http://localhost:8080")
@Controller
@RequestMapping("/api")
public class ExcelController {
    private final ExcelService excelService;
    private final ProductRepository productRepository;

    public ExcelController(ExcelService excelService,
                           ProductRepository productRepository) {
        this.excelService = excelService;
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

    @GetMapping("/products")
    public ResponseEntity<List<Product>> getAllProducts(@RequestParam(required = false)String name,@RequestParam(defaultValue = "0")int page,@RequestParam(defaultValue = "5") int size) {
        try {
            List<Product> products = excelService.getAllProducts();
            Pageable paging = PageRequest.of(page, size);

            if (products.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            else{
                Page<Product>productPage;
                if (name==null){
                    productPage = productRepository.findAll(paging);

                }
                else{
                    productPage = productRepository.searchProductsByNameIsContainingIgnoreCase(name,paging);
                }
                products = productPage.getContent();
                Map<String, Object> response = new HashMap<>();
                response.put("tutorials", products);
                response.put("currentPage", productPage.getNumber());
                response.put("totalItems", productPage.getTotalElements());
                response.put("totalPages", productPage.getTotalPages());
                return new ResponseEntity<>(products, HttpStatus.OK);
            }

        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
