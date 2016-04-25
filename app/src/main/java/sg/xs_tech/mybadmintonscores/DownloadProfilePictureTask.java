package sg.xs_tech.mybadmintonscores;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class DownloadProfilePictureTask extends AsyncTask<String, Void, Bitmap> {
    @Override
    protected Bitmap doInBackground(String... paths) {
        final String uri = paths[0];
        final String method = "GET";
        InputStream in;
        URL url;
        HttpURLConnection connection;
        Bitmap image = null;
        try {
            url = new URL(uri);
            connection = (HttpURLConnection) url.openConnection();
            connection.setReadTimeout(10000);
            connection.setConnectTimeout(15000);
            connection.setRequestMethod(method);
            connection.setDoInput(true);
            connection.connect();
            if (connection.getResponseCode() == 200) {
                in = connection.getInputStream();
                image = BitmapFactory.decodeStream(in);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }
}
