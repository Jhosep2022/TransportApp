package com.example.apptransport.Adapter;

import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.apreciasoft.mobile.asremis.Entity.ChatOperator;
import com.apreciasoft.mobile.asremis.Fragments.placeholder.PlaceholderContent.PlaceholderItem;
import com.apreciasoft.mobile.asremis.R;


import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link PlaceholderItem}.
 * TODO: Replace the implementation with code for your data type.
 */
public class ChatOperatorsRecyclerViewAdapter extends RecyclerView.Adapter<ChatOperatorsRecyclerViewAdapter.ViewHolder> {

    private final List<ChatOperator> mValues;
    private Context mContext;

    public ChatOperatorsRecyclerViewAdapter(Context context, List<ChatOperator> items) {
        this.mValues = items;
        this.mContext=context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.fragment_chat_operators,
                parent,
                false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.txtName.setText(holder.mItem.getFirstNameUser().concat(" ").concat(holder.mItem.getLastNameUser()));
        showOrHideCountMessageSection(holder);
    }

    private void showOrHideCountMessageSection(ViewHolder holder){
        if(holder.mItem.getCountNewMessages()>0){
            holder.viewMessagesCount.setVisibility(View.VISIBLE);
        }
        else{
            holder.viewMessagesCount.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView txtCountMessages;
        public final TextView txtName;
        public final View viewMessagesCount;
        public ChatOperator mItem;

        public ViewHolder(View v) {
            super(v);
            txtCountMessages    = v.findViewById(R.id.txtCountMessages);
            txtName             = v.findViewById(R.id.txtName);
            viewMessagesCount   = v.findViewById(R.id.viewMessagesCount);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + txtName.getText() + "'";
        }
    }
}