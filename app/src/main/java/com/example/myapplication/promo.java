package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ViewFlipper;

public class promo extends AppCompatActivity implements View.OnClickListener {
private ViewFlipper flipper;
private ImageView prev;
private ImageView next;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promo);
        flipper=findViewById(R.id.flipper);
        prev=findViewById(R.id.prev);
        next=findViewById(R.id.next);
        prev.setOnClickListener(this);
        next.setOnClickListener(this);
        Animation in = AnimationUtils.loadAnimation(this, android.R.anim.fade_in);
        Animation out = AnimationUtils.loadAnimation(this, android.R.anim.slide_out_right);
        flipper.setAnimation(in);
    }

    @Override
    public void onClick(View v) {
        if(v==prev)
        {
            flipper.showPrevious();
        }
        else if(v==next)
        {
            flipper.showNext();
        }
    }
}
