package com.example.syakimchik.testapp.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.syakimchik.testapp.R;
import com.example.syakimchik.testapp.models.City;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by syakimchik on 1/27/2015.
 */
public class CityAdapter extends ArrayAdapter<City> {

    public CityAdapter(Context context) {
        super(context, 0);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null){
            LayoutInflater mInflater = (LayoutInflater) getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.item_layout, null);
        }

        ((TextView) convertView.findViewById(R.id.nameTxt)).setText(getItem(position).getName());

        ImageView imageView = (ImageView) convertView.findViewById(R.id.cityImg);

        if(imageView.getTag()==null) {
            imageView.setTag(getItem(position).getImageUrl());
            (new DownloadImagesTask()).execute(imageView);
        }

        return convertView;
    }

    private class DownloadImagesTask extends AsyncTask<ImageView, Void, Bitmap>{

        ImageView imageView = null;

        @Override
        protected Bitmap doInBackground(ImageView... imageViews) {
            this.imageView = imageViews[0];
            return download_Image((String)imageView.getTag());
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            imageView.setImageBitmap(bitmap);
        }

        private Bitmap download_Image(String url) {

            Bitmap bmp =null;
            try{
                URL ulrn = new URL(url);
                HttpURLConnection con = (HttpURLConnection)ulrn.openConnection();
                InputStream is = con.getInputStream();
                bmp = BitmapFactory.decodeStream(is);
                if (null != bmp)
                    return bmp;

            }catch(Exception e){}
            return bmp;
        }
    }
}
