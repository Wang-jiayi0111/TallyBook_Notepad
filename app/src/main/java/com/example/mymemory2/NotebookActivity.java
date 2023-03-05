package com.example.mymemory2;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import com.example.mymemory2.Database.NoteTB;
import com.example.mymemory2.Utils.ToastUtil;
import com.example.mymemory2.adapter.NotebookAdapter;
import com.example.mymemory2.Bean.NotebookBean;
import com.example.mymemory2.Database.NotepadDB.DatabaseHelper;

public class NotebookActivity extends Activity {
    ListView listView;
    List<NotebookBean> list;
    NotebookAdapter adapter;
    private NoteTB noteTB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notepad);
        //用于显示便签的列表
        listView = (ListView) findViewById(R.id.listview);
        ImageView add = (ImageView) findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NotebookActivity.this,
                        RecordActivity.class);
                startActivityForResult(intent, 1);
            }
        });
        initData();
    }


    protected void initData() {
        noteTB = new NoteTB(this);
        noteTB.open();
        showQueryData();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent,View view,int position,long id){
                NotebookBean notepadBean = list.get(position);
                Intent intent = new Intent(NotebookActivity.this, RecordActivity.class);
                intent.putExtra("id", notepadBean.getId());
                intent.putExtra("time", notepadBean.getNotebookTime()); //记录的时间
                intent.putExtra("content", notepadBean.getNotebookContent()); //记录的内容
                NotebookActivity.this.startActivityForResult(intent, 1);
                System.out.printf(notepadBean.getId());
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog dialog;
                AlertDialog.Builder builder = new AlertDialog.Builder( NotebookActivity.this)
                        .setMessage("是否删除此记录？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                NotebookBean notepadBean = list.get(position);
                                if(noteTB.deleteData(notepadBean.getId())){
                                    list.remove(position);
                                    adapter.notifyDataSetChanged();
                                    Toast.makeText(NotebookActivity.this,"删除成功",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                dialog =  builder.create();
                dialog.show();
                return true;
            }
        });

    }

    private void showQueryData(){
        if (list!=null){
            list.clear();
        }
        //从数据库中查询数据(保存的标签)
        list = noteTB.query();
        adapter = new NotebookAdapter(this, list);
        listView.setAdapter(adapter);
    }
    @Override
    protected void onActivityResult(int requestCode,int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==1&&resultCode==2){
            showQueryData();
        }
    }
}
