package com.andrewchelladurai.app.simplebible.data;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;

import com.andrewchelladurai.app.simplebible.utils.BookUtils;

import java.util.Objects;

@Entity(tableName = "sb_verses", primaryKeys = {"book", "chapter", "verse"})
public class Verse
    implements Parcelable {

  public static final Creator<Verse> CREATOR = new Creator<Verse>() {
    @Override
    public Verse createFromParcel(Parcel in) {
      return new Verse(in);
    }

    @Override
    public Verse[] newArray(int size) {
      return new Verse[size];
    }
  };
  @NonNull
  @ColumnInfo(name = "translation")
  private final String translation;
  @ColumnInfo(name = "book")
  @IntRange(from = 1, to = BookUtils.EXPECTED_COUNT)
  private final int book;
  @ColumnInfo(name = "chapter")
  @IntRange(from = 1)
  private final int chapter;
  @ColumnInfo(name = "verse")
  @IntRange(from = 1)
  private final int verse;
  @NonNull
  @ColumnInfo(name = "text")
  private final String text;

  public Verse(@NonNull final String translation,
               @IntRange(from = 1, to = BookUtils.EXPECTED_COUNT) final int book,
               @IntRange(from = 1) final int chapter,
               @IntRange(from = 1) final int verse,
               @NonNull final String text) {
    this.translation = translation;
    this.book = book;
    this.chapter = chapter;
    this.verse = verse;
    this.text = text;
  }

  protected Verse(Parcel in) {
    translation = in.readString();
    book = in.readInt();
    chapter = in.readInt();
    verse = in.readInt();
    text = in.readString();
  }

  @Override
  public int hashCode() {
    return Objects.hash(getTranslation(), getBook(), getChapter(), getVerse(), getText());
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    final Verse verse1 = (Verse) o;
    return getBook() == verse1.getBook()
           && getChapter() == verse1.getChapter()
           && getVerse() == verse1.getVerse()
           && getTranslation().equals(verse1.getTranslation())
           && getText().equals(verse1.getText());
  }

  int getBook() {
    return book;
  }

  int getChapter() {
    return chapter;
  }

  int getVerse() {
    return verse;
  }

  @NonNull
  String getTranslation() {
    return translation;
  }

  @NonNull
  String getText() {
    return text;
  }

  @NonNull
  @Override
  public String toString() {
    return "Verse{"
           + "translation='" + translation + '\''
           + ", book=" + book
           + ", chapter=" + chapter
           + ", verse=" + verse
           + ", text='" + text + '\''
           + '}';
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(final Parcel dest, final int flags) {
    dest.writeString(translation);
    dest.writeInt(book);
    dest.writeInt(chapter);
    dest.writeInt(verse);
    dest.writeString(text);
  }

}
