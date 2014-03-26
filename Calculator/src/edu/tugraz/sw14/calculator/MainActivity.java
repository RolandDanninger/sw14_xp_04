package edu.tugraz.sw14.calculator;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity implements OnClickListener {

	private EditText txtNum1;
	private EditText txtNum2;
	private Button btnAdd;
	private Button btnSub;
	private Button btnMul;
	private Button btnDiv;
	private TextView lblResult;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtNum1 = (EditText) findViewById(R.id.txtNum1);
        txtNum2 = (EditText) findViewById(R.id.txtNum2);
        btnAdd = (Button) findViewById(R.id.btnAdd);
        btnSub = (Button) findViewById(R.id.btnSub);
        btnMul = (Button) findViewById(R.id.btnMul);
        btnDiv = (Button) findViewById(R.id.btnDiv);
        lblResult = (TextView) findViewById(R.id.lblResult);

        btnAdd.setOnClickListener(this);
        btnSub.setOnClickListener(this);
        btnMul.setOnClickListener(this);
        btnDiv.setOnClickListener(this);
    }

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnAdd:
			btnAddClicked();
			break;

		case R.id.btnSub:
			btnSubClicked();
			break;

		case R.id.btnMul:
			btnMulClicked();
			break;

		case R.id.btnDiv:
			btnDivClicked();
			break;

		default:
			break;
		}
	}

	private void btnAddClicked() {
		String num1Str = txtNum1.getText().toString();
		String num2Str = txtNum2.getText().toString();

		float num1 = Float.valueOf(num1Str);
		float num2 = Float.valueOf(num2Str);
		float result = num1 + num2;
		result = Math.round(result * 100) / 100f;
		
		String resultText = getResources().getString(R.string.lblResult);
		resultText += " " + String.valueOf(result);
		
		lblResult.setText(resultText);
	}
	

	private void btnSubClicked() {

		String num1Str = txtNum1.getText().toString();
		String num2Str = txtNum2.getText().toString();

		float num1 = Float.valueOf(num1Str);
		float num2 = Float.valueOf(num2Str);
		float result = num1 - num2;
		result = Math.round(result * 100) / 100f;
		
		String resultText = getResources().getString(R.string.lblResult);
		resultText += " " + String.valueOf(result);
		
		lblResult.setText(resultText);
	}
	
	private void btnMulClicked() {

		String num1Str = txtNum1.getText().toString();
		String num2Str = txtNum2.getText().toString();

		float num1 = Float.valueOf(num1Str);
		float num2 = Float.valueOf(num2Str);
		float result = num1 * num2;
		result = Math.round(result * 100) / 100f;
		
		String resultText = getResources().getString(R.string.lblResult);
		resultText += " " + String.valueOf(result);
		
		lblResult.setText(resultText);
	}
	
	private void btnDivClicked() {

		String num1Str = txtNum1.getText().toString();
		String num2Str = txtNum2.getText().toString();

		float num1 = Float.valueOf(num1Str);
		float num2 = Float.valueOf(num2Str);
		float result = num1 / num2;
		result = Math.round(result * 100) / 100f;
		
		String resultText = getResources().getString(R.string.lblResult);
		resultText += " " + String.valueOf(result);
		
		lblResult.setText(resultText);
	}
}
