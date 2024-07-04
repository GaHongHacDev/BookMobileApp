package com.example.e_commerce.Activity.message;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.e_commerce.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MessagesAdapter extends RecyclerView.Adapter<MessagesAdapter.MyViewHolder> {

    private List<MessagesList> messageLists;
    private final Context context;

    public MessagesAdapter(List<MessagesList> messageLists, Context context) {
        this.messageLists = messageLists;
        this.context = context;
    }

    @NonNull
    @Override
    public MessagesAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.messages_adapter_layout, null));
    }

    @Override
    public void onBindViewHolder(@NonNull MessagesAdapter.MyViewHolder holder, int position) {

        try{
            MessagesList list2 = messageLists.get(position);

            if (!list2.getProfilePic().isEmpty()){
                Picasso.get().load(list2.getProfilePic()).into(holder.profilePic);
            }

            holder.name.setText(list2.getName());
            holder.lastMessages.setText(list2.getLastMessage());

            if (list2.getUnssenMessages() == 0){
                holder.unseenMessages.setVisibility(View.GONE);
            }
            else{
                holder.unseenMessages.setVisibility(View.VISIBLE);
            }

            holder.rootLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ChatMessageActivity.class);
                    intent.putExtra("mobile", list2.getMobile());
                    intent.putExtra("name", list2.getName());
                    intent.putExtra("profile_pic", list2.getProfilePic());
                    intent.putExtra("chat_key", list2.getMobile());
                    context.startActivity(intent);
                }
            });


        }catch(Exception e){
            Log.d("Error", e.getMessage());
        }


    }

    public void updateData(List<MessagesList> messagesLists){

        this.messageLists = messagesLists;
        notifyDataSetChanged();

    }

    @Override
    public int getItemCount() {
        return messageLists.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {

        private ImageView profilePic;
        private TextView name, lastMessages, unseenMessages;

        private LinearLayout rootLayout;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            profilePic = itemView.findViewById(R.id.profilePic);
            name = itemView.findViewById(R.id.name);
            lastMessages = itemView.findViewById(R.id.lastMessages);
            unseenMessages = itemView.findViewById(R.id.unseenMessages);
            rootLayout = itemView.findViewById(R.id.rootLayout);

        }
    }
}
