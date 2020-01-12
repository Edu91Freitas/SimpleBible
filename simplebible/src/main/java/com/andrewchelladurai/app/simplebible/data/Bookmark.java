package com.andrewchelladurai.app.simplebible.data;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Objects;

@Entity(tableName = "sb_bookmarks")
public class Bookmark
    implements Parcelable {

  public static final Creator<Bookmark> CREATOR = new Creator<Bookmark>() {
    @Override
    public Bookmark createFromParcel(Parcel in) {
      return new Bookmark(in);
    }

    @Override
    public Bookmark[] newArray(int size) {
      return new Bookmark[size];
    }
  };
  @PrimaryKey
  @ColumnInfo(name = "reference")
  @NonNull
  private final String reference;
  @ColumnInfo(name = "note")
  @NonNull
  private final String note;

  public Bookmark(@NonNull final String reference, @NonNull final String note) {
    this.reference = reference;
    this.note = note;
  }

  protected Bookmark(Parcel in) {
    reference = in.readString();
    note = in.readString();
  }

  @Override
  public int hashCode() {
    return Objects.hash(getReference(), getNote());
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    final Bookmark bookmark = (Bookmark) o;
    return getReference().equals(bookmark.getReference())
           && getNote().equals(bookmark.getNote());
  }

  @NonNull
  String getReference() {
    return reference;
  }

  @NonNull
  String getNote() {
    return note;
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(final Parcel dest, final int flags) {
    dest.writeString(reference);
    dest.writeString(note);
  }

}
