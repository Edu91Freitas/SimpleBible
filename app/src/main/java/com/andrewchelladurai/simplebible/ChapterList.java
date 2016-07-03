/*
 *
 * This file 'ChapterList.java' is part of SimpleBible :
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

package com.andrewchelladurai.simplebible;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChapterList {

    private static final String             TAG          = "SB_ChapterList";
    private static final StringBuilder      mPrependText = new StringBuilder();
    private static final List<Entry>        ITEMS        = new ArrayList<>();
    private static final Map<String, Entry> ITEM_MAP     = new HashMap<>();

    private static String makeDetails(int position) {
        StringBuilder builder = new StringBuilder();
        builder.append("Details about Item: ").append(position);
        for (int i = 0; i < position; i++) {
            builder.append("\nMore details information here.");
        }
        return builder.toString();
    }

    public static void populateList(int chapterCount, String prependText) {
        Log.d(TAG, "populateList() called with: [" + chapterCount + "], [" + prependText + "]");
        if (mPrependText.length() < 1) {
            mPrependText.append(prependText);
        }
        ITEMS.clear();
        ITEM_MAP.clear();
        Entry entry;
        for (int i = 1; i <= chapterCount; i++) {
            entry = new Entry(String.valueOf(i), makeDetails(i));
            ITEMS.add(entry);
            ITEM_MAP.put(entry.content, entry);
        }
        Log.d(TAG, "populateList() returned");
    }

    public static int getCount() {
        return ITEMS.size();
    }

    public static List<Entry> getList() {
        return ITEMS;
    }

    public static Entry getItem(String keyValue) {
        return ITEM_MAP.get(keyValue);
    }

    public static class Entry
            implements Parcelable {

        private final String content;
        public final  String details;

        public Entry(String content, String details) {
            this.content = content;
            this.details = details;
        }

        protected Entry(Parcel in) {
            content = in.readString();
            details = in.readString();
        }

        public static final Creator<Entry> CREATOR = new Creator<Entry>() {

            @Override
            public Entry createFromParcel(Parcel in) {
                return new Entry(in);
            }

            @Override
            public Entry[] newArray(int size) {
                return new Entry[size];
            }
        };

        @Override
        public String toString() {
            return getContent();
        }

        @Override public int describeContents() {
            return 0;
        }

        @Override public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(content);
            dest.writeString(details);
        }

        public String getContent() {
            return mPrependText + " " + content;
        }
    }
}
