package com.andrewchelladurai.app.simplebible.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.andrewchelladurai.simplebible.R;

public class ScreenBookmarkDetails
    extends Fragment {

  private InteractionListener listener;

  public ScreenBookmarkDetails() {
    // Required empty public constructor
  }

  @Override
  public void onAttach(@NonNull Context context) {
    super.onAttach(context);
    if (context instanceof InteractionListener) {
      listener = (InteractionListener) context;
    } else {
      throw new RuntimeException(context.toString() + " must implement InteractionListener");
    }
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    return inflater.inflate(R.layout.screen_bookmark_details, container, false);
  }

  @Override
  public void onDetach() {
    super.onDetach();
    listener = null;
  }

  public interface InteractionListener {

  }

}
