package com.andrewchelladurai.app.simplebible;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.andrewchelladurai.app.simplebible.ui.ops.SimpleBibleOps;
import com.andrewchelladurai.simplebible.R;
import com.google.android.material.bottomappbar.BottomAppBar;

public class SimpleBible
    extends AppCompatActivity
    implements SimpleBibleOps {

  private static final String TAG = "SimpleBible";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_simple_bible);

    final BottomAppBar bar = findViewById(R.id.activity_bottom_app_bar);
    bar.setOnMenuItemClickListener(item -> {
      switch (item.getItemId()) {
        case R.id.activity_bottom_app_bar_menu_home:
          return showScreenHome();
        case R.id.activity_bottom_app_bar_menu_book_list:
          return showScreenBooksList();
        case R.id.activity_bottom_app_bar_menu_search:
          return showScreenSearch();
        case R.id.activity_bottom_app_bar_menu_bookmark_list:
          return showScreenBookmarksList();
        case R.id.activity_bottom_app_bar_menu_settings:
          return showScreenSettings();
        default:
          return false;
      }
    });

  }

  private boolean showScreenHome() {
    Log.d(TAG, "showScreenHome:");
    return false;
  }

  private boolean showScreenBooksList() {
    Log.d(TAG, "showScreenBooksList:");
    return false;
  }

  private boolean showScreenSearch() {
    Log.d(TAG, "showScreenSearch:");
    return false;
  }

  private boolean showScreenBookmarksList() {
    Log.d(TAG, "showScreenBookmarksList:");
    return false;
  }

  private boolean showScreenSettings() {
    Log.d(TAG, "showScreenSettings:");
    return false;
  }

}
