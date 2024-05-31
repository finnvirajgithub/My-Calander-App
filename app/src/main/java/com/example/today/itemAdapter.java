package com.example.today;

import android.content.Context;
import android.content.pm.LabeledIntent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class itemAdapter extends RecyclerView.Adapter<itemAdapter.itemViewHolder>{

    private ArrayList<String> topiclist;
    private ArrayList<String>detaillist;
    private Context context;

    public itemAdapter(ArrayList<String> topiclist, ArrayList<String> detaillist, Context context) {
        this.topiclist = topiclist;
        this.detaillist = detaillist;
        this.context = context;
    }

    @NonNull
    @Override
    public itemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.infoitem,parent,false);
        return new itemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull itemViewHolder holder, int position) {
        holder.topic.setText(topiclist.get(position));
        holder.detail.setText(detaillist.get(position));

    }

    @Override
    public int getItemCount() {
        return topiclist.size();
    }

    public class itemViewHolder extends RecyclerView.ViewHolder {

        private TextView topic, detail;
        public itemViewHolder(@NonNull View itemView) {
            super(itemView);
            topic = itemView.findViewById(R.id.infotopic);
            detail = itemView.findViewById(R.id.infodetail);
        }
    }
}
