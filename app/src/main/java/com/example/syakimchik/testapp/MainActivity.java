package com.example.syakimchik.testapp;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;

import com.example.syakimchik.testapp.adapters.CityAdapter;
import com.example.syakimchik.testapp.models.City;
import com.google.gson.Gson;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;


public class MainActivity extends Activity {

    private static final String TAG = "MainActivity";

    private ListView mCitiesListView;
    private View mLoadMoreView;

    private CityAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initUI();
        setupAdapter();
    }

    private void initUI(){
        mCitiesListView = (ListView) findViewById(R.id.cityListView);
        mLoadMoreView = ((LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.load_more_layout, null, false);
        mCitiesListView.addFooterView(mLoadMoreView);
        mCitiesListView.setOnScrollListener(new OnListScrollListener());
    }

    private void setupAdapter(){
        mAdapter = new CityAdapter(this);
        mCitiesListView.setAdapter(mAdapter);
    }

    protected class OnListScrollListener implements AbsListView.OnScrollListener{

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem,
                             int visibleItemCount, int totalItemCount) {
            int lastInScreen = firstVisibleItem + visibleItemCount;
            if(lastInScreen == totalItemCount){
                loadMoreData();
            }
        }
    }

    private void loadMoreData() {
        (new GetDataTask()).execute();
    }

    private class GetDataTask extends AsyncTask<Void, Void, City[]> {

        @TargetApi(Build.VERSION_CODES.KITKAT)
        @Override
        protected City[] doInBackground(Void... params) {

            HttpClient httpClient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(Constants.HOST+"cities");
            try {
                HttpResponse httpResponse = httpClient.execute(httpGet);
                int statusCode = httpResponse.getStatusLine().getStatusCode();
                Log.i(TAG, "status code: " + statusCode);
                if(statusCode== HttpStatus.SC_OK) {
                    String response = EntityUtils.toString(httpResponse.getEntity());
                    Log.i(TAG, "Response: "+response);
                    JSONObject object = new JSONObject(response);
                    JSONArray array = object.getJSONArray("cities");
                    return (new Gson()).fromJson(array.toString(), City[].class);
                }
                else if(statusCode==HttpStatus.SC_INTERNAL_SERVER_ERROR){
                    Log.i(TAG, "Internal server error");
                    return null;
                }
                else if(statusCode==HttpStatus.SC_NOT_FOUND){
                    Log.i(TAG, "Not found");
                    return null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            catch (Exception e){
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(City[] result) {
            if(result!=null) {
                mAdapter.notifyDataSetChanged();
                mAdapter.addAll(result);
            }

            super.onPostExecute(result);
        }
    }
}
