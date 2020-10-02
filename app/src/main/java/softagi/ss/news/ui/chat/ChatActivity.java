package softagi.ss.news.ui.chat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import softagi.ss.news.R;
import softagi.ss.news.models.MessageModel;
import softagi.ss.news.models.UserModel;
import softagi.ss.news.ui.profile.ProfileActivity;
import softagi.ss.news.utils.Constants;

public class ChatActivity extends AppCompatActivity
{
    RecyclerView recyclerView;
    FloatingActionButton floatingActionButton;
    EditText messageField;
    List<MessageModel> modelList = new ArrayList<>();
    UserModel receiverModel;
    UserModel senderModel;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        receiverModel = (UserModel) getIntent().getSerializableExtra("user");
        initViews();
    }

    private void initViews()
    {
        recyclerView = findViewById(R.id.chat_recycler);
        floatingActionButton = findViewById(R.id.send_message_fab);
        messageField = findViewById(R.id.message_field);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String m = messageField.getText().toString();

                sendMessage(m);
            }
        });

        Constants.initRef().child("Chats").child(Constants.getUid(ChatActivity.this)).child(receiverModel.getuId()).addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                modelList.clear();

                for (DataSnapshot e : snapshot.getChildren())
                {
                    MessageModel m = e.getValue(MessageModel.class);
                    modelList.add(m);
                }

                recyclerView.setAdapter(new ChatAdapter(modelList));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {

            }
        });
    }

    private void sendMessage(final String m)
    {
        Constants.initRef().child("Users").child(Constants.getUid(ChatActivity.this)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                senderModel = snapshot.getValue(UserModel.class);

                MessageModel model = new MessageModel(m, senderModel.getName(), senderModel.getuId(), receiverModel.getName(), receiverModel.getuId());

                String key = Constants.initRef().child("Chats").child(Constants.getUid(ChatActivity.this)).child(receiverModel.getuId()).push().getKey();

                Constants.initRef().child("Chats").child(Constants.getUid(ChatActivity.this)).child(receiverModel.getuId()).child(key).setValue(model);
                Constants.initRef().child("Chats").child(receiverModel.getuId()).child(Constants.getUid(ChatActivity.this)).child(key).setValue(model);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {

            }
        });
    }

    public class ChatAdapter extends RecyclerView.Adapter<ChatVH>
    {
        List<MessageModel> messageModelList;

        public ChatAdapter(List<MessageModel> messageModelList)
        {
            this.messageModelList = messageModelList;
        }

        @NonNull
        @Override
        public ChatVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
        {
            View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.message_item, parent, false);
            return new ChatVH(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ChatVH holder, int position)
        {
            MessageModel model = messageModelList.get(position);

            String senderId = model.getSenderUid();
            String message = model.getMessage();

            if(senderId.equals(Constants.getUid(ChatActivity.this)))
            {
                holder.linearLayout.setGravity(Gravity.END);
                holder.textView.setBackgroundColor(getResources().getColor(R.color.senderColor));
            }

            holder.textView.setText(message);
        }

        @Override
        public int getItemCount() {
            return messageModelList.size();
        }
    }

    public class ChatVH extends RecyclerView.ViewHolder
    {
        TextView textView;
        LinearLayout linearLayout;

        public ChatVH(@NonNull View itemView)
        {
            super(itemView);

            textView = itemView.findViewById(R.id.message_text);
            linearLayout = itemView.findViewById(R.id.message_linear);
        }
    }
}