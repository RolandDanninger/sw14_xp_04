package edu.tugraz.sw14.calculator.test;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.EditText;

import com.robotium.solo.Solo;

import edu.tugraz.sw14.calculator.MainActivity;
import edu.tugraz.sw14.calculator.R;

public class MainActivityTest extends
		ActivityInstrumentationTestCase2<MainActivity> {

	private Solo solo;

	public MainActivityTest() {
		super(MainActivity.class);
	}

	@Override
	protected void setUp() throws Exception {
		solo = new Solo(getInstrumentation(), getActivity());
	}

	@Override
	protected void tearDown() throws Exception {
		solo.finishOpenedActivities();
	}

	public void testAdd() {
		EditText txtNum1 = (EditText) solo.getView(R.id.txtNum1);
		EditText txtNum2 = (EditText) solo.getView(R.id.txtNum2);

		solo.enterText(txtNum1, "5");
		solo.enterText(txtNum2, "3");

		solo.clickOnButton(solo.getString(R.string.btnAdd));

		assertTrue(solo.waitForText("Result: 8.0"));
	}

	public void testSub() {
		EditText txtNum1 = (EditText) solo.getView(R.id.txtNum1);
		EditText txtNum2 = (EditText) solo.getView(R.id.txtNum2);

		solo.enterText(txtNum1, "5");
		solo.enterText(txtNum2, "3");

		solo.clickOnButton(solo.getString(R.string.btnSub));

		assertTrue(solo.waitForText("Result: 2.0"));
	}

	public void testMul() {
		EditText txtNum1 = (EditText) solo.getView(R.id.txtNum1);
		EditText txtNum2 = (EditText) solo.getView(R.id.txtNum2);

		solo.enterText(txtNum1, "2");
		solo.enterText(txtNum2, "3");

		solo.clickOnButton(solo.getString(R.string.btnMul));

		assertTrue(solo.waitForText("Result: 6.0"));
	}

	public void testDiv() {
		EditText txtNum1 = (EditText) solo.getView(R.id.txtNum1);
		EditText txtNum2 = (EditText) solo.getView(R.id.txtNum2);

		solo.enterText(txtNum1, "2");
		solo.enterText(txtNum2, "3");

		solo.clickOnButton(solo.getString(R.string.btnDiv));

		assertTrue(solo.waitForText("Result: 0.67"));
	}
	
}
