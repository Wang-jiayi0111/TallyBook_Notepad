package com.example.mymemory2;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import com.example.mymemory2.Bean.Tally;
import com.example.mymemory2.Database.TallyTB;
import com.example.mymemory2.Utils.ToastUtil;
import com.example.mymemory2.adapter.TallyAdapter;

import java.util.Calendar;
import java.util.List;
/**
 *
 * 记账本的添加（按钮）、删除（选中长按 或 选中点击删除按钮）、修改（选中修改后删除）
 * ListView数据更新问题（原本是刷新，但会产生新页面，点击返回会返回原本页面，若操作太多次，要返回很多次才能返回到主页
 * 使用adapter.notifyDataSetChanged()会根据list的变化进行更新
 * 所以添加、修改、删除也要对list进行操作
 * 添加实现的结果是最新添加的项只能在最上面显示，不能跟其他一样按日期排序，退出后再进入才能按日期排序，且刚添加的项目不能删除和修改，只能退出该页面再操作
 * 删除要根据点击的账目项的id在list中找到包含该id，然后再删除该项
 * 修改先删除list中对应项，再添加
 *
 */
public class ManageActivity extends AppCompatActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener{

    private EditText et_date;
    private TallyTB tallyTB;
    private Spinner sp_type;
    private EditText et_in_money;
    private EditText et_in_state;
    private ListView lv_record;
    List<Tally> list;
    private TallyAdapter adapter;
    private int tally_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage);

        lv_record = findViewById(R.id.lv_record);
        et_date = findViewById(R.id.et_date);
        sp_type = findViewById(R.id.sp_type);
        et_in_money = findViewById(R.id.et_in_money);
        et_in_state = findViewById(R.id.et_in_state);

        et_date.setOnClickListener(this);
        et_date.setInputType(InputType.TYPE_NULL);
        findViewById(R.id.btn_add).setOnClickListener(this);
        findViewById(R.id.btn_update).setOnClickListener(this);
        findViewById(R.id.btn_delete).setOnClickListener(this);

        initData();
    }

    private void initData() {
        tallyTB = new TallyTB(this);
        tallyTB.open();
        //账目项的显示
        showQueryData();
        //单击账目项修改
        lv_record.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            Tally tally;
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                tally = list.get(position);
                tally_id = tally.getId();
                et_date.setText(tally.getTallyTime());
                et_in_state.setText(tally.getTallyState());
                et_in_money.setText(String.valueOf(tally.getTallyMoney()));
                sp_selectItem();
            }

            //根据数据库存放的收入or支出设定类型的默认选择值
            public void sp_selectItem(){
                SpinnerAdapter apsAdapter = sp_type.getAdapter();
                if(tally.getTallyType().equals(apsAdapter.getItem(0).toString()))
                    sp_type.setSelection(0, true);
                else sp_type.setSelection(1, true);
                }
            });
        //长按删除
        lv_record.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int position, long id) {
                AlertDialog dialog;
                AlertDialog.Builder builder = new AlertDialog.Builder(ManageActivity.this)
                        .setMessage("是否删除此项？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Tally tally = list.get(position);
                                if(tallyTB.delete(tally.getId())){
                                    list.remove(position);
                                    adapter.notifyDataSetChanged();
                                    ToastUtil.show(ManageActivity.this, "删除成功");
                                }
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int i) {
                                dialog.dismiss();
                            }
                        });
                dialog = builder.create();
                dialog.show();
                return true;
            }
        });
    }

    private void showQueryData() {
        if(list != null){
            list.clear();
        }

        list = tallyTB.query();
        adapter = new TallyAdapter(this, list);
        lv_record.setAdapter(adapter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        tallyTB.close();
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.et_date:
                Calendar calendar = Calendar.getInstance();
                DatePickerDialog dialog = new DatePickerDialog(this, this,
                        calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                dialog.show();
                break;
            case R.id.btn_add:
                add();
                break;
            case R.id.btn_update:
                update();
                break;
            case R.id.btn_delete:
                delete();
                break;
        }
    }

    //账目删除实现方法
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void delete() {
        if(tallyTB.delete(tally_id)){
            ToastUtil.show(this, "删除成功！");
            list.removeIf(tally -> tally.getId() == tally_id);
            adapter.notifyDataSetChanged();
        }else
            ToastUtil.show(this, "删除失败。。");

    }

    //账目更新实现方法
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void update() {
        Tally tally1 = new Tally();
        String tallyState = et_in_state.getText().toString();
        String tallyType = sp_type.getSelectedItem().toString();
        double money = Double.parseDouble(et_in_money.getText().toString());
        String tallyDate = et_date.getText().toString();

        tally1.setTallyMoney(money);
        tally1.setTallyTime(tallyDate);
        tally1.setTallyType(tallyType);
        tally1.setTallyState(tallyState);
        if(tally_id > 0){
            if(tallyTB.update(tally_id, tallyDate, tallyType, money, tallyState)){
                ToastUtil.show(this, "修改成功");
                list.removeIf(tally -> tally.getId() == tally_id);
                list.add(0, tally1);
                adapter.notifyDataSetChanged();
            } else ToastUtil.show(this, "修改失败");
        }
    }

    //账目添加实现方法
    private void add() {
        String str_money = et_in_money.getText().toString();
        String state = et_in_state.getText().toString();
        String type = sp_type.getSelectedItem().toString();
        String date = et_date.getText().toString();
        Tally tally = new Tally();
        if(TextUtils.isEmpty(str_money) || TextUtils.isEmpty(state) || TextUtils.isEmpty(type)
         || TextUtils.isEmpty(date)){
            ToastUtil.show(this, "请输入完整账目信息");
            return;
        }
        double money = Double.parseDouble(str_money);
        tally.setTallyMoney(money);
        tally.setTallyTime(date);
        tally.setTallyType(type);
        tally.setTallyState(state);
        if(tallyTB.insert(tally)){
            ToastUtil.show(this, "添加成功！");
            list.add(0, tally);
            adapter.notifyDataSetChanged();
        }else {
            ToastUtil.show(this, "添加失败。。");
        }
    }

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
        et_date.setText(String.format(year + "-" + month + "-" + day));
    }
}