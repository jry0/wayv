package io.chirp.connectdemoapp;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.FirebaseUserMetadata;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.IOException;
import java.util.Arrays;
import java.util.Random;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;
import io.chirp.connect.ChirpConnect;
import io.chirp.connect.interfaces.ConnectEventListener;
import io.chirp.connect.interfaces.ConnectSetConfigListener;
import io.chirp.connect.models.ChirpError;
import io.chirp.connect.models.ConnectState;

import static android.provider.AlarmClock.EXTRA_MESSAGE;


public class MainActivity extends AppCompatActivity {

    String name = "Database no work";

    String message = "Not logged in";

    String username = "there";

    String email = "foo";


    private static final int RC_SIGN_IN = 123;



    CircularProgressButton circularProgressButton;

    private ChirpConnect chirpConnect;

    private static final int RESULT_REQUEST_RECORD_AUDIO = 1;

    LottieAnimationView animationView;


    TextView headText;
    TextView status;
    TextView lastChirp;
    TextView versionView;

    Button startStopSdkBtn;
    Button startStopSendingBtn;

    Boolean startStopSdkBtnPressed = false;

    String APP_KEY = "39caccCd9F7a1A7F0BD2d27bA";
    String APP_SECRET = "1dfD0C1d04eBB8DF7a27C1c4b8Cf4F0De1C2CD3365EAEEfec4";
    //Default:
    String APP_CONFIG = "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";
    //Ultrasonic:
    // APP_CONFIG = "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";

    String TAG = "ConnectDemoApp";

    View parentLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(Arrays.asList(
                                new AuthUI.IdpConfig.GoogleBuilder().build()))
                        .setIsSmartLockEnabled(!BuildConfig.DEBUG /* credentials */, true /* hints */)
                        .build(),
                RC_SIGN_IN);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            for (UserInfo profile : user.getProviderData()) {
                // Id of the provider (ex: google.com)
                String providerId = profile.getProviderId();

                // Name, email address, and profile photo Url
                username = profile.getDisplayName();
                email = profile.getEmail();
            }
        }

        headText = (TextView) findViewById(R.id.textView2);

        animationView = findViewById(R.id.animation_view);
        animationView.useHardwareAcceleration();

        FirebaseFirestore db = FirebaseFirestore.getInstance();


        DocumentReference docRef = db.collection("users").document(email);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        message = document.getString("ID");
                        username = document.getString("name");
                    } else {
                        Log.d(TAG, "No such document");
                        message = "No such document";
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                    message = "get failed";
                }
            }
        });




//
//
// Get the Intent that started this activity and extract the string
//        Intent intent = getIntent();
//        String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);

        circularProgressButton = (CircularProgressButton)findViewById(R.id.progressBtn);

        parentLayout = findViewById(android.R.id.content);

        status = (TextView) findViewById(R.id.stateValue);
        lastChirp = (TextView) findViewById(R.id.lastChirp);
        versionView = (TextView) findViewById(R.id.versionView);

        startStopSdkBtn = (Button) findViewById(R.id.startStopSdkBtn);
        startStopSendingBtn = (Button) findViewById(R.id.startStopSengingBtn);

        startStopSendingBtn.setAlpha(.4f);
        startStopSendingBtn.setClickable(false);
        startStopSdkBtn.setAlpha(.4f);
        startStopSdkBtn.setClickable(false);

        String customMsg = "Hello " + username + ". Welcome to WAYV.";
        headText.setText(customMsg);


        Log.v("Connect Version: ", ChirpConnect.getVersion());
        versionView.setText(ChirpConnect.getVersion());

        if (APP_KEY.equals("") || APP_SECRET.equals("")) {
            Log.e(TAG, "APP_KEY or APP_SECRET is not set. " +
                    "Please update with your APP_KEY/APP_SECRET from admin.chirp.io");
            return;
        }
        
        /**
         * Key and secret initialisation
         */
        chirpConnect = new ChirpConnect(this, APP_KEY, APP_SECRET);
        chirpConnect.setConfig(APP_CONFIG, new ConnectSetConfigListener() {

            @Override
            public void onSuccess() {

                //Set-up the connect callbacks
                chirpConnect.setListener(connectEventListener);
                //Enable Start/Stop button
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        startStopSdkBtn.setAlpha(1f);
                        startStopSdkBtn.setClickable(true);
                    }
                });
            }

            @Override
            public void onError(ChirpError setConfigError) {
                Log.e("setConfigError", setConfigError.getMessage());
            }
        });

    }


    ConnectEventListener connectEventListener = new ConnectEventListener() {

        @Override
        public void onSending(byte[] data, byte channel) {
            /**
             * onSending is called when a send event begins.
             * The data argument contains the payload being sent.
             */
            String lastPayload = new String(data);
            String hexData = "null";
            if (data != null) {
                hexData = chirpConnect.payloadToHexString(data);
            }
            Log.v("connectdemoapp", "ConnectCallback: onSending: " + hexData + " on channel: " + channel);
            //updateLastPayload(lastPayload);
        }

        @Override
        public void onSent(byte[] data, byte channel) {
            /**
             * onSent is called when a send event has completed.
             * The data argument contains the payload that was sent.
             */
            String hexData = "null";
            if (data != null) {
                hexData = chirpConnect.payloadToHexString(data);
            }

            String lastPayload = new String(data);
            updateLastPayload(lastPayload);

            Log.v("connectdemoapp", "ConnectCallback: onSent: " + hexData + " on channel: " + channel);
        }

        @Override
        public void onReceiving(byte channel) {
            /**
             * onReceiving is called when a receive event begins.
             * No data has yet been received.
             */
            Log.v("connectdemoapp", "ConnectCallback: onReceiving on channel: " + channel);
        }

        @Override
        public void onReceived(byte[] data, byte channel) {
            /**
             * onReceived is called when a receive event has completed.
             * If the payload was decoded successfully, it is passed in data.
             * Otherwise, data is null.
             */
            String hexData = "null";
            if (data != null) {
                hexData = chirpConnect.payloadToHexString(data);
            }
            Log.v("connectdemoapp", "ConnectCallback: onReceived: " + hexData + " on channel: " + channel);
            updateLastPayload(hexData);
        }

        @Override
        public void onStateChanged(byte oldState, byte newState) {
            /**
             * onStateChanged is called when the SDK changes state.
             */
            ConnectState state = ConnectState.createConnectState(newState);
            Log.v("connectdemoapp", "ConnectCallback: onStateChanged " + oldState + " -> " + newState);
            if (state == ConnectState.ConnectNotCreated) {
                updateStatus("NotCreated");
            } else if (state == ConnectState.AudioStateStopped) {
                updateStatus("Stopped");
            } else if (state == ConnectState.AudioStatePaused) {
                updateStatus("Paused");
            } else if (state == ConnectState.AudioStateRunning) {
                updateStatus("Running");
            } else if (state == ConnectState.AudioStateSending) {
                updateStatus("Sending");
            } else if (state == ConnectState.AudioStateReceiving) {
                updateStatus("Receiving");
            } else {
                updateStatus(newState + "");
            }

        }

        @Override
        public void onSystemVolumeChanged(int oldVolume, int newVolume) {
            /**
             * onSystemVolumeChanged is called when the system volume is changed.
             */
            Snackbar snackbar = Snackbar.make(parentLayout, "System volume has been changed to: " + newVolume, Snackbar.LENGTH_LONG);
            snackbar.setAction("CLOSE", new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            })
                    .setActionTextColor(getResources().getColor(android.R.color.holo_red_light ))
                    .show();
            Log.v("connectdemoapp", "System volume has been changed, notify user to increase the volume when sending data");
        }

    };

    @Override
    protected void onResume() {
        super.onResume();

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.RECORD_AUDIO}, RESULT_REQUEST_RECORD_AUDIO);
        }
        else {
            if (startStopSdkBtnPressed) startSdk();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case RESULT_REQUEST_RECORD_AUDIO: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (startStopSdkBtnPressed) stopSdk();
                }
                return;
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        chirpConnect.stop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            chirpConnect.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        circularProgressButton.dispose();

    }

    @Override
    public void onStop() {
        super.onStop();
        stopSdk();
    }

    public void updateStatus(final String newStatus) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                status.setText(newStatus);
            }
        });
    }
    public void updateLastPayload(final String newPayload) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                lastChirp.setText(newPayload);
                new CountDownTimer(1000, 500) {

                    @Override
                    public void onTick(long millisUntilFinished) {
                        // do something after 1s
                    }

                    @Override
                    public void onFinish() {
                        //circularProgressButton.doneLoadingAnimation(333639, BitmapFactory.decodeResource(getResources(),R.drawable.ic_done_white_48dp));
                        String send = "Send";
                        circularProgressButton.revertAnimation();
                        circularProgressButton.setText(send);
                        animationView.cancelAnimation();
                        animationView.setFrame(0);
                    }

                }.start();

            }
        });
    }

    public void stopSdk() {
        ChirpError error = chirpConnect.stop();
        if (error.getCode() > 0) {
            Log.e("ConnectError: ", error.getMessage());
            return;
        }
        String send = "Send";
        circularProgressButton.revertAnimation();
        circularProgressButton.setText(send);
        startStopSendingBtn.setAlpha(.4f);
        startStopSendingBtn.setClickable(false);
        startStopSdkBtn.setText("Start Sdk");
    }

    public void startSdk() {
        ChirpError error = chirpConnect.start();
        if (error.getCode() > 0) {
            Log.e("ConnectError: ", error.getMessage());
            return;
        }
        startStopSendingBtn.setAlpha(1f);
        startStopSendingBtn.setClickable(true);
        startStopSdkBtn.setText("Stop Sdk");
    }

    public void startStopSdk(View view) {
        /**
         * Start or stop the SDK.
         * Audio is only processed when the SDK is running.
         */
        startStopSdkBtnPressed = true;
        if (chirpConnect.getConnectState() == ConnectState.AudioStateStopped) {
            startSdk();
        } else {
            stopSdk();
        }
    }

    public void sendPayload(View view) {
    	/**
         * A payload is a byte array dynamic size with a maximum size defined by the config string.
         *
         * Generate a random payload, and send it.
         */
        if (chirpConnect.getConnectState() == ConnectState.AudioStateStopped) {
            startSdk();
        }
        animationView.setSpeed(1);
        animationView.playAnimation();
        //circularProgressButton.startAnimation();
    	//EditText editText = (EditText) findViewById(R.id.editText);
        //String message = editText.getText().toString();


//        DocumentReference docRef = db.collection("users").document("UW0896");
//        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                if (task.isSuccessful()) {
//                    DocumentSnapshot document = task.getResult();
//                    if (document.exists()) {
//                        name = document.getString("name");
//                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
//
//                    } else {
//                        Log.d(TAG, "No such document");
//                        name = document.getString("name");
//                    }
//                } else {
//                    Log.d(TAG, "get failed with ", task.getException());
//                    name = "Touched 3rd else";
//                }
//            }
//        });


    	long maxPayloadLength = chirpConnect.getMaxPayloadLength();
    	long size = (long) new Random().nextInt((int) maxPayloadLength) + 1;
        //byte[] payload = chirpConnect.randomPayload(size);

        byte[] payload = message.getBytes();
        long maxSize = chirpConnect.getMaxPayloadLength();
        if (maxSize < payload.length) {
            Log.e("ConnectError: ", "Invalid Payload");
            return;
        }

        ChirpError error = chirpConnect.send(payload);
        if (error.getCode() > 0) {
            Log.e("ConnectError: ", error.getMessage());
        }
    }
}
