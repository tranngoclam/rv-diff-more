package io.github.tranngoclam.rvdiffmore;

import java.util.List;

import io.reactivex.Flowable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface UserService {

  @GET("/api/")
  Flowable<Response<List<User>>> getUsers(@Query("page") int page, @Query("results") int results);
}
