package edu.tugraz.sw14.xp04;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;

public class FragmentMainStart extends Fragment {

    private static final String ARG_OBJECT = "argument_object";

    private Context context;

    public static FragmentMainStart newInstance(Serializable arg) {
        FragmentMainStart fragment = new FragmentMainStart();
        Bundle args = new Bundle();
        args.putSerializable(ARG_OBJECT, arg);
        fragment.setArguments(args);
        return fragment;
    }

    public FragmentMainStart() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main_start, container, false);
        if(rootView != null){
            this.context = rootView.getContext();
            Bundle args = getArguments();
            if(args != null){
                Serializable arg_obj = getArguments().getSerializable(ARG_OBJECT);
                // do some with the argument of newInstance(arg)
                // ...
            }
            Context context = inflater.getContext();
            
            // ToDo Layout here
        }
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        //((ActivityMain) activity).onSectionAttached(getArguments().getString(getString(R.string.fr_main_start_title)));
    }
}
