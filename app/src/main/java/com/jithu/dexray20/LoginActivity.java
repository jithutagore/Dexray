package com.jithu.dexray20;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import android.util.Base64;

public class LoginActivity extends AppCompatActivity {
    private de.hdodenhof.circleimageview.CircleImageView imgaechange;
    private EditText username;
    private EditText email;
    private Button save;
    SharedPreferences sharedPreferences;
    private static final int pick_img=1;
    Uri imageuri;
    Bitmap bitmap;
    String img_str;
    String val1;
    String val2;
    String val3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        imgaechange=findViewById(R.id.change_photo);
        username=findViewById(R.id.username);
        email=findViewById(R.id.email);
        save=findViewById(R.id.register);
        sharedPreferences=getSharedPreferences("Shared_pref",MODE_PRIVATE);
        val1=sharedPreferences.getString("username","");
        val2=sharedPreferences.getString("email","");
        val3=sharedPreferences.getString("uri","");
        username.setText(val1);
        email.setText(val2);
        byte[] imageAsBytes = Base64.decode(val3.getBytes(), Base64.DEFAULT);
        imgaechange.setImageBitmap(BitmapFactory.decodeByteArray(imageAsBytes,0, imageAsBytes.length));


        imgaechange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galary =new Intent();
                galary.setType("Image/*");
                galary.setAction(Intent.ACTION_GET_CONTENT);

                startActivityForResult(Intent.createChooser(galary,"Select Picture"),pick_img);

            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imgaechange.buildDrawingCache();
                Bitmap bitmap = imgaechange.getDrawingCache();
                ByteArrayOutputStream stream=new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 90, stream);
                byte[] image=stream.toByteArray();
                //System.out.println("byte array:"+image);
                //final String img_str = "data:image/png;base64,"+ Base64.encodeToString(image, 0);
                //System.out.println("string:"+img_str);
                img_str = Base64.encodeToString(image, 0);

                 val1=username.getText().toString();
                val2=email.getText().toString();
                val3=img_str;
                SharedPreferences.Editor editor=sharedPreferences.edit();

                    editor.putString("username",val1);


                    editor.putString("email",val2);



                    editor.putString("uri",val3);





                editor.apply();
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();


            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        imageuri=data.getData();
        if (requestCode==pick_img){
            imageuri=data.getData();


            try {


               imgaechange.setImageURI(imageuri);
                imgaechange.buildDrawingCache();
                Bitmap bitmap = imgaechange.getDrawingCache();
                ByteArrayOutputStream stream=new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 90, stream);
                byte[] image=stream.toByteArray();
                //System.out.println("byte array:"+image);
                //final String img_str = "data:image/png;base64,"+ Base64.encodeToString(image, 0);
                //System.out.println("string:"+img_str);
                img_str = Base64.encodeToString(image, 0);




            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }


}