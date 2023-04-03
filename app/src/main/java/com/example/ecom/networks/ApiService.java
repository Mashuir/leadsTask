package com.example.ecom.networks;

import com.example.ecom.models.Category;
import com.example.ecom.models.Product;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    @GET("categories")
    Call<List<String>> getCategories();

    @GET("category/{categoryName}")
    Call<List<Product>> getProductListByCategory(
            @Path("categoryName") String categoryName,
            @Query("limit") int limit,
            @Query("offset") int offset
    );

    @GET("{categoryID}")
    Call<Product> getProductDetails(@Path("categoryID") String categoryID);

}
