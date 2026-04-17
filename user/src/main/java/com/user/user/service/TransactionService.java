package com.user.user.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.user.user.entity.*;
import com.user.user.entity.reqdto.BuyRequest;
import com.user.user.entity.reqdto.PortfolioResponse;
import com.user.user.entity.reqdto.SellRequest;
import com.user.user.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransactionService {


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FundRepository fundRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private PortfolioRepository portfolioRepository;

    @Autowired
    private AuditLogRepository auditLogRepository;

    @Autowired
    private ObjectMapper objectMapper;


    @Transactional
    public void buy(BuyRequest request){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Fund fund = fundRepository.findById(request.getFundId())
                .orElseThrow(() -> new RuntimeException("Fund not found"));

        //  Get fund
        BigDecimal nav = fund.getNav();

        //  Calculate units
        BigDecimal units = request.getAmount()
                .divide(nav, 4, RoundingMode.HALF_UP);


        //  Idempotency check
        if (transactionRepository.existsByIdempotencyKey(request.getIdempotencyKey())) {
            throw new RuntimeException("Duplicate transaction request");
        }

        //  Save transaction
        Transaction txn = new Transaction();

        txn.setUser(user);
        txn.setFund(fund);
        txn.setType("BUY");
        txn.setUnits(units);
        txn.setAmount(request.getAmount());
        txn.setNavAtTransaction(nav);
        txn.setIdempotencyKey(request.getIdempotencyKey());
        txn.setCreatedAt(LocalDateTime.now());

        transactionRepository.save(txn);

        // Update portfolio
        Portfolio portfolio = portfolioRepository
                .findByUserAndFund(user, fund)
                .orElseGet(() -> {
                    Portfolio p = new Portfolio();
                    p.setUser(user);
                    p.setFund(fund);
                    p.setTotalUnits(BigDecimal.ZERO);
                    p.setTotalInvestment(BigDecimal.ZERO);
                    return p;
                });


        portfolio.setTotalUnits(
                portfolio.getTotalUnits().add(units)
        );

        portfolio.setTotalInvestment(
                portfolio.getTotalInvestment().add(request.getAmount())
        );

        portfolio.setLastUpdated(LocalDateTime.now());

        portfolioRepository.save(portfolio);

        String requestJson;
        try {
            requestJson = objectMapper.writeValueAsString(request);
        } catch (Exception e) {
            requestJson = "ERROR_CONVERTING_REQUEST";
        }

        AuditLog log = new AuditLog();
        log.setUserId(user.getId());
        log.setAction("BUY");
        log.setRequestData(requestJson);
        log.setCreatedAt(LocalDateTime.now());

        auditLogRepository.save(log);


    }


    @Transactional
    public void sell(SellRequest request){

        //  Get logged-in user
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        //  Get fund
        Fund fund = fundRepository.findById(request.getFundId())
                .orElseThrow(() -> new RuntimeException("Fund not found"));

        BigDecimal nav = fund.getNav();

        //  Get portfolio
        Portfolio portfolio = portfolioRepository
                .findByUserAndFund(user, fund)
                .orElseThrow(() -> new RuntimeException("No holdings found"));

        //  Validate units
        if (request.getUnits().compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Units must be greater than zero");
        }

        if (portfolio.getTotalUnits().compareTo(request.getUnits()) < 0) {
            throw new RuntimeException("Insufficient units to sell");
        }

        //  Idempotency check
        if (transactionRepository.existsByIdempotencyKey(request.getIdempotencyKey())) {
            throw new RuntimeException("Duplicate transaction request");
        }

        //  Calculate amount
        BigDecimal amount = request.getUnits()
                .multiply(nav)
                .setScale(2, RoundingMode.HALF_UP);

        //  Save transaction
        Transaction txn = new Transaction();
        txn.setUser(user);
        txn.setFund(fund);
        txn.setType("SELL");
        txn.setUnits(request.getUnits());
        txn.setAmount(amount);
        txn.setNavAtTransaction(nav);
        txn.setIdempotencyKey(request.getIdempotencyKey());
        txn.setCreatedAt(LocalDateTime.now());

        transactionRepository.save(txn);

        //  Update portfolio
        portfolio.setTotalUnits(
                portfolio.getTotalUnits().subtract(request.getUnits())
        );

        // Optional: adjust investment proportionally (basic approach)
        BigDecimal investmentReduction = portfolio.getTotalInvestment()
                .multiply(request.getUnits())
                .divide(portfolio.getTotalUnits().add(request.getUnits()), 2, RoundingMode.HALF_UP);

        portfolio.setTotalInvestment(
                portfolio.getTotalInvestment().subtract(investmentReduction)
        );

        portfolio.setLastUpdated(LocalDateTime.now());

        portfolioRepository.save(portfolio);

        String requestJson;
        try {
            requestJson = objectMapper.writeValueAsString(request);
        } catch (Exception e) {
            requestJson = "ERROR_CONVERTING_REQUEST";
        }
        AuditLog log = new AuditLog();
        log.setUserId(user.getId());
        log.setAction("SELL");
        log.setRequestData(requestJson);
        log.setCreatedAt(LocalDateTime.now());

        auditLogRepository.save(log);
    }

    public List<PortfolioResponse> getPortfolio() {

        //  Get logged-in user
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        //  Fetch all portfolio entries for user
        List<Portfolio> portfolios = portfolioRepository.findByUser(user);

        //  Map to response
        return portfolios.stream().map(p -> {

            Fund fund = p.getFund();
            BigDecimal nav = fund.getNav();

            // Current value = units × NAV
            BigDecimal currentValue = p.getTotalUnits()
                    .multiply(nav)
                    .setScale(2, RoundingMode.HALF_UP);

            // Profit/Loss
            BigDecimal profitOrLoss = currentValue.subtract(p.getTotalInvestment());

            PortfolioResponse response = new PortfolioResponse();
            response.setFundName(fund.getFundName());
            response.setTotalUnits(p.getTotalUnits());
            response.setTotalInvestment(p.getTotalInvestment());
            response.setCurrentNav(nav);
            response.setCurrentValue(currentValue);
            response.setProfitOrLoss(profitOrLoss);

            return response;

        }).collect(Collectors.toList());
    }


}
