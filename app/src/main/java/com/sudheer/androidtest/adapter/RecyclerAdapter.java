package com.sudheer.androidtest.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sudheer.androidtest.R;
import com.sudheer.androidtest.model.DataListModel;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    ArrayList<DataListModel> mList;
    Context mContext;

    public RecyclerAdapter(Context context, ArrayList<DataListModel> list) {
        mList = list;
        mContext = context;
    }

    @NonNull
    @Override
    public RecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.textTitle.setText(mList.get(position).title);
        holder.textDate.setText(mList.get(position).created_at);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView textTitle;
        TextView textDate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textTitle = (TextView) itemView.findViewById(R.id.txtTitle);
            textDate = (TextView) itemView.findViewById(R.id.txtDate);
        }
    }
}
