package com.jithu.dexray20;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;


public class FavoriteFragment extends Fragment {
    private UUID mDeviceUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private BluetoothAdapter mBTAdapter;
    private BluetoothSocket mBTSocket;
    private static final int BT_ENABLE_REQUEST = 10;
    private TextView mTxtReceiv;
    private TextView motor;

    private static final int Request_enable=0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ((MainActivity) getActivity()).getSupportActionBar().hide();
        new ReadInput();
        return inflater.inflate(R.layout.fragment_favorite, container, false);
    }
    private class ReadInput implements Runnable{
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
                int counter=0;
                do {

                    try {
                        mBTSocket = hc05.createRfcommSocketToServiceRecord(mDeviceUUID);
                        System.out.println(mBTSocket);
                        mBTSocket.connect();
                        System.out.println("isconnected");
                        System.out.println(mBTSocket.isConnected());
                    } catch (IOException e) {
                        e.printStackTrace();
                        counter++;
                    }
                }while (!mBTSocket.isConnected() && counter<100);

                //sent data code to edit
                try {
                    mBTSocket.getOutputStream().write(on.getBytes());

                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                // till here.....................
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
                            mTxtReceiv = (TextView) getView().findViewById(R.id.txtreceive);
                            motor=(TextView) getView().findViewById(R.id.motor);


                            mTxtReceiv.post(new Runnable() {
                                @Override
                                public void run() {
                                    if (result.length>=1){


                                        mTxtReceiv.setText(result[0]);
                                    }}
                            });
                            motor.post(new Runnable() {
                                @Override
                                public void run() {
                                    if (result.length>=2){


                                        motor.setText(result[1]);
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


//.....................

        }

        public void stop() {
            bStop = true;
        }

    }


}