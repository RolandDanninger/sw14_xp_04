package edu.tugraz.sw14.xp04.navigation;

import java.util.ArrayList;

import com.google.android.gms.internal.et;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.text.Editable;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import edu.tugraz.sw14.xp04.ActivityMain;
import edu.tugraz.sw14.xp04.ActivitySendTestMessage;
import edu.tugraz.sw14.xp04.R;
import edu.tugraz.sw14.xp04.adapters.ContactAdapter;
import edu.tugraz.sw14.xp04.contacts.Contact;
import edu.tugraz.sw14.xp04.controllers.NavigationController;
import edu.tugraz.sw14.xp04.database.Database;
import edu.tugraz.sw14.xp04.gcm.GCM;
import edu.tugraz.sw14.xp04.helpers.MApp;
import edu.tugraz.sw14.xp04.helpers.MToast;
import edu.tugraz.sw14.xp04.helpers.UserInfo;
import edu.tugraz.sw14.xp04.server.AddContactTask;
import edu.tugraz.sw14.xp04.server.LoginTask;
import edu.tugraz.sw14.xp04.server.ServerConnection;
import edu.tugraz.sw14.xp04.server.AddContactTask.AddContactTaskListener;
import edu.tugraz.sw14.xp04.stubs.AddContactRequest;
import edu.tugraz.sw14.xp04.stubs.AddContactResponse;
import edu.tugraz.sw14.xp04.stubs.ContactStub;
import edu.tugraz.sw14.xp04.stubs.LoginRequest;

/**
 * Fragment used for managing interactions for and presentation of a navigation
 * drawer. See the <a href=
 * "https://developer.android.com/design/patterns/navigation-drawer.html#Interaction"
 * > design guidelines</a> for a complete explanation of the behaviors
 * implemented here.
 */
public class NavigationDrawerFragment extends Fragment {

	/**
	 * Remember the position of the selected item.
	 */
	private static final String STATE_SELECTED_POSITION = "selected_navigation_drawer_position";

	/**
	 * Per the design guidelines, you should show the drawer on launch until the
	 * user manually expands it. This shared preference tracks this.
	 */
	private static final String PREF_USER_LEARNED_DRAWER = "navigation_drawer_learned";

	/**
	 * A pointer to the current callbacks instance (the Activity).
	 */
	private NavigationDrawerCallbacks mCallbacks;

	/**
	 * Helper component that ties the action bar to the navigation drawer.
	 */
	private ActionBarDrawerToggle mDrawerToggle;

	private DrawerLayout mDrawerLayout;
	private View mLayout;
	private View mFragmentContainerView;

	private int mCurrentSelectedPosition = 0;
	private boolean mFromSavedInstanceState;
	private boolean mUserLearnedDrawer;

	private Activity context;

	private ArrayList<Contact> list;
	private ContactAdapter adapter;
	private ListView listView;
	private Button addBtn;
	private LinearLayout form;
	private ImageButton addBtnGo;
	private EditText etEmail;

	private ProgressDialog dialog;

	private boolean contacts_loaded = false;

	private NavigationController controller;

	public boolean contactsLoaded() {
		return this.contacts_loaded;
	}

	public NavigationDrawerFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Read in the flag indicating whether or not the user has demonstrated
		// awareness of the
		// drawer. See PREF_USER_LEARNED_DRAWER for details.
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(getActivity());
		mUserLearnedDrawer = sp.getBoolean(PREF_USER_LEARNED_DRAWER, false);

		if (savedInstanceState != null) {
			mCurrentSelectedPosition = savedInstanceState
					.getInt(STATE_SELECTED_POSITION);
			mFromSavedInstanceState = true;
		}

		// Select either the default item (0) or the last selected item.
		selectItem(mCurrentSelectedPosition);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		// Indicate that this fragment would like to influence the set of
		// actions in the action bar.
		setHasOptionsMenu(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		mLayout = inflater.inflate(R.layout.fragment_navigation_drawer,
				container, false);
		if (mLayout == null)
			return null;
		this.context = getActivity();

		MApp app = MApp.getApp(context);
		ServerConnection connection = app.getServerConnection();

		setController(new NavigationController(this, connection, new Database(context)));

		this.addBtn = (Button) mLayout.findViewById(R.id.nav_btn_add);
		if (this.addBtn != null) {
			this.addBtn.setVisibility(View.VISIBLE);
			this.addBtn.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (form != null)
						form.setVisibility(View.VISIBLE);
					if (v != null)
						v.setVisibility(View.INVISIBLE);

				}
			});
		}

		this.form = (LinearLayout) mLayout.findViewById(R.id.nav_lin_add_form);
		if (this.form != null) {
			this.form.setVisibility(View.INVISIBLE);
		}
		this.etEmail = (EditText) mLayout.findViewById(R.id.nav_et_add_mail);
		this.addBtnGo = (ImageButton) mLayout.findViewById(R.id.nav_btn_add_go);
		if (this.addBtnGo != null) {
			this.addBtnGo.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (!MApp.isNetworkAvailable(getActivity())) {
						MToast.errorNetwork(getActivity(), true);
					}
					if (etEmail != null) {
						Editable eaEmail = etEmail.getText();
						if (eaEmail != null) {
							String id = eaEmail.toString();
							if (id != null && !id.isEmpty())
								doAddContact(id);
							else
								MToast.errorAddContactEmail(getActivity(), true);
						}
					}

				}
			});
		}

		this.list = new ArrayList<Contact>();
		this.listView = (ListView) mLayout.findViewById(R.id.nav_list);
		this.adapter = new ContactAdapter(getActivity(), R.layout.item_contact,
				this.list);
		this.listView.setAdapter(this.adapter);
		loadContacts();

		return mLayout;
	}

	private void loadContacts() {
		// TODO read from database (sort by date??)
		this.list.clear();
		Database db = new Database(getActivity());

		this.list.addAll(db.getAllContacts());
		this.adapter.notifyDataSetChanged();
		this.contacts_loaded = true;
	}

	public boolean isDrawerOpen() {
		return mDrawerLayout != null
				&& mDrawerLayout.isDrawerOpen(mFragmentContainerView);
	}

	/**
	 * Users of this fragment must call this method to set up the navigation
	 * drawer interactions.
	 * 
	 * @param fragmentId
	 *            The android:id of this fragment in its activity's layout.
	 * @param drawerLayout
	 *            The DrawerLayout containing this fragment's UI.
	 */
	public void setUp(int fragmentId, DrawerLayout drawerLayout) {
		mFragmentContainerView = getActivity().findViewById(fragmentId);
		mDrawerLayout = drawerLayout;

		// set a custom shadow that overlays the main content when the drawer
		// opens
		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
				GravityCompat.START);
		// set up the drawer's list view with items and click listener

		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setHomeButtonEnabled(true);

		// ActionBarDrawerToggle ties together the the proper interactions
		// between the navigation drawer and the action bar app icon.
		mDrawerToggle = new ActionBarDrawerToggle(getActivity(), /* host Activity */
		mDrawerLayout, /* DrawerLayout object */
		R.drawable.ic_drawer, /* nav drawer image to replace 'Up' caret */
		R.string.navigation_drawer_open, /*
										 * "open drawer" description for
										 * accessibility
										 */
		R.string.navigation_drawer_close /*
										 * "close drawer" description for
										 * accessibility
										 */
		) {
			@Override
			public void onDrawerClosed(View drawerView) {
				super.onDrawerClosed(drawerView);
				if (!isAdded()) {
					return;
				}

				getActivity().invalidateOptionsMenu(); // calls
														// onPrepareOptionsMenu()
			}

			@Override
			public void onDrawerOpened(View drawerView) {
				super.onDrawerOpened(drawerView);
				if (!isAdded()) {
					return;
				}

				if (!mUserLearnedDrawer) {
					// The user manually opened the drawer; store this flag to
					// prevent auto-showing
					// the navigation drawer automatically in the future.
					mUserLearnedDrawer = true;
					SharedPreferences sp = PreferenceManager
							.getDefaultSharedPreferences(getActivity());
					sp.edit().putBoolean(PREF_USER_LEARNED_DRAWER, true)
							.apply();
				}

				getActivity().invalidateOptionsMenu(); // calls
														// onPrepareOptionsMenu()
			}
		};

		// If the user hasn't 'learned' about the drawer, open it to introduce
		// them to the drawer,
		// per the navigation drawer design guidelines.
		if (!mUserLearnedDrawer && !mFromSavedInstanceState) {
			mDrawerLayout.openDrawer(mFragmentContainerView);
		}

		// Defer code dependent on restoration of previous instance state.
		mDrawerLayout.post(new Runnable() {
			@Override
			public void run() {
				mDrawerToggle.syncState();
			}
		});

		mDrawerLayout.setDrawerListener(mDrawerToggle);
	}

	public void selectItemAtPos(int position) {
		selectItem(position);
	}

	private void selectItem(int position) {
		mCurrentSelectedPosition = position;
		if (mDrawerLayout != null) {
			mDrawerLayout.closeDrawer(mFragmentContainerView);
		}
		if (mCallbacks != null) {
			mCallbacks.onNavigationDrawerItemSelected(position);
		}
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mCallbacks = (NavigationDrawerCallbacks) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(
					"Activity must implement NavigationDrawerCallbacks.");
		}
	}

	@Override
	public void onDetach() {
		super.onDetach();
		mCallbacks = null;
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt(STATE_SELECTED_POSITION, mCurrentSelectedPosition);
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Forward the new configuration the drawer toggle component.
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// If the drawer is open, show the global app actions in the action bar.
		// See also
		// showGlobalContextActionBar, which controls the top-left area of the
		// action bar.
		boolean open = isDrawerOpen();
		if (mDrawerLayout != null && open) {
			inflater.inflate(R.menu.global, menu);
			boolean connected = true;
			Activity activity = getActivity();
			if (activity != null) {
				Context context = activity.getApplicationContext();
				if (context != null) {
					switch (MApp.getNetworkState(context)) {
					case DISCONNECTED:
						connected = false;
						break;
					default:
						break;
					}

					if (menu != null) {
						MenuItem item = menu.getItem(0);
						if (item != null)
							item.setVisible(!connected);
					}
				}
			}
			showGlobalContextActionBar();
		}
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * Per the navigation drawer design guidelines, updates the action bar to
	 * show the global app 'context', rather than just what's in the current
	 * screen.
	 */
	private void showGlobalContextActionBar() {
		ActionBar actionBar = getActionBar();
		if (actionBar != null) {
			actionBar.setDisplayShowTitleEnabled(true);
			actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
			actionBar.setTitle(R.string.app_name_short);
			actionBar.setIcon(R.drawable.icon_sam);
		}
	}

	private ActionBar getActionBar() {
		return getActivity().getActionBar();
	}

	/**
	 * Callbacks interface that all activities using this fragment must
	 * implement.
	 */
	public static interface NavigationDrawerCallbacks {
		/**
		 * Called when an item in the navigation drawer is selected.
		 */
		void onNavigationDrawerItemSelected(int position);
	}

	public void setController(NavigationController controller){
		this.controller = controller;
	}
	
	protected void doAddContact(String id) {
		controller.startAddContactTask(id);
	}

	public void onAddContactTaskStarted() {
		dialog = new ProgressDialog(context);
		dialog.setCancelable(false);
		dialog.show();
		dialog.setContentView(new ProgressBar(context));
	}

	public void onAddContactTaskFailed() {
		MToast.errorUserAlreadyExists(getActivity(), true);
	}

	public void onAddContactTaskFinished(NavigationController.State state) {
		if (dialog != null)
			dialog.dismiss();
		
		switch(state){
		
		case SUCCESS:
			if (form != null)
				form.setVisibility(View.INVISIBLE);
			if (etEmail != null)
				etEmail.setText("");
			if (addBtn != null)
				addBtn.setVisibility(View.VISIBLE);

			loadContacts();
			break;
		case ERROR: MToast.error(context, true);
			break;
		case FAILED: MToast.errorAddContact(context, true);
			break;
		}
	}
}
