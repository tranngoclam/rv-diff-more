package io.github.tranngoclam.rvdiffmore;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Response<T> {

  @Expose @SerializedName("results") private T data;

  public T getData() {
    return data;
  }
}
