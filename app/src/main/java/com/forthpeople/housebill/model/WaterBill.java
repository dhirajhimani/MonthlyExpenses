package com.forthpeople.housebill.model;

import com.orm.dsl.Table;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by dhiraj on 16-12-2015.
 */
@Table
public class WaterBill extends Bill {

    private Long id;

    public WaterBill(){}

    public WaterBill(long amount, Date billDated) {
        super(amount, billDated);
    }
}
