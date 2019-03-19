package com.example.music_58.data.source.remote;

import android.os.AsyncTask;

import com.example.music_58.data.source.TrackDataSource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public abstract class BaseAsyncTask<T> extends AsyncTask<String, T, List<T>> {
    private static final String METHOD_REQUEST_API = "GET";
    private static final int CONNECTION_TIMEOUT = 15000;
    private static final int READ_TIME_OUT = 15000;
    protected TrackDataSource.DataCallback<T> mDataCallback;
    protected Exception mException;
    private HttpURLConnection mURLConnection;

    public BaseAsyncTask(TrackDataSource.DataCallback<T> callback) {
        mDataCallback = callback;
    }

    @Override
    protected List<T> doInBackground(String... strings) {
        String respond = "";
        try {
            URL url = new URL(strings[0]);
            mURLConnection = (HttpURLConnection) url.openConnection();
            mURLConnection.setRequestMethod(METHOD_REQUEST_API);
            mURLConnection.setConnectTimeout(CONNECTION_TIMEOUT);
            mURLConnection.setReadTimeout(READ_TIME_OUT);
            mURLConnection.connect();
            InputStream inputStream = mURLConnection.getInputStream();
            respond = readResponse(inputStream);
        } catch (IOException e) {
            mException = e;
        }
        mURLConnection.disconnect();
        return convertJSON(respond);
    }

    @Override
    protected void onPostExecute(List<T> data) {
        super.onPostExecute(data);
        if (mException == null && mDataCallback != null) {
            mDataCallback.onSuccess(data);
        } else {
            mDataCallback.onFailure(mException.getMessage());
        }
    }

    public abstract List<T> convertJSON(String respond);

    private String readResponse(InputStream inputStream) throws IOException {
        if (inputStream == null) {
            return null;
        }
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder builder = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            builder.append(line);
        }
        reader.close();
        return builder.toString();
    }
}
