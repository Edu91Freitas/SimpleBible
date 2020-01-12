package com.andrewchelladurai.app.simplebible;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

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
    Log.d(TAG, "onCreate:");
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_simple_bible);

    final BottomAppBar bar = findViewById(R.id.activity_bottom_app_bar);
    bar.setOnMenuItemClickListener(item -> {
      hideKeyboard();
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

    if (savedInstanceState == null) {
      // Need this only once when the application launches
      createNotificationChannel();
    }

  }

  private void createNotificationChannel() {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
      Log.d(TAG, "createNotificationChannel: Skipped coz feature not present in Android "
                 + "versions lower than Oreo / Android 8.0 (API level 26)");
      return;
    }

    NotificationChannel notificationChannel = new NotificationChannel(
        getPackageName(),
        getString(R.string.app_notif_channel_name),
        NotificationManager.IMPORTANCE_HIGH);
    notificationChannel.setDescription(getString(R.string.app_notif_channel_desc));

    final NotificationManager systemService = getSystemService(NotificationManager.class);
    if (systemService != null) {
      systemService.createNotificationChannel(notificationChannel);
      Log.d(TAG, "createNotificationChannel: created");
    } else {
      Log.e(TAG, "createNotificationChannel: failed [Null systemService received]");
    }
  }

  public boolean showScreenHome() {
    Log.d(TAG, "showScreenHome:");
    navController.navigate(R.id.show_screenHome);
    return true;
  }

  public boolean showScreenBooksList() {
    Log.d(TAG, "showScreenBooksList:");
    navController.navigate(R.id.show_screenBookList);
    return true;
  }

  public boolean showScreenSearch() {
    Log.d(TAG, "showScreenSearch:");
    navController.navigate(R.id.show_screenSearch);
    return true;
  }

  public boolean showScreenBookmarksList() {
    Log.d(TAG, "showScreenBookmarksList:");
    navController.navigate(R.id.show_screenBookmarkList);
    return true;
  }

  public boolean showScreenSettings() {
    Log.d(TAG, "showScreenSettings:");
    navController.navigate(R.id.show_screenSettings);
    return true;
  }

  public void hideKeyboard() {
    Log.d(TAG, "hideKeyboard:");
    final InputMethodManager inputMethodManager =
        (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);

    //Find the currently focused view, so we can grab the correct window token from it.
    //If no view currently has focus, create a new one, just so we can grab a window token from it
    final View view = (getCurrentFocus() != null) ? getCurrentFocus() : new View(this);

    if (inputMethodManager != null) {
      inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
  }

  @Override
  public void hideNavigationBar() {
    Log.d(TAG, "hideNavigationBar:");
    findViewById(R.id.activity_bottom_app_bar).setVisibility(View.GONE);
    findViewById(R.id.activity_bottom_app_bar_fab).setVisibility(View.GONE);
  }

  @Override
  public void showNavigationBar() {
    Log.d(TAG, "showNavigationBar:");
    findViewById(R.id.activity_bottom_app_bar).setVisibility(View.VISIBLE);
    findViewById(R.id.activity_bottom_app_bar_fab).setVisibility(View.VISIBLE);
  }

}
