package com.example.tride;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class matched extends Fragment {

    TextView name;
    String uname;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_matched, container, false);

        name=view.findViewById(R.id.matched_name);
        Bundle data = getArguments();
        assert data != null;
        uname = data.getString("uname");
        name.setText(uname);

        return view;



    }

}