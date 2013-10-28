package com.example.testsendfile;

import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Date;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ProgressEvent;
import com.amazonaws.services.s3.model.ProgressListener;
import com.amazonaws.services.s3.model.PutObjectRequest;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.telephony.TelephonyManager;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class MainActivity extends Activity {
	public static final String S3_BUCKET = "com.mycellspeed.dev";
	public static final String ACCESS_KEY_ID = "AKIAJ3W2NBVOWKCXES7Q";
	public static final String SECRET_KEY = "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";
	
	private AmazonS3Client client = new AmazonS3Client(new BasicAWSCredentials(ACCESS_KEY_ID, SECRET_KEY));
	private String fileName = "";
	private Date newDate;

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
    
    private String openFile() {
    	TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
    	fileName = tm.getSubscriberId() + "_" + newDate.getTime() + ".txt"; 
    	return fileName;
    }
    
    private void writeToFile(String file, String data) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(openFileOutput(file, Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        }
        catch (IOException e) {
        	((TextView) findViewById(R.id.outText)).setText("ERROR: " + e.getMessage());
        }        
    }
    
    public void sendFile(View view) {
    	// reset base values
    	((TextView) findViewById(R.id.outText)).setText("");
    	newDate = new Date();
    	
    	// open and write to new file
    	String fc = ((EditText) findViewById(R.id.textInput)).getText().toString() + "\n" + newDate.toString();
    	writeToFile(openFile(), fc);
    	
    	// send file
    	File outFile = getFileStreamPath(fileName);
    	new SendFileToS3().execute(outFile);
    }
    
    public void getFiles(View view) {
    	// initialize
    	((TextView) findViewById(R.id.outText)).setText("");
    	newDate = new Date();
    	
    	// pull drop-down value
    	Spinner tempSpin = (Spinner) findViewById(R.id.fetch_spinner);
    	String fileGet = (String) tempSpin.getSelectedItem();
    	String fileSize = null;
    	int size = Integer.parseInt(fileGet.substring(0, fileGet.length()-2));
    	
    	//parse for file size
    	switch (fileGet.charAt(fileGet.length()-2)) {
    	case 'k':
    		fileSize = "" + (1024*size);
    		break;
    	case 'm':
    		fileSize = "" + (1024*1024*size);
    		break;
    	}
    	
    	String[] request = {fileGet + "file.txt", fileSize};
    	new GetFilesOfS3().execute(request);
    }
    
    public void printDirectory(View view) {
    	TextView tv = (TextView) findViewById(R.id.outText);
    	tv.setText("");
    	String[] files = fileList();
    	for (int i = 0; i < files.length; i++)
    		tv.append("\nFile Path " + (i+1) + ": " + files[i]);
    	tv.append("\nDirectory listing complete!");
    }
    
    public void clearDirectory(View view) {
    	String[] files = fileList();
    	for (int i = 0; i < files.length; i++)
    		this.deleteFile(files[i]);
    	((TextView) findViewById(R.id.outText)).append("\nDirectory cleaned!");
    }
    
    class SendFileToS3 extends AsyncTask<File, Void, S3TaskResult> {
    	
    	S3TaskResult result = new S3TaskResult();
    	
    	@Override
		protected S3TaskResult doInBackground(File... files) {
    		// break if no item to be returned
    		if (files == null || files.length != 1)
				return null;
    		
    		//pull file from input params, initialise results
    		File file = files[0];
    		double fileSize = file.length() / 1024.0;
    		
			// attempt file delivery
    		client.setEndpoint("s3.amazonaws.com");
	    	try {
	    		// initialize connection parameters and request
	    		PutObjectRequest por = new PutObjectRequest(S3_BUCKET + "/android_inbound", file.getName(), file);
	    		
	    		// send file and track duration
	    		Date startTime = new Date();
	    		client.putObject(por);
	    		Date endTime = new Date();
	    		
	    		// collect stats
	    		double time = (endTime.getTime() - startTime.getTime()) / 1000.0;
	    		result.setOutput(result.getOutput() + "\n File Size: " + fileSize + "kb");
	    		result.setOutput(result.getOutput() + "\n Upload duration: " + time + "s");
	    		result.setOutput(result.getOutput() + "\n Upload rate: " + (fileSize/time) + "kbps");
	    		result.setSuccess(true);
	    	} catch (Throwable e) {
	    		result.setSuccess(false);
	    		result.setErrorMessage(result.getErrorMessage() + "\n" + e.getMessage());
	    	}

			return result;
		}

		protected void onPostExecute(S3TaskResult result) {
			TextView tv = (TextView) findViewById(R.id.outText);
			
			if (result.isSuccess()) tv.setText(result.getOutput());
			else tv.setText("ERROR: " + result.getErrorMessage());
		}
    }

    class GetFilesOfS3 extends AsyncTask<String, Integer, S3TaskResult> {
    	
    	ProgressDialog dialog;
    	
    	@Override
    	protected void onPreExecute() {
    	    // Set up progress dialog
    	    dialog = new ProgressDialog(MainActivity.this);
    	    dialog.setMessage(getString(R.string.uploading));
    	    dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
    	    dialog.setCancelable(false);
    	    dialog.show();
    	}

		@Override
		protected S3TaskResult doInBackground(String... strings) {
			// check for input
			if (strings != null && strings.length != 2) {
				if (strings[0] == null || strings[1] == null)
					return null;
			}
			
			//initialize variables
			String fileToFetch = strings[0];
			S3TaskResult result = new S3TaskResult();
			dialog.setMax(Integer.parseInt(strings[1]));
			
			// attempt file listing
			client.setEndpoint("s3.amazonaws.com");
	    	try {
	    		result.setSuccess(downloadFile(result, fileToFetch));
	    	} catch (Throwable e) {
	    		result.setSuccess(false);
	        	result.setErrorMessage(e.getMessage());
	        }
			return result;
		}
		
		private boolean downloadFile(S3TaskResult result, String fileName) {
			File inFile = new File(getFilesDir(), fileName);
			try {
				result.setOutput(result.getOutput() + "\n" + fileName + " file size PRE-download: " + inFile.length());
				
				GetObjectRequest gor = new GetObjectRequest(S3_BUCKET + "/android_outbound", fileName);
				gor.setProgressListener(new ProgressListener() {
				    int total = 0;
				 
				    @Override
				    public void progressChanged(ProgressEvent pv) {
				        total += (int) pv.getBytesTransferred();
				        publishProgress(total);
				    }
				});
				
				Date startTime = new Date();
				client.getObject(gor, inFile);
				Date endTime = new Date();
				double time = (endTime.getTime() - startTime.getTime()) / 1000.0;
				
				result.setOutput(result.getOutput() + "\n" + fileName + " file size POST-download: " + (inFile.length()/1024.0) + " (" + time + "s)");
				result.setOutput(result.getOutput() + "\n Download rate: " + ((inFile.length()/1024.0)/time) + "kbps");
			} catch(Throwable e) {
				result.setErrorMessage(result.getErrorMessage() + "\n" + e.getMessage());
			} finally {
				inFile.delete();
			}
			return true;
		}
		
		@Override
		protected void onProgressUpdate(Integer... values) {
		    // Update the progress bar
		    dialog.setProgress(values[0]);
		}
		
		protected void onPostExecute(S3TaskResult result) {
			TextView tv = (TextView) findViewById(R.id.outText);
			
			if (result.getErrorMessage().length() == 0) tv.setText(result.getOutput());
			else tv.setText("ERROR: " + result.getErrorMessage());
			dialog.dismiss();
		}
    	
    }
    
    private class S3TaskResult {
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
