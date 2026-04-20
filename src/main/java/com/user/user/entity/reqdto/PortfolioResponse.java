package com.user.user.entity.reqdto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PortfolioResponse {

    private String fundName;
    private BigDecimal totalUnits;
    private BigDecimal totalInvestment;
    private BigDecimal currentNav;
    private BigDecimal currentValue;
    private BigDecimal profitOrLoss;
    private BigDecimal navAtBuy ;


    public BigDecimal getNavAtBuy() {
        return navAtBuy;
    }

    public void setNavAtBuy(BigDecimal navAtBuy) {
        this.navAtBuy = navAtBuy;
    }

    public String getFundName() {
        return fundName;
    }

    public void setFundName(String fundName) {
        this.fundName = fundName;
    }

    public BigDecimal getTotalUnits() {
        return totalUnits;
    }

    public void setTotalUnits(BigDecimal totalUnits) {
        this.totalUnits = totalUnits;
    }

    public BigDecimal getTotalInvestment() {
        return totalInvestment;
    }

    public void setTotalInvestment(BigDecimal totalInvestment) {
        this.totalInvestment = totalInvestment;
    }

    public BigDecimal getCurrentNav() {
        return currentNav;
    }

    public void setCurrentNav(BigDecimal currentNav) {
        this.currentNav = currentNav;
    }

    public BigDecimal getCurrentValue() {
        return currentValue;
    }

    public void setCurrentValue(BigDecimal currentValue) {
        this.currentValue = currentValue;
    }

    public BigDecimal getProfitOrLoss() {
        return profitOrLoss;
    }

    public void setProfitOrLoss(BigDecimal profitOrLoss) {
        this.profitOrLoss = profitOrLoss;
    }
}
