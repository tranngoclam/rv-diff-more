package io.github.tranngoclam.rvdiffmore;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Locale;

import androidx.annotation.Nullable;

public class User {

  @Expose @SerializedName("gender") private String gender;

  @Expose @SerializedName("id") private Id id;

  @Expose @SerializedName("name") private Name name;

  @Expose @SerializedName("picture") private Picture picture;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof User)) {
      return false;
    }

    User user = (User) o;

    if (getGender() != null ? !getGender().equals(user.getGender()) : user.getGender() != null) {
      return false;
    }
    if (getId() != null ? !getId().equals(user.getId()) : user.getId() != null) {
      return false;
    }
    if (getName() != null ? !getName().equals(user.getName()) : user.getName() != null) {
      return false;
    }
    return getPicture() != null ? getPicture().equals(user.getPicture()) : user.getPicture() == null;
  }

  @Override
  public int hashCode() {
    int result = getGender() != null ? getGender().hashCode() : 0;
    result = 31 * result + (getId() != null ? getId().hashCode() : 0);
    result = 31 * result + (getName() != null ? getName().hashCode() : 0);
    result = 31 * result + (getPicture() != null ? getPicture().hashCode() : 0);
    return result;
  }

  public String getGender() {
    return gender;
  }

  @Nullable
  public Id getId() {
    return id;
  }

  public long getIdAsLong() {
    return id != null ? Long.parseLong(id.getValue()) : 0L;
  }

  public Name getName() {
    return name;
  }

  public Picture getPicture() {
    return picture;
  }

  public static class Id {

    @Expose @SerializedName("name") private String name;

    @Expose @SerializedName("value") private String value;

    @Override
    public boolean equals(Object o) {
      if (this == o) {
        return true;
      }
      if (!(o instanceof Id)) {
        return false;
      }

      Id id = (Id) o;

      if (getName() != null ? !getName().equals(id.getName()) : id.getName() != null) {
        return false;
      }
      return getValue() != null ? getValue().equals(id.getValue()) : id.getValue() == null;
    }

    @Override
    public int hashCode() {
      int result = getName() != null ? getName().hashCode() : 0;
      result = 31 * result + (getValue() != null ? getValue().hashCode() : 0);
      return result;
    }

    public String getName() {
      return name;
    }

    public String getValue() {
      return value;
    }
  }

  public static class Name {

    @Expose @SerializedName("first") private String first;

    @Expose @SerializedName("last") private String last;

    @Expose @SerializedName("title") private String title;

    @Override
    public boolean equals(Object o) {
      if (this == o) {
        return true;
      }
      if (!(o instanceof Name)) {
        return false;
      }

      Name name = (Name) o;

      if (getFirst() != null ? !getFirst().equals(name.getFirst()) : name.getFirst() != null) {
        return false;
      }
      if (getLast() != null ? !getLast().equals(name.getLast()) : name.getLast() != null) {
        return false;
      }
      return getTitle() != null ? getTitle().equals(name.getTitle()) : name.getTitle() == null;
    }

    @Override
    public int hashCode() {
      int result = getFirst() != null ? getFirst().hashCode() : 0;
      result = 31 * result + (getLast() != null ? getLast().hashCode() : 0);
      result = 31 * result + (getTitle() != null ? getTitle().hashCode() : 0);
      return result;
    }

    @Override
    public String toString() {
      return String.format(Locale.getDefault(), "%s %s %s", title, first, last);
    }

    public String getFirst() {
      return first;
    }

    public String getLast() {
      return last;
    }

    public String getTitle() {
      return title;
    }
  }

  public static class Picture {

    @Expose @SerializedName("large") private String large;

    @Expose @SerializedName("medium") private String medium;

    @Expose @SerializedName("thumbnail") private String thumbnail;

    @Override
    public boolean equals(Object o) {
      if (this == o) {
        return true;
      }
      if (!(o instanceof Picture)) {
        return false;
      }

      Picture picture = (Picture) o;

      if (getLarge() != null ? !getLarge().equals(picture.getLarge()) : picture.getLarge() != null) {
        return false;
      }
      if (getMedium() != null ? !getMedium().equals(picture.getMedium()) : picture.getMedium() != null) {
        return false;
      }
      return getThumbnail() != null ? getThumbnail().equals(picture.getThumbnail()) : picture.getThumbnail() == null;
    }

    @Override
    public int hashCode() {
      int result = getLarge() != null ? getLarge().hashCode() : 0;
      result = 31 * result + (getMedium() != null ? getMedium().hashCode() : 0);
      result = 31 * result + (getThumbnail() != null ? getThumbnail().hashCode() : 0);
      return result;
    }

    public String getLarge() {
      return large;
    }

    public String getMedium() {
      return medium;
    }

    public String getThumbnail() {
      return thumbnail;
    }
  }
}
