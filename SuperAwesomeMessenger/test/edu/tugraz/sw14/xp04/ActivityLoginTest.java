package edu.tugraz.sw14.xp04;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.EditText;

import com.robotium.solo.Solo;

import edu.tugraz.sw14.xp04.gcm.GCM;
import edu.tugraz.sw14.xp04.helpers.UserInfo;

public class ActivityLoginTest extends
		ActivityInstrumentationTestCase2<ActivityLogin> {

	private Solo solo;

	public ActivityLoginTest() {
		super(ActivityLogin.class);
	}

	@Override
	protected void setUp() throws Exception {
		solo = new Solo(getInstrumentation(), getActivity());
	}

	@Override
	protected void tearDown() throws Exception {
		solo.finishOpenedActivities();
	}

	public void testLoginOnFail() {
		EditText txtID = (EditText) solo.getView(R.id.a_login_txt_email);
		EditText txtPassword = (EditText) solo
				.getView(R.id.a_login_txt_password);
		
		GCM.storeIdPair(getInstrumentation().getContext(), new UserInfo("aabbccddeeff"));

		solo.enterText(txtID, "fail@user.sw");
		solo.enterText(txtPassword, "1234");

		solo.clickOnButton(solo.getString(R.string.a_login_btn_login));

		assertTrue(solo.waitForText(solo.getString(R.string.a_login_error_login)));
	}
	
	public void testLoginOnSuccess() {
		EditText txtID = (EditText) solo.getView(R.id.a_login_txt_email);
		EditText txtPassword = (EditText) solo
				.getView(R.id.a_login_txt_password);
		
		GCM.storeIdPair(getInstrumentation().getContext(), new UserInfo("aabbccddeeff"));

		solo.enterText(txtID, "success@user.sw");
		solo.enterText(txtPassword, "1234");

		solo.clickOnButton(solo.getString(R.string.a_login_btn_login));

		assertTrue(solo.waitForActivity(solo.getString(R.string.title_activity_activity_send_test_message)));
	}

	public void testLoginFailedIDMissing() {
		EditText txtID = (EditText) solo.getView(R.id.a_login_txt_email);
		EditText txtPassword = (EditText) solo
				.getView(R.id.a_login_txt_password);

		solo.enterText(txtID, "");
		solo.enterText(txtPassword, "1234");

		solo.clickOnButton(solo.getString(R.string.a_login_btn_login));

		assertTrue(solo.waitForText(solo.getString(R.string.a_login_error_login_email)));
	}

	public void testLoginFailedPasswordMissing() {
		EditText txtID = (EditText) solo.getView(R.id.a_login_txt_email);
		EditText txtPassword = (EditText) solo
				.getView(R.id.a_login_txt_password);

		solo.enterText(txtID, "test@gmail.com");
		solo.enterText(txtPassword, "");

		solo.clickOnButton(solo.getString(R.string.a_login_btn_login));

		assertTrue(solo.waitForText(solo.getString(R.string.a_login_error_login_password)));
	}

}
