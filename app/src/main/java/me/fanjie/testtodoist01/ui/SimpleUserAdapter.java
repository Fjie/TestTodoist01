package me.fanjie.testtodoist01.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import me.fanjie.testtodoist01.R;
import me.fanjie.testtodoist01.model.User;

/**
 * Created by fanjie on 2016/6/1.
 */
public class SimpleUserAdapter extends BaseAdapter {

    private Context context;
    private List<User> users;

    public SimpleUserAdapter(Context context, List<User> users) {
        this.context = context;
        this.users = users;
    }

    @Override
    public int getCount() {
        if(users == null){
            return 0;
        }else {
            return users.size();
        }
    }

    @Override
    public User getItem(int position) {
        return users.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View v, ViewGroup parent) {
        User user = getItem(position);
        ViewHolder h;
        if(v == null){
            v = LayoutInflater.from(context).inflate(R.layout.item_simple_user_list,null);
            h = new ViewHolder();
            h.tvUserName = (TextView) v.findViewById(R.id.tv_user_name);
            v.setTag(h);
        }else {
            h = (ViewHolder) v.getTag();
        }
        h.tvUserName.setText(user.getPetName());
        return v;
    }

    private class ViewHolder{
        TextView tvUserName;
    }
}
