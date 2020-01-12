package com.andrewchelladurai.app.simplebible;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.andrewchelladurai.app.simplebible.ops.SimpleBibleOps;
import com.andrewchelladurai.simplebible.R;
import com.google.android.material.bottomappbar.BottomAppBar;

public class SimpleBible
    extends AppCompatActivity
    implements SimpleBibleOps {

  private static final String TAG = "SimpleBible";
  private NavController navController;

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

    navController = Navigation.findNavController(findViewById(R.id.activity_nav_host_fragment));

  }

  private boolean showScreenHome() {
    Log.d(TAG, "showScreenHome:");
    navController.navigate(R.id.show_screenHome);
    return true;
  }

  private boolean showScreenBooksList() {
    Log.d(TAG, "showScreenBooksList:");
    navController.navigate(R.id.show_screenBookList);
    return true;
  }

  private boolean showScreenSearch() {
    Log.d(TAG, "showScreenSearch:");
    navController.navigate(R.id.show_screenSearch);
    return true;
  }

  private boolean showScreenBookmarksList() {
    Log.d(TAG, "showScreenBookmarksList:");
    navController.navigate(R.id.show_screenBookmarkList);
    return true;
  }

  private boolean showScreenSettings() {
    Log.d(TAG, "showScreenSettings:");
    navController.navigate(R.id.show_screenSettings);
    return true;
  }

}
