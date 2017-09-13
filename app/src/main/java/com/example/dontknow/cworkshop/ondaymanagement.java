package com.example.dontknow.cworkshop;


import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class ondaymanagement extends AppCompatActivity {

    private Query databasereference;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ondaymanagement);


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);

        recyclerView = (RecyclerView ) findViewById(R.id.idondaymanagementRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    public static class ondaymanagementListViewHolder extends RecyclerView.ViewHolder
    {
        View view =null;

        public ondaymanagementListViewHolder(View itemView) {
            super(itemView);

            view = itemView;
        }

        private void setOnlineRowid(String id)
        {
            TextView textView = (TextView ) view.findViewById(R.id.idondaymanagement_row_id);
            textView.setText(id);
        }
        private void setOnlineRowname(String name)
        {
            TextView textView = (TextView ) view.findViewById(R.id.idondaymanagement_row_name);
            textView.setText(name);
        }

        private void setOnlinesession1(String remaining)
        {
            TextView textView = (TextView ) view.findViewById(R.id.idondaymanagement_row_session1);
            textView.setText(remaining);
        }
        private void setOnlinesession2(String remaining)
        {
            TextView textView = (TextView ) view.findViewById(R.id.idondaymanagement_row_session2);
            textView.setText(remaining);
        }
        private void setOnlinesession3(String remaining)
        {
            TextView textView = (TextView ) view.findViewById(R.id.idondaymanagement_row_session3);
            textView.setText(remaining);
        }
        private void setOnlinesession4(String remaining)
        {
            TextView textView = (TextView ) view.findViewById(R.id.idondaymanagement_row_session4);
            textView.setText(remaining);
        }
        private void setOnlineRowremaining(String remaining)
        {
            TextView textView = (TextView ) view.findViewById(R.id.idondaymanagement_row_remaining);
            textView.setText(remaining);
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
       // getSupportMenuInflater().inflate(R.menu.main_menu, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.idSearch)
                .getActionView();
        if (null != searchView) {
            searchView.setSearchableInfo(searchManager
                    .getSearchableInfo(getComponentName()));
            searchView.setIconifiedByDefault(true);
        }

        SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {
            public boolean onQueryTextChange(String newText) {
                // this is your adapter that will be filtered
                return true;
            }

            public boolean onQueryTextSubmit(final String query) {
                //Here u can get the value "query" which is entered in the search box.

                Toast.makeText(getApplicationContext(),query,Toast.LENGTH_LONG).show();

               databasereference = FirebaseDatabase.getInstance().getReference().child("CWEntry").orderByChild("Phone").equalTo(query);
                databasereference.keepSynced(true);

                Toast.makeText(getApplicationContext(),query,Toast.LENGTH_LONG).show();

                FirebaseRecyclerAdapter<CWOnlineEntry_accept,ondaymanagementListViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<CWOnlineEntry_accept, ondaymanagementListViewHolder>(
                        CWOnlineEntry_accept.class,R.layout.ondaymanagement_row,ondaymanagement.ondaymanagementListViewHolder.class,databasereference) {
                    @Override
                    protected void populateViewHolder(final ondaymanagement.ondaymanagementListViewHolder viewHolder, CWOnlineEntry_accept model, final int position) {

                        viewHolder.setOnlineRowid(model.getID());
                        viewHolder.setOnlineRowname(model.getFirstName()+" "+model.getMidName()+" "+model.getLastName());
                        viewHolder.setOnlineRowremaining(model.getRemaining());
                        viewHolder.setOnlinesession1(model.getSession1());
                        viewHolder.setOnlinesession2(model.getSession2());
                        viewHolder.setOnlinesession3(model.getSession3());
                        viewHolder.setOnlinesession4(model.getSession4());

                        viewHolder.view.setOnLongClickListener(new View.OnLongClickListener() {
                            @Override
                            public boolean onLongClick(View v) {

                                Intent intent = new Intent(ondaymanagement.this,EditOnDay.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                intent.putExtra("Post_Key",getRef(position).getKey());
                                startActivity(intent);
                                return true;
                            }
                        });


                    }
                };
                recyclerView.setAdapter(firebaseRecyclerAdapter);

                return true;
            }
        };
        searchView.setOnQueryTextListener(queryTextListener);

        return super.onCreateOptionsMenu(menu);
    }

}
