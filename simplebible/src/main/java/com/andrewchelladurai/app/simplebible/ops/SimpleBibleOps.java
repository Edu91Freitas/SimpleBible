package com.andrewchelladurai.app.simplebible.ops;

public interface SimpleBibleOps {

  boolean showScreenHome();

  boolean showScreenBooksList();

  boolean showScreenSearch();

  boolean showScreenBookmarksList();

  boolean showScreenSettings();

  void hideKeyboard();

  void hideNavigationBar();

  void showNavigationBar();

}
