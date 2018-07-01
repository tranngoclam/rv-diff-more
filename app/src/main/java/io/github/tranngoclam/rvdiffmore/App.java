package io.github.tranngoclam.rvdiffmore;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.backends.okhttp3.OkHttpImagePipelineConfigFactory;
import com.facebook.imagepipeline.core.ImagePipelineConfig;

import android.app.Application;

import javax.inject.Inject;
import javax.inject.Named;

import okhttp3.OkHttpClient;

public class App extends Application {

  @Inject @Named("image_ok_http_client") OkHttpClient imageOkHttpClient;

  private AppComponent appComponent;

  @Override
  public void onCreate() {
    super.onCreate();

    if (appComponent == null) {
      appComponent = DaggerAppComponent.builder().appModule(new AppModule()).build();
    }
    appComponent.inject(this);

    ImagePipelineConfig imagePipelineConfig = OkHttpImagePipelineConfigFactory.newBuilder(this, imageOkHttpClient)
        .setDownsampleEnabled(true)
        .build();
    Fresco.initialize(this, imagePipelineConfig);
  }

  public AppComponent getAppComponent() {
    return appComponent;
  }
}
