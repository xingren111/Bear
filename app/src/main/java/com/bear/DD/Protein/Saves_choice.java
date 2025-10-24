package com.bear.DD.Protein;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;

import com.bear.DD.R;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Saves_choice extends AppCompatActivity {
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saves_choice);


        File privatedir = getFilesDir();
        File saves = new File(privatedir.getAbsolutePath() + "/saves");
        if (!saves.exists()) {
            saves.mkdirs();
        }
        List<String> data = new ArrayList<String>();
        MyAdapter adapter = new MyAdapter(data);
        RecyclerView recyclerView = findViewById(R.id.saves_list);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        Button confirm = findViewById(R.id.confirm);
        Button newsave = findViewById(R.id.newsave);
        Button delete = findViewById(R.id.delete);
        adapter.delete=delete;
        System.out.println(saves.listFiles().length);
        List<File> savetofile=new ArrayList<File>();

        File[] files = saves.listFiles();
        if (files == null) return;

        Arrays.sort(files, (f1, f2) ->
                Long.compare(
                        Long.parseLong(f1.getName()), // 最新文件在前
                        Long.parseLong(f2.getName())  // 升序排序
                )
        );
        for (File f : files) {

            if (f != null) {
                Gson gson=new Gson();
                try {
                    BufferedReader brlevel=new BufferedReader(new InputStreamReader(new FileInputStream(new File(f,"level.json"))));
                    String line;
                    StringBuilder sb=new StringBuilder();
                    while((line= brlevel.readLine())!=null){
                        sb.append(line);
                    }
                    Level l= gson.fromJson(sb.toString(),Level.class);
                    adapter.addItem("第"+l.number+"关");
                    savetofile.add(f);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        newsave.setOnClickListener(v -> {
            long currenttimemillis=System.currentTimeMillis();
            File a_save = new File(saves,String.valueOf(currenttimemillis));

            a_save.mkdir();
            try {
                File filelevel = new File(a_save, "level.json");
                filelevel.createNewFile();
                File fileboss = new File(a_save, "boss.json");
                fileboss.createNewFile();
                System.out.println(saves.listFiles().length);

                Gson gson = new Gson();
                BufferedWriter boslevel = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filelevel)));
                Level l = new Level(1);
                boslevel.write(gson.toJson(l));
                boslevel.flush();
                boslevel.close();

                BufferedWriter bosboss = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileboss)));
                Boss b = new Boss();
                bosboss.write(gson.toJson(b));
                bosboss.flush();
                adapter.addItem("第1关", adapter.getItemCount());
                savetofile.add(a_save);
                bosboss.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        delete.setOnClickListener(v->{
            int position= adapter.selectposition;
            AlertDialog.Builder ab=new AlertDialog.Builder(this);
            ab.setMessage("确定要删除此存档吗？删除后不可恢复。");
            ab.setPositiveButton("确认", (dialog, which) -> {
                try {
                    Files.walk(Paths.get(savetofile.get(position).getAbsolutePath())).sorted(Comparator.reverseOrder()).forEach(path->{
                        try {
                            Files.delete(path);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
                savetofile.remove(position);
                adapter.removeItem(position);



            });
            ab.setNegativeButton("取消",(dialog,which)->{

            });
            ab.create().show();
        });
        confirm.setOnClickListener(v->{
            int position=adapter.selectposition;
            AlertDialog.Builder ab=new AlertDialog.Builder(this);
            ab.setMessage("确定开始此存档吗？");
            ab.setPositiveButton("确认",(dialog, which) -> {

            }).setNegativeButton("取消",(dialog, which) -> {

            }).create().show();
        });
    }
}