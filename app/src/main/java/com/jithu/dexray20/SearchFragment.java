package com.jithu.dexray20;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class SearchFragment extends Fragment {
//private Button location;
    private androidx.appcompat.widget.AppCompatButton Station;
    private TextView receieved;

    SmsMessage messages[];
    String message_value;
    IntentFilter intentFilter;
    String ur;
    private BroadcastReceiver intentReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            ur=intent.getExtras().getString("message");

        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.fragment_search, container, false);
        //location=rootView.findViewById(R.id.loaction_track);
        Station=rootView.findViewById(R.id.charging_station);
        ((MainActivity) getActivity()).getSupportActionBar().hide();


       /* location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                //Intent intent= new Intent(Intent.ACTION_VIEW);
                //intent.setData(Uri.parse("https://www.google.com/maps/place/Kollam+Junction+Railway+Station+Terminal+2/@8.8870174,76.5910028,17z/data=!3m1!4b1!4m5!3m4!1s0x3b05fd43e996472b:0x8bdbd0e37cfda879!8m2!3d8.8870174!4d76.5931915"));
                //Intent choser=Intent.createChooser(intent,"Launch Map");
                if(ContextCompat.checkSelfPermission(getActivity().getApplicationContext(),Manifest.permission.SEND_SMS)==PackageManager.PERMISSION_GRANTED){
                    smsMsg(phoneNo,message);
                }
                else {
                    ActivityCompat.requestPermissions(getActivity(),new  String[] {Manifest.permission.SEND_SMS},100);
                }
                if(ContextCompat.checkSelfPermission(getActivity().getApplicationContext(),Manifest.permission.RECEIVE_SMS)==PackageManager.PERMISSION_GRANTED){
                    smsMsg(phoneNo,message);
                }



            }
        });*/
        Station.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://www.google.com/maps/search/nearest+charging+station"));
                Intent choser=Intent.createChooser(intent,"Launch Map");
                startActivity(choser);
            }
        });

    return rootView;
    }

}