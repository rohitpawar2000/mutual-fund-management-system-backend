package com.user.user.entity.reqdto;


import lombok.Data;

import java.math.BigDecimal;

@Data
public class fundIdResp {

    private String  fund_name ;
    private BigDecimal nav ;
    private BigDecimal availablUnit ;

    public String getFund_name() {
        return fund_name;
    }

    public void setFund_name(String fund_name) {
        this.fund_name = fund_name;
    }

    public BigDecimal getNav() {
        return nav;
    }

    public void setNav(BigDecimal nav) {
        this.nav = nav;
    }

    public BigDecimal getAvailablUnit() {
        return availablUnit;
    }

    public void setAvailablUnit(BigDecimal availablUnit) {
        this.availablUnit = availablUnit;
    }
}
