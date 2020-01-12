package com.andrewchelladurai.app.simplebible.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.andrewchelladurai.app.simplebible.ui.ops.ScreenHomeOps;
import com.andrewchelladurai.app.simplebible.ui.ops.SimpleBibleOps;
import com.andrewchelladurai.simplebible.R;

public class ScreenHome
    extends Fragment
    implements ScreenHomeOps {

  private static final String TAG = "ScreenHome";

  private SimpleBibleOps activityOps;

  public ScreenHome() {
    // Required empty public constructor
  }

  @Override
  public void onAttach(@NonNull Context context) {
    super.onAttach(context);
    if (context instanceof SimpleBibleOps) {
      activityOps = (SimpleBibleOps) context;
    } else {
      throw new RuntimeException(context.toString() + " must implement SimpleBibleOps");
    }
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    final View view = inflater.inflate(R.layout.screen_home, container, false);
    return view;
  }

  @Override
  public void onDetach() {
    super.onDetach();
    activityOps = null;
  }

}
