package softagi.ss.news.ui.main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import softagi.ss.news.R;
import softagi.ss.news.models.UserModel;
import softagi.ss.news.ui.authentication.Authentication;
import softagi.ss.news.ui.chat.ChatActivity;
import softagi.ss.news.ui.profile.ProfileActivity;
import softagi.ss.news.utils.Constants;

public class MainActivity extends AppCompatActivity
{
    RecyclerView recyclerView;
    ProgressBar progressBar;
    FloatingActionButton floatingActionButton;
    List<UserModel> userModels = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
    }

    private void initViews()
    {
        recyclerView = findViewById(R.id.users_recycler);
        progressBar = findViewById(R.id.progress_bar);
        floatingActionButton = findViewById(R.id.insert_fab);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(), Authentication.class));
                finish();
//                MultipartBody.Part id = MultipartBody.Part.createFormData("userId", "1");
//                MultipartBody.Part title = MultipartBody.Part.createFormData("title", "yyyyyy");
//                MultipartBody.Part body = MultipartBody.Part.createFormData("body", "body t");
//
//                Call<PostModel> call = RetrofitClient.getInstance().insertPostMulti(
//                        id,
//                        title,
//                        body
//                );
//
//                Call<PostModel> call3 = RetrofitClient.getInstance().putPost("1", new PostModel(""));
//                Call<PostModel> call4 = RetrofitClient.getInstance().patchPost("1",new PostModel(""));
//
//                call.enqueue(new Callback<PostModel>() {
//                    @Override
//                    public void onResponse(Call<PostModel> call, Response<PostModel> response) {
//                        Toast.makeText(MainActivity.this, response.body().getTitle() + "\n" + response.body().getBody() + "\n" + response.body().getId(), Toast.LENGTH_LONG).show();
//                    }
//
//                    @Override
//                    public void onFailure(Call<PostModel> call, Throwable t) {
//
//                    }
//                });
            }
        });

//        Call<List<PostModel>> call = RetrofitClient.getInstance().getPosts();
//
//        call.enqueue(new Callback<List<PostModel>>() {
//            @Override
//            public void onResponse(Call<List<PostModel>> call, Response<List<PostModel>> response)
//            {
//                progressBar.setVisibility(View.GONE);
//                recyclerView.setAdapter(new NewsAdapter(response.body()));
//            }
//
//            @Override
//            public void onFailure(Call<List<PostModel>> call, Throwable t)
//            {
//                progressBar.setVisibility(View.GONE);
//                Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
//            }
//        });

        Constants.initRef().child("Users").addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                userModels.clear();

                for (DataSnapshot e : snapshot.getChildren())
                {
                    UserModel u = e.getValue(UserModel.class);

                    if(!u.getuId().equals(Constants.getUid(MainActivity.this)))
                        userModels.add(u);
                }

                progressBar.setVisibility(View.GONE);
                recyclerView.setAdapter(new UsersAdapter(userModels));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {

            }
        });
    }

    public class UsersAdapter extends RecyclerView.Adapter<UsersVH>
    {
        List<UserModel> userModels;

        public UsersAdapter(List<UserModel> userModels)
        {
            this.userModels = userModels;
        }

        @NonNull
        @Override
        public UsersVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
        {
            View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.user_item, parent, false);
            return new UsersVH(view);
        }

        @Override
        public void onBindViewHolder(@NonNull UsersVH holder, int position)
        {
            final UserModel userModel = userModels.get(position);

            holder.name.setText(userModel.getName());
            holder.phone.setText(userModel.getPhone());

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
                    intent.putExtra("user", userModel);
                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount()
        {
            return userModels.size();
        }
    }

    public class UsersVH extends RecyclerView.ViewHolder
    {
        TextView name,phone;

        public UsersVH(@NonNull View itemView)
        {
            super(itemView);

            name = itemView.findViewById(R.id.name_text);
            phone = itemView.findViewById(R.id.phone_text);
        }
    }
}