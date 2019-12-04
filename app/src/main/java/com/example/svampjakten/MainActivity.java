package com.example.svampjakten;

import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    MarkerOptions customMarker;
    long timeStamp = 1;
    long timeStampEnd = 0;
    boolean markerExist;
    Toolbar tl;
    ImageButton imgLeft, imgRight;
    DrawerLayout mDrawerLayout;
    //ActionBarDrawerToggle mToggle;
    View leftDrawer, rightDrawer;

    private int darkModes;
    private LatLng myLatLng;

    FloatingActionButton fab;

    LocationListener locationListener;

    FirebaseDatabase firebaseDatabase;
    FirebaseUser firebaseUser;
    DatabaseReference myDbRef;

    static String PINS_DB_REF = "Pins";
    private ImageView imageView;
    static final int TAKE_IMAGE_CODE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView =findViewById(R.id.imageView);


        //Getting data from SQLite
        getData();

        firebaseDatabase = FirebaseDatabase.getInstance();
        myDbRef = firebaseDatabase.getReference(PINS_DB_REF);


        /*if(firebaseUser != null){
            mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        }*/

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) ==
                        PackageManager.PERMISSION_GRANTED) {
        } else {
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
                SharedPreferences sp = getSharedPreferences("SWITCH_CHANGED", MODE_PRIVATE);
                Boolean switchStatus = sp.getBoolean("switchStatus", false);
                System.out.println(switchStatus + " <--");

                Animation anim = AnimationUtils.loadAnimation(getBaseContext(), R.anim.fast_fade_out);
                findViewById(R.id.ui_and_fragment).startAnimation(anim);
                findViewById(R.id.ui_and_fragment).setVisibility(View.GONE);

                Switch darkModeSwitch = findViewById(R.id.darkmode_switch);


                if (switchStatus) {
                    darkModeSwitch.setChecked(true);
                } else {
                    darkModeSwitch.setChecked(false);
                }

                darkModeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        SharedPreferences sp = getSharedPreferences("SWITCH_CHANGED", MODE_PRIVATE);
                        SharedPreferences.Editor spEdit = sp.edit();


                        if (isChecked) {

                            setData(1);
                            spEdit.putBoolean("switchStatus", true);
                            spEdit.commit();
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            finish();
                            startActivity(intent);

                        } else {
                            setData(0);
                            spEdit.putBoolean("switchStatus", false);
                            spEdit.commit();
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            finish();
                            startActivity(intent);
                        }
                    }
                });
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
                if (mDrawerLayout.isDrawerOpen(leftDrawer)) {
                    mDrawerLayout.closeDrawer(leftDrawer);
                } else {
                    mDrawerLayout.openDrawer(leftDrawer);
                }

                if (mDrawerLayout.isDrawerOpen(rightDrawer)) {
                    mDrawerLayout.closeDrawer(rightDrawer);
                }
            }
        });

        imgRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDrawerLayout.isDrawerOpen(rightDrawer)) {
                    mDrawerLayout.closeDrawer(rightDrawer);
                } else {
                    mDrawerLayout.openDrawer(rightDrawer);
                }

                if (mDrawerLayout.isDrawerOpen(leftDrawer)) {
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
        if (firebaseUser == null) {
            //mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            hideUI();
            popLogin();
        }
    }

    void popLogin() {
        /*View loginView = findViewById(R.id.include_center_fragment);

        getSupportFragmentManager().beginTransaction().replace(R.id.include_center_fragment, new LoginFragment()).commit();

        Animation anim1 = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        Animation anim2 = AnimationUtils.loadAnimation(this, R.anim.scale_up);

        loginView.startAnimation(anim1);
        loginView.startAnimation(anim2);*/

        getSupportFragmentManager().beginTransaction().replace(R.id.include_center_fragment, new LoginFragment()).commit();
    }

    private GoogleMap mMap;

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        mMap.getUiSettings().setMyLocationButtonEnabled(false);
        mMap.setBuildingsEnabled(true);

        DatabaseReference pinsRef = firebaseDatabase.getReference("Pins");

        Log.d("myTag", "pinlist Created");
        pinsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot dataValues : dataSnapshot.getChildren()) {
                    Pin myPin = dataValues.child("pin").getValue(Pin.class);
                    try {
                        MarkerOptions pinMarker = new MarkerOptions().position(new LatLng(myPin.pinLocation.latitude, myPin.pinLocation.longitude));
                        pinMarker.icon(BitmapDescriptorFactory.fromResource(R.drawable.logo_pin));
                        mMap.addMarker(pinMarker);
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        if (darkModes == 1) {

            try {
                boolean isSuccsess = mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.night_mode));
                if (!isSuccsess) {
                    Toast.makeText(this, "Maps Styles load fail", Toast.LENGTH_SHORT).show();
                }
            } catch (Resources.NotFoundException ex) {
                ex.printStackTrace();
            }
        } else {
            try {
                boolean isSuccsess = googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.day_mode));
                if (!isSuccsess)
                    Toast.makeText(this, "Maps Styles load fail", Toast.LENGTH_SHORT).show();
            } catch (Resources.NotFoundException ex) {
                ex.printStackTrace();
            }
        }

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) ==
                        PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);

        }

        final float zoomLevel = (float) 16.0;


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
            } else {
                try {
                    Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();
                    mMap.setMyLocationEnabled(true);
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }
        }

        myLatLng = new LatLng(latitude, longitude);

        if (latitude == 0.0 && longitude == 0.0) {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 2));
        } else {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), zoomLevel));
        }


        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng destination) {

                if (firebaseUser != null) {
                    if (timeStamp < timeStampEnd) {

                        Log.d("victor", "Marker exists alredy");
                        long testMilli = Math.abs(timeStampEnd - timeStamp);
                        long testMilliToSec = testMilli / 1000;
                        Log.d("victor", "kvar" + testMilliToSec);
                        Toast.makeText(getApplicationContext(), getApplicationContext().getString(R.string.please_wait) + testMilliToSec + getString(R.string.time_until), Toast.LENGTH_LONG).show();

                        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                            @Override
                            public boolean onMarkerClick(Marker marker) {
                                getSupportFragmentManager().beginTransaction().replace(R.id.include_center_fragment, new PinInfoFragment("Alfred", "God smak! Mysigt ställe!", null)).commit();
                                Animation anim = AnimationUtils.loadAnimation(getBaseContext(), R.anim.pin_info_animation);
                                findViewById(R.id.include_center_fragment).startAnimation(anim);
                                return true;
                            }
                        });


                    } else {

                        timeStampEnd = System.currentTimeMillis() + 10000;

                        getSupportFragmentManager().beginTransaction().replace(R.id.include_center_fragment, new CreatePinFragment()).commit();
                        customMarker = new MarkerOptions().position(new LatLng(destination.latitude, destination.longitude));
                        customMarker.icon(BitmapDescriptorFactory.fromResource(R.drawable.logo_pin));
                        mMap.addMarker(customMarker);
                        mMap.animateCamera(CameraUpdateFactory.zoomTo(zoomLevel));
                        myDbRef.push().setValue(new Pin(new PinLocation(customMarker.getPosition().latitude, customMarker.getPosition().longitude), "Makidonken", firebaseUser.getUid(), 4.3)).addOnFailureListener(new OnFailureListener() {

                            @Override
                            public void onFailure(@NonNull Exception e) {
                                myDbRef.child(firebaseUser.getUid()).setValue("failures!!!");
                            }

                        });
                    }
                    timeStamp = System.currentTimeMillis();
                    Log.d("victor", "" + new Date(timeStamp));
                    Log.d("victor", "" + new Date(timeStampEnd));


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
                    } else {
                        try {
                            Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), zoomLevel));
                            Log.d("myTag", "Move camera to location.");
                        } catch (NullPointerException e) {
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

                } catch (SecurityException e) {
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

    private void hideUI() {
        findViewById(R.id.main_layout).setVisibility(View.GONE);
    }

    private void showUI() {
        findViewById(R.id.main_layout).setVisibility(View.VISIBLE);
    }


    public void setData(int darkmodeStatus) {
        FeedReaderDbHelper dbHelper = new FeedReaderDbHelper(getApplicationContext());
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(FeedReaderContract.FeedEntry.DARKMODE, darkmodeStatus);

        db.insert(FeedReaderContract.FeedEntry.TABLE_NAME, null, values);
    }

    public void getData() {
        FeedReaderDbHelper dbHelper = new FeedReaderDbHelper(getApplicationContext());

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] projection = {
                BaseColumns._ID,
                FeedReaderContract.FeedEntry.DARKMODE,
        };

        Cursor cursor = db.query(
                FeedReaderContract.FeedEntry.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null,
                null
        );
        List itemIds = new ArrayList<>();
        while (cursor.moveToNext()) {
            long itemId = cursor.getLong(
                    cursor.getColumnIndexOrThrow(FeedReaderContract.FeedEntry._ID));
            int darkMode = cursor.getInt(
                    cursor.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.DARKMODE)
            );
            System.out.println(darkMode + " <-- ");
            itemIds.add(itemId);


            darkModes = darkMode;
        }
        cursor.close();
    }

    //Kod för att ladda upp en ny profilbild med kameran /Anton
    public void handelImageClick(View view) {

        Intent intentCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intentCamera.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intentCamera, TAKE_IMAGE_CODE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == TAKE_IMAGE_CODE) {
            switch (requestCode) {
                case RESULT_OK:
                    Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                    imageView.setImageBitmap(bitmap);
                    handleUpload(bitmap);
            }
        }
    }

    private void handleUpload(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        final StorageReference reference = FirebaseStorage.getInstance().getReference().child("profilImages").child(uid + ".jpeg");

        //Uri uri = data.getData();
        //StorageReference filepath = reference.child("ProfileImage").child(uri.getLastPathSegment());
        reference.putBytes(baos.toByteArray()).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            /* filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                 @Override
                 public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                     getDownloadUrl(reference);
                 }
             });
         }*/
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                getDownloadUrl(reference);
                Log.d("Anton", "onSuccess: ");
                Toast.makeText(MainActivity.this,"Picture uploaded!",Toast.LENGTH_LONG);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("Anton", "onFailure: ", e.getCause());
                Toast.makeText(MainActivity.this,"Uploaded failed!",Toast.LENGTH_LONG);
            }
        });
    }


    private void getDownloadUrl(StorageReference reference) {
        reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Log.d("Anton", "onSuccess: " + uri);
                setUserProfileUrl(uri);
            }
        });

    }

    private void setUserProfileUrl(Uri uri) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        UserProfileChangeRequest request = new UserProfileChangeRequest.Builder().setPhotoUri(uri).build();

        user.updateProfile(request).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(MainActivity.this, "Profile image uploaded", Toast.LENGTH_SHORT);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MainActivity.this, "Profile image failed...", Toast.LENGTH_SHORT);
            }
        });


    }
}