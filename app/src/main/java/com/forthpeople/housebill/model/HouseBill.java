package com.forthpeople.housebill.model;

import com.orm.SugarApp;
import com.orm.SugarRecord;
import com.orm.dsl.Ignore;
import com.orm.dsl.Table;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by dhiraj on 16-12-2015.
 */
@Table
public class HouseBill extends Bill {

    private Long id;
    @Ignore
    ElectricityBill electricityBill;
    @Ignore
    WaterBill waterBill;

     public HouseBill(){}

    public HouseBill(ElectricityBill electricityBill, WaterBill waterBill) {
        super(electricityBill.getAmount() + waterBill.getAmount(), electricityBill.getBillDated());
        this.electricityBill = electricityBill;
        this.waterBill = waterBill;
    }

    @Override
    public long save() {
        electricityBill.save();
        waterBill.save();
        return super.save();
    }
}
