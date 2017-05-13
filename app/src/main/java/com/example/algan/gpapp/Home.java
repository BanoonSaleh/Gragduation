package com.example.algan.gpapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ViewFlipper;

import com.example.algan.gpapp.helper.SessionManager;


public class Home extends AppCompatActivity {
    Animation fade_in, fade_out;
    ViewFlipper viewFlipper;
    SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        final Button Login=(Button) findViewById(R.id.login);
        final  Button sighup=(Button) findViewById(R.id.signup);
        final Button Search=(Button) findViewById(R.id.search);
        session = new SessionManager(getApplicationContext());


        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home.this,MainActivity.class);
                startActivity(intent);
            }
        });

        sighup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home.this,RegisterActivity.class);
                startActivity(intent);
            }
        });

        Search.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
if(session.isLoggedIn()== true)
{  Intent intent = new Intent(Home.this, Search.class);
                    startActivity(intent);}
else
{
    Intent intent = new Intent(Home.this, SearchForm.class);
    startActivity(intent);
}
            }

        });
        viewFlipper = (ViewFlipper) this.findViewById(R.id.bckgrndViewFlipper1);
        fade_in = AnimationUtils.loadAnimation(this,
                android.R.anim.fade_in);
        fade_out = AnimationUtils.loadAnimation(this,
                android.R.anim.fade_out);
        viewFlipper.setInAnimation(fade_in);
        viewFlipper.setOutAnimation(fade_out);
        viewFlipper.setAutoStart(true);
        viewFlipper.setFlipInterval(5000);
        viewFlipper.startFlipping();
    }
}
