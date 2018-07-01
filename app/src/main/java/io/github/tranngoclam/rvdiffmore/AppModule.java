package io.github.tranngoclam.rvdiffmore;

import com.google.gson.Gson;

import java.util.concurrent.TimeUnit;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class AppModule {

  private static int DEFAULT_TIMEOUT_SECONDS = 30;

  @Named("api_ok_http_client")
  @Singleton
  @Provides
  OkHttpClient provideApiOkHttpClient(HttpLoggingInterceptor interceptor) {
    return new OkHttpClient.Builder()
        .connectTimeout(DEFAULT_TIMEOUT_SECONDS, TimeUnit.SECONDS)
        .readTimeout(DEFAULT_TIMEOUT_SECONDS, TimeUnit.SECONDS)
        .writeTimeout(DEFAULT_TIMEOUT_SECONDS, TimeUnit.SECONDS)
        .addInterceptor(interceptor)
        .build();
  }

  @Singleton
  @Provides
  HttpLoggingInterceptor provideHttpLoggingInterceptor() {
    HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
    interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
    return interceptor;
  }

  @Named("image_ok_http_client")
  @Singleton
  @Provides
  OkHttpClient provideImageOkHttpClient() {
    return new OkHttpClient.Builder()
        .connectTimeout(DEFAULT_TIMEOUT_SECONDS, TimeUnit.SECONDS)
        .readTimeout(DEFAULT_TIMEOUT_SECONDS, TimeUnit.SECONDS)
        .writeTimeout(DEFAULT_TIMEOUT_SECONDS, TimeUnit.SECONDS)
        .build();
  }

  @Singleton
  @Provides
  UserService provideUserService(@Named("api_ok_http_client") OkHttpClient okHttpClient) {
    return new Retrofit.Builder()
        .baseUrl("https://randomuser.me")
        .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
        .addConverterFactory(GsonConverterFactory.create(new Gson()))
        .client(okHttpClient)
        .build()
        .create(UserService.class);
  }
}
