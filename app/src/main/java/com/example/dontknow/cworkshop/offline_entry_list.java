package com.example.dontknow.cworkshop;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class offline_entry_list extends AppCompatActivity {

    private static int flg=0;
    private Button updata;
    private static RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private static RecyclerView recyclerView;
    private ArrayList<CWDatabase> data;
    private ArrayList<CWDatabase>databaseupload;
    private static MydbHandler mydbHandler;
    private DatabaseReference databaseReference;
    private static DatabaseReference databaseReferenceCWCount;
    private static DatabaseReference databaseReferenceCWEntry;
    private static int cnt;
    private static Uri ImageUri;
    private static StorageReference storageReference;
    private Bundle bundle;
    static View.OnLongClickListener myOnLongClickListener;
    public static final int Request_Code = 1243;
    private static ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bundle = savedInstanceState;
        setContentView(R.layout.activity_offline_entry_list);

        myOnLongClickListener  = new MyOnLongClickListener(this);

        recyclerView = (RecyclerView )findViewById(R.id.idofflineentrylist);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);

        mydbHandler = new MydbHandler(this,null,null,1);

        data = mydbHandler.getDatabase();

        adapter = new offline_entry_adapter(data);
        recyclerView.setAdapter(adapter);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReferenceCWCount = FirebaseDatabase.getInstance().getReference().child("CWCount");
        databaseReferenceCWEntry = FirebaseDatabase.getInstance().getReference().child("CWEntry");
        storageReference = FirebaseStorage.getInstance().getReference();

    }
    private void refresh()
    {
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }


    private static class MyOnLongClickListener implements View.OnLongClickListener
    {
        private final Context context;
        private MyOnLongClickListener(Context context)
        {
            this.context = context;
        }

        @Override
        public boolean onLongClick(final View v) {
            progressDialog.setMessage("Uploading!!!");
            progressDialog.show();
            int selecteditemposition = recyclerView.getChildPosition(v);
            RecyclerView.ViewHolder viewHolder = recyclerView.findViewHolderForPosition(selecteditemposition);
            TextView mail=(TextView)viewHolder.itemView.findViewById(R.id.idoffline_entry_email);
            TextView phone=(TextView)viewHolder.itemView.findViewById(R.id.idoffline_entry_phone);
            TextView remaining=(TextView)viewHolder.itemView.findViewById(R.id.idoffline_entry_remaining);
            final CWDatabase cwDatabase = mydbHandler.getCWdatabase(mail.getText().toString(),phone.getText().toString(),remaining.getText().toString());
            if(cwDatabase == null)
            {
                progressDialog.dismiss();
                Toast.makeText(v.getContext().getApplicationContext(),"Entry Already Uploaded",Toast.LENGTH_LONG).show();
                return true;
            }


            //Toast.makeText(getApplicationContext(),""+dataSnapshot.getValue(),Toast.LENGTH_LONG).show();
            cnt++;
            final DatabaseReference newEntry = databaseReferenceCWEntry.push();
            final String id =newEntry.getKey();

            ImageUri = Uri.fromFile(new File(cwDatabase.get_image()));
            if(ImageUri!=null)
            {
                StorageReference filepath = storageReference.child("SignatureImages").child(id);
                filepath.putFile(ImageUri).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        databaseReferenceCWEntry.child(id).removeValue();
                        progressDialog.dismiss();
                        Toast.makeText(v.getContext(),"Failed To Upload Image To Database",Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                });

                filepath.putFile(ImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        Uri downloadUrl = taskSnapshot.getDownloadUrl();


                        newEntry.child("ID").setValue(id);
                        newEntry.child("FirstName").setValue(cwDatabase.get_firstname());
                        newEntry.child("MidName").setValue(cwDatabase.get_lastname());
                        newEntry.child("LastName").setValue(cwDatabase.get_lastname());
                        newEntry.child("Email").setValue(cwDatabase.get_email());
                        newEntry.child("Phone").setValue(cwDatabase.get_phone());
                        newEntry.child("College").setValue(cwDatabase.get_college());
                        newEntry.child("Year").setValue(cwDatabase.get_year());
                        newEntry.child("Remaining").setValue(cwDatabase.get_remaining());
                        newEntry.child("Signature").setValue(downloadUrl.toString().trim());
                        newEntry.child("Date").setValue(""+System.currentTimeMillis());
                        newEntry.child("Session1").setValue("Absent");
                        newEntry.child("Session2").setValue("Absent");
                        newEntry.child("Session3").setValue("Absent");
                        newEntry.child("Session4").setValue("Absent");
                        DatabaseReference data=FirebaseDatabase.getInstance().getReference().child("Users");
                        data.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if(dataSnapshot.hasChild(FirebaseAuth.getInstance().getCurrentUser().getUid().toString()))
                                {
                                    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid().toString();
                                    String name = ""+dataSnapshot.child(uid).child("First_Name").getValue().toString()+" "+dataSnapshot.child(uid).child("Last_Name").getValue().toString()+" "+dataSnapshot.child(uid).child("Post_Of_User").getValue().toString();
                                    newEntry.child("userName").setValue(name);
                                    newEntry.child("userUID").setValue(uid);
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }

                        });

                        //databaseReferenceCWCount.setValue(""+cnt);
                        mydbHandler.setUploaded(cwDatabase);
                        String url = "https://mahajanvv.000webhostapp.com/send-mail.php";
                        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Toast.makeText(v.getContext().getApplicationContext(),"mail is sent",Toast.LENGTH_LONG).show();
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(v.getContext().getApplicationContext(),"mail is not sent"+error.toString(),Toast.LENGTH_LONG).show();
                            }
                        }){
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                Map<String,String>params = new HashMap<>();
                                params.put("email",cwDatabase.get_email());
                                params.put("id",id);
                                params.put("firstname",cwDatabase.get_firstname());
                                params.put("lastname",cwDatabase.get_lastname());
                                return params;
                            }
                        };
                        MySingleton.getInstance(v.getContext().getApplicationContext()).addToRequestQueue(stringRequest);

                        String wayurl="https://mahajanvv.000webhostapp.com/Way2SMS-API-master/sendsms.php";
                        StringRequest stringRequest1 = new StringRequest(Request.Method.POST, wayurl, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Toast.makeText(v.getContext().getApplicationContext(),"message is sent",Toast.LENGTH_LONG).show();
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(v.getContext().getApplicationContext(),"message is not sent"+error.toString(),Toast.LENGTH_LONG).show();
                            }
                        }){
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                Map<String,String>params = new HashMap<>();
                                params.put("to",cwDatabase.get_phone());
                                params.put("sub","Thank You!\n"+" "+cwDatabase.get_firstname()+" "+cwDatabase.get_lastname()+" You are successfully registered for CWorkshop ."+" . Workshop is on 3rd and 9th September @ 9:00 AM."
                                        +"\n\n\n\n\n\n\n\n");
                                return params;
                            }
                        };
                        MySingleton.getInstance(v.getContext().getApplicationContext()).addToRequestQueue(stringRequest1);
                        flg=1;
                        progressDialog.dismiss();
                        Toast.makeText(v.getContext().getApplicationContext(),"Uploaded the entry!!! Click on referesh button",Toast.LENGTH_LONG).show();
                    }
                });


            }
            else {
                databaseReferenceCWEntry.child(id).removeValue();
                progressDialog.dismiss();
                Toast.makeText(v.getContext().getApplicationContext(), "ImageUri is null", Toast.LENGTH_LONG).show();
            }


            //FirebaseUpload(databaseupload.get(0));
            //Toast.makeText(this,"Wait Until menu pops up that will send mail to participant",Toast.LENGTH_LONG).show();
            //Toast.makeText(this,"Uploaded Sucessfully the entry of "+databaseupload.get(0).get_firstname()+" "+databaseupload.get(0).get_midname()+" "+databaseupload.get(0).get_lastname(),Toast.LENGTH_LONG).show();
            //updata.setEnabled(true);

            return true;

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu2,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemid = item.getItemId();
        if(itemid==R.id.idRefresh)
        {
            refresh();
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == Request_Code)
        {

            Toast.makeText(this,"Mail is sent to Participant!!!",Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        if(flg==1)
        {
            flg=0;
            refresh();

        }


    }
}

