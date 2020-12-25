package com.example.aegisproject;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.wenlong.aegis.annotation.Aegis;
import com.wenlong.aegis.core.interfaces.AegisIdentify;

public class FirstFragment extends Fragment implements AegisIdentify {

    private static final String TAG = "FirstFragment";
    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_first, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.button_first).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(FirstFragment.this)
                        .navigate(R.id.action_FirstFragment_to_SecondFragment);
            }
        });
        final View layoutBtn = view.findViewById(R.id.ll_btn);
        layoutBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.d(TAG, "onTouch");
                return false;
            }
        });
        layoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            @Aegis
            public void onClick(View view) {
                Log.e(TAG,  "click layout button");
            }
        });
    }

    @Override
    public String getIdentify() {
        return "123";
    }
}