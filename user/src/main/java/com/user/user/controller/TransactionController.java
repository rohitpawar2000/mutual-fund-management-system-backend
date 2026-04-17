package com.user.user.controller;

import com.user.user.entity.reqdto.BuyRequest;
import com.user.user.entity.reqdto.PortfolioResponse;
import com.user.user.entity.reqdto.SellRequest;
import com.user.user.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;


    @PostMapping("/buy")
    public ResponseEntity<String> buy(@RequestBody BuyRequest request) {
        transactionService.buy(request);
        return ResponseEntity.ok("Units purchased successfully");
    }

    @PostMapping("/sell")
    public ResponseEntity<String> sell(@RequestBody SellRequest request) {
        transactionService.sell(request);
        return ResponseEntity.ok("Units sold successfully");
    }

    @GetMapping("/portfolio")
    public ResponseEntity<List<PortfolioResponse>> getPortfolio() {
        return ResponseEntity.ok(transactionService.getPortfolio());
    }

}
