package com.example.mymemory2;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.Layout;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.mymemory2.Bean.Tally;
import com.example.mymemory2.Database.TallyTB;
import com.example.mymemory2.Database.UserTB;
import com.example.mymemory2.Utils.ToastUtil;
import com.example.mymemory2.adapter.TallyAdapter;

import java.util.Calendar;
import java.util.List;

public class SearchRecordActivity extends AppCompatActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener {

    private TextView tv_date;
    private TextView tv_month;
    private LinearLayout ll_month;
    private LinearLayout ll_date;
    private Spinner sp_month_type;
    private Spinner sp_date_type;
    private TallyTB tallyTB;
    private ListView lv_search;
    private List<Tally> tallyList;
    private TallyAdapter adapter;
    private double sum;
    private TextView tv_show_money;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_record);

        //查看日收入时点击出现日期选择器年月日
        tv_date = findViewById(R.id.tv_date);
        tv_month = findViewById(R.id.tv_month);
        sp_month_type = findViewById(R.id.sp_month_type);
        sp_date_type = findViewById(R.id.sp_date_type);
        lv_search = findViewById(R.id.lv_search);
        tv_show_money = findViewById(R.id.tv_show_money);
        findViewById(R.id.btn_month).setOnClickListener(this);
        findViewById(R.id.btn_date).setOnClickListener(this);
        findViewById(R.id.btn_day_search).setOnClickListener(this);
        findViewById(R.id.btn_month_search).setOnClickListener(this);
        findViewById(R.id.btn_cal).setOnClickListener(this);
        tv_date.setOnClickListener(this);
        tv_month.setOnClickListener(this);
        ll_month = findViewById(R.id.ll_month);
        ll_date = findViewById(R.id.ll_date);
    }

    @Override
    protected void onStart() {
        super.onStart();
        tallyTB = new TallyTB(this);
        tallyTB.open();
    }

    @Override
    protected void onStop() {
        super.onStop();
        tallyTB.close();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            //按日期查询
            case R.id.btn_date:
                //切换为按日期查询后
                //清空listView和总金额中的内容
                lv_search.setAdapter(null);
                tv_show_money.setText("");
                //设置关于月份查询的不可见，日查询的可见
                ll_month.setVisibility(View.GONE);
                ll_date.setVisibility(View.VISIBLE);
                break;
            //按月份查询
            case R.id.btn_month:
                //切换为按月份查询后
                //清空listView和总金额内容
                lv_search.setAdapter(null);
                tv_show_money.setText("");
                //设置关于月份查询的不可见，日期查询的可见
                ll_month.setVisibility(View.VISIBLE);
                ll_date.setVisibility(View.GONE);
                break;
            //日期选择对话框
            case R.id.tv_date:
                Calendar calendar = Calendar.getInstance();
                DatePickerDialog dialog = new DatePickerDialog(this, this,
                        calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                dialog.show();
                break;
            //月份选择对话框
            case R.id.tv_month:
                final Calendar c = Calendar.getInstance();
                DatePickerDialog datePickerDialog = new DatePickerDialog(this, DatePickerDialog.THEME_HOLO_LIGHT, mDateSetListener,
                        c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
                DatePicker datePicker = datePickerDialog.getDatePicker();
                //隐藏月份
//                ((ViewGroup) ((ViewGroup) datePicker.getChildAt(0)).getChildAt(0)).getChildAt(1).setVisibility(View.GONE);
                ((ViewGroup) ((ViewGroup) datePicker.getChildAt(0)).getChildAt(0)).getChildAt(2).setVisibility(View.GONE);
                datePickerDialog.show();
                break;
            //按日期选择搜索按钮
            case R.id.btn_day_search:
                daySearch();
                break;
            //按月份选择搜索按钮
            case R.id.btn_month_search:
                monthSearch();
                break;
            //计算总金额
            case R.id.btn_cal:
                tv_show_money.setText(String.valueOf(sum));
                sum = 0;        //显示后总金额重置
                break;
        }
    }

    //按月份查询
    private void monthSearch() {
        String type = sp_month_type.getSelectedItem().toString();
        System.out.println(type);
        String month = tv_month.getText().toString();

        if (TextUtils.isEmpty(month)) {
            ToastUtil.show(this, "请选择月份");
            return;
        }
        tallyList = tallyTB.selectByMonthAndType(month, type);
        if (!tallyList.isEmpty()) {
            sum = 0;
            adapter = new TallyAdapter(this, tallyList);
            for(int i = 0; i < tallyList.size(); i++){
                Tally tally = tallyList.get(i);
                sum += tally.getTallyMoney();
            }
            lv_search.setAdapter(adapter);
        }
    }

    //按日期查询--日期和收支类型
    private void daySearch() {
        String type = sp_date_type.getSelectedItem().toString();
        String date = tv_date.getText().toString();

        if (TextUtils.isEmpty(date)) {
            ToastUtil.show(this, "请选择日期");
            return;
        }
        tallyList = tallyTB.selectByDateAndType(date, type);
        //非空说明查到了
        if (!tallyList.isEmpty()) {
            sum = 0;
            adapter = new TallyAdapter(this, tallyList);
            for(int i = 0; i < tallyList.size(); i++){
                Tally tally = tallyList.get(i);
                sum += tally.getTallyMoney();
            }
            lv_search.setAdapter(adapter);
        }
    }


    //日收支下选择日期并显示在TextView上
    @Override
    public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
        String month;
        if (monthOfYear + 1 < 10) {
            month = "0" + (monthOfYear + 1);
        } else month = String.valueOf(monthOfYear + 1);
        String day;
        if (dayOfMonth < 10) {
            day = "0" + dayOfMonth;
        } else day = String.valueOf(dayOfMonth);
        tv_date.setText(String.format(year + "-" + month + "-" + day));
    }

    //月收支下的月份选择并显示在TextView上
    private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            String month;
            if (monthOfYear + 1 < 10) {
                month = "0" + (monthOfYear + 1);
            } else month = String.valueOf(monthOfYear + 1);
            tv_month.setText(String.valueOf(year) + "-" + month);
        }
    };
}