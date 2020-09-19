package softagi.ss.news.database;

import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import softagi.ss.news.models.NewsModel;
import softagi.ss.news.models.PostModel;
import softagi.ss.news.network.remote.RetrofitHelper;

public class RetrofitClient
{
    private final static String BASE_URL = "https://jsonplaceholder.typicode.com/";
    private RetrofitHelper retrofitHelper;


    private static RetrofitClient retrofitClient;

    private RetrofitClient()
    {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        retrofitHelper = retrofit.create(RetrofitHelper.class);
    }

    public static RetrofitClient getInstance()
    {
        if (retrofitClient == null)
        {
            retrofitClient = new RetrofitClient();
        }

        return retrofitClient;
    }

    public Call<NewsModel> getNews(String country, String category, String key)
    {
        return retrofitHelper.getNews(country,category,key);
    }

    public Call<List<PostModel>> getPosts()
    {
        return retrofitHelper.getPosts(2);
    }

    public Call<PostModel> insertPost(String id, String title, String body)
    {
        return retrofitHelper.insertPost(id, title, body);
    }

    public Call<PostModel> insertPostRaw(PostModel postModel)
    {
        return retrofitHelper.insertPostRaw(postModel);
    }

    public Call<PostModel> insertPostMulti(MultipartBody.Part id, MultipartBody.Part title, MultipartBody.Part body)
    {
        return retrofitHelper.insertPostMulti(id, title, body);
    }
}