package yliu.andriod_player;

import android.content.Context;
import android.hardware.usb.UsbAccessory;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.w3c.dom.Document;

import java.io.File;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;


import java.io.File;
import java.io.FilenameFilter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;


public class MainActivity extends ActionBarActivity {
    private static final String ACTIVITY_TAG="LogDemo";
    private TextView tv;
    public class MyFileFilter implements FilenameFilter {

        @Override
        public boolean accept(File directory, String fileName) {
            if (fileName.endsWith(".mkv")) {
                return true;
            }
            return false;
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.tv = (TextView) findViewById(R.id.tv);
        RequestTask request = new RequestTask();
        request.execute("http://192.168.33.169/sort/liu.xml"
        );

        int max = 0;


        VideoView vidView = (VideoView)findViewById(R.id.myVideo);
        File[] file1 = null;

        //




        //
        File vSDCard = null;
        UsbManager manager = (UsbManager) getSystemService(Context.USB_SERVICE);
        List<String> usbAccessories = new ArrayList<String>();

        UsbAccessory[] accessoryList = manager.getAccessoryList();
        if (accessoryList != null) {
            Log.e(MainActivity.ACTIVITY_TAG, ""+Thread.currentThread().getStackTrace()[2].getLineNumber());
            usbAccessories.add(accessoryList[0].getSerial());
        }
        Log.e(MainActivity.ACTIVITY_TAG, ""+Thread.currentThread().getStackTrace()[2].getLineNumber());
        Log.e(MainActivity.ACTIVITY_TAG, "" + accessoryList);
        //UsbManager manager = (UsbManager) getSystemService(Context.USB_SERVICE);

        HashMap<String, UsbDevice> deviceList = manager.getDeviceList();
        Iterator<UsbDevice> deviceIterator = deviceList.values().iterator();
        while (deviceIterator.hasNext())
        {
            UsbDevice device = deviceIterator.next();
            Log.e(MainActivity.ACTIVITY_TAG, "device name = " + device.getDeviceName());
        }


        try {
            // 判斷 SD Card 有無插入
            if( Environment.getExternalStorageState().equals(Environment.MEDIA_REMOVED) ) {
                Log.e(MainActivity.ACTIVITY_TAG, ""+Thread.currentThread().getStackTrace()[2].getLineNumber());
                return;
            }
            else
            {
                Log.e(MainActivity.ACTIVITY_TAG, ""+Thread.currentThread().getStackTrace()[2].getLineNumber());
                // 取得 SD Card 位置
                //vSDCard = Environment.getExternalStoragePublicDirectory(Environment.MEDIA_UNMOUNTABLE);
                vSDCard = Environment.getExternalStorageDirectory();
                Log.e(MainActivity.ACTIVITY_TAG, ""+vSDCard);
            }

            // 判斷目錄是否存在
            File vPath = new File(  "/storage/external_storage/sda1/" );
            //File vPath = new File(  vSDCard+"/" );
            FilenameFilter filter = new MyFileFilter();

             file1 = vPath.listFiles(filter);

            int i = 0;
            long max_byte = 0;
            while(file1[i]!= null) {
                Log.e(MainActivity.ACTIVITY_TAG, "" + file1[i].getName());
                Log.e(MainActivity.ACTIVITY_TAG, "" + file1[i].length());
                if(file1[i].length() >max_byte ) {
                    max = i;
                    max_byte = file1[i].length();
                }
                i++;

            }
            if( !vPath.exists() ) {
                Log.e(MainActivity.ACTIVITY_TAG, ""+Thread.currentThread().getStackTrace()[2].getLineNumber());
                vPath.mkdirs();
            }

            Log.e(MainActivity.ACTIVITY_TAG, ""+Thread.currentThread().getStackTrace()[2].getLineNumber());  // 寫入檔案
            FileWriter vFile = new FileWriter(  "/storage/external_storage/sda1/myTest.txt" );
            Log.e(MainActivity.ACTIVITY_TAG, ""+Thread.currentThread().getStackTrace()[2].getLineNumber());

            vFile.write("Hello Android");
            Log.e(MainActivity.ACTIVITY_TAG, ""+Thread.currentThread().getStackTrace()[2].getLineNumber());
            vFile.close();
            Log.e(MainActivity.ACTIVITY_TAG, "" + Thread.currentThread().getStackTrace()[2].getLineNumber());

        } catch (Exception e) {
            // 錯誤處理
        }



        ///
        String usbAddress = file1[max].getAbsolutePath();
        Log.e(MainActivity.ACTIVITY_TAG, "" + usbAddress);
        String vidAddress = "http://192.168.33.169/mediafile/%e5%9b%bd%e4%ba%a7%e9%9b%b6%e9%9b%b6%e6%bc%86.mkv";
        Uri vidUri = Uri.parse(usbAddress);
        vidView.setVideoURI(vidUri);
        Log.e(MainActivity.ACTIVITY_TAG, ""+Thread.currentThread().getStackTrace()[2].getLineNumber());
        vidView.start();

    }

    public class RequestTask extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            this.publishProgress(null);
            try {
                HttpGet httpget = new HttpGet(params[0]);
                HttpClient client = new DefaultHttpClient();
                HttpResponse response = client.execute(httpget);
                HttpEntity resEntityGet = response.getEntity();
                if (resEntityGet != null) {
                    return EntityUtils.toString(resEntityGet, "utf-8");
                }

            } catch (Exception e) {
                return e.toString();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String text){
            MainActivity.this.tv.setText(text);
        }

        @Override
        protected void onProgressUpdate(String... result){
            MainActivity.this.tv.setText("Start");
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
