package fr.telecom_paris.fdelima.cambio;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioGroup;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.util.concurrent.ExecutionException;

public class CurrencyChooserActivity extends AppCompatActivity {

    private Currency fromCurrency, toCurrency;
    private JSONObject rates;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_currency_chooser);
        downloadCurrencyRates();
    }

    public void setTaxFromEuro(View sender) { fromCurrency = Currency.EUR; }
    public void setTaxFromDollar(View sender) { fromCurrency = Currency.USD; }
    public void setTaxFromPound(View sender) { fromCurrency = Currency.GBP; }
    public void setTaxFromReal(View sender) { fromCurrency = Currency.BRL; }
    public void setTaxToEuro(View sender) { toCurrency = Currency.EUR; }
    public void setTaxToDollar(View sender) { toCurrency = Currency.USD; }
    public void setTaxToPound(View sender) { toCurrency = Currency.GBP; }
    public void setTaxToReal(View sender) { toCurrency = Currency.BRL; }

    public void chooseCurrency(View sender) {
        double fromTax, toTax, tax;

        switch (fromCurrency) {
            case EUR:
                fromTax = obtainRate(Currency.EUR.code());
                break;
            case USD:
                fromTax = obtainRate(Currency.USD.code());
                break;
            case GBP:
                fromTax = obtainRate(Currency.GBP.code());
                break;
            case BRL:
                fromTax = obtainRate(Currency.BRL.code());
                break;
            default:
                fromTax = 1.0;
        }

        switch (toCurrency) {
            case EUR:
                toTax = obtainRate(Currency.EUR.code());
                break;
            case USD:
                toTax = obtainRate(Currency.USD.code());
                break;
            case GBP:
                toTax = obtainRate(Currency.GBP.code());
                break;
            case BRL:
                toTax = obtainRate(Currency.BRL.code());
                break;
            default:
                toTax = 1.0;
        }

        tax = toTax/fromTax;

        sendTaxResult(tax);
    }

    public void sendTaxResult(double tax) {
        Intent resultData = new Intent(Intent.ACTION_VIEW);
        resultData.putExtra("tax", tax);
        resultData.putExtra("origin", fromCurrency.value());
        resultData.putExtra("result", toCurrency.value());
        this.setResult(RESULT_OK, resultData);
        finish();
    }

    // Mèthode utilisé pour le chargement des taux à partir du fichier local (non plus en usage)
    private JSONObject loadCurrency() {
        InputStream inputStream = getResources().openRawResource(R.raw.taux_2017_11_02);
        StringBuilder stringBuilder = new StringBuilder();

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line = null;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line + "\n");
            }

            String jsonString = stringBuilder.toString();
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

    private void downloadCurrencyRates() {
        DataDownloadTask ratesDownload = new DataDownloadTask();
        ratesDownload.execute("https://perso.telecom-paristech.fr/eagan/cours/igr201/data/taux_2017_11_02.json");

        try {
            rates = ratesDownload.get();
        }
        catch (InterruptedException e) {
            System.err.println("Warning: Could not read rates: " + e.getLocalizedMessage());
        }
        catch (ExecutionException e) {
            System.err.println("Warning: Could not read rates: " + e.getLocalizedMessage());
        }
    }

    private double obtainRate(String currency) {
        try {
            return rates.getJSONObject("rates").getDouble(currency);
        }
        catch (JSONException e) {
            System.err.println("Warning: Could not parse rates: " + e.getLocalizedMessage());
            return 0;
        }
    }

}