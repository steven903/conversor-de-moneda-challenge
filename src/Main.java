import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import org.json.JSONObject;

class CurrencyConverter {
    // URL base de la API
    private static final String API_URL = "https://v6.exchangerate-api.com/v6/fdd4bd00acd3e15a22cc5682/latest/";

    public static void main(String[] args) {
        JFrame frame = new JFrame("Conversor de Monedas");
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new GridLayout(6, 2, 10, 10));

        // Monedas disponibles (10 monedas adicionales)
        String[] currencies = {
                "USD", "EUR", "COP", "MXN", "BRL", "JPY", "GBP", "CAD", "AUD", "CNY", "INR", "CHF", "KRW", "SGD", "ZAR"
        };

        JLabel baseCurrencyLabel = new JLabel("Moneda base:");
        JComboBox<String> baseCurrencyBox = new JComboBox<>(currencies);

        JLabel targetCurrencyLabel = new JLabel("Moneda destino:");
        JComboBox<String> targetCurrencyBox = new JComboBox<>(currencies);

        JLabel amountLabel = new JLabel("Cantidad:");
        JTextField amountField = new JTextField();

        JButton convertButton = new JButton("Convertir");
        JLabel resultLabel = new JLabel("Resultado: ");

        frame.add(baseCurrencyLabel);
        frame.add(baseCurrencyBox);
        frame.add(targetCurrencyLabel);
        frame.add(targetCurrencyBox);
        frame.add(amountLabel);
        frame.add(amountField);
        frame.add(new JLabel()); // Espaciador
        frame.add(convertButton);
        frame.add(new JLabel()); // Espaciador
        frame.add(resultLabel);

        convertButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String baseCurrency = baseCurrencyBox.getSelectedItem().toString();
                String targetCurrency = targetCurrencyBox.getSelectedItem().toString();
                try {
                    double amount = Double.parseDouble(amountField.getText());
                    double rate = getExchangeRate(baseCurrency, targetCurrency);
                    double convertedAmount = amount * rate;
                    resultLabel.setText(String.format("Resultado: %.2f %s", convertedAmount, targetCurrency));
                } catch (NumberFormatException ex) {
                    resultLabel.setText("Por favor, ingrese una cantidad válida.");
                } catch (Exception ex) {
                    resultLabel.setText("Error: " + ex.getMessage());
                }
            }
        });

        frame.setVisible(true);
    }

    private static double getExchangeRate(String baseCurrency, String targetCurrency) throws Exception {
        String urlString = API_URL + baseCurrency;
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        if (connection.getResponseCode() != 200) {
            throw new Exception("Error al obtener tasas de cambio.");
        }

        Scanner scanner = new Scanner(url.openStream());
        StringBuilder response = new StringBuilder();
        while (scanner.hasNext()) {
            response.append(scanner.nextLine());
        }
        scanner.close();

        JSONObject jsonResponse = new JSONObject(response.toString());
        JSONObject rates = jsonResponse.getJSONObject("conversion_rates");
        return rates.getDouble(targetCurrency);
    }
}


