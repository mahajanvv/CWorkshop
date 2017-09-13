package com.example.dontknow.cworkshop;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MakeEntries extends AppCompatActivity {

    private Button next;

    private EditText firstname;
    private EditText midname;
    private EditText lastname;
    private EditText email;
    private EditText phone;
    private EditText college;
    private EditText year;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_entries);

        next = (Button)findViewById(R.id.idNext);

        firstname = (EditText)findViewById(R.id.idFirstName);
        midname = (EditText)findViewById(R.id.idMidName);
        lastname = (EditText)findViewById(R.id.idLastName);
        email = (EditText)findViewById(R.id.idEmailAddress);
        phone = (EditText)findViewById(R.id.idPhone);
        college = (EditText)findViewById(R.id.idCollegeName);
        year = (EditText)findViewById(R.id.idYear);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(TextUtils.isEmpty(firstname.getText())||TextUtils.isEmpty(lastname.getText())||TextUtils.isEmpty(midname.getText())||TextUtils.isEmpty(email.getText())||TextUtils.isEmpty(phone.getText())||TextUtils.isEmpty(college.getText()))
                {
                    Toast.makeText(getApplicationContext(),"Please Fill Out All The Fields",Toast.LENGTH_LONG).show();
                }
                else
                {

                    Intent intent =new Intent(v.getContext(),Signature.class);
                    intent.putExtra("FirstName",firstname.getText().toString());
                    intent.putExtra("MidName",midname.getText().toString());
                    intent.putExtra("LastName",lastname.getText().toString());
                    intent.putExtra("Email",email.getText().toString());
                    intent.putExtra("Phone",phone.getText().toString());
                    intent.putExtra("College",college.getText().toString());
                    intent.putExtra("Year",year.getText().toString());
                    startActivity(intent);
                }

            }
        });

        year.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCreateDialog(savedInstanceState);
            }
        });

    }
    public void onCreateDialog(Bundle savedInstanceState) {

        final CharSequence[] items = {"F.Y","S.Y","T.Y","B.TECH"};

        // Creating and Building the Dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Year");
        builder.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {


                switch(item)
                {
                    case 0:
                        // Your code when first option seletced
                        year.setText("F.Y");
                        break;
                    case 1:
                        // Your code when 2nd  option seletced
                        year.setText("S.Y");
                        break;
                    case 2:
                        // Your code when 3rd option seletced
                        year.setText("T.Y");
                        break;
                    case 3:
                        // Your code when 4th  option seletced
                        year.setText("B.TECH");
                        break;
                }

                dialog.dismiss();
            }
        });
        Dialog dialog = builder.create();
        dialog.show();


    }
}
