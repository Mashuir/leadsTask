package com.example.ecom.networks;

import com.example.ecom.models.Category;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

public interface ApiService {

    @GET("categories")
    Call<List<String>> getCategories();

}
