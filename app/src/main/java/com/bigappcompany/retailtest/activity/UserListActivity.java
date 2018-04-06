package com.bigappcompany.retailtest.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bigappcompany.retailtest.R;
import com.bigappcompany.retailtest.Utilz.Utility;
import com.bigappcompany.retailtest.adapter.UserListAdapter;
import com.bigappcompany.retailtest.api.Download_web;
import com.bigappcompany.retailtest.database.RetailDatabase;
import com.bigappcompany.retailtest.model.UserDetailsModel;
import com.bigappcompany.retailtest.webservices.WebServices;
import com.bigappcompany.retailtest.interfaces.OnTaskCompleted;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by shankar on 6/4/18.
 */

public class UserListActivity extends AppCompatActivity {

    @BindView(R.id.rv_userlist)
    RecyclerView rv_userlist;
    @BindView(R.id.progress)
    ProgressBar progress;

    private int pagination;
    private boolean has_more;
    private ArrayList<UserDetailsModel> userList;
    private UserListAdapter adapter;
    private int pastVisiblesItems, visibleItemCount, totalItemCount;
    private  LinearLayoutManager mLayoutManager;
    private RetailDatabase database ;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initWidgit();
    }

    private void initWidgit() {

        userList = new ArrayList<>();
        pagination = 1;
        database = new RetailDatabase(UserListActivity.this);
        has_more = true;
        mLayoutManager = new LinearLayoutManager(this);
        rv_userlist.setLayoutManager(mLayoutManager);
        //rv_userlist.setLayoutManager(new LinearLayoutManager(this));
       // getUserList();

        userList = database.getUserDetaiks();
        if (userList.size()>0){
            try {
                pagination = Integer.parseInt(userList.get((userList.size()) - 1).getPaginationCount());
            }
            catch (NullPointerException e){
                e.printStackTrace();
            }
        }
        else {
            if (Utility.isInternetAvailable(UserListActivity.this))
                getUserList();
            else
                Toast.makeText(UserListActivity.this, "No Internet connection", Toast.LENGTH_SHORT).show();
        }

        rv_userlist.addOnScrollListener(new RecyclerView.OnScrollListener()
        {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy)
            {
                if(dy > 0) //check for scroll down
                {
                    visibleItemCount = mLayoutManager.getChildCount();
                    totalItemCount = mLayoutManager.getItemCount();
                    pastVisiblesItems = mLayoutManager.findFirstVisibleItemPosition();

                        if ( (visibleItemCount + pastVisiblesItems) >= totalItemCount)
                        {
                            if (has_more)
                            {

                           // loading = false;
                                if (Utility.isInternetAvailable(UserListActivity.this)){

                                progress.setVisibility(View.VISIBLE);
                                pagination = pagination+1;
                                 getUserList();
                                }
                                else
                                    Toast.makeText(UserListActivity.this, "No Internet connection", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });
        adapter = new UserListAdapter(UserListActivity.this, userList, R.layout.userdetails_row);
        rv_userlist.setAdapter(adapter);
    }

    private void getUserList() {

        Download_web web = new Download_web(UserListActivity.this, new OnTaskCompleted() {
            @Override
            public void onTaskCompleted(String response) {

                if (response!=null && response.length()>0){
                    progress.setVisibility(View.GONE);
                    try {
                        JSONObject object = new JSONObject(response);
                        has_more = object.optBoolean("has_more");
                        JSONArray array = object.getJSONArray("items");
                        for (int i=0; i<array.length(); i++){
                            JSONObject jo = array.getJSONObject(i);

                            UserDetailsModel userDetailsModel = (new UserDetailsModel(jo.optString("display_name"), jo.optString("profile_image"),
                                    jo.optString("reputation"),""+pagination));
                            userList.add(userDetailsModel);
                            database.insertUserData(userDetailsModel);
                        }



                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                adapter.notifyDataSetChanged();

            }
        });

        web.setReqType(true);
        web.execute(WebServices.BASE_URL+pagination+WebServices.GETUSERDETAILS);
    }
}






   /* Fetch list of users by using stackoverflow api (public api):
        https://api.stackexchange.com/docs/users
        2. Displays below information :
        Name , Image . Reputation.
        3. Add load more user when reach to bottom with showing loading indicator at end.
        4. Fetched list of users should be displayed in offline mode as well.
        5. Add a search filter edit text for search using name.

        Note :
        - Implement using any of the following architecture pattern
        MVP or MVC or MVVM

        - Use glide for image loading and recyclerview for display user list.*/