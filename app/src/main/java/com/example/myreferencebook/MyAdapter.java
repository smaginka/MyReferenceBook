package com.example.myreferencebook;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    private List<ListItem> listItems;
    private Context context;
    private OnListItemListener onListItemListener;

    public MyAdapter(List<ListItem> listItems, Context context, OnListItemListener onListItemListener) {
        this.listItems = listItems;
        this.context = context;
        this.onListItemListener = onListItemListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);
        return new ViewHolder(v, onListItemListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ListItem listItem = listItems.get(position);
        holder.textViewText.setText(listItem.getText());
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView textViewText;
        OnListItemListener onListItemListener;

        public ViewHolder(@NonNull View itemView, OnListItemListener onListItemListener) {
            super(itemView);

            textViewText = (TextView) itemView.findViewById(R.id.text);
            this.onListItemListener = onListItemListener;

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            onListItemListener.onListItemClick(getAdapterPosition());
        }
    }

    public interface OnListItemListener{
        void onListItemClick(int position);
    }

}
