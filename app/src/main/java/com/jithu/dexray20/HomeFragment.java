package com.jithu.dexray20;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;


public class HomeFragment extends Fragment {

    private UUID mDeviceUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private BluetoothAdapter mBTAdapter;
    private BluetoothSocket mBTSocket;
    String phoneNo = "9895786840";
    String message = "Data Sented";
    private static final int BT_ENABLE_REQUEST = 10;
    private TextView mTextViewPercentage;
    private androidx.appcompat.widget.AppCompatButton location;
    private ProgressBar mProgressBar;
    private ImageView bluetoothicon;
    private Switch powerbutton;
    private TextView bluetoothtxt;
    private static final int Request_enable=0;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =inflater.inflate(R.layout.fragment_home, container, false);
        ((MainActivity) getActivity()).getSupportActionBar().show();
        location=rootView.findViewById(R.id.loaction_track);
        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                //Intent intent= new Intent(Intent.ACTION_VIEW);
                //intent.setData(Uri.parse("https://www.google.com/maps/place/Kollam+Junction+Railway+Station+Terminal+2/@8.8870174,76.5910028,17z/data=!3m1!4b1!4m5!3m4!1s0x3b05fd43e996472b:0x8bdbd0e37cfda879!8m2!3d8.8870174!4d76.5931915"));
                //Intent choser=Intent.createChooser(intent,"Launch Map");
                if(ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.SEND_SMS)== PackageManager.PERMISSION_GRANTED){
                    smsMsg(phoneNo,message);
                }
                else {
                    ActivityCompat.requestPermissions(getActivity(),new  String[] {Manifest.permission.SEND_SMS},100);
                }
                if(ContextCompat.checkSelfPermission(getActivity().getApplicationContext(),Manifest.permission.RECEIVE_SMS)==PackageManager.PERMISSION_GRANTED){
                    smsMsg(phoneNo,message);
                }



            }
        });

        new ReadInput();

        return rootView;
    }
    protected void smsMsg(String number,String message){
        SmsManager sms=SmsManager.getDefault();
        sms.sendTextMessage(number,null,message,null,null);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull  int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode==100 && grantResults.length >0 && grantResults[0]== PackageManager.PERMISSION_GRANTED){
            smsMsg(phoneNo,message);
        }
        else {
            Toast.makeText(getActivity().getApplicationContext(),"Permission Denied",Toast.LENGTH_SHORT).show();
        }
    }

    private class ReadInput implements Runnable {
        private boolean bStop = false;
        private Thread t;

        public ReadInput() {
            t = new Thread(this, "Input Thread");
            t.start();
        }

        public boolean isRunning() {
            return t.isAlive();
        }
        @Override
        public void run(){
            mBTAdapter = BluetoothAdapter.getDefaultAdapter();
            if(!mBTAdapter.isEnabled()){
                Intent intent= new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(intent,Request_enable);
            }
            else {
                System.out.println(mBTAdapter.getBondedDevices());
                BluetoothDevice hc05=mBTAdapter.getRemoteDevice("00:18:E4:40:00:06");
                System.out.println(hc05.getName());
                mBTSocket=null;
                String on="5";
                String off="10";
                int counter=0;
                do {

                    try {
                        mBTSocket = hc05.createRfcommSocketToServiceRecord(mDeviceUUID);
                        System.out.println(mBTSocket);
                        mBTSocket.connect();
                        System.out.println("isconnected");
                        System.out.println(mBTSocket.isConnected());
                        bluetoothicon=(ImageView) getView().findViewById(R.id.bluetooth);
                        bluetoothtxt=(TextView) getView().findViewById(R.id.bluetoothconect);

                        if(mBTSocket.isConnected()){
                            bluetoothicon.setImageResource(R.drawable.bluetooth_on);
                            bluetoothtxt.post(new Runnable() {
                                @Override
                                public void run() {



                                    bluetoothtxt.setText("Dexray is Connected");
                                }
                            });

                        }
                        else if(!mBTSocket.isConnected()) {
                            bluetoothicon.setImageResource(R.drawable.bluetooth_off);
                            bluetoothtxt.post(new Runnable() {
                                @Override
                                public void run() {



                                    bluetoothtxt.setText("Dexray is not Connected");
                                }
                            });
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        counter++;
                    }
                }while (!mBTSocket.isConnected() && counter<100);


                //sent data code to edit
                powerbutton= (Switch) getView().findViewById(R.id.lock);

                    powerbutton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if(powerbutton.isChecked()){
                                try {
                                mBTSocket.getOutputStream().write(on.getBytes());
                            } catch (IOException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                            }
                            else if(!powerbutton.isChecked()){
                                try {
                                mBTSocket.getOutputStream().write(off.getBytes());
                        } catch (IOException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                            }
                        }
                    });




                InputStream inputStream;
                try {
                    inputStream = mBTSocket.getInputStream();
                    while (!bStop) {
                        byte[] buffer = new byte[256];
                        if (inputStream.available() > 0) {
                            inputStream.read(buffer);
                            int i = 0;
                            /*
                             * This is needed because new String(buffer) is taking the entire buffer i.e. 256 chars on Android 2.3.4 http://stackoverflow.com/a/8843462/1287554
                             */
                            for (i = 0; i < buffer.length && buffer[i] != 0; i++) {
                            }
                            String strInput = new String(buffer, 0, i);
                            System.out.println(strInput);
                            String[] result=strInput.split(" ");

                            mTextViewPercentage = (TextView) getView().findViewById(R.id.tv_percentage);
                            mProgressBar = (ProgressBar) getView().findViewById(R.id.pb);
                            mTextViewPercentage.post(new Runnable() {
                                @Override
                                public void run() {
                                    if(result.length>=3){



                                        mTextViewPercentage.setText(result[2]+"%");
                                        mProgressBar.setProgress(Integer.parseInt(result[2].trim()));
                                    }}


                            });

                        }

                        Thread.sleep(500);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
        public void stop() {
            bStop = true;
        }
    }

}