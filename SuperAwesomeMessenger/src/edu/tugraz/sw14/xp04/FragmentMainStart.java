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
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;

import edu.tugraz.sw14.xp04.adapters.ChatOverviewAdapter;
import edu.tugraz.sw14.xp04.adapters.ContactAdapter;
import edu.tugraz.sw14.xp04.contacts.Contact;
import edu.tugraz.sw14.xp04.database.Database;
import edu.tugraz.sw14.xp04.helpers.ChatOverview;
import edu.tugraz.sw14.xp04.helpers.MApp;

public class FragmentMainStart extends Fragment {

	private static final String ARG_OBJECT = "argument_object";
	public static final int DEFAULT_LIMIT = 50;

	private Context context;

	private ArrayList<ChatOverview> list;
	private ChatOverviewAdapter adapter;
	private ListView listView;
	private Button btn;

	private boolean chat_overviews_loaded = false;

	public boolean chatOverviewsLoaded() {
		return this.chat_overviews_loaded;
	}

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
		View rootView = inflater.inflate(R.layout.fragment_main_start,
				container, false);
		if (rootView != null) {
			this.context = rootView.getContext();
			Bundle args = getArguments();
			if (args != null) {
				Serializable arg_obj = getArguments().getSerializable(
						ARG_OBJECT);
				// do some with the argument of newInstance(arg)
				// ...
			}
			Context context = inflater.getContext();

			this.list = new ArrayList<ChatOverview>();

			this.listView = (ListView) rootView.findViewById(R.id.main_list);
			this.adapter = new ChatOverviewAdapter(getActivity(),
					R.layout.item_chat_overview, list);
			this.listView.setAdapter(this.adapter);
			loadChatOverviews(0);
		}
		return rootView;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		// ((ActivityMain)
		// activity).onSectionAttached(getArguments().getString(getString(R.string.fr_main_start_title)));
	}

	private void loadChatOverviews(int limit) {
		this.list.clear();
		Database db = new Database(getActivity());
		int limit_tmp = limit > 0 ? limit : DEFAULT_LIMIT;
		this.list.addAll(db.getAllMsgs(limit_tmp));

		adapter.notifyDataSetChanged();
		this.chat_overviews_loaded = true;

	}
}
