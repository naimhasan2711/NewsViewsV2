package newsviews.naimsplanet.com.newsviews.retrofit;

import newsviews.naimsplanet.com.newsviews.model.ArticleList;
import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiService
{
    @GET("v2/everything?q=apple&from=2018-12-29&to=2018-12-29&sortBy=popularity&apiKey=9cc62f9636b743039f95021245730b96")
    Call<ArticleList>getMyGson();
}
