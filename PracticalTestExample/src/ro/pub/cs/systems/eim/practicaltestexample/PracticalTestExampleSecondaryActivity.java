package ro.pub.cs.systems.eim.practicaltestexample;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class PracticalTestExampleSecondaryActivity extends Activity {
	private class ButtonClickListener implements View.OnClickListener {
		@Override
		public void onClick(View v) {
			Button button = (Button)v;
			if(button == null) {
				return;
			}
			
			switch(button.getId()) {
				case R.id.ok_button:
					setResult(RESULT_OK, null);
					break;
				case R.id.cancel_button:
					setResult(RESULT_CANCELED, null);
					break;
			}
			
			finish();
		}
	}
	
	private ButtonClickListener listener = new ButtonClickListener();
	private Button okButton = null;
	private Button cancelButton = null;
	private TextView textView = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_practical_test_example_secondary);
		
		okButton = (Button)findViewById(R.id.ok_button);
		cancelButton = (Button)findViewById(R.id.cancel_button);
		textView = (TextView)findViewById(R.id.sum_box);
		
		Intent intent = getIntent();
		if(intent != null && intent.getExtras().containsKey("sum")) {
			textView.setText(intent.getExtras().getString("sum"));
		}
		
		okButton.setOnClickListener(listener);
		cancelButton.setOnClickListener(listener);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.practical_test_example_secondary, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
