package com.mycellspeed.testreadfile;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import org.apache.http.util.ByteArrayBuffer;

import android.os.Bundle;
import android.os.Environment;
import android.os.StatFs;
import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.util.Log;
import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	/** Class variables */
	private static final String TAG = MainActivity.class.getName();
    private static final String FILENAME = "myFile001.txt";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
        String textToSaveString = "Hello android, again" + "\n";		
        
        //deleteFile(FILENAME);
        writeToFile(textToSaveString);
        //deleteFile(FILENAME);
        
        String[] arrayOfFiles = fileList ();
        String csvOfFiles = "";
        
        for (int i=0; i < arrayOfFiles.length; i++) {
        	csvOfFiles += arrayOfFiles[i].toLowerCase() + "   ";
        }
        
        String textFromFileString = readFromFile();
        int countFromFileString = countFromFile();
        
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
        
        File file = new File(FILENAME);
        int size = (int) file.length();
        byte[] bytes = new byte[size];
        try {
            BufferedInputStream buf = new BufferedInputStream(new FileInputStream(file));
            buf.read(bytes, 0, bytes.length);
            buf.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }        
        
        textView = (TextView)findViewById(R.id.RowThree);
        textView.setText("" + myfile.length());  
        
        textView = (TextView)findViewById(R.id.RowFour);
        textView.setText("" + countFromFileString);
        
        textView = (TextView)findViewById(R.id.RowFive);        
        textView.setText("" + getInternalStorageSpace() + " " 
        					+ getInternalFreeSpace() + " " 
        					+ getInternalUsedSpace());  
        
        textView = (TextView)findViewById(R.id.RowSix);
        textView.setText("" + readFromFile());        
     
	}
	
    private void writeToFile(String data) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(openFileOutput(FILENAME, Context.MODE_APPEND));      
            outputStreamWriter.write(data);            
            outputStreamWriter.close();
            //deleteFile(FILENAME);
        }
        catch (IOException e) {
            Log.e(TAG, "File write failed: " + e.toString());
        }
    }	
    
    private int countFromFile() {
        
        String ret = "";
        int retCount = 0;
         
        try {
            InputStream inputStream = openFileInput(FILENAME);
             
            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();
                 
                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append(receiveString+"\n");    
                    retCount = retCount + 1;
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
 
        return retCount;
        //return retCount;
    }      
    
    private String readFromFile() {
        
        String ret = "";
        int retCount = 0;
         
        try {
            InputStream inputStream = openFileInput(FILENAME);
             
            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();
                 
                while ( (receiveString = bufferedReader.readLine()) != null ) {
                	stringBuilder.append(receiveString+"\n");    
                    retCount = retCount + 1;
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
        //return retCount;
    }    
    
    public static long getInternalStorageSpace()
    {
        StatFs statFs = new StatFs(Environment.getDataDirectory().getAbsolutePath());
        //StatFs statFs = new StatFs("/data");
        long total = ((long)statFs.getBlockCount() * (long)statFs.getBlockSize()) / 1048576;
        return total;
    }

    public static long getInternalFreeSpace()
    {
        StatFs statFs = new StatFs(Environment.getDataDirectory().getAbsolutePath());
        //StatFs statFs = new StatFs("/data");
        long free  = ((long)statFs.getAvailableBlocks() * (long)statFs.getBlockSize()) / 1048576;
        return free;
    }    
    
    public static long getInternalUsedSpace()
    {
        StatFs statFs = new StatFs(Environment.getDataDirectory().getAbsolutePath());
        //StatFs statFs = new StatFs("/data");
        long total = ((long)statFs.getBlockCount() * (long)statFs.getBlockSize()) / 1048576;
        long free  = ((long)statFs.getAvailableBlocks() * (long)statFs.getBlockSize()) / 1048576;
        long busy  = total - free;
        return busy;
    }    
    
    private String readFromInternalStorage(String filename) {
        FileInputStream fis = null;
        File file = new File(getFilesDir(), filename);

        long size = file.length();
        // impossible to have more than that (= 2GB)
        if (size > Integer.MAX_VALUE) {
            Log.d("XXX", "File too big");
            return null;
        }

        int iSize = (int) size;
        try {
            fis = new FileInputStream(file);

            // part of Android since API level 1 - buffer can scale
            ByteArrayBuffer bb = new ByteArrayBuffer(iSize);

            // some rather small fixed buffer for actual reading
            byte[] buffer = new byte[1024];

            int read;
            while ((read = fis.read(buffer)) != -1) {
                // just append data as long as we can read more
                bb.append(buffer, 0, read);
            }

            // return a new string based on the large buffer
            return new String(bb.buffer(), 0, bb.length());
        } catch (FileNotFoundException e) {
            Log.w("XXX", e);
        } catch (IOException e) {
            Log.w("XXX", e);
        } catch (OutOfMemoryError e) {
            // this could be left out. Keep if you read several MB large files.
            Log.w("XXX", e);
        } finally {
            // finally is executed even if you return in above code
            // fis will be null if new FileInputStream(file) throws
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    // ignored, nothing can be done if closing fails
                }
            }
        }
        return null;
    }    

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
