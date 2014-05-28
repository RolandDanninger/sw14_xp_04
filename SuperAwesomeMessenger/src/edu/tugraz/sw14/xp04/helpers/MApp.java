package edu.tugraz.sw14.xp04.helpers;

import java.util.concurrent.Callable;

import edu.tugraz.sw14.xp04.ActivityQuit;
import edu.tugraz.sw14.xp04.R;
import edu.tugraz.sw14.xp04.server.ServerConnection;
import android.app.Activity;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class MApp extends Application {

	private final ServerConnection serverConnection = new ServerConnection(
			ServerConnection.SERVER_URL);

	public ServerConnection getServerConnection() {
		return serverConnection;
	}

	public static MApp getApp(final Context context) {
		return (MApp) context.getApplicationContext();
	}

	public static void goToActivity(final Activity activity, Class target,
			boolean doFinish) {
		if (activity != null && target != null) {
			Intent intent = new Intent(activity, target);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			activity.startActivity(intent);
			activity.overridePendingTransition(android.R.anim.fade_in,
					android.R.anim.fade_out);
			if (doFinish)
				activity.finish();
		}
	}

	public static void goToActivity(final Activity activity, Intent target,
			boolean doFinish) {
		if (activity != null && target != null) {
			target.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			activity.startActivity(target);
			activity.overridePendingTransition(android.R.anim.fade_in,
					android.R.anim.fade_out);
			if (doFinish)
				activity.finish();
		}
	}
	
	public static void finishActivity(final Activity activity){
		activity.overridePendingTransition(android.R.anim.fade_in,
				android.R.anim.fade_out);
		activity.finish();
	}
	
    public static void quitApp(final Activity activity){
        activity.startActivity(new Intent(activity, ActivityQuit.class));
        activity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        activity.finish();
    }
	
	
	// NETWORK
    public static boolean isNetworkAvailable(final Context context) {
        ConnectivityManager cm = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        // if no network is available networkInfo will be null
        // otherwise check if we are connected
        return networkInfo != null && networkInfo.isConnected();
    }
    public static NetworkState getNetworkState(final Context context) {
        ConnectivityManager cm = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if(networkInfo != null ){
            if(networkInfo.isConnected()) return NetworkState.CONNECTED;
        }
        return NetworkState.DISCONNECTED;
    }

    // NetworkReceiver
    public static final String TAG_NetworkState = "NetworkState";
    public static enum NetworkState {
        UNKNOWN,
        CONNECTED,
        DISCONNECTED
    }
    public static class NetworkStateReceiver extends BroadcastReceiver{
        private Callable callable;

        public NetworkStateReceiver(Callable callable){
            this.callable = callable;
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent != null){
                String action = intent.getAction();
                if(action != null) {
                    if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
                        if (callable != null) try { callable.call(); }
                        catch (Exception e) { e.printStackTrace();}
                    }
                }
            }
        }
    }

    public static class NetworkStateFilter extends IntentFilter{
        public NetworkStateFilter(){
            this.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
            this.addAction("android.net.wifi.WIFI_STATE_CHANGED");
        }
    }
    public static void registerNetworkStateReceiver(Activity activity, NetworkStateReceiver receiver){
        if(activity != null){
            NetworkStateFilter filter = new NetworkStateFilter();
            activity.registerReceiver(receiver, filter);
        }
    }
    public static void unregisterNetworkStateReceiver(Activity activity, NetworkStateReceiver receiver){
        if(activity != null){
            activity.unregisterReceiver(receiver);
        }
    }


}
