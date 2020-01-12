package com.andrewchelladurai.app.simplebible.ui;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.andrewchelladurai.app.simplebible.ops.LoadScreenOps;
import com.andrewchelladurai.app.simplebible.ops.SimpleBibleOps;
import com.andrewchelladurai.app.simplebible.viewmodels.LoadScreenVM;
import com.andrewchelladurai.simplebible.R;

public class LoadScreen
    extends Fragment
    implements LoadScreenOps {

  private static final String TAG = "LoadScreen";

  private LoadScreenVM vModel;
  private SimpleBibleOps activityOps;

  @Override
  public void onAttach(@NonNull Context context) {
    Log.d(TAG, "onAttach:");
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
    Log.d(TAG, "onCreateView:");
    final View view = inflater.inflate(R.layout.load_screen, container, false);
    activityOps.hideNavigationBar();
    return view;
  }

  @Override
  public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    Log.d(TAG, "onActivityCreated:");
    super.onActivityCreated(savedInstanceState);
    vModel = ViewModelProviders.of(this)
                               .get(LoadScreenVM.class);
    // TODO: Use the ViewModel
  }

  @Override
  public void onDetach() {
    Log.d(TAG, "onDetach:");
    super.onDetach();
    activityOps = null;
  }

}
