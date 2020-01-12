package com.andrewchelladurai.app.simplebible;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.andrewchelladurai.app.simplebible.ui.ScreenBookDetails;
import com.andrewchelladurai.app.simplebible.ui.ScreenBookList;
import com.andrewchelladurai.app.simplebible.ui.ScreenBookmarkDetails;
import com.andrewchelladurai.app.simplebible.ui.ScreenBookmarkList;
import com.andrewchelladurai.app.simplebible.ui.ScreenChapter;
import com.andrewchelladurai.app.simplebible.ui.ScreenHome;
import com.andrewchelladurai.app.simplebible.ui.ScreenMessage;
import com.andrewchelladurai.app.simplebible.ui.ScreenSearch;
import com.andrewchelladurai.app.simplebible.ui.ScreenSettings;
import com.andrewchelladurai.simplebible.R;

public class SimpleBible
    extends AppCompatActivity
    implements ScreenBookDetails.InteractionListener,
               ScreenBookList.InteractionListener,
               ScreenBookmarkDetails.InteractionListener,
               ScreenBookmarkList.InteractionListener,
               ScreenChapter.InteractionListener,
               ScreenHome.InteractionListener,
               ScreenMessage.InteractionListener,
               ScreenSearch.InteractionListener,
               ScreenSettings.InteractionListener {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_simple_bible);
  }

}
