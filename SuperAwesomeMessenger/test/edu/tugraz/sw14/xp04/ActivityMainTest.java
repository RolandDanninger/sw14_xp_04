package edu.tugraz.sw14.xp04;


import org.easymock.EasyMock;
import org.powermock.api.easymock.PowerMock;

import com.robotium.solo.Solo;

import edu.tugraz.sw14.xp04.controllers.NavigationController;
import android.test.ActivityInstrumentationTestCase2;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

public class ActivityMainTest extends ActivityInstrumentationTestCase2<ActivityMain> {

	private Solo solo;
	private NavigationController controllerMock;

	public ActivityMainTest() {
		super(ActivityMain.class);
	}
	
	
	@Override
	protected void setUp() throws Exception {
		System.setProperty("dexmaker.dexcache", getInstrumentation()
				.getTargetContext().getCacheDir().getPath());

		ActivityMain activity = getActivity();

		controllerMock = EasyMock.createMock(NavigationController.class);
		activity.getCurrentFragment().setController(controllerMock);
		
		solo = new Solo(getInstrumentation(), activity);
	}

	@Override
	protected void tearDown() throws Exception {
		solo.finishOpenedActivities();
	}
	
	
	
	public void testAddContactValidEmail(){
		String email = "a@bu.com";
		
		controllerMock.startAddContactTask(email);
		EasyMock.replay(controllerMock);
		
		View ndf = solo.getView(R.id.navigation_drawer);
		
		Button btnAdd = (Button)ndf.findViewById(R.id.nav_btn_add);
		EditText etMail = (EditText)ndf.findViewById(R.id.nav_et_add_mail);
		ImageButton btnAddGo = (ImageButton)ndf.findViewById(R.id.nav_btn_add_go);
		
		
		
		solo.clickOnMenuItem(solo.getString(R.string.menu));
		solo.clickOnView(btnAdd);
		solo.enterText(etMail, email);	
		solo.clickOnView(btnAddGo);
		
		for (int i = 0; i < Integer.MAX_VALUE / 8; i++) {
		}
		
		EasyMock.verify(controllerMock);
	}
}
