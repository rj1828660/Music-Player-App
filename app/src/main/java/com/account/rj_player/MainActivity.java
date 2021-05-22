package com.account.rj_player;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    ListView listView;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView=findViewById(R.id.listView);
        Dexter.withContext(this)
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                             final ArrayList<File>res=fetchsongs(Environment.getExternalStorageDirectory());
                            String [] item= new String[res.size()];
                            for(int i=0;i<res.size();i++)
                                item[i]=res.get(i).getName().replace(".mp3","");
                           ArrayAdapter<String>adapter=new ArrayAdapter<>(MainActivity.this,android.R.layout.simple_expandable_list_item_1,item);
                           listView.setAdapter(adapter);

                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Intent intent=new Intent(MainActivity.this,PlaySong.class);
                                intent.putExtra("songlist",res);
                             //  intent.putExtra("currentsong",listView.getItemAtPosition(position).toString());
                                intent.putExtra("position",position);
                                startActivity(intent);
                            }
                        });
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                          permissionToken.continuePermissionRequest();
                    }
                })
                .check();


    }
        public ArrayList<File> fetchsongs(File  file)
        {   ArrayList<File>song=new ArrayList();
             File [] songs= file.listFiles();
               if(songs!=null)
               { for(File myfile:songs) {
                   if (myfile.isDirectory() && !myfile.isHidden()) {
                       song.addAll(fetchsongs(myfile));
                   } else {
                       if(myfile.getName().endsWith(".mp3")&&!myfile.getName().startsWith("."))
                           song.add(myfile);
                   }
               }
               }
               return song;

        }

}