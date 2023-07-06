package com.bear.DD;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

public class ClassicSettings extends AppCompatActivity {

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classic_settings);
        Button b_add=findViewById(R.id.classic_add);
        Button b_sub=findViewById(R.id.classic_sub);
        TextView t_maxplaynum=findViewById(R.id.maxplaynum);
        RollInt ri=new RollInt(1,5);
        b_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ri.add();
                t_maxplaynum.setText(String.valueOf(ri.getp()));
            }
        });
        b_sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ri.sub();
                t_maxplaynum.setText(String.valueOf(ri.getp()));
            }
        });
        Button beginclassic=findViewById(R.id.beginclassic);
        beginclassic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder buider=new AlertDialog.Builder(ClassicSettings.this);
                buider.setTitle("确定开始经典模式？");
                buider.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

//                        CheckBox c_isroundback=findViewById(R.id.isroundback);
                        ClassicSet.maxplayernum=ri.getp()+1;
//                        ClassicSet.isroundback=c_isroundback.isChecked();
                        ClassicSet.isroundback=false;
                        Intent in=new Intent();
                        in.setClass(ClassicSettings.this,Classic.class);
                        startActivity(in);
                    }
                });
                buider.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ;
                    }
                });
                AlertDialog al=buider.create();
                al.show();
            }
        });
    }
}