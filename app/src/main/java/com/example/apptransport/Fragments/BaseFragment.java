package com.example.apptransport.Fragments;

import androidx.fragment.app.Fragment;

public class BaseFragment extends Fragment {

    /**
     * Could handle back press.
     * @return true if back press was handled
     */
    public boolean onBackPressed() {
        return false;
    }
}
