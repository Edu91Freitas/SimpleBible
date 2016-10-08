package com.andrewchelladurai.simplebible.interaction;

import android.content.Context;

import com.andrewchelladurai.simplebible.presentation.SimpleBibleActivityPresenter;

import java.io.InputStreamReader;

/**
 * Created by Andrew Chelladurai - TheUnknownAndrew[at]GMail[dot]com on 05-Sep-2016 @ 4:55 PM
 */
public interface SimpleBibleActivityOperations
        extends BasicOperations {

    Context getThisApplicationContext();

    String getTabTitle(int position);

    String[] getBookNameChapterCountArray();

    SimpleBibleActivityPresenter getPresenter();

    InputStreamReader getMainScript();

    InputStreamReader getDowngradeScript();

    InputStreamReader getUpgradeScript();
}
