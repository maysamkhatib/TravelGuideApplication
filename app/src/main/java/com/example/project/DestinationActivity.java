package com.example.project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.Application;
import android.content.Intent;
import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapsSdkInitializedCallback;

public class DestinationActivity extends AppCompatActivity {
    boolean descShow = true, imgShow = true, mapShow = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_destination);
        MapsInitializer.initialize(this, MapsInitializer.Renderer.LATEST, new OnMapsSdkInitializedCallback() {
            @Override
            public void onMapsSdkInitialized(@NonNull MapsInitializer.Renderer renderer) {
                Log.d("TAG", "onMapsSdkInitialized: ");
            }
        });
        TextView cityName = findViewById(R.id.cityDest);
        TextView countryName = findViewById(R.id.countryDest);
        Button back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DestinationActivity.this, HomeScreen.class));
            }
        });
        cityName.setText(HomeScreen.travelDestination.getCity());
        countryName.setText(HomeScreen.travelDestination.getCountry());
        LinearLayout description = findViewById(R.id.seeDesc);
        DescriptionFragment descriptionFragment = new DescriptionFragment();
        ImageView arrow1 = findViewById(R.id.descArrow);
        description.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (descShow){
                    getSupportFragmentManager().beginTransaction().replace(R.id.frameL2, descriptionFragment).commit();
                    arrow1.setImageResource(R.drawable.arrow2);
                    descShow = false;
                }else {
                    getSupportFragmentManager().beginTransaction().remove(descriptionFragment).commit();
                    arrow1.setImageResource(R.drawable.arrow1);
                    descShow = true;
                }
            }
        });

        LinearLayout img = findViewById(R.id.seeImg);
        ImageFragment imageFragment = new ImageFragment();
        ImageView arrow2 = findViewById(R.id.imgArrow);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imgShow){
                    getSupportFragmentManager().beginTransaction().replace(R.id.frameL3, imageFragment).commit();
                    arrow2.setImageResource(R.drawable.arrow2);
                    imgShow = false;
                }else {
                    getSupportFragmentManager().beginTransaction().remove(imageFragment).commit();
                    arrow2.setImageResource(R.drawable.arrow1);
                    imgShow = true;
                }
            }
        });

        LinearLayout map = findViewById(R.id.seeMap);
        LocationFragment locationFragment = new LocationFragment();
        ImageView arrow3 = findViewById(R.id.mapArrow);
        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mapShow){
                    getSupportFragmentManager().beginTransaction().replace(R.id.frameL4, locationFragment).commit();
                    arrow3.setImageResource(R.drawable.arrow2);
                    mapShow = false;
                }else {
                    getSupportFragmentManager().beginTransaction().remove(locationFragment).commit();
                    arrow3.setImageResource(R.drawable.arrow1);
                    mapShow = true;
                }
            }
        });
    }

    /*@Override
    public void onMapsSdkInitialized(@NonNull MapsInitializer.Renderer renderer) {
        switch (renderer) {
            case LATEST:
                Log.d("MapsDemo", "The latest version of the renderer is used.");
                break;
            case LEGACY:
                Log.d("MapsDemo", "The legacy version of the renderer is used.");
                break;
        }
    }*/
}