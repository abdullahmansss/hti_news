package softagi.ss.news.ui.main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import softagi.ss.news.R;
import softagi.ss.news.database.RetrofitClient;
import softagi.ss.news.models.NewsModel;
import softagi.ss.news.models.PostModel;

public class MainActivity extends AppCompatActivity
{
    RecyclerView recyclerView;
    ProgressBar progressBar;
    FloatingActionButton floatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
    }

    private void initViews()
    {
        recyclerView = findViewById(R.id.news_recycler);
        progressBar = findViewById(R.id.progress_bar);
        floatingActionButton = findViewById(R.id.insert_fab);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                MultipartBody.Part id = MultipartBody.Part.createFormData("userId", "1");
                MultipartBody.Part title = MultipartBody.Part.createFormData("title", "yyyyyy");
                MultipartBody.Part body = MultipartBody.Part.createFormData("body", "body t");


                Call<PostModel> call = RetrofitClient.getInstance().insertPostMulti(
                        id,
                        title,
                        body
                );

                call.enqueue(new Callback<PostModel>() {
                    @Override
                    public void onResponse(Call<PostModel> call, Response<PostModel> response) {
                        Toast.makeText(MainActivity.this, response.body().getTitle() + "\n" + response.body().getBody() + "\n" + response.body().getId(), Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onFailure(Call<PostModel> call, Throwable t) {

                    }
                });
            }
        });

        Call<List<PostModel>> call = RetrofitClient.getInstance().getPosts();

        call.enqueue(new Callback<List<PostModel>>() {
            @Override
            public void onResponse(Call<List<PostModel>> call, Response<List<PostModel>> response)
            {
                progressBar.setVisibility(View.GONE);
                recyclerView.setAdapter(new NewsAdapter(response.body()));
            }

            @Override
            public void onFailure(Call<List<PostModel>> call, Throwable t)
            {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public class NewsAdapter extends RecyclerView.Adapter<NewsVH>
    {
        List<PostModel> detailsList;

        public NewsAdapter(List<PostModel> detailsList)
        {
            this.detailsList = detailsList;
        }

        @NonNull
        @Override
        public NewsVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
        {
            View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.news_item, parent, false);
            return new NewsVH(view);
        }

        @Override
        public void onBindViewHolder(@NonNull NewsVH holder, int position)
        {
            PostModel m = detailsList.get(position);

            //String image = m.getUrlToImage();
            String title = m.getTitle();
            String body = m.getBody();

            holder.title.setText(title);
            holder.date.setText(body);

//            Picasso
//                    .get()
//                    .load(image)
//                    .into(holder.image);
        }

        @Override
        public int getItemCount() {
            return detailsList.size();
        }
    }

    public class NewsVH extends RecyclerView.ViewHolder
    {
        //ImageView image;
        TextView title,date;

        public NewsVH(@NonNull View itemView)
        {
            super(itemView);

            //image = itemView.findViewById(R.id.news_image);
            title = itemView.findViewById(R.id.news_title);
            date = itemView.findViewById(R.id.news_date);
        }
    }
}