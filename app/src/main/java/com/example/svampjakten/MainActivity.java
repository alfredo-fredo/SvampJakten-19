package com.example.svampjakten;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity {

    Toolbar tl;
    ImageButton imgLeft, imgRight;
    DrawerLayout mDrawerLayout;
    ActionBarDrawerToggle mToggle;
    View leftDrawer, rightDrawer;

    boolean mustLogin = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tl = findViewById(R.id.toolbar);
        setSupportActionBar(tl);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        mDrawerLayout = findViewById(R.id.drawerLayout);
        leftDrawer = findViewById(R.id.leftDrawer);
        rightDrawer = findViewById(R.id.rightDrawer);
        imgLeft = findViewById(R.id.imgLeft);
        imgRight = findViewById(R.id.imgRight);
        mDrawerLayout.setDrawerListener(mToggle);

        imgLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mDrawerLayout.isDrawerOpen(leftDrawer)){
                    mDrawerLayout.closeDrawer(leftDrawer);
                }
                else{
                    mDrawerLayout.openDrawer(leftDrawer);
                }

                if(mDrawerLayout.isDrawerOpen(rightDrawer)){
                    mDrawerLayout.closeDrawer(rightDrawer);
                }
            }
        });

        imgRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mDrawerLayout.isDrawerOpen(rightDrawer)){
                    mDrawerLayout.closeDrawer(rightDrawer);
                }
                else{
                    mDrawerLayout.openDrawer(rightDrawer);
                }

                if(mDrawerLayout.isDrawerOpen(leftDrawer)){
                    mDrawerLayout.closeDrawer(leftDrawer);
                }
            }
        });

        if(mustLogin){
            popLogin();
        }

    }


    void popLogin(){
        View loginView = findViewById(R.id.include_login);
        loginView.setVisibility(View.VISIBLE);
        Animation anim1 = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        Animation anim2 = AnimationUtils.loadAnimation(this, R.anim.scale_up);

        loginView.startAnimation(anim1);
        loginView.startAnimation(anim2);
    }
}
