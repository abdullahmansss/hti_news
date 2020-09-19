package softagi.ss.news.network.remote;

import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
import softagi.ss.news.models.NewsModel;
import softagi.ss.news.models.PostModel;

public interface RetrofitHelper
{
    @GET("v2/top-headlines")
    Call<NewsModel> getNews(
            @Query("country") String country,
            @Query("category") String category,
            @Query("apiKey") String apiKey
    );

    @GET("posts")
    Call<List<PostModel>> getPosts(
            @Query("userId") int id
    );

    @POST("posts")
    @FormUrlEncoded
    Call<PostModel> insertPost(
            @Field("userId") String id,
            @Field("title") String title,
            @Field("body") String body
    );

    @POST("posts")
    Call<PostModel> insertPostRaw(
            @Body PostModel postModel
    );

    @POST("posts")
    @Multipart
    Call<PostModel> insertPostMulti(
            @Part MultipartBody.Part userId,
            @Part MultipartBody.Part title,
            @Part MultipartBody.Part body
    );
}