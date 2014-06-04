package edu.tugraz.sw14.xp04;

import static org.easymock.EasyMock.createMock;

import org.easymock.EasyMock;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;
import android.widget.EditText;

import com.robotium.solo.Solo;

import edu.tugraz.sw14.xp04.controllers.RegistrationController;
import edu.tugraz.sw14.xp04.helpers.MApp;
import edu.tugraz.sw14.xp04.server.ServerConnection;
import edu.tugraz.sw14.xp04.stubs.RegistrationResponse;

public class ActivityRegistrationTest extends
		ActivityInstrumentationTestCase2<ActivityRegistration> {

	private Solo solo;
	private RegistrationController controllerMock;

	public ActivityRegistrationTest() {
		super(ActivityRegistration.class);
	}

	@Override
	protected void setUp() throws Exception {
		System.setProperty("dexmaker.dexcache", getInstrumentation()
				.getTargetContext().getCacheDir().getPath());

		ActivityRegistration activity = getActivity();

		controllerMock = createMock(RegistrationController.class);
		activity.setController(controllerMock);
		
		solo = new Solo(getInstrumentation(), activity);
	}

	@Override
	protected void tearDown() throws Exception {
		solo.finishOpenedActivities();
	}
	
	public void testOnRegistrationFinishedWithValidRegistration()
	{
		RegistrationResponse response = new RegistrationResponse();
		response.setError(false);
		response.setErrorMessage(null);
		
		getActivity().onRegistrationTaskFinished(response);
		assertTrue(solo.waitForActivity(ActivityLogin.class));
	}
	
	public void testOnRegistrationFinishedWithNullRegistration()
	{
		RegistrationResponse response = null;

		getActivity().onRegistrationTaskFinished(response);
		assertFalse(solo.waitForActivity(ActivityLogin.class, 1000));
	}
	
	public void testOnRegistrationFinishedWithRegistrationError()
	{
		RegistrationResponse response = new RegistrationResponse();
		response.setError(true);
		response.setErrorMessage(null);
		
		getActivity().onRegistrationTaskFinished(response);
		assertFalse(solo.waitForActivity(ActivityLogin.class, 1000));
	}
	
	

	public void testRegistrationTaskStarted() throws Exception {
		Log.d("ActivityRegistrationTest", "testRegistrationStarted");
		
		String email = "test@gmail.com";
		String password = "1234";

		controllerMock.startRegistrationTask(EasyMock.isA(String.class),
				EasyMock.isA(String.class));
		
		PowerMock.replayAll();

		ServerConnection connection = MApp.getApp(getActivity())
				.getServerConnection();
		new RegistrationController(getActivity(), connection);
		
		EditText txtID = (EditText) solo.getView(R.id.a_registration_txt_id);
		EditText txtPassword = (EditText) solo
				.getView(R.id.a_registration_txt_password);
		EditText txtReenterPassword = (EditText) solo
				.getView(R.id.a_registration_txt_reenter_password);

		solo.enterText(txtID, email);
		solo.enterText(txtPassword, password);
		solo.enterText(txtReenterPassword, password);

		solo.clickOnButton(solo.getString(R.string.a_registration_btn_register));

		for (int i = 0; i < Integer.MAX_VALUE / 8; i++) {
		}

		PowerMock.verifyAll();
	}

	public void testRegistrationFailedIDMissing() {
		EditText txtID = (EditText) solo.getView(R.id.a_registration_txt_id);
		EditText txtPassword = (EditText) solo
				.getView(R.id.a_registration_txt_password);
		EditText txtReenterPassword = (EditText) solo
				.getView(R.id.a_registration_txt_reenter_password);

		solo.enterText(txtID, "");
		solo.enterText(txtPassword, "1234");
		solo.enterText(txtReenterPassword, "1234");

		solo.clickOnButton(solo.getString(R.string.a_registration_btn_register));

		assertTrue(solo.waitForText(solo
				.getString(R.string.a_registration_error_missing_id)));
	}

	public void testRegistrationFailedPasswordMissing() {
		EditText txtID = (EditText) solo.getView(R.id.a_registration_txt_id);
		EditText txtPassword = (EditText) solo
				.getView(R.id.a_registration_txt_password);
		EditText txtReenterPassword = (EditText) solo
				.getView(R.id.a_registration_txt_reenter_password);

		solo.enterText(txtID, "test@gmail.com");
		solo.enterText(txtPassword, "");
		solo.enterText(txtReenterPassword, "1234");

		solo.clickOnButton(solo.getString(R.string.a_registration_btn_register));

		assertTrue(solo.waitForText(solo
				.getString(R.string.a_registration_error_missing_password)));
	}

	public void testRegistrationFailedPasswordsMismatch() {
		EditText txtID = (EditText) solo.getView(R.id.a_registration_txt_id);
		EditText txtPassword = (EditText) solo
				.getView(R.id.a_registration_txt_password);
		EditText txtReenterPassword = (EditText) solo
				.getView(R.id.a_registration_txt_reenter_password);

		solo.enterText(txtID, "test@gmail.com");
		solo.enterText(txtPassword, "1234");
		solo.enterText(txtReenterPassword, "2345");

		solo.clickOnButton(solo.getString(R.string.a_registration_btn_register));

		assertTrue(solo.waitForText(solo
				.getString(R.string.a_registration_error_passwords_mismatch)));
	}

	public void testRegistrationFailedInvalidID() {
		EditText txtID = (EditText) solo.getView(R.id.a_registration_txt_id);
		EditText txtPassword = (EditText) solo
				.getView(R.id.a_registration_txt_password);
		EditText txtReenterPassword = (EditText) solo
				.getView(R.id.a_registration_txt_reenter_password);

		solo.enterText(txtID, "test");
		solo.enterText(txtPassword, "1234");
		solo.enterText(txtReenterPassword, "1234");

		solo.clickOnButton(solo.getString(R.string.a_registration_btn_register));

		assertTrue(solo.waitForText(solo
				.getString(R.string.a_registration_error_invalid_id)));
	}

}
