package com.example.automessagingapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    EditText etIncomingText, etOutgoingText;
    Button btnSave, btnView;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(MainActivity.this, "android.permission.RECEIVE_SMS") != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{"android.permission.RECEIVE_SMS", "android.permission.SEND_SMS"}, 1);
            }
        }


        db = openOrCreateDatabase("messagedb", MODE_PRIVATE, null);
        db.execSQL("Create table if not exists messages (IncomingMessage varchar(100),OutgoingMessage varchar(100))");


        etIncomingText = findViewById(R.id.etIncomingText);
        etOutgoingText = findViewById(R.id.etOutgoingText);
        btnSave = findViewById(R.id.btnSave);
        btnView = findViewById(R.id.btnView);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String incomingMessage = etIncomingText.getText().toString().toLowerCase();
                String outgoingMessage = etOutgoingText.getText().toString();

                if (incomingMessage.length() == 0) {
                    Toast.makeText(MainActivity.this, "Enter incoming message", Toast.LENGTH_SHORT).show();
                } else if (outgoingMessage.length() == 0) {
                    Toast.makeText(MainActivity.this, "Enter outgoing message", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        db.execSQL("insert into messages(IncomingMessage,OutgoingMessage) values('" + incomingMessage + "','" + outgoingMessage + "')");
                        Toast.makeText(MainActivity.this, "Message inserted", Toast.LENGTH_SHORT).show();
                        etIncomingText.setText(" ");
                        etOutgoingText.setText(" ");
                    } catch (Exception e) {
                        Toast.makeText(MainActivity.this, "Error : " + e, Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });

        btnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, DisplayActivity.class);
                startActivity(i);
            }
        });

    }
}