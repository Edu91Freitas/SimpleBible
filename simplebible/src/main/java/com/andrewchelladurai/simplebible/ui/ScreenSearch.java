package com.andrewchelladurai.simplebible.ui;

import android.content.Context;
import android.os.Bundle;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.text.HtmlCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.andrewchelladurai.simplebible.R;
import com.andrewchelladurai.simplebible.data.entity.Verse;
import com.andrewchelladurai.simplebible.model.ScreenSearchModel;
import com.andrewchelladurai.simplebible.ui.adapter.SearchAdapter;
import com.andrewchelladurai.simplebible.ui.ops.ScreenSearchOps;
import com.andrewchelladurai.simplebible.ui.ops.ScreenSimpleBibleOps;
import com.google.android.material.bottomappbar.BottomAppBar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class ScreenSearch
    extends Fragment
    implements ScreenSearchOps {

  private static final String TAG = "ScreenSearch";

  private ScreenSimpleBibleOps mainOps;
  private View rootView;
  private ScreenSearchModel model;
  private SearchAdapter adapter;
  private String searchResultContentTemplate;

  public ScreenSearch() {
  }

  @Override
  public void onAttach(@NonNull Context context) {
    super.onAttach(context);
    if (!(context instanceof ScreenSimpleBibleOps)) {
      throw new RuntimeException(context.toString() + " must implement ScreenSimpleBibleOps");
    }
    mainOps = (ScreenSimpleBibleOps) context;
    model = ViewModelProviders.of(this).get(ScreenSearchModel.class);
    adapter = new SearchAdapter(this);
  }

  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                           Bundle savedState) {
    rootView = inflater.inflate(R.layout.screen_search, container, false);
    mainOps.showNavigationView();
    mainOps.hideKeyboard();

    searchResultContentTemplate = getString(R.string.itm_search_result_content_template);

    ((BottomAppBar) rootView.findViewById(R.id.scr_search_menu))
        .setOnMenuItemClickListener(item -> {
          switch (item.getItemId()) {
            case R.id.scr_search_menu_bookmark:
              handleClickActionBookmark();
              return true;
            case R.id.scr_search_menu_share:
              handleClickActionShare();
              return true;
            case R.id.scr_search_menu_clear:
              handleClickActionClear();
              return true;
            case R.id.scr_search_menu_reset:
              handleClickActionReset();
              return true;
            default:
              Log.d(TAG, "onNavigationItemSelected: [" + item.getTitle() + "] "
                         + "Unknown Item in screen search verse selection actions menu");
              return false;
          }
        });

    final SearchView searchView = rootView.findViewById(R.id.scr_search_input);
    searchView.setSubmitButtonEnabled(true);
    searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

      @Override
      public boolean onQueryTextSubmit(final String searchQuery) {
        handleClickActionSearch(searchQuery);
        return true;
      }

      @Override
      public boolean onQueryTextChange(final String newText) {
        return false;
      }
    });

    ((RecyclerView) rootView.findViewById(R.id.scr_search_list))
        .setAdapter(adapter);

    // first run - since we do not have a previously saved instance
    if (savedState == null) {
      Log.d(TAG, "onCreateView: empty state");
      showSearchDefaultUi();
    } else {
      if (adapter.getItemCount() == 0) {
        Log.d(TAG, "onCreateView: empty cached results");
        showSearchDefaultUi();
      } else {
        Log.d(TAG, "onCreateView: cached results exist");
        showSearchResultsUi();
        toggleSelectedActionsView();
      }
    }

    return rootView;
  }

  @Override
  public void onDetach() {
    super.onDetach();
    model = null;
    adapter = null;
    mainOps = null;
  }

  private void handleClickActionSearch(@NonNull final String searchText) {
    mainOps.hideKeyboard();
    if (searchText.equalsIgnoreCase("")
        || searchText.isEmpty()
        || searchText.length() < 4
        || searchText.length() > 50) {
      mainOps.showMessage(getString(R.string.scr_search_msg_input_invalid));
      showSearchDefaultUi();
      return;
    }

    Log.d(TAG, "handleClickActionSearch: searchQuery = [" + searchText + "]");
    model.searchTextInVerses(searchText).observe(this, list -> {
      if (list == null || list.isEmpty()) {
        mainOps.showMessage(getString(R.string.scr_search_msg_no_results));
        showSearchDefaultUi();
        return;
      }

      Log.d(TAG, "handleClickActionSearch: found [" + list.size() + "] verses");
      adapter.updateList(list);
      showSearchResultsUi();
    });
  }

  private void handleClickActionReset() {
    ((SearchView) rootView.findViewById(R.id.scr_search_input)).setQuery("", false);
    showSearchDefaultUi();
  }

  private void handleClickActionClear() {
    if (adapter.getSelectedItemCount() < 1) {
      mainOps.showMessage(getString(R.string.scr_search_msg_no_selection_clear));
      return;
    }
    adapter.clearSelection();
  }

  private void handleClickActionShare() {
    if (adapter.getSelectedItemCount() < 1) {
      mainOps.showMessage(getString(R.string.scr_search_msg_no_selection_share));
      return;
    }

    final StringBuilder shareText = new StringBuilder();

    // get the list of all verses that are selected and sort it
    final HashMap<Verse, String> versesMap = adapter.getSelectedVerses();
    final ArrayList<Verse> keySet = new ArrayList<>(versesMap.keySet());
    //noinspection unchecked
    Collections.sort(keySet);

    // now get the text from the selected verses
    for (final Verse verse : keySet) {
      shareText.append(versesMap.get(verse)).append("\n");
    }

    mainOps.shareText(String.format(getString(R.string.scr_search_template_share), shareText));
  }

  private void handleClickActionBookmark() {
    if (adapter.getSelectedItemCount() < 1) {
      mainOps.showMessage(getString(R.string.scr_search_msg_no_selection_bookmark));
      return;
    }

    // get the list of all verses that are selected and sort it
    final ArrayList<Verse> list = new ArrayList<>(adapter.getSelectedVerses().keySet());
    //noinspection unchecked
    Collections.sort(list);

    // now clear the selection, since our work is done.
    handleClickActionClear();

    // convert the list into an array
    final Verse[] array = new Verse[list.size()];
    for (int i = 0; i < array.length; i++) {
      array[i] = list.get(i);
    }

    // now create a bundle of the verses to pass to the Bookmark Screen
    final Bundle bundle = new Bundle();
    bundle.putParcelableArray(ScreenBookmarkDetail.ARG_VERSE_LIST, array);

    NavHostFragment.findNavController(this)
                   .navigate(R.id.action_screenSearch_to_screenBookmark, bundle);
  }

  private void showSearchDefaultUi() {
    Log.d(TAG, "showSearchDefaultUi:");
    adapter.clearList();

    final Spanned htmlText =
        HtmlCompat.fromHtml(getString(R.string.scr_search_tips_text),
                            HtmlCompat.FROM_HTML_MODE_LEGACY);
    final TextView textView = rootView.findViewById(R.id.scr_search_tips_text);
    textView.setText(htmlText);
    rootView.findViewById(R.id.scr_search_container_help).setVisibility(View.VISIBLE);
    rootView.findViewById(R.id.scr_search_container_result).setVisibility(View.GONE);
    mainOps.showNavigationView();
  }

  private void showSearchResultsUi() {
    Log.d(TAG, "showSearchResultsUi:");
    rootView.findViewById(R.id.scr_search_container_help).setVisibility(View.GONE);
    rootView.findViewById(R.id.scr_search_container_result).setVisibility(View.VISIBLE);
    rootView.findViewById(R.id.scr_search_menu).setVisibility(View.VISIBLE);

    updateTitle();
    mainOps.hideNavigationView();
  }

  private void updateTitle() {
    Log.d(TAG, "updateTitle:");
    final int resultCount = adapter.getItemCount();
    final String titleTemplate = getResources()
        .getQuantityString(R.plurals.scr_search_title_template, resultCount);
    final String formattedText = String.format(titleTemplate, resultCount);
    final Spanned htmlText = HtmlCompat.fromHtml(formattedText, HtmlCompat.FROM_HTML_MODE_COMPACT);
    final TextView titleView = rootView.findViewById(R.id.scr_search_title);
    titleView.setText(htmlText);
  }

  @Override
  public void updateSearchResultView(@NonNull Verse verse, @NonNull TextView textView) {
    model.getBook(verse.getBook()).observe(this, book -> {
      if (book == null) {
        Log.e(TAG, "updateSearchResultView: book not found for verse [" + verse + "]");
        return;
      }

      textView.setText(HtmlCompat.fromHtml(
          String.format(searchResultContentTemplate,
                        book.getName(),
                        verse.getChapter(),
                        verse.getVerse(),
                        verse.getText()),
          HtmlCompat.FROM_HTML_MODE_LEGACY));
    });
  }

  @Override
  public void toggleSelectedActionsView() {
    final BottomAppBar bAppBar = rootView.findViewById(R.id.scr_search_menu);
    final Menu menu = bAppBar.getMenu();

    final int selectedCount = adapter.getSelectedItemCount();

    rootView.findViewById(R.id.scr_search_title)
            .setVisibility(selectedCount > 0 ? View.GONE : View.VISIBLE);
    menu.setGroupVisible(R.id.scr_search_menu_container_selected, selectedCount > 0);
  }

}
