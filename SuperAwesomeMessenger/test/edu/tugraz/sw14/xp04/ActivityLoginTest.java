package edu.tugraz.sw14.xp04;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.EditText;

import com.robotium.solo.Solo;

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

	public void testRegistrationSuccessfull() {
		EditText txtID = (EditText) solo.getView(R.id.a_registration_txt_id);
		EditText txtPassword = (EditText) solo
				.getView(R.id.a_registration_txt_password);
		EditText txtReenterPassword = (EditText) solo
				.getView(R.id.a_registration_txt_reenter_password);

		solo.enterText(txtID, "test@gmail.com");
		solo.enterText(txtPassword, "1234");
		solo.enterText(txtReenterPassword, "1234");

		solo.clickOnButton(solo.getString(R.string.a_registration_btn_register));

		assertTrue(solo.waitForText("Registration successfull!"));
	}

	public void testLoginFailedIDMissing() {
		EditText txtID = (EditText) solo.getView(R.id.a_login_txt_email);
		EditText txtPassword = (EditText) solo
				.getView(R.id.a_login_txt_password);

		solo.enterText(txtID, "");
		solo.enterText(txtPassword, "1234");

		solo.clickOnButton(solo.getString(R.string.a_login_btn_login));

		assertTrue(solo.waitForText("Email oder Passwort falsch!"));
	}

	public void testLoginFailedPasswordMissing() {
		EditText txtID = (EditText) solo.getView(R.id.a_login_txt_email);
		EditText txtPassword = (EditText) solo
				.getView(R.id.a_login_txt_password);

		solo.enterText(txtID, "test@gmail.com");
		solo.enterText(txtPassword, "");

		solo.clickOnButton(solo.getString(R.string.a_login_btn_login));

		assertTrue(solo.waitForText("Email oder Passwort falsch!"));
	}

}
