package ro.pub.cs.systems.eim.practicaltestexample;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class PracticalTestExampleActivity extends Activity {
	private class PressMeButtonClickListener implements View.OnClickListener {
		@Override
		public void onClick(View v) {
			Button button = (Button)v;
			if(button == null) {
				return;
			}
			
			EditText box = null;
			switch(button.getId()) {
				case R.id.press_me:
					box = firstBoxEditText;
					break;
				case R.id.press_me_too:
					box = secondBoxEditText;
					break;
			}
			
			Integer number = Integer.parseInt(box.getText().toString());
			box.clearComposingText();
			number++;
			box.setText(number.toString());
			
			int leftNumberOfClicks = Integer.parseInt(firstBoxEditText.getText().toString());
			int rightNumberOfClicks = Integer.parseInt(secondBoxEditText.getText().toString());
			if(leftNumberOfClicks + rightNumberOfClicks > Constants.NUMBER_OF_CLICKS_TRESHOLD
					&& serviceStatus == Constants.SERVICE_STOPPED) {
				Intent intent = new Intent(getApplicationContext(), PracticalTestExampleService.class);
				intent.putExtra("firstNumber", leftNumberOfClicks);
				intent.putExtra("secondNumber", rightNumberOfClicks);
				getApplicationContext().startService(intent);
				serviceStatus = Constants.SERVICE_STARTED;
			}
		}
	}
	
	private class SecondaryActivityButtonClickListener implements View.OnClickListener {
		@Override
		public void onClick(View v) {
			Intent intent = new Intent(getApplicationContext(), PracticalTestExampleSecondaryActivity.class);
			Integer numberOfClicks = Integer.parseInt(firstBoxEditText.getText().toString())
				+ Integer.parseInt(secondBoxEditText.getText().toString());
			intent.putExtra("sum", numberOfClicks.toString());
			
			startActivityForResult(intent, SECONDARY_ACTIVITY_REQUEST_CODE);
		}
	}
	
	private class MessageBroadcastReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			Log.d("[Message]", intent.getStringExtra("message"));			
		}
	}
	
	private static final int SECONDARY_ACTIVITY_REQUEST_CODE = 2016;
	private PressMeButtonClickListener pressMeListener = new PressMeButtonClickListener();
	private SecondaryActivityButtonClickListener secondaryActivityListener = new SecondaryActivityButtonClickListener();
	private MessageBroadcastReceiver messageBroadcastReceiver = new MessageBroadcastReceiver();
	private IntentFilter intentFilter = new IntentFilter();
	private EditText firstBoxEditText = null;
	private EditText secondBoxEditText = null;
	private Button pressMeButton = null;
	private Button pressMeTooButton = null;
	private Button secondaryActivityButton = null;
	private int serviceStatus = Constants.SERVICE_STOPPED;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_practical_test_example);
		
		firstBoxEditText = (EditText)findViewById(R.id.first_box);
		secondBoxEditText = (EditText)findViewById(R.id.second_box);
		
		if(savedInstanceState != null) {
			if(savedInstanceState.containsKey("firstBox")) {
				firstBoxEditText.setText(savedInstanceState.getString("firstBox"));
			}
			if(savedInstanceState.containsKey("secondBox")) {
				firstBoxEditText.setText(savedInstanceState.getString("secondBox"));
			}
		}
		
		pressMeButton = (Button)findViewById(R.id.press_me);
		pressMeTooButton = (Button)findViewById(R.id.press_me_too);
		secondaryActivityButton = (Button)findViewById(R.id.navigate_to_secondary_activity);
		
		pressMeButton.setOnClickListener(pressMeListener);
		pressMeTooButton.setOnClickListener(pressMeListener);
		secondaryActivityButton.setOnClickListener(secondaryActivityListener);
		
		for(int i = 0; i < Constants.actionType.length; ++i) {
			intentFilter.addAction(Constants.actionType[i]);
		}
	}
	
	@Override
	protected void onSaveInstanceState(Bundle savedInstanceState) {
		if(savedInstanceState == null) {
			return;
		}
		
		savedInstanceState.putString("firstBox", firstBoxEditText.getText().toString());
		savedInstanceState.putString("secondBox", secondBoxEditText.getText().toString());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.practical_test_example, menu);
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
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		if(requestCode == SECONDARY_ACTIVITY_REQUEST_CODE) {
			Toast t = Toast.makeText(this, "Activity exited with code: " + resultCode, Toast.LENGTH_LONG);
			t.show();
		}
	}
	
	@Override
	protected void onDestroy() {
		Intent intent = new Intent(this, PracticalTestExampleService.class);
		stopService(intent);
		super.onDestroy();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		registerReceiver(messageBroadcastReceiver, intentFilter);
	}
	
	@Override
	protected void onPause() {
		unregisterReceiver(messageBroadcastReceiver);
		super.onPause();
	}
}
