package com.andrewchelladurai.simplebible.ui;

import android.content.Context;
import android.os.Bundle;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.text.HtmlCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.andrewchelladurai.simplebible.R;
import com.andrewchelladurai.simplebible.data.entity.Bookmark;
import com.andrewchelladurai.simplebible.data.entity.Verse;
import com.andrewchelladurai.simplebible.model.ScreenBookListModel;
import com.andrewchelladurai.simplebible.model.ScreenBookmarkListModel;
import com.andrewchelladurai.simplebible.ui.adapter.ScreenBookmarkListAdapter;
import com.andrewchelladurai.simplebible.ui.ops.ScreenBookmarkListOps;
import com.andrewchelladurai.simplebible.ui.ops.ScreenSimpleBibleOps;
import com.google.android.material.chip.Chip;

public class ScreenBookmarkList
    extends Fragment
    implements ScreenBookmarkListOps {

  private static final String TAG = "ScreenBookmarkList";

  private ScreenSimpleBibleOps mainOps;
  private View rootView;
  private ScreenBookmarkListModel bookmarkListModel;
  private ScreenBookmarkListAdapter adapter;
  private ScreenBookListModel bookModel;

  public ScreenBookmarkList() {
    // TODO: 17/8/19 Beautify the Help Info display
  }

  @Override
  public void onAttach(@NonNull Context context) {
    super.onAttach(context);
    if (!(context instanceof ScreenSimpleBibleOps)) {
      throw new RuntimeException(context.toString() + " must implement InteractionListener");
    }
    final String templateEmptyNoteContent = getString(R.string.scr_bmark_list_msg_empty_note);

    mainOps = (ScreenSimpleBibleOps) context;

    adapter = new ScreenBookmarkListAdapter(this, templateEmptyNoteContent);
    bookmarkListModel = ViewModelProviders.of(this).get(ScreenBookmarkListModel.class);
    bookModel = ViewModelProviders.of(this).get(ScreenBookListModel.class);
  }

  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                           Bundle savedState) {
    rootView = inflater.inflate(R.layout.screen_bookmark_list, container, false);

    bookmarkListModel.getBookmarkList().observe(this, bookmarkList -> {
      if (bookmarkList == null || bookmarkList.isEmpty()) {
        hideList();
        showHelpInfo();
      } else {
        hideHelpInfo();
        adapter.clearList();
        adapter.updateList(bookmarkList);
        showList();
      }
    });

    mainOps.hideKeyboard();
    mainOps.showNavigationView();

    return rootView;
  }

  private void showList() {
    final RecyclerView recyclerView = rootView.findViewById(R.id.scr_bmark_list_list);
    recyclerView.setAdapter(adapter);
  }

  private void hideList() {
    rootView.findViewById(R.id.scr_bmark_list_list).setVisibility(View.GONE);
  }

  private void showHelpInfo() {
    final String rawText = getString(R.string.scr_bmark_list_help_txt);
    final Spanned htmlText = HtmlCompat.fromHtml(rawText, HtmlCompat.FROM_HTML_MODE_COMPACT);

    final TextView textView = rootView.findViewById(R.id.scr_bmark_list_help);
    textView.setText(htmlText);

    textView.setVisibility(View.VISIBLE);
    rootView.findViewById(R.id.scr_bmark_list_container_help).setVisibility(View.VISIBLE);
    rootView.findViewById(R.id.scr_bmark_list_image).setVisibility(View.VISIBLE);
  }

  private void hideHelpInfo() {
    rootView.findViewById(R.id.scr_bmark_list_image).setVisibility(View.GONE);
    rootView.findViewById(R.id.scr_bmark_list_help).setVisibility(View.GONE);
    rootView.findViewById(R.id.scr_bmark_list_container_help).setVisibility(View.GONE);
  }

  @Override
  public void onDetach() {
    super.onDetach();
    mainOps = null;
  }

  @Override
  public void updateVerseList(@NonNull final Chip verseCountChip,
                              @NonNull final TextView verseField,
                              @NonNull final String bookmarkReference) {
    bookmarkListModel.getBookmarkedVerse(bookmarkReference).observe(this, verseList -> {

      final String verseCountTemplate = getResources()
          .getQuantityString(
              R.plurals.itm_bmark_list_verse_count_template,
              verseList.size());
      verseCountChip.setText(String.format(verseCountTemplate, verseList.size()));

      if (verseList.isEmpty()) {
        final String message = getString(R.string.itm_bmark_list_verse_count_msg_no_verse);
        verseCountChip.setText(HtmlCompat.fromHtml(message, HtmlCompat.FROM_HTML_MODE_COMPACT));
        return;
      }

      final Verse verse = verseList.get(0);

      bookModel.getBookUsingNumber(verse.getBook()).observe(this, book -> {
        if (book == null) {
          final String message = getString(R.string.itm_bmark_list_verse_count_msg_no_verse);
          verseCountChip.setText(HtmlCompat.fromHtml(message, HtmlCompat.FROM_HTML_MODE_COMPACT));
          return;
        }

        final String template = getString(R.string.itm_bmark_list_first_verse_template);
        final String formattedString = String.format(template,
                                                     book.getName(),
                                                     verse.getChapter(),
                                                     verse.getVerse(),
                                                     verse.getText());
        final Spanned htmlText = HtmlCompat.fromHtml(formattedString,
                                                     HtmlCompat.FROM_HTML_MODE_COMPACT);
        verseField.setText(htmlText);
      });
    });
  }

  @Override
  public void handleBookmarkClick(@NonNull final Bookmark bookmark) {
    Log.d(TAG, "handleBookmarkClick: bookmark [" + bookmark + "]");

    final String reference = bookmark.getReference();
    bookmarkListModel.getBookmarkedVerse(reference).observe(this, verses -> {
      if (verses == null || verses.isEmpty()) {
        final String message = getString(R.string.scr_bmark_msg_no_verse);
        Log.e(TAG, "handleBookmarkClick: " + message);
        return;
      }

      final Verse[] array = new Verse[verses.size()];
      for (int i = 0; i < verses.size(); i++) {
        array[i] = verses.get(i);
      }

      Log.d(TAG, "handleBookmarkClick: got [" + array.length + "] verses");

      final Bundle bundle = new Bundle();
      bundle.putParcelableArray(ScreenBookmarkDetail.ARG_VERSE_LIST, array);
      Log.d(TAG, "handleBookmarkClick: created bundle to pass to fragment");

      NavHostFragment.findNavController(this)
                     .navigate(R.id.action_NavIdScreenBookmarkList_to_NavIdScreenBookmarkDetail,
                               bundle);
    });

  }

}
