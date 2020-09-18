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

import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import softagi.ss.news.R;
import softagi.ss.news.database.RetrofitClient;
import softagi.ss.news.models.NewsModel;

public class MainActivity extends AppCompatActivity
{
    RecyclerView recyclerView;
    ProgressBar progressBar;

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

        Call<NewsModel> call = RetrofitClient.getInstance().getNews(
                "us",
                "business",
                "65f7f556ec76449fa7dc7c0069f040ca");

        call.enqueue(new Callback<NewsModel>() {
            @Override
            public void onResponse(Call<NewsModel> call, Response<NewsModel> response)
            {
                progressBar.setVisibility(View.GONE);
                recyclerView.setAdapter(new NewsAdapter(response.body().getArticles()));
            }

            @Override
            public void onFailure(Call<NewsModel> call, Throwable t)
            {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public class NewsAdapter extends RecyclerView.Adapter<NewsVH>
    {
        List<NewsModel.NewsDetails> detailsList;

        public NewsAdapter(List<NewsModel.NewsDetails> detailsList)
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
            NewsModel.NewsDetails m = detailsList.get(position);

            String image = m.getUrlToImage();
            String title = m.getTitle();
            String date = m.getPublishedAt();

            holder.title.setText(title);
            holder.date.setText(date);

            Picasso
                    .get()
                    .load(image)
                    .into(holder.image);
        }

        @Override
        public int getItemCount() {
            return detailsList.size();
        }
    }

    public class NewsVH extends RecyclerView.ViewHolder
    {
        ImageView image;
        TextView title,date;

        public NewsVH(@NonNull View itemView)
        {
            super(itemView);

            image = itemView.findViewById(R.id.news_image);
            title = itemView.findViewById(R.id.news_title);
            date = itemView.findViewById(R.id.news_date);
        }
    }
}