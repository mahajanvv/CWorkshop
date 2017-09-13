package com.example.dontknow.cworkshop;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EditOnDay extends AppCompatActivity {

    private TextView id;
    private TextView name;
    private TextView email;private TextView phone;
    private EditText remaining;
    private Switch session1;
    private Switch session2;
   private Switch session3;
    private Switch session4;
    private Button submit;
    private Button cancel;

    Bundle bundle;
    private String post_key;
    private ProgressDialog progressDialog;

    DatabaseReference databaseReference;
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_on_day);

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);

        id = (TextView)findViewById(R.id.ideditid);
        name=(TextView)findViewById(R.id.ideditname);
        email = (TextView)findViewById(R.id.ideditemail);
        phone =(TextView)findViewById(R.id.ideditphone);
        remaining = (EditText)findViewById(R.id.ideditRemaining);
        session1 = (Switch)findViewById(R.id.switch1);
        session2 = (Switch)findViewById(R.id.switch2);
        session3 = (Switch)findViewById(R.id.switch3);
        session4 = (Switch)findViewById(R.id.switch4);
        submit = (Button)findViewById(R.id.ideditsubmit);
        cancel = (Button)findViewById(R.id.ideditcancel);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("CWEntry");

        bundle = getIntent().getExtras();

        post_key = bundle.getString("Post_Key");

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(dataSnapshot.hasChild(post_key))
                {
                    id.setText(dataSnapshot.child(post_key).child("ID").getValue().toString());
                    name.setText(dataSnapshot.child(post_key).child("FirstName").getValue().toString()+" "+dataSnapshot.child(post_key).child("MidName").getValue().toString()+" "+dataSnapshot.child(post_key).child("LastName").getValue().toString());
                    email.setText(dataSnapshot.child(post_key).child("Email").getValue().toString());
                    phone.setText(dataSnapshot.child(post_key).child("Phone").getValue().toString());
                    remaining.setText(dataSnapshot.child(post_key).child("Remaining").getValue().toString());
                    if(dataSnapshot.child(post_key).child("Session1").getValue().toString().equals("Absent"))
                    {
                        session1.setChecked(false);
                    }
                    else
                    {
                        session1.setChecked(true);
                    }
                    if(dataSnapshot.child(post_key).child("Session2").getValue().toString().equals("Absent"))
                    {
                        session2.setChecked(false);
                    }
                    else {
                        session2.setChecked(true);
                    }
                    if(dataSnapshot.child(post_key).child("Session3").getValue().toString().equals("Absent"))
                    {
                        session3.setChecked(false);
                    }
                    else
                    {
                        session3.setChecked(true);
                    }
                    if(dataSnapshot.child(post_key).child("Session4").getValue().toString().equals("Absent"))
                    {
                        session4.setChecked(false);
                    }
                    else
                    {
                        session4.setChecked(true);
                    }

                }
                else
                {
                    Toast.makeText(EditOnDay.this,"Record Not Available",Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.setMessage("Submitting");
                progressDialog.show();
                if(new String(""+remaining.getText()).equals("0"))
                {
                    databaseReference.child(post_key).child("Remaining").setValue("0");
                    if(session1.isChecked())
                    {
                        databaseReference.child(post_key).child("Session1").setValue("Present");
                    }
                    else
                    {
                        databaseReference.child(post_key).child("Session1").setValue("Absent");
                    }
                    if(session2.isChecked())
                    {
                        databaseReference.child(post_key).child("Session2").setValue("Present");

                    }
                    else
                    {
                        databaseReference.child(post_key).child("Session2").setValue("Absent");
                    }
                    if(session3.isChecked())
                    {
                        databaseReference.child(post_key).child("Session3").setValue("Present");

                    }
                    else
                    {
                        databaseReference.child(post_key).child("Session3").setValue("Absent");
                    }
                    if(session4.isChecked())
                    {
                        databaseReference.child(post_key).child("Session4").setValue("Present");

                    }
                    else
                    {
                        databaseReference.child(post_key).child("Session4").setValue("Absent");
                    }
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(),"Session Verification Is Done!!!!",Toast.LENGTH_LONG).show();
                    Intent intent =new Intent(EditOnDay.this,ondaymanagement.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
                else
                {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(),"Please Collect The Cash From Participant",Toast.LENGTH_LONG).show();
                }

            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditOnDay.this,ondaymanagement.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }
}
