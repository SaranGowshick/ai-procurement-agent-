package com.ai.procurement.controller;

import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/procurement")
public class SupplierOnboardingController {

    @PostMapping("/addSupplier")
    public ResponseEntity<?> onBoardSupplier(RequestEntity<?> data){
        return ResponseEntity.ok(data);
    }

}
