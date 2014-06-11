package edu.tugraz.sw14.xp04;

import android.app.Activity;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;
import edu.tugraz.sw14.xp04.gcm.GCM;
import edu.tugraz.sw14.xp04.helpers.Encryption;
import edu.tugraz.sw14.xp04.helpers.EncryptionDES;
import edu.tugraz.sw14.xp04.helpers.MApp;
import edu.tugraz.sw14.xp04.helpers.MToast;
import edu.tugraz.sw14.xp04.helpers.ShPref;
import edu.tugraz.sw14.xp04.helpers.UserInfo;
import edu.tugraz.sw14.xp04.navigation.NavigationDrawerFragment;
import edu.tugraz.sw14.xp04.navigation.NavigationDrawerOrder;
import edu.tugraz.sw14.xp04.server.LoginTask;
import edu.tugraz.sw14.xp04.server.ServerConnection;
import edu.tugraz.sw14.xp04.server.LoginTask.LoginTaskListener;
import edu.tugraz.sw14.xp04.stubs.LoginRequest;
import edu.tugraz.sw14.xp04.stubs.LoginResponse;

import java.util.concurrent.Callable;

public class ActivityMain extends Activity implements
		NavigationDrawerFragment.NavigationDrawerCallbacks {

	private MApp.NetworkStateReceiver mNetworkStateReceiver;
	private MApp.NetworkState networkState = MApp.NetworkState.UNKNOWN;

	private Menu menu = null;

	private NavigationDrawerFragment mNavigationDrawerFragment;
	private CharSequence mTitle;

	private int curr_fragment = NavigationDrawerOrder.BTN_START;

	private Context context;
	private final MApp app = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		this.context = this;

		mNavigationDrawerFragment = (NavigationDrawerFragment) getFragmentManager()
				.findFragmentById(R.id.navigation_drawer);
		mTitle = getString(R.string.menu);

		// Set up the drawer.
		mNavigationDrawerFragment.setUp(R.id.navigation_drawer,
				(DrawerLayout) findViewById(R.id.drawer_layout));

		Callable updateUICallable = new Callable() {
			@Override
			public Object call() throws Exception {
				updateUI(MApp.getNetworkState(context));
				return null;
			}
		};
		mNetworkStateReceiver = new MApp.NetworkStateReceiver(updateUICallable);
	}

	public NavigationDrawerFragment getCurrentFragment() {
		return mNavigationDrawerFragment;
	}

	// NETWORK UI
	private void updateUI(final MApp.NetworkState state) {
		Log.d(MApp.TAG_NetworkState, "updateUI(): State -> " + state);
		switch (state) {
		case DISCONNECTED:
			notConnectedUI();
			break;
		case CONNECTED:
			connectedUI();
			break;
		default:
			break;
		}
		networkState = state;
	}

	private void notConnectedUI() {
		Log.d(MApp.TAG_NetworkState, "notConnectedUI()");
		if (this.menu != null) {
			MenuItem item = this.menu.getItem(0);
			if (item != null)
				item.setVisible(true);
		}
	}

	private void connectedUI() {
		Log.d(MApp.TAG_NetworkState, "connected()");
		if (this.menu != null) {
			MenuItem item = this.menu.getItem(0);
			if (item != null)
				item.setVisible(false);
		}
	}

	AlertDialog quit_dialog;
	// * QUICK_QUIT
	private final long[] quick_quit = new long[] { 0, 0, 0 };;

	@Override
	public void onBackPressed() {
		quick_quit[0] = quick_quit[1];
		quick_quit[1] = quick_quit[2];
		quick_quit[2] = System.currentTimeMillis();
		long diff = quick_quit[2] - quick_quit[0];
		if (diff > 200 && diff < 1000) {
			MApp.quitApp(ActivityMain.this);
			return;
		}

		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setCancelable(true);

		builder.setTitle(R.string.a_main_quit_app_title);
		builder.setMessage(R.string.a_main_quit_app_desc);

		builder.setPositiveButton(R.string.yes, new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				if (quit_dialog != null)
					quit_dialog.dismiss();
				MApp.quitApp(ActivityMain.this);

			}
		});
		builder.setNegativeButton(R.string.no, new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				if (quit_dialog != null)
					quit_dialog.dismiss();
			}
		});

		quit_dialog = builder.create();
		quit_dialog.show();
	}

	@Override
	public void onNavigationDrawerItemSelected(int position) {
		this.curr_fragment = position;
		switch (position) {
		case NavigationDrawerOrder.BTN_START:
			loadFragment(FragmentMainStart.newInstance(null), null);
			break;

		default:
			loadFragment(null, null);
			break;
		}
	}

	public void onSectionAttached(String Title) {
		if (Title != null)
			mTitle = Title;
	}

	public void restoreActionBar() {
		ActionBar actionBar = getActionBar();
		if (actionBar != null) {
			actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
			actionBar.setDisplayShowTitleEnabled(true);
			actionBar.setTitle(mTitle);
			actionBar.setIcon(R.drawable.ico_down);
			updateUI(MApp.getNetworkState(context));
			Log.d("ACTIONBAR", "restoreActionBar() called");
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		if (!mNavigationDrawerFragment.isDrawerOpen()) {
			// Only show items in the action bar relevant to this screen
			// if the drawer is not showing. Otherwise, let the drawer
			// decide what to show in the action bar.
			getMenuInflater().inflate(R.menu.activity_main, menu);
			this.menu = menu;
			restoreActionBar();
			MApp app = MApp.getApp(context);
			if (app.isLoggedIn()) {
				MenuItem item = menu.findItem(R.id.action_state);
				if (item != null)
					item.setIcon(R.drawable.ico_state_ready);
			}

			return true;
		}
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		switch (item.getItemId()) {
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	private void loadFragment(Fragment target_fragment, String tag) {
		if (target_fragment == null) {
			Context context = getApplicationContext();
			if (context != null) {
				Toast.makeText(context, getString(R.string.error),
						Toast.LENGTH_SHORT).show();
			}
		} else {
			// update the main content by replacing fragments
			FragmentManager fragmentManager = getFragmentManager();
			if (tag == null) {
				fragmentManager
						.beginTransaction()
						.replace(R.id.container, target_fragment)
						.setTransition(
								FragmentTransaction.TRANSIT_FRAGMENT_FADE)
						.commit();
			} else {
				fragmentManager
						.beginTransaction()
						.replace(R.id.container, target_fragment, tag)
						.setTransition(
								FragmentTransaction.TRANSIT_FRAGMENT_FADE)
						.commit();
			}
		}
	}

	// current handler
	private void handler_curr() {

	}

	private void doAutoLogin() {
		String email = ShPref.getShPrefString(context, "logininfo_email");
		String password = ShPref.getShPrefString(context, "logininfo_password");
		if ((email != null && !email.isEmpty())
				&& (password != null && !password.isEmpty())) {

			LoginRequest request = new LoginRequest();
			Encryption encryptor = new EncryptionDES();
			UserInfo info = GCM.loadIdPair(context);
			if (info == null) {
				Log.e("ActivityLogin", "UserInfo is null");
				return;
			}
			String gmcId = info.getGcmRegId();
			if (gmcId == null) {
				Log.e("ActivityLogin", "gmcId is null");
				return;
			}
			request.setGcmId(gmcId);
			request.setId(encryptor.encrypt(email));
			request.setPassword(encryptor.encrypt(password));

			MApp app = MApp.getApp(context);
			ServerConnection connection = app.getServerConnection();

			LoginTask loginTask = new LoginTask(connection, loginTaskListener);
			loginTask.execute(request);
		}
	}

	private final LoginTaskListener loginTaskListener = new LoginTaskListener() {

		@Override
		public void onPreExecute() {

		}

		@Override
		public void onPostExecute(LoginResponse response) {
			if (response != null) {
				if (!response.isError()) {
					MApp app = MApp.getApp(context);
					app.setLoggedIn();
					if (menu != null) {
						MenuItem item = menu.findItem(R.id.action_state);
						if (item != null)
							item.setIcon(R.drawable.ico_state_ready);
					}
				}
			}
		}
	};

	@Override
	protected void onResume() {
		Log.d("LIFECYCLE", "onResume");

		MApp app = MApp.getApp(this);
		if (app.isLoggedIn()) {
			if (menu != null) {
				MenuItem item = menu.findItem(R.id.action_state);
				if (item != null)
					item.setIcon(R.drawable.ico_state_ready);
			}
		} else
			doAutoLogin();

		super.onResume();
		this.overridePendingTransition(android.R.anim.fade_in,
				android.R.anim.fade_out);
		loadFragment(FragmentMainStart.newInstance(null), null);
		getCurrentFragment().refreshList();
		updateUI(MApp.getNetworkState(context));
		MApp.registerNetworkStateReceiver(this, mNetworkStateReceiver);
	}

	@Override
	protected void onPause() {
		Log.d("LIFECYCLE", "onPause");
		super.onPause();
		MApp.unregisterNetworkStateReceiver(this, mNetworkStateReceiver);
	}
}
