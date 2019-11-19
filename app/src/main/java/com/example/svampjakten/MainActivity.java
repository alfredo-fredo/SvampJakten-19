package com.example.svampjakten;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {
    boolean markerExist;
    Toolbar tl;
    ImageButton imgLeft, imgRight;
    DrawerLayout mDrawerLayout;
    ActionBarDrawerToggle mToggle;
    View leftDrawer, rightDrawer;
    private GoogleMap mMap;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference myDbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseDatabase = FirebaseDatabase.getInstance();
        myDbRef = firebaseDatabase.getReference("coordinates");


        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 2);


        //

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);

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

    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if(currentUser == null){
            popLogin();
        }

    }

    void popLogin(){
        View loginView = findViewById(R.id.include_center_fragment);

        getSupportFragmentManager().beginTransaction().replace(R.id.include_center_fragment, new LoginFragment()).commit();

        Animation anim1 = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        Animation anim2 = AnimationUtils.loadAnimation(this, R.anim.scale_up);

        loginView.startAnimation(anim1);
        loginView.startAnimation(anim2);
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {

        //googleMap.setPadding(100, 1600, 100, 100);

        try {
            boolean isSuccsess = googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.style_json));
            if (!isSuccsess)
                Toast.makeText(this, "Maps Syles load fail", Toast.LENGTH_SHORT).show();
        }
        catch (Resources.NotFoundException ex){
            ex.printStackTrace();
        }

        mMap = googleMap;

        float zoomLevel = (float) 16.0;

        /**
         * Asks users for location permission
         */

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) ==
                        PackageManager.PERMISSION_GRANTED) {
                mMap.setMyLocationEnabled(true);
        } else {
        Log.d("myTag", "permission denied");
        }

        /**
         * Getting the user current location
         */
        double longitude = 0.0;
        double latitude = 0.0;

        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            else{
                Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                longitude = location.getLongitude();
                latitude = location.getLatitude();
                mMap.setMyLocationEnabled(true);
            }
        }
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude,longitude), zoomLevel));
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng destination) {

            FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

                if(markerExist){
                    Log.d("victor","Marker exists alredy");
                }else {
                    MarkerOptions options = new MarkerOptions();
                    Marker marker = mMap.addMarker(new MarkerOptions().position(destination).draggable(false).title("test"));
                    mMap.animateCamera(CameraUpdateFactory.zoomTo(17));
                    myDbRef.child(firebaseUser.getUid()).setValue(marker.getPosition());
                    markerExist = false;

                }

            }
        });


    }
}