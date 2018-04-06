package com.bigappcompany.retailtest.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bigappcompany.retailtest.R;
import com.bigappcompany.retailtest.model.UserDetailsModel;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by shankar on 6/4/18.
 */

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.MyViewHolder> {

    private List<UserDetailsModel> userList;
    private Context context;
    private int userdetails_row;

    public UserListAdapter(Context context, ArrayList<UserDetailsModel> userList, int userdetails_row) {
        this.context = context;
        this.userList = userList;
        this.userdetails_row = userdetails_row;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.userName)
        TextView userName;
        @BindView(R.id.userReputation)
        TextView userReputation;
        @BindView(R.id.userImage)
        ImageView userImage;

        public MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this,view);
        }
    }



    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(userdetails_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final int pos = position;
        final UserDetailsModel pl = userList.get(position);

        holder.userName.setText(" Name: "+pl.getName());
        holder.userReputation.setText(" Reputation: "+pl.getReputation());

       Glide.with(context).load(pl.getImage()).into(holder.userImage);

    }

    @Override
    public int getItemCount() {
        return userList.size();
    }
}

