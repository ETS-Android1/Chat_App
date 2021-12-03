package com.example.mychatapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mychatapp.R;
import com.example.mychatapp.models.MessageModel;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final Context context;
    private final List<MessageModel> chatMessages;
    private final String senderId;

    public static final int SENT_VIEW_TYPE = 1;
    public static final int RECEIVED_VIEW_TYPE = 2;

    public MessageAdapter(Context context, List<MessageModel> chatMessages, String senderId) {
        this.context = context;
        this.chatMessages = chatMessages;
        this.senderId = senderId;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == SENT_VIEW_TYPE) {
            return new SentMsgViewHolder(
                    LayoutInflater.from(context)
                    .inflate(R.layout.item_container_sent_msg, parent, false)
            );
        } else {
            return new ReceivedMsgViewHolder(
                    LayoutInflater.from(context)
                    .inflate(R.layout.item_container_received_msg, parent, false)
            );
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == SENT_VIEW_TYPE) {
            ((SentMsgViewHolder) holder).setData(chatMessages.get(position));
        } else {
            ((ReceivedMsgViewHolder) holder).setData(chatMessages.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return chatMessages.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (chatMessages.get(position).getSenderId().equals(senderId)) {
            return SENT_VIEW_TYPE;
        } else {
            return RECEIVED_VIEW_TYPE;
        }
    }

    static class SentMsgViewHolder extends RecyclerView.ViewHolder {
        private final TextView mSentMsg;
        private final TextView mSentMsgTime;

        public SentMsgViewHolder(@NonNull View itemView) {
            super(itemView);
            mSentMsg = itemView.findViewById(R.id.textViewSenderMsg);
            mSentMsgTime = itemView.findViewById(R.id.textViewSenderMsgTime);
        }

        public void setData(MessageModel messageModel) {
            mSentMsg.setText(messageModel.getMessage());
            mSentMsgTime.setText(messageModel.getTimestamp());
        }
    }

    static class ReceivedMsgViewHolder extends RecyclerView.ViewHolder {
        private final TextView mReceivedMsg;
        private final TextView mReceivedMsgTime;

        public ReceivedMsgViewHolder(@NonNull View itemView) {
            super(itemView);
            mReceivedMsg = itemView.findViewById(R.id.textViewReceiverMsg);
            mReceivedMsgTime = itemView.findViewById(R.id.textViewReceiverMsgTime);
        }

        public void setData(MessageModel messageModel) {
            mReceivedMsg.setText(messageModel.getMessage());
            mReceivedMsgTime.setText(messageModel.getTimestamp());
        }
    }
}
