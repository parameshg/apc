package com.paramg.android.trydemo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.VpnService;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.Time;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.paramg.android.trydemo.service.LocalVPNService;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity
{
    private class FetchMessagesTask extends AsyncTask<String, Void, Data[]>
    {
        @Override
        protected Data[] doInBackground(String... params)
        {
            Data[] result = null;

            try
            {
                Data data = new Data();

                Time time = new Time();
                time.setToNow();
                int timestamp = Time.getJulianDay(System.currentTimeMillis(), time.gmtoff);

                data.timestamp = String.valueOf(timestamp);
                data.httpMethod = "GET";
                data.url = "www.paramg.com/favicon.ico";
                data.userAgent = "Mozilla/5.0 (Windows NT 10.0; WOW64; rv:43.0) Gecko/20100101 Firefox/43.0";
                data.referrer = "paramg.com";
                data.contentType = "image/vnd.microsoft.icon";
                data.sourceIpAddress = "192.168.1.100";
                data.destinationIpAddress = "8.8.4.4";
                data.statusCode = "200";

                result = new Data[]{data};
            }
            catch (Exception e)
            {
                Log.e(FetchMessagesTask.class.getSimpleName(), e.getMessage());
            }

            return result;
        }

        @Override
        protected void onPostExecute(Data[] data)
        {
            super.onPostExecute(data);

            _adapter.clear();
            _adapter.addAll(new ArrayList<Data>(Arrays.asList(data)));
        }
    }

    private static final int VPN_REQUEST_CODE = 0x0F;

    private boolean _wait;

    private ArrayAdapter<Data> _adapter;

    private BroadcastReceiver receiver = new BroadcastReceiver()
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            if (LocalVPNService.BROADCAST_VPN_STATE.equals(intent.getAction()))
            {
                if (intent.getBooleanExtra("running", false))
                {
                    _wait = false;
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        _adapter = new ArrayAdapter(this, R.layout.message, R.id.message_textview, new ArrayList<Data>());

        ListView list = (ListView) findViewById(R.id.messages);

        list.setAdapter(_adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                startActivity(new Intent(getApplicationContext(), DetailActivity.class).putExtra(Intent.EXTRA_STREAM, _adapter.getItem(position).toString()));
            }
        });

        new FetchMessagesTask().execute();

        _wait = false;

        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, new IntentFilter(LocalVPNService.BROADCAST_VPN_STATE));
    }

    @Override
    protected void onResume()
    {
        super.onResume();

        toggleVpnButton(!_wait && !LocalVPNService.isRunning());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == VPN_REQUEST_CODE && resultCode == RESULT_OK)
        {
            _wait = true;

            startService(new Intent(this, LocalVPNService.class));

            toggleVpnButton(false);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();

        if (id == R.id.action_settings)
        {
            startActivity(new Intent(this, SettingsActivity.class));
        }

        if (id == R.id.action_vpn)
        {
            Intent intent = VpnService.prepare(this);

            if (intent != null)
            {
                startActivityForResult(intent, VPN_REQUEST_CODE);
            }
            else
            {
                onActivityResult(VPN_REQUEST_CODE, RESULT_OK, null);
            }
        }

        return super.onOptionsItemSelected(item);
    }

    private void toggleVpnButton(boolean flag)
    {
        final Button btn = (Button) findViewById(R.id.action_vpn);

        if (btn != null)
        {
            btn.setEnabled(flag);

            btn.setText(flag ? R.string.action_start : R.string.action_stop);
        }
    }
}