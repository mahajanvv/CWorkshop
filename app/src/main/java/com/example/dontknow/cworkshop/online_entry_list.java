package com.example.dontknow.cworkshop;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.HashMap;
import java.util.Map;

public class online_entry_list extends AppCompatActivity {

    private Query databaseReference;

    private RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online_entry_list);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("CWEntry").orderByChild("Date");
        databaseReference.keepSynced(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);

        recyclerView = (RecyclerView)findViewById(R.id.idonline_entry_List);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

    @Override
    protected void onStart() {
        super.onStart();


        FirebaseRecyclerAdapter<CWOnlineEntry_accept,onlineListViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<CWOnlineEntry_accept, onlineListViewHolder>(
                CWOnlineEntry_accept.class,R.layout.online_entry_row,onlineListViewHolder.class,databaseReference) {
            @Override
            protected void populateViewHolder(onlineListViewHolder viewHolder, final CWOnlineEntry_accept model, int position) {

                viewHolder.setOnlineRowid(model.getID());
                viewHolder.setOnlineRowname(model.getFirstName()+" "+model.getMidName()+" "+model.getLastName());
                viewHolder.setOnlineRowemail(model.getEmail());
                viewHolder.setOnlineRowphone(model.getPhone());
                viewHolder.setOnlineRowremaining(model.getRemaining());
                viewHolder.setOnlineRowmembername(model.getUserName());

                viewHolder.view.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        //final ProgressDialog dialog = ProgressDialog.show(this, "", "Loading. Please wait...", false);

//whatever you want just you have to launch overhere.

                                Toast.makeText(getApplicationContext(),"Preparing mail!!",Toast.LENGTH_LONG).show();
                                String url = "https://mahajanvv.000webhostapp.com/send-mail.php";
                                StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        Toast.makeText(getApplicationContext(),"mail is sent",Toast.LENGTH_LONG).show();
                                    }
                                }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Toast.makeText(getApplicationContext(),"mail is not sent"+error.toString(),Toast.LENGTH_LONG).show();
                                    }
                                }){
                                    @Override
                                    protected Map<String, String> getParams() throws AuthFailureError {
                                        Map<String,String>params = new HashMap<>();
                                        params.put("email",model.getEmail());
                                        params.put("id",model.getID());
                                        params.put("firstname",model.getFirstName());
                                        params.put("lastname",model.getLastName());
                                        return params;
                                    }
                                };
                                Toast.makeText(getApplicationContext(),"Sending mail!!!",Toast.LENGTH_LONG).show();
                                MySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);

                                stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                                        5000,
                                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                                Toast.makeText(getApplicationContext(),"Preparing Message!!!",Toast.LENGTH_LONG).show();
                                String wayurl="https://mahajanvv.000webhostapp.com/Way2SMS-API-master/sendsms.php";
                                StringRequest stringRequest1 = new StringRequest(Request.Method.POST, wayurl, new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        Toast.makeText(getApplicationContext(),"message is sent",Toast.LENGTH_LONG).show();
                                    }
                                }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Toast.makeText(getApplicationContext(),"message is not sent"+error.toString(),Toast.LENGTH_LONG).show();
                                    }
                                }){
                                    @Override
                                    protected Map<String, String> getParams() throws AuthFailureError {
                                        Map<String,String>params = new HashMap<>();
                                        params.put("to",model.getPhone());
                                        params.put("sub","Thank You!\n"+" "+model.getFirstName()+" "+model.getLastName()+" You are successfully registered for CWorkshop ."+" . Workshop is on 3rd and 9th September @ 9:00 AM."
                                                +"\n\n\n\n\n\n\n\n");
                                        return params;
                                    }
                                };
                                Toast.makeText(getApplicationContext(),"Sending Message!!!",Toast.LENGTH_LONG).show();
                                MySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest1);
                                stringRequest1.setRetryPolicy(new DefaultRetryPolicy(
                                        5000,
                                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

                        return true;
                    }
                });

            }
        };
        recyclerView.setAdapter(firebaseRecyclerAdapter);
    }
    public static class onlineListViewHolder extends RecyclerView.ViewHolder
    {
        View view =null;

        public onlineListViewHolder(View itemView) {
            super(itemView);

            view = itemView;
        }

        private void setOnlineRowid(String id)
        {
            TextView textView = (TextView ) view.findViewById(R.id.idonline_entry_id);
            textView.setText(id);
        }
        private void setOnlineRowname(String name)
        {
            TextView textView = (TextView ) view.findViewById(R.id.idonline_entry_name);
            textView.setText(name);
        }
        private void setOnlineRowemail(String email)
        {
            TextView textView = (TextView ) view.findViewById(R.id.idonline_entry_email);
            textView.setText(email);
        }
        private void setOnlineRowphone(String phone)
        {
            TextView textView = (TextView ) view.findViewById(R.id.idonline_entry_phone);
            textView.setText(phone);
        }
        private void setOnlineRowremaining(String remaining)
        {
            TextView textView = (TextView ) view.findViewById(R.id.idonline_entry_remaininig);
            textView.setText(remaining);
        }
        private void setOnlineRowmembername(String membername)
        {
            TextView textView = (TextView ) view.findViewById(R.id.idonline_entry_membername);
            textView.setText(membername);
        }
    }
}
