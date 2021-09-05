package com.CleverTapTest.clevertapandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.clevertap.android.sdk.CleverTapAPI;
import com.clevertap.android.sdk.pushnotification.amp.CTPushAmpListener;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements CTPushAmpListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        CleverTapAPI clevertapDefaultInstance = CleverTapAPI.getDefaultInstance(getApplicationContext());
        clevertapDefaultInstance.setCTPushAmpListener(this);

        EditText userName=findViewById(R.id.nameEditText);
        EditText userEmail=findViewById(R.id.emailEditText);

        Button viewProductBtn=findViewById(R.id.viewProductButton);
        Button pushProfile=findViewById(R.id.pushDetailsButton);

        HashMap<String, Object> prodViewedAction = new HashMap<String, Object>();
        viewProductBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prodViewedAction.put("Product Name", "CleverTap");
                prodViewedAction.put("Product ID", "1");
                prodViewedAction.put("Product Image", "https://d35fo82fjcw0y8.cloudfront.net/2018/07/26020307/customer-success-clevertap.jpg");
                clevertapDefaultInstance.pushEvent("Product Viewed", prodViewedAction);
            }
        });


        pushProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name=userName.getText().toString();
                String email=userEmail.getText().toString();

                HashMap<String, Object> profile= new HashMap<String, Object>();
                profile.put("Name", name);                  // String
                profile.put("Email",email );
                clevertapDefaultInstance.pushProfile(profile);

                Bundle b=new Bundle();
                b.putString("Message","test");

                Bundle extras = new Bundle();
                extras.putString("Message","Username :"+userName+", Email :"+userEmail);
                extras.putString("Title","CleverTap");
                extras.putString("Subject","UserPush");
                CleverTapAPI.processPushNotification(getApplicationContext(),extras);

            }
        });

    }

    @Override
    public void onPushAmpPayloadReceived(Bundle extras) {
        String message=extras.getString("Message");
        String title=extras.getString("Title");
        String subject=extras.getString("Subject");

        System.out.println("Recieved");
        NotificationManager notif=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notify=new Notification.Builder
                (getApplicationContext()).setContentTitle(title).setContentText(message).
                setContentTitle(subject).setSmallIcon(R.drawable.ic_launcher_background).build();

        notify.flags |= Notification.FLAG_AUTO_CANCEL;
        notif.notify(0, notify);
    }
}
