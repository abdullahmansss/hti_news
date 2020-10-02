package softagi.ss.news.network.remote;

import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
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

    @POST("demo/themes/Discy/Boxed/api/user/edit_profile")
    @FormUrlEncoded
    Call<PostModel> editProfile(
            @Header("Authorization") String token,
            @Field("name") String name,
            @Field("password") String password,
            @Field("email") String email,
            @Field("description") String description
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

    @PUT("posts/{id}")
    Call<PostModel> putPost(
            @Path("id") String id,
            @Body PostModel postModel
    );

    @PATCH("posts/{id}")
    Call<PostModel> patchPost(
            @Path("id") String id,
            @Body PostModel postModel
    );

    @DELETE("posts/{id}")
    Call<PostModel> deletePost(
            @Path("id") String id
    );
}