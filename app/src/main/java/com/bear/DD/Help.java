package com.bear.DD;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

public class Help extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        Button unfold_DD=findViewById(R.id.unfold_DD);
        Button unfold_bekey=findViewById(R.id.unfold_bekey);
        Button unfold_BOBO=findViewById(R.id.unfold_BOBO);
        TextView helptext_DD=findViewById(R.id.helptext_DD);
        TextView helptext_bekey=findViewById(R.id.helptext_bekey);
        TextView helptext_BOBO=findViewById(R.id.helptext_BOBO);
        unfold_DD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(unfold_DD.getText().toString().equals("展开")){
                    helptext_DD.setVisibility(View.VISIBLE);
                    unfold_DD.setText("收起");
                }else{
                    helptext_DD.setVisibility(View.GONE);
                    unfold_DD.setText("展开");
                }
            }
        });
        unfold_bekey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(unfold_bekey.getText().toString().equals("展开")){
                    helptext_bekey.setVisibility(View.VISIBLE);
                    unfold_bekey.setText("收起");
                }else{
                    helptext_bekey.setVisibility(View.GONE);
                    unfold_bekey.setText("展开");
                }
            }
        });
        unfold_BOBO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(unfold_BOBO.getText().toString().equals("展开")){
                    helptext_BOBO.setVisibility(View.VISIBLE);
                    unfold_BOBO.setText("收起");
                }else{
                    helptext_BOBO.setVisibility(View.GONE);
                    unfold_BOBO.setText("展开");
                }
            }
        });
    }
}