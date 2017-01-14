package com.example.dungkunit.inventorymanagement;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.example.dungkunit.inventorymanagement.Model.Inventory;
import com.example.dungkunit.inventorymanagement.Model.InventoryAdapter;

import java.util.ArrayList;
import java.util.List;

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
        List<Inventory> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Inventory inventory = new Inventory(i + 1, "Demo Img", "Demo Title " + i, " " + i, "" + i);
            list.add(inventory);
        }
        mGridView.setAdapter(new InventoryAdapter(getActivity(), list));
        return view;
    }
}
