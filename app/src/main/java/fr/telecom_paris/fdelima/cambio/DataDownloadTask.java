package fr.telecom_paris.fdelima.cambio;

/**
 * Created by fdelima on 08/10/19.
 */

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;


class DataDownloadTask extends AsyncTask<String, Void, JSONObject> {

    @Override
    protected JSONObject doInBackground(String ...urls) {
        return downloadCurrency(urls);
    }

    @Override
    protected void onPostExecute(JSONObject result) {
        System.out.println("Download task finished");
    }

    private JSONObject downloadCurrency(String ...urls) {
        try {
            String url = urls[0];
            System.out.println("url: " + url);

            URL exchangeRatesURL = new URL(url);
            InputStream inputStream = exchangeRatesURL.openStream();

            StringBuilder stringBuilder = new StringBuilder();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line = null;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line + "\n");
            }

            String jsonString = stringBuilder.toString();
            System.out.println("JSON: " + jsonString);
            return new JSONObject(jsonString);
        }
        catch (IOException e) {
            System.err.println("Warning: Could not read rates: " + e.getLocalizedMessage());
        }
        catch (JSONException e) {
            System.err.println("Warning: Could not parse rates: " + e.getLocalizedMessage());
        }

        return null;
    }
}
