package com.example.electricitybill;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class MainActivity extends AppCompatActivity {

    private Toolbar myToolbar;
    private EditText inputUnits, inputRebate;
    private Button calculateButton, clearButton;
    private TextView resultText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Toolbar setup
        myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.app_name);
        }

        // Initialize UI components
        inputUnits = findViewById(R.id.inputUnits);
        inputRebate = findViewById(R.id.inputRebate);
        calculateButton = findViewById(R.id.calculateButton);
        clearButton = findViewById(R.id.clearButton);
        resultText = findViewById(R.id.resultText);

        // Calculate Button Click Listener
        calculateButton.setOnClickListener(v -> {
            String unitsText = inputUnits.getText().toString();
            String rebateText = inputRebate.getText().toString();

            if (unitsText.isEmpty() || rebateText.isEmpty()) {
                Toast.makeText(this, "Please enter all fields!", Toast.LENGTH_SHORT).show();
                return;
            }

            int units = Integer.parseInt(unitsText);
            double rebate = Double.parseDouble(rebateText);

            if (rebate < 0 || rebate > 5) {
                Toast.makeText(this, "Rebate must be between 0% and 5%.", Toast.LENGTH_SHORT).show();
                return;
            }

            double totalCost = calculateElectricityBill(units, rebate);
            resultText.setText(String.format("Total Cost (After Rebate): RM %.2f", totalCost));
        });

        // Clear Button Click Listener
        clearButton.setOnClickListener(v -> {
            inputUnits.setText("");
            inputRebate.setText("");
            resultText.setText("Enter Amount below");
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int selected = item.getItemId();

        if (selected == R.id.menuAbout) {
            Intent aboutIntent = new Intent(this, AboutActivity.class);
            startActivity(aboutIntent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // Electricity Bill Calculation
    private double calculateElectricityBill(int units, double rebatePercentage) {
        double totalCharges = 0.0;

        if (units <= 200) {
            totalCharges = units * 0.218;
        } else if (units <= 300) {
            totalCharges = (200 * 0.218) + ((units - 200) * 0.334);
        } else if (units <= 600) {
            totalCharges = (200 * 0.218) + (100 * 0.334) + ((units - 300) * 0.516);
        } else {
            totalCharges = (200 * 0.218) + (100 * 0.334) + (300 * 0.516) + ((units - 600) * 0.546);
        }

        double rebate = totalCharges * (rebatePercentage / 100);
        return totalCharges - rebate;
    }
}