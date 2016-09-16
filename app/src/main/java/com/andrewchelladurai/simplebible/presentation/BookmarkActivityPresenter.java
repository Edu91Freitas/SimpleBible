/*
 *
 * This file 'BookmarkEntryActivityPresenter.java' is part of SimpleBible :
 *
 * Copyright (c) 2016.
 *
 * This Application is available at below locations
 * Binary : https://play.google.com/store/apps/developer?id=Andrew+Chelladurai
 * Source : https://github.com/andrewchelladurai/
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * OR <http://www.gnu.org/licenses/gpl-3.0.txt>
 *
 */

package com.andrewchelladurai.simplebible.presentation;

import android.util.Log;

import com.andrewchelladurai.simplebible.interaction.BookmarkActivityInterface;

/**
 * Created by Andrew Chelladurai - TheUnknownAndrew[at]GMail[dot]com on 17-Sep-2016 @ 12:48 AM
 */
public class BookmarkActivityPresenter {

    private static final String TAG = "SB_BA_Presenter";
    private BookmarkActivityInterface mInterface;

    public BookmarkActivityPresenter(BookmarkActivityInterface aInterface) {
        mInterface = aInterface;
        Log.d(TAG, "BookmarkActivityPresenter: init done");
    }

    public boolean buttonShareClicked() {
        Log.d(TAG, "buttonShareClicked() called");
        return true;
    }

    public boolean buttonDeleteClicked() {
        Log.d(TAG, "buttonDeleteClicked() called");
        return true;
    }

    public boolean buttonEditClicked() {
        Log.d(TAG, "buttonEditClicked() called");
        return true;
    }

    public boolean buttonSaveClicked() {
        Log.d(TAG, "buttonSaveClicked() called");
        return true;
    }
}