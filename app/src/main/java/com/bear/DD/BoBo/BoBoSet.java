package com.bear.DD.BoBo;

import androidx.appcompat.app.AppCompatActivity;
import com.bear.DD.R;
import com.bear.DD.RollInt;
import com.google.gson.Gson;

import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class BoBoSet extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bo_bo_set);

        TextView bomaxbotnum=findViewById(R.id.bomaxplaynum);

        Gson gson=new Gson();
        Config config=new Config();
        AssetManager am = getAssets();
        StringBuilder sb = new StringBuilder();
        try {

            BufferedReader br = new BufferedReader(new InputStreamReader(am.open("Config.json")));

            String line = new String();
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            config=gson.fromJson(sb.toString(),Config.class);

        } catch (IOException e) {
            e.printStackTrace();
        }
        RollInt ri=new RollInt(1,config.maxbotnum);

        Button boboadd=findViewById(R.id.bobo_add);
        boboadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ri.add();
                bomaxbotnum.setText(String.valueOf(ri.getp()));
            }
        });

        Button bobosub=findViewById(R.id.bobo_sub);
        bobosub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ri.sub();
                bomaxbotnum.setText(String.valueOf(ri.getp()));
            }
        });

        Button BeginBoBo=findViewById(R.id.beginbobo);

        BeginBoBo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConstantPool.botnum=ri.getp();
                System.out.println("人机数："+ri.getp());
                Intent in=new Intent();
                in.setClass(BoBoSet.this,BoBoActivity.class);
                startActivity(in);
            }
        });
    }
}