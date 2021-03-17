package com.example.automessagingapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class DisplayActivity extends AppCompatActivity {
    ListView lvShow;
    SQLiteDatabase db;
    ArrayList al = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);

        lvShow = findViewById(R.id.lvShow);
        db = openOrCreateDatabase("messagedb", MODE_PRIVATE, null);
        load();


        lvShow.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            //"setOnItemLongClickListener" works when we press on an item for long time
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {
                String data = al.get(position).toString();
                //Here we don't know the index number, "al.get(position)" is to get the position from the Array List
                String arr[] = data.split("\n");
                //As we have two rows(of data in a single row) in this,"data.split("\n")" will split/separate the 2rows
                final String iMessage = arr[0];
                //Here we are storing the 1st row data (i.e incoming message)

                AlertDialog.Builder ab = new AlertDialog.Builder(DisplayActivity.this);
                ab.setTitle("Action");
                ab.setMessage("Do you want to delete ?");
                ab.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            db.execSQL("delete from messages where incomingmessage='" + iMessage + "'");
                            al.clear();
                            load();
                            //After deleting something we need to clear the array list and load the db data again
                        } catch (Exception e) {
                            Toast.makeText(DisplayActivity.this, "Error : " + e, Toast.LENGTH_LONG).show();
                        }
                    }
                });
                ab.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                ab.show();
                return false;
            }
        });

    }

    public void load() {
        Cursor cur = db.rawQuery("select * from messages", null);
        while (cur.moveToNext()) {
            String incomingmessage = cur.getString(0);
            //"(0)" is the 1st line of data from the 1st row
            String outgoingmessage = cur.getString(1);
            //"(1)" is the 2nd line of data from the 1st row
            al.add(incomingmessage + "\n" + outgoingmessage);
        }
        cur.close();
        ArrayAdapter aa = new ArrayAdapter(DisplayActivity.this, android.R.layout.simple_list_item_1, al);
        lvShow.setAdapter(aa);
    }

}