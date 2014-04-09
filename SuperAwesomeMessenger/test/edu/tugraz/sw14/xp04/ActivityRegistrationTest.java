package edu.tugraz.sw14.xp04;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.EditText;

import com.robotium.solo.Solo;

public class ActivityRegistrationTest extends
		ActivityInstrumentationTestCase2<ActivityRegistration> {

	private Solo solo;

	public ActivityRegistrationTest() {
		super(ActivityRegistration.class);
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

		assertTrue(solo.waitForText(solo
				.getString(R.string.a_registration_success)));
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
