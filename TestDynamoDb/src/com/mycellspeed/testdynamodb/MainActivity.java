package com.mycellspeed.testdynamodb;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.mycellspeed.datafunction.DataFunction;

import android.os.AsyncTask;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends Activity {

	static DataFunction localDataFunction = new DataFunction(); 
    static AmazonDynamoDBClient client;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
		
	 @SuppressLint("NewApi")
		public void insertTable (View view) {
		
			TextView textView = (TextView)findViewById(R.id.RowOne);
	     	textView.setText("the data was inserted");
     	
     	// create a string on the fly in Java
     	String[] myStringArray = {"DropTable","myTable001"};
     	
     	new RunInsertTableProcess().execute();
     	     	
	    }	
	 
	 @SuppressLint("NewApi")
		public void dropTable (View view) {
		
		TextView textView = (TextView)findViewById(R.id.RowOne);
     	textView.setText("the table was dropped");

     	new RunDropTableProcess().execute();
     	     	
	    }		
	 
	 @SuppressLint("NewApi")
		public void createTable (View view) {
		
		TextView textView = (TextView)findViewById(R.id.RowOne);
     	textView.setText("the table was created");

     	new RunCreateTableProcess().execute();
     	     	
	    }		 
	 
	 @SuppressLint("NewApi")
		public void clearAll (View view) {
		TextView textView = (TextView)findViewById(R.id.RowOne);
		textView.setText(R.string.default_val);
     	textView = (TextView)findViewById(R.id.RowTwo);
     	textView.setText(R.string.default_val);
     	textView = (TextView)findViewById(R.id.RowThree);
     	textView.setText(R.string.default_val);
     	textView = (TextView)findViewById(R.id.RowFour);
     	textView.setText(R.string.default_val);
     	textView = (TextView)findViewById(R.id.RowFive);
     	textView.setText(R.string.default_val);
     	textView = (TextView)findViewById(R.id.RowSix);
     	textView.setText(R.string.default_val);
     	
	    }	
	 
	 class RunInsertTableProcess extends AsyncTask <Void, Void, SomeResult> {
		 
		 SomeResult result = new SomeResult();
	    	
	    	@Override
			protected SomeResult doInBackground(Void ... void0) {
	    		
	    		localDataFunction.runUploadProcess01(); 	    			  		
				return result;
			}

			protected void onPostExecute(SomeResult result) {
				TextView tv = (TextView) findViewById(R.id.RowThree);
				
				if (result.isSuccess()) tv.setText(result.getOutput());
				else tv.setText("ERROR: " + result.getErrorMessage());
			}		 
		 
	 }
	 
	 class RunDropTableProcess extends AsyncTask <Void, Void, SomeResult> {
		 
		 SomeResult result = new SomeResult();
	    	
	    	@Override
			protected SomeResult doInBackground(Void ... void0) {
	    		
	    		localDataFunction.runDropTable(); 	    			  		
				return result;
			}

			protected void onPostExecute(SomeResult result) {
				TextView tv = (TextView) findViewById(R.id.RowThree);
				
				if (result.isSuccess()) tv.setText(result.getOutput());
				else tv.setText("ERROR: " + result.getErrorMessage());
			}		 
		 
	 }	 
	 
	 class RunCreateTableProcess extends AsyncTask <Void, Void, SomeResult> {
		 
		 SomeResult result = new SomeResult();
	    	
	    	@Override
			protected SomeResult doInBackground(Void ... void0) {
	    		
	    		localDataFunction.runCreateTable02(); 	    			  		
				return result;
			}

			protected void onPostExecute(SomeResult result) {
				TextView tv = (TextView) findViewById(R.id.RowThree);
				
				if (result.isSuccess()) tv.setText(result.getOutput());
				else tv.setText("ERROR: " + result.getErrorMessage());
			}		 
		 
	 }	 
	 
    private class SomeResult {
    	private Boolean success = false;
		private String errorMessage = "";
		private String output = "";

		public Boolean isSuccess() { return success; }
		public void setSuccess(Boolean value) { this.success = value; }
		
		public String getErrorMessage() { return errorMessage; }
		public void setErrorMessage(String value) { this.errorMessage = value; }
		
		public String getOutput() { return output; }
		public void setOutput(String value) { this.output = value; }
	}		  
	 
}
