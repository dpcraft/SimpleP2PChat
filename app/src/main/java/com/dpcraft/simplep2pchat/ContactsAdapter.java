package com.dpcraft.simplep2pchat;

import android.content.ContentValues;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.dpcraft.simplep2pchat.data.UserInfo;

import java.util.List;

/**
 * Created by dpcraft on 19/11/2017.
 */

public class ContactsAdapter extends ArrayAdapter<UserInfo> {
    int resourceId;
    public ContactsAdapter(Context context, int textViewResourceId, List<UserInfo> userInfoList){
        super(context,textViewResourceId,userInfoList);
        resourceId = textViewResourceId;
    }
    @Override
    public View getView(int position,View convertView,ViewGroup parent){
        UserInfo userInfo = getItem(position);
        View view;
        ViewHolder viewHolder;
        if(convertView == null){
            view = LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
            viewHolder = new ViewHolder();
            viewHolder.userNameTextView = view.findViewById(R.id.tv_username);
            viewHolder.addressTextView = view.findViewById(R.id.tv_address);
            view.setTag(viewHolder);
        }else{
            view = convertView;
            viewHolder = (ViewHolder)view.getTag();
        }
        viewHolder.userNameTextView.setText(userInfo.getUsername());
        viewHolder.addressTextView.setText(userInfo.getAddress() + ":" + userInfo.getPort());
        return view;
    }
    class ViewHolder{
        TextView userNameTextView,addressTextView;

    }
}
