package com.andrewchelladurai.app.simplebible.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.andrewchelladurai.app.simplebible.ui.ops.ScreenSettingsOps;
import com.andrewchelladurai.app.simplebible.ui.ops.SimpleBibleOps;
import com.andrewchelladurai.simplebible.R;

public class ScreenSettings
    extends Fragment
    implements ScreenSettingsOps {

  private SimpleBibleOps activityOps;

  public ScreenSettings() {
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
    return inflater.inflate(R.layout.screen_settings, container, false);
  }

  @Override
  public void onDetach() {
    super.onDetach();
    activityOps = null;
  }

}
