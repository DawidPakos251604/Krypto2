import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.math.BigInteger;

public class TextModeController {

    @FXML private TextArea inputArea;
    @FXML private TextField s1Field, s2Field, pField, gField, hField, aField;
    @FXML private Label resultLabel;
    @FXML private ComboBox<String> keySizeCombo;

    private ElGamal.ElGamalKeyPair keys;

    @FXML
    private void initialize() {
        keySizeCombo.getItems().addAll("512", "1024", "2048", "4096");
        keySizeCombo.setValue("1024");
        hField.setEditable(false);
    }

    @FXML
    private void generateKeys() {
        int bitLength = Integer.parseInt(keySizeCombo.getValue());
        keys = ElGamal.generateKeys(bitLength);

        aField.setText(keys.a.toString(16));
        pField.setText(keys.p.toString(16));
        gField.setText(keys.g.toString(16));
        hField.setText(keys.h.toString(16));

        resultLabel.setText("Keys generated with " + bitLength + " bits.");
    }

    @FXML
    private void signMessage() {
        try {
            BigInteger p = new BigInteger(pField.getText().trim(), 16);
            BigInteger g = new BigInteger(gField.getText().trim(), 16);
            BigInteger a = new BigInteger(aField.getText().trim(), 16);

            if (inputArea.getText().isEmpty()) {
                resultLabel.setText("Input message is empty.");
                return;
            }

            BigInteger msg = new BigInteger(inputArea.getText().getBytes());

            BigInteger h = g.modPow(a, p);
            hField.setText(h.toString(16));

            ElGamal.ElGamalKeyPair manualKey = new ElGamal.ElGamalKeyPair();
            manualKey.p = p;

            if (p.equals(BigInteger.TWO)) {
                resultLabel.setText("p must be a large prime (not 2).");
                return;
            }

            if (!Utils.isPrime(p)) {
                resultLabel.setText("p is not a prime number.");
                return;
            }

            manualKey.g = g;
            manualKey.a = a;
            manualKey.h = h;

            ElGamal.ElGamalSignature signature = ElGamal.sign(msg, manualKey);

            s1Field.setText(signature.s1.toString(16));
            s2Field.setText(signature.s2.toString(16));

            resultLabel.setText("Signature generated.");
        } catch (Exception e) {
            resultLabel.setText("Invalid input in fields.");
            e.printStackTrace();
        }
    }

    @FXML
    private void verifySignature() {
        if (inputArea.getText().isEmpty()) {
            resultLabel.setText("Message is empty.");
            return;
        }

        BigInteger message = new BigInteger(inputArea.getText().getBytes());
        BigInteger s1 = new BigInteger(s1Field.getText().trim(), 16);
        BigInteger s2 = new BigInteger(s2Field.getText().trim(), 16);
        BigInteger p = new BigInteger(pField.getText().trim(), 16);
        BigInteger g = new BigInteger(gField.getText().trim(), 16);
        BigInteger h = new BigInteger(hField.getText().trim(), 16);

        ElGamal.ElGamalKeyPair publicKey = new ElGamal.ElGamalKeyPair();
        publicKey.p = p;

        if (!Utils.isPrime(p)) {
            resultLabel.setText("p is not a prime number.");
            return;
        }

        publicKey.g = g;
        publicKey.h = h;

        ElGamal.ElGamalSignature signature = new ElGamal.ElGamalSignature(s1, s2);

        boolean valid = ElGamal.verify(message, signature, publicKey);
        resultLabel.setText(valid ? "Signature is VALID" : "INVALID signature");
    }

    @FXML
    private void goBack(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/mode_selection.fxml"));
            AnchorPane root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Mode selection");
            stage.setScene(new Scene(root));
            stage.show();

            Stage oldStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            oldStage.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
