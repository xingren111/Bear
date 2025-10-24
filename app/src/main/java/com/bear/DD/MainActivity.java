package com.bear.DD;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.bear.DD.BoBo.BoBoActivity;
import com.bear.DD.BoBo.BoBoSet;
import com.bear.DD.ClassicDD.ClassicSettings;
import com.bear.DD.Protein.Saves_choice;

public class MainActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button BeginClassic=findViewById(R.id.Classic);
        BeginClassic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in=new Intent();
                in.setClass(MainActivity.this, ClassicSettings.class);
                startActivity(in);
            }
        });
        Button BeginBoBo=findViewById(R.id.bobo);
        BeginBoBo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in=new Intent();
                in.setClass(MainActivity.this, BoBoSet.class);
                startActivity(in);
            }
        });
        Button Protein=findViewById(R.id.protein);
        Protein.setOnClickListener(v->{
            Intent in=new Intent();
            in.setClass(MainActivity.this,Saves_choice.class);
            startActivity(in);
        });
        Button Help=findViewById(R.id.help);
        Help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in=new Intent();
                in.setClass(MainActivity.this, Help.class);
                startActivity(in);
            }
        });
    }
}