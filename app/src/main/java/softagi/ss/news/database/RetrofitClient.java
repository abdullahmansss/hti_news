package softagi.ss.news.database;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import softagi.ss.news.models.NewsModel;
import softagi.ss.news.network.remote.RetrofitHelper;

public class RetrofitClient
{
    private final static String BASE_URL = "http://newsapi.org/";
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
}