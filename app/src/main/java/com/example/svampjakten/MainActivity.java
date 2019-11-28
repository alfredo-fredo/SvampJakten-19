package com.example.svampjakten;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Location;
import android.location.LocationListener;
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
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback{

    MarkerOptions customMarker;
    boolean markerExist;
    Toolbar tl;
    ImageButton imgLeft, imgRight;
    DrawerLayout mDrawerLayout;
    //ActionBarDrawerToggle mToggle;
    View leftDrawer, rightDrawer;


    private LatLng myLatLng;

    FloatingActionButton fab;

    LocationListener locationListener;

    FirebaseDatabase firebaseDatabase;
    FirebaseUser firebaseUser;
    DatabaseReference myDbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseDatabase = FirebaseDatabase.getInstance();
        myDbRef = firebaseDatabase.getReference("coordinates");

        /*if(firebaseUser != null){
            mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        }*/

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) ==
                        PackageManager.PERMISSION_GRANTED) {
        } else{
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 2);
        }



        //

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);

        tl = findViewById(R.id.toolbar);
        setSupportActionBar(tl);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        fab = findViewById(R.id.fab);

        mDrawerLayout = findViewById(R.id.drawerLayout);
        leftDrawer = findViewById(R.id.leftDrawer);
        rightDrawer = findViewById(R.id.rightDrawer);
        imgLeft = findViewById(R.id.imgLeft);
        imgRight = findViewById(R.id.imgRight);

        //mDrawerLayout.setDrawerListener(mToggle);
        mDrawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(@NonNull View drawerView) {
                Animation anim = AnimationUtils.loadAnimation(getBaseContext(), R.anim.fast_fade_out);
                findViewById(R.id.ui_and_fragment).startAnimation(anim);
                findViewById(R.id.ui_and_fragment).setVisibility(View.GONE);
            }

            @Override
            public void onDrawerClosed(@NonNull View drawerView) {
                findViewById(R.id.ui_and_fragment).setVisibility(View.VISIBLE);
                Animation anim = AnimationUtils.loadAnimation(getBaseContext(), R.anim.fast_fade_in);
                findViewById(R.id.ui_and_fragment).startAnimation(anim);
            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });

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
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Intent startOver = new Intent(this, MainActivity.class);
            startActivity(startOver);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if(firebaseUser == null){
            //mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            hideUI();
            popLogin();
        }
    }

    void popLogin(){
        /*View loginView = findViewById(R.id.include_center_fragment);

        getSupportFragmentManager().beginTransaction().replace(R.id.include_center_fragment, new LoginFragment()).commit();

        Animation anim1 = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        Animation anim2 = AnimationUtils.loadAnimation(this, R.anim.scale_up);

        loginView.startAnimation(anim1);
        loginView.startAnimation(anim2);*/

        getSupportFragmentManager().beginTransaction().replace(R.id.include_center_fragment, new LoginFragment()).commit();
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {

        final GoogleMap mMap;
        mMap = googleMap;
        mMap.getUiSettings().setMyLocationButtonEnabled(false);
        mMap.setBuildingsEnabled(true);

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) ==
                        PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);

        }

        final float zoomLevel = (float) 16.0;

        try {
            boolean isSuccsess = googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.day_mode));
            if (!isSuccsess)
                Toast.makeText(this, "Maps Styles load fail", Toast.LENGTH_SHORT).show();
        }
        catch (Resources.NotFoundException ex){
            ex.printStackTrace();
        }

        /**
         * Asks users for location permission
         */

        /**
         * Getting the user current location
         */
        double latitude = 0.0;
        double longitude = 0.0;

        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            else{
                try {
                    Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();
                    mMap.setMyLocationEnabled(true);
                }catch (NullPointerException e){
                    e.printStackTrace();
                }
            }
        }

        myLatLng = new LatLng(latitude, longitude);

        if(latitude == 0.0 && longitude == 0.0){
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude,longitude), 2));
        }else{
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude,longitude), zoomLevel));
        }

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng destination) {

            if(firebaseUser != null){
                if(markerExist){
                    Log.d("victor","Marker exists alredy");
                    mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                        @Override
                        public boolean onMarkerClick(Marker marker) {
                            getSupportFragmentManager().beginTransaction().replace(R.id.include_center_fragment, new PinInfoFragment("Alfred", "God smak! Mysigt ställe!", null)).commit();
                            Animation anim = AnimationUtils.loadAnimation(getBaseContext(), R.anim.pin_info_animation);
                            findViewById(R.id.include_center_fragment).startAnimation(anim);
                            return true;
                        }
                    });
                }else {

                    getSupportFragmentManager().beginTransaction().replace(R.id.include_center_fragment, new CreatePinFragment()).commit();
                    customMarker = new MarkerOptions().position(new LatLng(destination.latitude,destination.longitude));
                    customMarker.icon(BitmapDescriptorFactory.fromResource(R.drawable.logo_pin));
                    mMap.addMarker(customMarker);
                    mMap.animateCamera(CameraUpdateFactory.zoomTo(zoomLevel));
                    myDbRef.child(firebaseUser.getUid()).setValue(customMarker.getPosition());
                    markerExist = true;
                }
            }
            }
        });


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myLatLng, zoomLevel));
                Log.d("myTag", myLatLng.latitude + " hello " + myLatLng.longitude);*/
                LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        Log.d("myTag", "Access denied");
                    }
                    else{
                        try {
                            Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), zoomLevel));
                            Log.d("myTag", "Move camera to location.");
                        }catch (NullPointerException e){
                            e.printStackTrace();
                        }
                    }
                }
            }
        });



        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                try {
                    myLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                    Log.d("myTag", myLatLng.latitude + " changed " + myLatLng.longitude);

                }catch (SecurityException e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };



    }

    private void hideUI(){
        findViewById(R.id.main_layout).setVisibility(View.GONE);
    }

    private void showUI(){
        findViewById(R.id.main_layout).setVisibility(View.VISIBLE);
    }

}