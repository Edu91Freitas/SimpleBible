package com.andrewchelladurai.simplebible.ui;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.andrewchelladurai.simplebible.R;
import com.andrewchelladurai.simplebible.data.VerseRepository;
import com.andrewchelladurai.simplebible.data.entities.Book;
import com.andrewchelladurai.simplebible.data.entities.Verse;
import com.andrewchelladurai.simplebible.presenter.ChapterScreenPresenter;
import com.andrewchelladurai.simplebible.ui.adapter.VerseListAdapter;
import com.andrewchelladurai.simplebible.ui.ops.ChapterScreenOps;
import com.andrewchelladurai.simplebible.util.Utilities;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

public class ChapterScreen
    extends AppCompatActivity
    implements ChapterScreenOps {

    public static final  String BOOK_NUMBER    = "BOOK_NUMBER";
    public static final  String CHAPTER_NUMBER = "CHAPTER_NUMBER";
    private static final String TAG            = "ChapterScreen";
    private static final Bundle ARGS           = new Bundle();
    private static VerseRepository verseRepository;
    private RecyclerView listView = null;
    private static ChapterScreenPresenter sPresenter;
    private static VerseListAdapter       sAdapter;

    @Override
    protected void onCreate(Bundle savedState) {
        super.onCreate(savedState);
        setContentView(R.layout.activity_chapter);

        if (savedState != null && savedState.containsKey(BOOK_NUMBER) && savedState.containsKey(
            CHAPTER_NUMBER)) {
            ARGS.putInt(BOOK_NUMBER, savedState.getInt(BOOK_NUMBER));
            ARGS.putInt(CHAPTER_NUMBER, savedState.getInt(CHAPTER_NUMBER));
        } else {
            ARGS.putInt(BOOK_NUMBER, getIntent().getIntExtra(BOOK_NUMBER, 0));
            ARGS.putInt(CHAPTER_NUMBER, getIntent().getIntExtra(CHAPTER_NUMBER, 0));
        }

        if (verseRepository == null) {
            verseRepository = ViewModelProviders.of(this).get(VerseRepository.class);
        }

        if (sPresenter == null) {
            sPresenter = new ChapterScreenPresenter(this);
        }

        if (sAdapter == null) {
            sAdapter = new VerseListAdapter(this);
        }

        BottomAppBar bar = findViewById(R.id.act_chapter_appbar);
        bar.setOnMenuItemClickListener(this);
        bar.replaceMenu(R.menu.chapter_screen_appbar);
        findViewById(R.id.act_chapter_fab).setOnClickListener(this);

        listView = findViewById(R.id.act_chapter_list);
        listView.setAdapter(sAdapter);

        showChapter(getBookToShow(), getChapterToShow());
    }

    @Override
    protected void onSaveInstanceState(final Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(BOOK_NUMBER, ARGS.getInt(BOOK_NUMBER));
        outState.putInt(CHAPTER_NUMBER, ARGS.getInt(CHAPTER_NUMBER));
    }

    private void showChapter(final int bookNumber, final int chapterNumber) {
        Log.d(TAG, "showChapter: bookNumber = [" + bookNumber + "], chapterNumber = ["
                   + chapterNumber + "]");
        if (verseRepository.validate(bookNumber, chapterNumber)) {
            Log.d(TAG, "showChapter: already cached");
            return;
        }

        ARGS.putInt(BOOK_NUMBER, bookNumber);
        ARGS.putInt(CHAPTER_NUMBER, chapterNumber);
        verseRepository.setUpNewChapter(bookNumber, chapterNumber);

        verseRepository.getLiveData()
                       .observe(this,
                                new Observer<List<Verse>>() {
                                    @Override
                                    public void onChanged(@Nullable final List<Verse> verses) {
                                        updateScreen(verses);
                                    }
                                });
        listView.scrollToPosition(0);
    }

    private void updateScreen(@Nullable final List<Verse> verses) {
        if (null == verses) {
            Log.e(TAG, "updateScreen: No verses returned");
            return;
        }

        if (verseRepository.populate(verses)) {
            updateTitle();
            sAdapter.updateList(verses);
            sAdapter.notifyDataSetChanged();
        } else {
            Log.e(TAG, "updateScreen: error updating UI");
        }
    }

    private void updateTitle() {
        final String title = getString(R.string.act_ch_title);
        final String bookName = Utilities.getInstance().getBookName(getBookToShow());
        TextView textView = findViewById(R.id.act_chapter_titlebar);
        textView.setText(String.format(title, bookName, getChapterToShow()));
    }

    @NonNull
    @Override
    public String getVerseTemplateString() {
        return getString(R.string.content_item_verse_text_template);
    }

    @NonNull
    @Override
    public Context getSystemContext() {
        return getApplicationContext();
    }

    @IntRange(from = 1, to = 66)
    @Override
    public int getBookToShow() {
        return (ARGS.containsKey(BOOK_NUMBER) ? ARGS.getInt(BOOK_NUMBER) : 0);
    }

    @IntRange(from = 1)
    @Override
    public int getChapterToShow() {
        return (ARGS.containsKey(CHAPTER_NUMBER) ? ARGS.getInt(CHAPTER_NUMBER) : 0);
    }

    public void handleInteractionClickChapter(@NonNull final Integer chapterNumber) {
/*
        if (chapterListDialog != null) {
            chapterListDialog.dismiss();
            chapterListDialog = null;
        }
*/
        showChapter(getBookToShow(), chapterNumber);
    }

    public void showErrorFirstChapter() {
        Snackbar.make(findViewById(R.id.act_chapter_list), R.string.act_ch_err_first_chapter,
                      Snackbar.LENGTH_SHORT).show();
    }

    public void showErrorLastChapter() {
        Snackbar.make(findViewById(R.id.act_chapter_list), R.string.act_ch_err_last_chapter,
                      Snackbar.LENGTH_SHORT).show();
    }

    public void showErrorEmptySelectedList() {
        Snackbar.make(findViewById(R.id.act_chapter_list), R.string.act_ch_err_empty_selection_list,
                      Snackbar.LENGTH_SHORT).show();
    }

    public void handleInteractionClickSettings() {
        throw new UnsupportedOperationException(getString(R.string.msg_unexpected));
    }

    private void showMessageDiscardSelectedVerses() {
        Snackbar.make(findViewById(R.id.act_chapter_list),
                      R.string.act_ch_msg_discarded_selected_verses,
                      Snackbar.LENGTH_SHORT).show();
    }

    private void showErrorInvalidBookmarkReference() {
        Snackbar.make(findViewById(R.id.act_chapter_list),
                      R.string.act_ch_err_invalid_bookmark_reference, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public boolean onMenuItemClick(final MenuItem item) {
        switch (item.getItemId()) {
            case R.id.act_chapter_appbar_menu_list:
                actionListClicked();
                break;
            case R.id.act_chapter_appbar_menu_prev:
                actionPrevClicked();
                break;
            case R.id.act_chapter_appbar_menu_next:
                actionNextClicked();
                break;
            case R.id.act_chapter_appbar_menu_bookmark:
                actionBookmarkClicked();
                break;
            case R.id.act_chapter_appbar_menu_clear:
                actionClearClicked();
                break;
            case R.id.act_chapter_appbar_menu_settings:
                actionSettingsClicked();
                break;
            default:
                Log.e(TAG, "onClick: Unhandled click event" + getString(R.string.msg_unexpected));
        }
        return true;
    }

    @Override
    public void onClick(final View view) {
        switch (view.getId()) {
            case R.id.act_chapter_fab:
                actionShareClicked();
                break;
            default:
                Log.e(TAG, "onClick: Unhandled click event" + getString(R.string.msg_unexpected));
        }
    }

    private void actionSettingsClicked() {
        Log.d(TAG, "actionSettingsClicked:");
    }

    private void actionClearClicked() {
        Log.d(TAG, "actionClearClicked:");
        boolean anyVerseSelected = sAdapter.isAnyVerseSelected();
        if (!anyVerseSelected) {
            showErrorEmptySelectedList();
            return;
        }
        anyVerseSelected = sAdapter.discardSelectedVerses();
        if (!anyVerseSelected) {
            showMessageDiscardSelectedVerses();
            sAdapter.notifyDataSetChanged();
        }
    }

    private void actionBookmarkClicked() {
        Log.d(TAG, "actionBookmarkClicked:");
/*
        final boolean anyVerseSelected = sAdapter.isAnyVerseSelected();
        if (!anyVerseSelected) {
            showErrorEmptySelectedList();
            return;
        }

        final ArrayList<Verse> list = VerseList.getInstance().getList();
        final ArrayList<Verse> selectedList = new ArrayList<>();
        for (Verse verse : list) {
            if (verse.isSelected()) {
                selectedList.add(verse);
            }
        }
        final String bookmarkReference = Utilities.getInstance().createBookmarkReference(
            selectedList);
        if (!bookmarkReference.isEmpty()) {
*/
/*
            Bundle bundle = new Bundle();
            bundle.putString(ActivityBookmark.REFERENCES, bookmarkReference);
            final Intent intent = new Intent(this, ActivityBookmark.class);
            intent.putExtras(bundle);

            if (sAdapter.isAnyVerseSelected()) {
                sAdapter.discardSelectedVerses();
                sAdapter.notifyDataSetChanged();
            }

            startActivity(intent);
*//*

        } else {
            Log.e(TAG,
                  "handleInteractionClickFabBookmark: invalid bookmarkReference created from getAllRecords of selected verses");
            showErrorInvalidBookmarkReference();
        }
*/
    }

    private void actionNextClicked() {
        Log.d(TAG, "actionNextClicked:");
        int newChapter = getChapterToShow() + 1;
        Book book = Utilities.getInstance().getBookUsingNumber(getBookToShow());
        if (book != null && newChapter > book.getChapters()) {
            showErrorLastChapter();
            return;
        }
        showChapter(getBookToShow(), newChapter);
    }

    private void actionPrevClicked() {
        Log.d(TAG, "actionPrevClicked:");
        int newChapter = getChapterToShow() - 1;
        if (newChapter < 1) {
            showErrorFirstChapter();
            return;
        }
        showChapter(getBookToShow(), newChapter);
    }

    private void actionListClicked() {
        Log.d(TAG, "actionListClicked:");
        final Book book = Utilities.getInstance().getBookUsingNumber(getBookToShow());
        if (book == null) {
            Log.e(TAG, "handleInteractionClickList: invalid book returned");
            return;
        }
/*
        chapterListDialog = ChapterListDialog.newInstance(this, book.getChapters());
        chapterListDialog.show(getSupportFragmentManager(), "ChapterListDialog");
*/
    }

    private void actionShareClicked() {
        Log.d(TAG, "actionShareClicked:");
/*
        final boolean anyVerseSelected = sAdapter.isAnyVerseSelected();
        if (!anyVerseSelected) {
            showErrorEmptySelectedList();
            return;
        }
        final StringBuilder verses = new StringBuilder();
        final ArrayList<Verse> list = VerseList.getInstance().getList();
        for (Verse verse : list) {
            if (verse.isSelected()) {
                verses.append(String.format(getString(R.string.content_item_verse_text_template),
                                            verse.getVerse(), verse.getText())).append("\n");
            }
        }
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, String
            .format(getString(R.string.act_ch_template_share), verses, getTitle().toString()));
        sendIntent.setType("text/plain");

        if (sAdapter.isAnyVerseSelected()) {
            sAdapter.discardSelectedVerses();
            sAdapter.notifyDataSetChanged();
        }

        startActivity(sendIntent);
*/
    }

    @Override
    public void handleInteractionClickVerseItem(final Verse verse) {
        verse.setSelected(!verse.isSelected());
    }

}
