package com.andrewchelladurai.simplebible.data;

import android.app.Application;
import android.util.Log;

import com.andrewchelladurai.simplebible.data.entities.Verse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import androidx.annotation.IntRange;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class VerseRepository
    extends AndroidViewModel
    implements RepositoryOps {

    private static final String                 TAG         = "VerseRepository";
    private static final ArrayList<Verse>       sVersesList = new ArrayList<>();
    private static final HashMap<String, Verse> sVersesMap  = new HashMap<>();
    private static       LiveData<List<Verse>>  sLiveData   = new MutableLiveData<>();
    private static VerseRepository THIS_INSTANCE;
    private static int currentBook    = 0;
    private static int currentChapter = 0;

    public VerseRepository(final Application application) {
        super(application);
        THIS_INSTANCE = this;
    }

    public static VerseRepository getInstance() {
        return THIS_INSTANCE;
    }

    @Override
    public boolean populate(final List<?> list) {
        clear();
        Verse verse;
        for (final Object object : list) {
            verse = (Verse) object;
            sVersesList.add((Verse) object);
            sVersesMap.put(verse.getReference(), verse);
        }

        Log.d(TAG, "populate: cache now has [" + size()
                   + "] records for [book]:[chapter] = [" + currentBook + "]["
                   + currentChapter + "]");
        return true;
    }

    @Override
    public void clear() {
        sVersesList.clear();
        sVersesMap.clear();
        //  currentBook = currentChapter = 0;
    }

    @Override
    public boolean isEmpty() {
        return sVersesList.isEmpty() & sVersesMap.isEmpty();
    }

    @Override
    public int size() {
        return (sVersesMap.size() == sVersesList.size()) ? sVersesList.size() : -1;
    }

    @Override
    public Object getRecordUsingKey(final Object key) {
        final String reference = (String) key;
        if (sVersesMap.containsKey(reference)) {
            return sVersesMap.get(reference);
        }
        return null;
    }

    @Override
    public Object getRecordUsingValue(final Object value) {
        final String msg = "Do not look for verse using value";
        throw new UnsupportedOperationException(msg);
    }

    @Override
    public List<Verse> getList() {
        return sVersesList;
    }

    @Override
    public LiveData<List<Verse>> getLiveData() {
        if (currentBook == 0 || currentChapter == 0) {
            throw new UnsupportedOperationException("currentBook || currentChapter = 0");
        }
        sLiveData = SbDatabase.getInstance(getApplication()).getVerseDao()
                              .getChapter(currentBook, currentChapter);
        Log.d(TAG, "getLiveData: populated new chapter");
        return sLiveData;
    }

    @Override
    public boolean validate(final Object... objects) {
        if (isEmpty()) {
            Log.d(TAG, "validate: not cached - isEmpty");
            return false;
        }

        final int book = (int) objects[0];
        final int chapter = (int) objects[0];

        //noinspection RedundantIfStatement
        if (book != currentBook || chapter != currentChapter) {
            Log.d(TAG, "validate: not cached - book != currentBook | chapter != currentChapter");
            return false;
        }
        Log.d(TAG,
              "validate: already cached data [book=" + currentBook
              + "][chapter=" + currentChapter + "]");
        return true;
    }

    public LiveData<List<Verse>> getLiveData(@IntRange(from = 1, to = 66) int book,
                                             @IntRange(from = 1) int chapter) {
        if (validate(book, chapter)) {
            Log.d(TAG, "getLiveData: returned cached data");
            return sLiveData;
        }

        currentBook = book;
        currentChapter = chapter;

        return getLiveData();
    }
}
