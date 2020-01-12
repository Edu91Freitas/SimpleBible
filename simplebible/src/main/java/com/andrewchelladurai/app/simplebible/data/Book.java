package com.andrewchelladurai.app.simplebible.data;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;

import com.andrewchelladurai.app.simplebible.utils.BookUtils;

import java.util.Objects;

@Entity(tableName = "sb_books", primaryKeys = {"number", "name", "chapters", "verses"})
public class Book
    implements Parcelable {

  public static final Creator<Book> CREATOR = new Creator<Book>() {
    @Override
    public Book createFromParcel(Parcel in) {
      return new Book(in);
    }

    @Override
    public Book[] newArray(int size) {
      return new Book[size];
    }
  };
  @NonNull
  @ColumnInfo(name = "testament")
  private final String testament;
  @NonNull
  @ColumnInfo(name = "description")
  private final String description;
  @IntRange(from = 1, to = BookUtils.EXPECTED_COUNT)
  @ColumnInfo(name = "number")
  private final int number;
  @NonNull
  @ColumnInfo(name = "name")
  private final String name;
  @IntRange(from = 1)
  @ColumnInfo(name = "chapters")
  private final int chapters;
  @IntRange(from = 1)
  @ColumnInfo(name = "verses")
  private final int verses;

  public Book(@NonNull final String testament,
              @NonNull final String description,
              @IntRange(from = 1, to = BookUtils.EXPECTED_COUNT) final int number,
              @NonNull final String name,
              @IntRange(from = 1) final int chapters,
              @IntRange(from = 1) final int verses) {
    this.testament = testament;
    this.description = description;
    this.number = number;
    this.name = name;
    this.chapters = chapters;
    this.verses = verses;
  }

  protected Book(Parcel in) {
    testament = in.readString();
    description = in.readString();
    number = in.readInt();
    name = in.readString();
    chapters = in.readInt();
    verses = in.readInt();
  }

  @Override
  public int hashCode() {
    return Objects.hash(getTestament(), getDescription(), getNumber(), getName(), getChapters(),
                        getVerses());
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    final Book book = (Book) o;
    return getNumber() == book.getNumber()
           && getChapters() == book.getChapters()
           && getVerses() == book.getVerses()
           && getTestament().equals(book.getTestament())
           && getDescription().equals(book.getDescription())
           && getName().equals(book.getName());
  }

  @NonNull
  @Override
  public String toString() {
    return "Book{"
           + "testament='" + testament + '\''
           + ", description='" + description + '\''
           + ", number=" + number
           + ", name='" + name + '\''
           + ", chapters=" + chapters
           + ", verses=" + verses
           + '}';
  }

  int getNumber() {
    return number;
  }

  int getChapters() {
    return chapters;
  }

  int getVerses() {
    return verses;
  }

  @NonNull
  String getTestament() {
    return testament;
  }

  @NonNull
  String getDescription() {
    return description;
  }

  @NonNull
  String getName() {
    return name;
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(final Parcel dest, final int flags) {
    dest.writeString(testament);
    dest.writeString(description);
    dest.writeInt(number);
    dest.writeString(name);
    dest.writeInt(chapters);
    dest.writeInt(verses);
  }

}
