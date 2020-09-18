package softagi.ss.news.network.remote;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import softagi.ss.news.models.NewsModel;

public interface RetrofitHelper
{
    @GET("v2/top-headlines")
    Call<NewsModel> getNews(
            @Query("country") String country,
            @Query("category") String category,
            @Query("apiKey") String apiKey
    );
}