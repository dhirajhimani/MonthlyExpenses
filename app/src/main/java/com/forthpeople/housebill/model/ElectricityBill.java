package com.forthpeople.housebill.model;

import com.orm.dsl.Table;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by dhiraj on 16-12-2015.
 */
@Table
public class ElectricityBill extends Bill {

    private Long id;

    private long currentUnit;

    public ElectricityBill() {}

    public ElectricityBill(long lastMonthUnit, long currentUnit,
                           double perUnitAmount, Date billDated) {
        super(0, billDated);
        this.currentUnit = currentUnit;
        long amount = Math.round((currentUnit - lastMonthUnit) * perUnitAmount);
        setAmount(amount);
    }

    public ElectricityBill(long amount, Date billDated) {
        super(amount, billDated);
    }

    public long getCurrentUnit() {
        return currentUnit;
    }

    public void setCurrentUnit(long currentUnit) {
        this.currentUnit = currentUnit;
    }

}
