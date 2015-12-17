package com.forthpeople.housebill.model;

import com.orm.SugarRecord;
import com.orm.dsl.Unique;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Calendar;

/**
 * Created by dhiraj on 16-12-2015.
 */
public abstract class Bill extends SugarRecord {

    private long amount;
    private Date billDated;
    @Unique
    private String YEAR_MONTH;
    public static String YEAR_MONTH_FORMAT = "%d_%d";

    public Bill(){}

    public Bill(long amount, Date billDated) {
        this.amount = amount;
        this.billDated = billDated;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(billDated);
        YEAR_MONTH =  String.format(YEAR_MONTH_FORMAT, calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH));
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public Date getBillDated() {
        return billDated;
    }

    public void setBillDated(Date billDated) {
        this.billDated = billDated;
    }
}
