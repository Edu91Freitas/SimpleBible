package com.andrewchelladurai.app.simplebible.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.andrewchelladurai.app.simplebible.ops.ScreenSettingsOps;
import com.andrewchelladurai.app.simplebible.ops.SimpleBibleOps;
import com.andrewchelladurai.app.simplebible.viewmodels.SettingsScreenVM;
import com.andrewchelladurai.simplebible.R;

public class SettingsScreen
    extends Fragment
    implements ScreenSettingsOps {

  private SettingsScreenVM mViewModel;
  private SimpleBibleOps activityOps;

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
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                           @Nullable Bundle savedInstanceState) {
    return inflater.inflate(R.layout.settings_screen, container, false);
  }

  @Override
  public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    mViewModel = ViewModelProviders.of(this)
                                   .get(SettingsScreenVM.class);
    // TODO: Use the ViewModel
  }

  @Override
  public void onDetach() {
    super.onDetach();
    activityOps = null;
  }

}
