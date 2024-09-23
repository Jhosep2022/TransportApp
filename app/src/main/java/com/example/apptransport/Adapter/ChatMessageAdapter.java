package com.example.apptransport.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.apreciasoft.mobile.asremis.Entity.Chat;
import com.apreciasoft.mobile.asremis.R;
import com.apreciasoft.mobile.asremis.Util.Globales;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ChatMessageAdapter extends RecyclerView.Adapter<ChatMessageAdapter.MyViewHolder>{

    public static final int MSG_TYPE_LEFT=0;
    public static final int MSG_TYPE_RIGTH=1;

    private Context mContext;
    private List<Chat> mListChat;
    private String mMyId;


    public ChatMessageAdapter(Context context, List<Chat> listChat, String myId ){
     this.mContext=context;
     this.mListChat=listChat;
     this.mMyId=myId;
    }

    @NonNull
    @NotNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        int layout_id;
        if(viewType==MSG_TYPE_LEFT){
            layout_id = R.layout.chat_item_left;
        }else{
            layout_id = R.layout.chat_item_righ;
        }
        View view = LayoutInflater.from(mContext).inflate(layout_id,
                parent,
                false);
        return new MyViewHolder(view);
    }

    @Override
    public int getItemViewType(int position) {
        return mListChat.get(position).getSender().equals(mMyId)? MSG_TYPE_RIGTH:MSG_TYPE_LEFT;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull MyViewHolder holder, int position) {
        Chat chat = mListChat.get(position);
        holder.txtShowMessage.setText(chat.getMessage());
        holder.viewTime.setVisibility(View.GONE);
        holder.txtTime.setText(chat.getDateSent());
        holder.txtShowMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(holder.viewTime.getVisibility()==View.GONE){
                    holder.viewTime.setVisibility(View.VISIBLE);
                    //slideUp(holder.viewTime);
                }else{
                    holder.viewTime.setVisibility(View.GONE);
                    //slideDown(holder.viewTime);
                }
            }
        });
        checkIfMessageWasSent(holder, chat);
    }

    private void checkIfMessageWasSent(MyViewHolder holder, Chat chat){
        try{
            if(!chat.isWasSent()){
                holder.txtShowMessage.setBackgroundResource(R.drawable.chat_back_red);
                holder.viewTime.setVisibility(View.VISIBLE);
                holder.txtTime.setText(R.string.message_not_sent);
            }else{
                int resourceType = Globales.ChatMessageType.DRIVER_SENDER.equals(chat.getSender()) ? R.drawable.chat_back_right: R.drawable.chat_back_left;
                holder.txtShowMessage.setBackgroundResource(resourceType);
                holder.viewTime.setVisibility(View.GONE);
                holder.txtTime.setText(chat.getDateSent());
            }
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return mListChat.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView txtShowMessage;
        public View viewTime;
        public TextView txtTime;

        public MyViewHolder(View v) {
            super(v);
            txtShowMessage =  v.findViewById(R.id.txt_show_message);
            viewTime = v.findViewById(R.id.viewTime);
            txtTime=v.findViewById(R.id.txtTime);

        }
    }
}
