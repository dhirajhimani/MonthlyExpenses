package com.forthpeople.housebill;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.forthpeople.housebill.log.DLog;
import com.forthpeople.housebill.model.Bill;
import com.forthpeople.housebill.model.ElectricityBill;
import com.forthpeople.housebill.model.HouseBill;
import com.forthpeople.housebill.model.WaterBill;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

/**
 * Created by dhiraj on 16-12-2015.
 */
public class HouseBillActivity extends AppCompatActivity {

    private static final String TAG = "HouseBillActivity";
    LinearLayout fordate;
    EditText cur_mnth_unit, per_unit_charge, water_bill, house_rent, last_mnth_unit;
    TextView net_unit, electric_bill, total_bill, pickeddate;
    Button saveBill;
    ElectricityBill electricityBill;
    WaterBill waterBill;
    HouseBill houseBill;
    Calendar selectedCal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.housebill_dialog);
        initViews();
        assignListeners();
        defaultValues();
        calculateBill();
    }

    private void defaultValues() {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        selectedCal = c;
        showDate(year, month + 1, day);
        populateCurrentMonth();
        populateLastMonthBill();
    }

    private void populateCurrentMonth() {
        int year = selectedCal.get(Calendar.YEAR);
        int month = selectedCal.get(Calendar.MONTH);
        int day = selectedCal.get(Calendar.DAY_OF_MONTH);
        //Note month in the query points to last month
        DLog.d(TAG, "month = " + month + "");
        DLog.d(TAG, "year = " + year + "");

        String year_month = String.format(Bill.YEAR_MONTH_FORMAT, year, month);
        DLog.d(TAG, "Select * from ELECTRICITY_BILL where YEARMONTH = " + year_month);
        List<ElectricityBill> electricityBills = ElectricityBill.findWithQuery(ElectricityBill.class, "Select * from ELECTRICITY_BILL where YEARMONTH = ?", year_month);
        if (electricityBills != null && !electricityBills.isEmpty()) {
            ElectricityBill eb = electricityBills.get(0);
            cur_mnth_unit.setText(eb.getCurrentUnit() + "");
        } else {
            cur_mnth_unit.setText(0 + "");
        }

    }

    private void populateLastMonthBill() {
        int year = selectedCal.get(Calendar.YEAR);
        int month = selectedCal.get(Calendar.MONTH);
        int day = selectedCal.get(Calendar.DAY_OF_MONTH);
        //Note month in the query points to last month
        if (month == 0) {
            year--;
            month = 11;
        }else {
            month--;
        }
        DLog.d(TAG, "month = " + month + "");
        DLog.d(TAG, "year = " + year + "");

        String year_month = String.format(Bill.YEAR_MONTH_FORMAT, year, month);
        DLog.d(TAG, "Select * from ELECTRICITY_BILL where YEARMONTH = " + year_month);
        List<ElectricityBill> electricityBills = ElectricityBill.findWithQuery(ElectricityBill.class, "Select * from ELECTRICITY_BILL where YEARMONTH = ?", year_month);
        if (electricityBills != null && !electricityBills.isEmpty()) {
            ElectricityBill eb = electricityBills.get(0);
            last_mnth_unit.setText(eb.getCurrentUnit() + "");
        } else {
            last_mnth_unit.setText(0 + "");
        }

    }


    private void assignListeners() {
        fordate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //DateTime Picker
                int year = selectedCal.get(Calendar.YEAR);
                int month = selectedCal.get(Calendar.MONTH);
                int day = selectedCal.get(Calendar.DAY_OF_MONTH);

                new DatePickerDialog(HouseBillActivity.this, myDateListener, year, month, day).show();
            }
        });
        UpdateBillText updateBillText = new UpdateBillText();
        last_mnth_unit.addTextChangedListener(updateBillText);
        cur_mnth_unit.addTextChangedListener(updateBillText);
        per_unit_charge.addTextChangedListener(updateBillText);
        water_bill.addTextChangedListener(updateBillText);
        house_rent.addTextChangedListener(updateBillText);
        //
        saveBill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String current_unit = !TextUtils.isEmpty(cur_mnth_unit.getText()) ? cur_mnth_unit.getText().toString() : "0";
                long currentUnit = Long.parseLong(current_unit);
                long water_billL = Long.parseLong(!TextUtils.isEmpty(water_bill.getText()) ? water_bill.getText().toString() : "0");
                double electricityBillL = Double.parseDouble(!TextUtils.isEmpty(electric_bill.getText()) ? electric_bill.getText().toString() : "0");
                waterBill = new WaterBill(water_billL, selectedCal.getTime());
                electricityBill = new ElectricityBill((long)electricityBillL, selectedCal.getTime());
                electricityBill.setCurrentUnit(currentUnit);
                houseBill = new HouseBill(electricityBill, waterBill);

                houseBill.setAmount((long)Double.parseDouble(!TextUtils.isEmpty(total_bill.getText()) ? total_bill.getText().toString() : "0"));
                houseBill.save();
                finish();
            }
        });
    }

    class UpdateBillText implements TextWatcher {
        public void afterTextChanged(Editable s) {
            calculateBill();
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {}

    }


    private void calculateBill() {
        String current_unit = !TextUtils.isEmpty(cur_mnth_unit.getText()) ? cur_mnth_unit.getText().toString() : "0";
        long currentUnit = Long.parseLong(current_unit);
        long lastMonthUnit = Long.parseLong(!TextUtils.isEmpty(last_mnth_unit.getText()) ? last_mnth_unit.getText().toString() : "0");
        long netUnit = currentUnit - lastMonthUnit;
        net_unit.setText(netUnit + "");
        String perUnitS = !TextUtils.isEmpty(per_unit_charge.getText()) ? per_unit_charge.getText().toString() : "0";
        double perUnit = Double.parseDouble(perUnitS);
        double electricityBill = (netUnit * perUnit);
        electric_bill.setText(electricityBill + "");
        long waterBill = Long.parseLong(!TextUtils.isEmpty(water_bill.getText()) ? water_bill.getText().toString() : "0");
        long houseRent = Long.parseLong(!TextUtils.isEmpty(house_rent.getText()) ? house_rent.getText().toString() : "0");
        double totalBill = electricityBill + waterBill + houseRent;
        total_bill.setText(totalBill + "");
    }


    private void initViews() {
        fordate = (LinearLayout)findViewById(R.id.fordate);
        //
        cur_mnth_unit = (EditText)findViewById(R.id.cur_mnth_unit);
        per_unit_charge = (EditText)findViewById(R.id.per_unit_charge);
        water_bill = (EditText)findViewById(R.id.water_bill);
        house_rent = (EditText)findViewById(R.id.house_rent);
        last_mnth_unit = (EditText)findViewById(R.id.last_mnth_unit);
        //
        pickeddate = (TextView)findViewById(R.id.pickeddate);
        net_unit = (TextView)findViewById(R.id.net_unit);
        electric_bill = (TextView)findViewById(R.id.electric_bill);
        total_bill = (TextView)findViewById(R.id.total_bill);
        //
        saveBill = (Button)findViewById(R.id.saveBill);
        //
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private DatePickerDialog.OnDateSetListener myDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int day) {
            Calendar c = Calendar.getInstance();
            c.set(year, month, day);

            SimpleDateFormat sdf = new SimpleDateFormat(MainFragment.dateFormat);
            String formattedDate = sdf.format(c.getTime());
            selectedCal = c;
            showDate(year, month+1, day);
            populateCurrentMonth();
            populateLastMonthBill();
        }
    };

    private void showDate(int year, int month, int day) {
        pickeddate.setText(new StringBuilder().append(day).append("/")
                .append(month).append("/").append(year));
    }

}
