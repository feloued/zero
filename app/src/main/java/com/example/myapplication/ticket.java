package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;


public class ticket extends AppCompatActivity implements View.OnClickListener {
private ImageView img;
private Button download;
private String ticket;
DownloadManager downloadManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket);
        download=findViewById(R.id.download);
        download.setOnClickListener(this);
        img=findViewById(R.id.imgQR);
        img.setBackgroundResource(R.drawable.ticket_download);

        Intent intent = getIntent();
        if (intent != null){
            String str = "";
            if (getIntent().hasExtra("ticket_name")){ // vérifie qu'une valeur est associée à la clé “edittext”
                ticket= getIntent().getStringExtra("ticket_name"); // on récupère la valeur associée à la clé
                //Toast.makeText(getApplicationContext(),email,Toast.LENGTH_LONG).show();
            }
        }

    }

    @Override
    public void onClick(View v) {
        if(v==download)
        {
downloadManager=(DownloadManager)getSystemService(Context.DOWNLOAD_SERVICE);
            Uri uri=Uri.parse("http://192.168.1.100:8000/download_ticket/"+ticket);
          DownloadManager.Request request=new DownloadManager.Request(uri);
          request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
          Long reference=downloadManager.enqueue(request);
        }
    }
}
