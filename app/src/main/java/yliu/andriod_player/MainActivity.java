package yliu.andriod_player;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;


public class MainActivity extends ActionBarActivity {
    private static final String ACTIVITY_TAG="MainActivity";
    private ListView listView;
    private  String xml_url = "http://192.168.33.169/mediafile/liuyong/liu.xml";
    private String[] list = {"cctv5","cctv5","cctv5","cctv5 TV","cctv5 TV","usb1","usb2","usb3","usb4 ","usb5"};
    private String[] url_list = new String[10] ;
    UsbMoiveDetect UsbMoiveDetect = new UsbMoiveDetect();
    IptvXmlParser IptvXmlParser = new IptvXmlParser();
    List<yliu.andriod_player.UsbMoiveDetect.UsbEntry> usb_entries = null;
    List<IptvXmlParser.NetEntry> net_entries = null;
    private ArrayAdapter<String> listAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        usb_entries = UsbMoiveDetect.parse();

        new DownloadXmlTask().execute(xml_url);
        //net_entries = IptvXmlParser.parse(xml_url);
        int i = 0;
        for (yliu.andriod_player.UsbMoiveDetect.UsbEntry usb_entry : usb_entries) {
            Log.e(MainActivity.ACTIVITY_TAG, "" + Thread.currentThread().getStackTrace()[2].getLineNumber());
            Log.e(MainActivity.ACTIVITY_TAG, "" + usb_entry.title);
            url_list[i] = usb_entry.link;
            list[i++] =usb_entry.title;
        }

        listView = (ListView)findViewById(R.id.listView);
        listAdapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,list);
        listView.setAdapter(listAdapter);

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





    private class DownloadXmlTask extends AsyncTask<String, Void, List<yliu.andriod_player.IptvXmlParser.NetEntry>> {
        @Override
        protected List<yliu.andriod_player.IptvXmlParser.NetEntry> doInBackground(String... urls) {
            try {
                return loadXmlFromNetwork(urls[0]);
            } catch (IOException e) {
                return null;
                //  return getResources().getString(R.string.connection_error);
            } catch (XmlPullParserException e) {
                return null;
                // return getResources().getString(R.string.xml_error);
            }
        }

        @Override
        protected void onPostExecute(List<yliu.andriod_player.IptvXmlParser.NetEntry> net_entries) {
            // ArrayAdapter listAdapter2 = null;
            Context list2 ;
            //getContext()
            setContentView(R.layout.activity_main);
            listView = (ListView)findViewById(R.id.listView);


            //

            int i = 5;
            for (IptvXmlParser.NetEntry  net_entry: net_entries) {
                Log.e(MainActivity.ACTIVITY_TAG, "" + Thread.currentThread().getStackTrace()[2].getLineNumber());
                Log.e(MainActivity.ACTIVITY_TAG, "" + net_entry.title);
                url_list[i] = net_entry.link;
                list[i++] =net_entry.title;
                if(i >=10)
                    break;
            }



            //
            // listAdapter.remove(list[0]);
            list2 = listAdapter.getContext();
            ArrayAdapter listAdapter2 = new ArrayAdapter(list2,android.R.layout.simple_list_item_1,list);
            listView.setAdapter(listAdapter2);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                    VideoView vidView = (VideoView)findViewById(R.id.myVideo);
                    String vidAddress = url_list[position];
                    Uri vidUri = Uri.parse(vidAddress);
                    vidView.setVideoURI(vidUri);
                    Log.e(MainActivity.ACTIVITY_TAG, "" + Thread.currentThread().getStackTrace()[2].getLineNumber());
                    Log.e(MainActivity.ACTIVITY_TAG, "" + position);
                    vidView.start();
                    //  Toast.makeText(getApplicationContext(), "你選擇的是" + list[position], Toast.LENGTH_SHORT).show();
                }
            });
            // Displays the HTML string in the UI via a WebView
            //  WebView myWebView = (WebView) findViewById(R.id.webview);
            // myWebView.loadData(result, "text/html", null);
        }
    }

    private List<yliu.andriod_player.IptvXmlParser.NetEntry> loadXmlFromNetwork(String urlString) throws XmlPullParserException, IOException {

        InputStream stream = null;
        List<yliu.andriod_player.IptvXmlParser.NetEntry> entries = null;
        try {
            Log.e(MainActivity.ACTIVITY_TAG, ""+Thread.currentThread().getStackTrace()[2].getLineNumber());
            Log.e(MainActivity.ACTIVITY_TAG, ""+urlString);
            stream = downloadUrl(urlString);
            Log.e(MainActivity.ACTIVITY_TAG, ""+Thread.currentThread().getStackTrace()[2].getLineNumber());

            entries = IptvXmlParser.parse(stream);
            Log.e(MainActivity.ACTIVITY_TAG, "" + Thread.currentThread().getStackTrace()[2].getLineNumber());


            // Makes sure that the InputStream is closed after the app is
            // finished using it.
        }
        finally {
            if (stream != null) {
                stream.close();
            }
        }
        return entries;
    }
    private InputStream downloadUrl(String urlString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(10000 /* milliseconds */);
        conn.setConnectTimeout(15000 /* milliseconds */);
        conn.setRequestMethod("GET");
        conn.setDoInput(true);
        // Starts the query
        conn.connect();
        InputStream stream = conn.getInputStream();
        return stream;
    }

}
