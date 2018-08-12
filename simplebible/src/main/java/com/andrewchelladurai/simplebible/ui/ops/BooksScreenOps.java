package com.andrewchelladurai.simplebible.ui.ops;

import android.content.Context;

import com.andrewchelladurai.simplebible.data.entities.Book;

import androidx.annotation.NonNull;

/**
 * Created by : Andrew Chelladurai
 * Email : TheUnknownAndrew[at]GMail[dot]com
 * on : 11-Aug-2018 @ 7:16 PM.
 */
public interface BooksScreenOps {

    void onListFragmentInteraction(Book book);

    @NonNull
    Context getSystemContext();

    @NonNull
    String getFormattedBookListHeader(@NonNull Book book);

    @NonNull
    String getFormattedBookListDetails(@NonNull Book book);
}
