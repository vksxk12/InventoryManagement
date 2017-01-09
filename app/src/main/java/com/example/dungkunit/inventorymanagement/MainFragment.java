package com.example.dungkunit.inventorymanagement;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

/**
 * Created by dungkunit on 09/01/2017.
 */

public class MainFragment extends Fragment {
    private GridView mGridView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.activity_main, container, false);
        mGridView = (GridView) view.findViewById(R.id.list_view);
        View emptyView = view.findViewById(R.id.empty_view);
        mGridView.setEmptyView(emptyView);
        return view;
    }
}
