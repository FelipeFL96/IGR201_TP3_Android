package fr.telecom_paris.fdelima.cambio;

import android.icu.text.DecimalFormat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.EditText;
import android.content.Intent;

public class MainActivity extends AppCompatActivity {

    public final static int DEFINE_EXCHANGE_TAX = 1;
    private double exchangeTax = 1.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setScreenFields(Currency.EUR, Currency.EUR, 1.0, false);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == DEFINE_EXCHANGE_TAX) {
            if (resultCode == RESULT_OK) {
                double exchangeTax = data.getDoubleExtra("tax", 1.0);
                int originValue= data.getIntExtra("origin", Currency.EUR.value());
                int resultValue= data.getIntExtra("result", Currency.EUR.value());
                this.exchangeTax = exchangeTax;
                Currency origin = Currency.convertFromValue(originValue);
                Currency result = Currency.convertFromValue(resultValue);
                setScreenFields(origin, result, exchangeTax, true);
            }
        }
    }

    public void convert(View sender) {
        EditText eurosEditText = (EditText) findViewById(R.id.origin_field);
        EditText resultEditText = (EditText) findViewById(R.id.result_field);

        double eurosValue = Double.parseDouble(eurosEditText.getText().toString());

        double dollarsValue = eurosValue * exchangeTax;
        DecimalFormat numberFormat = new DecimalFormat("0.00");
        resultEditText.setText(numberFormat.format(dollarsValue));
    }

    public void chooseCurrency(View sender) {
        final Intent chooseIntent = new Intent(this, CurrencyChooserActivity.class);
        startActivityForResult(chooseIntent, DEFINE_EXCHANGE_TAX);
    }

    private void setScreenFields(Currency origin, Currency result, double tax, boolean convertButtonEnabled) {
        TextView originLabel = (TextView) findViewById(R.id.origin_label);
        TextView resultLabel = (TextView) findViewById(R.id.result_label);
        EditText originField = (EditText) findViewById(R.id.origin_field);
        EditText taxEditText = (EditText) findViewById(R.id.tax_field);
        EditText resultField = (EditText) findViewById(R.id.result_field);
        Button convertButton = (Button) findViewById(R.id.convert_button);
        DecimalFormat numberFormat = new DecimalFormat("0.00");

        originLabel.setText(origin.getName());
        originLabel.setHint(origin.getName());
        resultLabel.setText(result.getName());
        resultLabel.setHint(result.getName());
        convertButton.setEnabled(convertButtonEnabled);
        taxEditText.setText(numberFormat.format(tax));
    }
}