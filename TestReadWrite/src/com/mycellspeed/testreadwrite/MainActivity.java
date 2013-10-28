package com.mycellspeed.testreadwrite;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import android.util.Log;
import java.io.IOException;



//import com.example.positionpulltest.R;

public class MainActivity extends Activity {
	
	private static final String TAG = MainActivity.class.getName();
    private static final String FILENAME = "myFile001.txt";
    private static final String FILENAME001 = "myFile001.txt";

	/** Called when the activity is first created. */
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        String textToSaveString = "Hello Androidasdfasdfasdfasdfasdfasdfasdfasdf" +
        		"asdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdf" +
        		"asdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdf" +
        		"asdfasdfasdfasdfasdfasdfasdfasdfasdfjkasd;fjs;dfjasdjf;asjdf;asdf;sdf" +
        		"asj;fjasd;fljasd;fjas;jas;fj;lsdffjhkljsdfhlasjfhlkasjdhflfh" +
        		"ljhasfklashflkjhasfkljhfjklsdhfkljashdfklasdhfkljsdhfkljsdfkjlasd";
        
        writeToFile(textToSaveString);
        //deleteFile(FILENAME);
        
        String[] arrayOfFiles = fileList ();
        String csvOfFiles = "";
        
        for (int i=0; i < arrayOfFiles.length; i++) {
        	csvOfFiles += arrayOfFiles[i].toLowerCase() + ",";
        }
        
        String textFromFileString = readFromFile();
        
        if ( textToSaveString.equals(textFromFileString) ) 
            Toast.makeText(getApplicationContext(), "both string are equal", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(getApplicationContext(), "there is a problem", Toast.LENGTH_SHORT).show();
        
        TextView textView = (TextView)findViewById(R.id.FileLocation);
    	textView.setText("" + getFilesDir());    	
        //textView.setText("" + csvOfFiles);
        //textView.setText("" + file.length());  
    	
        textView = (TextView)findViewById(R.id.FileName);
        textView.setText("" + csvOfFiles);

        java.io.File myfile = new java.io.File(FILENAME);
        long lenght = myfile.length();
        lenght = lenght/1024;        
        
        //file.length();
        
        textView = (TextView)findViewById(R.id.FileSize);
        textView.setText("" + myfile.length());        
        
    }
	
    private void writeToFile(String data) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(openFileOutput(FILENAME, Context.MODE_PRIVATE));      
            outputStreamWriter.write(data);            
            outputStreamWriter.close();
            //deleteFile(FILENAME);
        }
        catch (IOException e) {
            Log.e(TAG, "File write failed: " + e.toString());
        }
    }

    private String readFromFile() {
        
        String ret = "";
         
        try {
            InputStream inputStream = openFileInput(FILENAME);
             
            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();
                 
                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append(receiveString);
                }
                 
                inputStream.close();
                ret = stringBuilder.toString();
            }
        }
        catch (FileNotFoundException e) {
            Log.e(TAG, "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e(TAG, "Can not read file: " + e.toString());
        }
 
        return ret;
    }    

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
}
