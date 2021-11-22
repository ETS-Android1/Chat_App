package com.example.mychatapp.utilities;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mychatapp.R;
import com.squareup.picasso.Picasso;

public class UserViewHolder extends RecyclerView.ViewHolder {
    private TextView mChatContainerUsername;
    private TextView mChatContainerLastMsg;
    private ImageView mChatContainerUserStatus;
    private ImageView mChatContainerImage;


    public UserViewHolder(@NonNull View itemView) {
        super(itemView);
        mChatContainerUsername = itemView.findViewById(R.id.chatContainerUsername);
        mChatContainerLastMsg = itemView.findViewById(R.id.chatContainerLastMsg);
        mChatContainerUserStatus = itemView.findViewById(R.id.chatContainerUserStatus);
        mChatContainerImage = itemView.findViewById(R.id.chatContainerImage);
    }

    public void setUsername(String username) {
        mChatContainerUsername.setText(username);
    }

    public void setImage(Context context, String url) {
        // Loading image from firebase to the image view
        Log.d("URL", url);
        if (url != null) {
            Picasso.with(context)
                    .load(url)
                    .placeholder(R.drawable.user)
                    .into(mChatContainerImage);
        }
    }

    public void setStatus(String status) {
        if (status.equals("Online")) {
            mChatContainerUserStatus.setVisibility(View.VISIBLE);
        } else {
            mChatContainerUserStatus.setVisibility(View.INVISIBLE);
        }
    }

    public void setLastMsg(String lastMsg) {
        mChatContainerLastMsg.setText(lastMsg);
    }
}
