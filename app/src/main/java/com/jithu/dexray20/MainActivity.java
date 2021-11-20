package com.jithu.dexray20;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private androidx.appcompat.widget.Toolbar toolbar;
    private TextView username;
    private TextView email;
    private de.hdodenhof.circleimageview.CircleImageView personimg;
    SharedPreferences preferences;
    Uri myUri;
    Uri imageuri;
    Bitmap bitmap;
    String img_str;
    String name="Abin";
    String emal="jayeshk451@gmail.com";
    String img="nono";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView bottonnav=findViewById(R.id.bottomnavigation);
        bottonnav.setOnNavigationItemSelectedListener(navlistner);
        toolbar=findViewById(R.id.toolbar);
        username=findViewById(R.id.personname);
        email=findViewById(R.id.personemail);
        personimg=findViewById(R.id.personimg);
        preferences=getSharedPreferences("Shared_pref",MODE_PRIVATE);

        name=preferences.getString("username","");
        emal=preferences.getString("email","");
        img=preferences.getString("uri","");

    username.setText(name);


            email.setText(emal);



    byte[] imageAsBytes = Base64.decode(img.getBytes(), Base64.DEFAULT);


    personimg.setImageBitmap(BitmapFactory.decodeByteArray(imageAsBytes,0, imageAsBytes.length));






        /*try {
            Bitmap bitmap= MediaStore.Images.Media.getBitmap(getContentResolver(),myUri);
            personimg.setImageBitmap(bitmap);
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        setSupportActionBar(toolbar);
        if(savedInstanceState==null){
            bottonnav.setSelectedItemId(R.id.home);
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.option,menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id=item.getItemId();
        preferences=getSharedPreferences("Shared_pref",MODE_PRIVATE);

        if(id==R.id.profile){
            SharedPreferences.Editor editor=preferences.edit();
            editor.putString("username",name);


            editor.putString("email",emal);



            editor.putString("uri",img);
            editor.apply();

            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);

            startActivity(intent);
            finish();

        }
        else if (id==R.id.aboutus){
            Intent intent = new Intent(getApplicationContext(), about_vehicle.class);
            startActivity(intent);

        }
        else if (id==R.id.dexray_te){
            Intent intent = new Intent(getApplicationContext(), dexray_team.class);
            startActivity(intent);

        }
        return super.onOptionsItemSelected(item);
    }



    private BottomNavigationView.OnNavigationItemSelectedListener navlistner=
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedfrageme=null;
                    switch (item.getItemId()){
                        case R.id.home:
                            selectedfrageme= new HomeFragment();

                            break;
                        case R.id.map:
                            selectedfrageme=new SearchFragment();
                            break;
                        case R.id.setting:
                            selectedfrageme= new FavoriteFragment();
                            break;

                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            selectedfrageme).commit();
                    return true;

                }
            };
}