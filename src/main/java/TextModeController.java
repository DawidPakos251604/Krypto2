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
    @FXML private TextField s1Field, s2Field, pField, gField, hField;
    @FXML private Label resultLabel;
    @FXML private ComboBox<String> keySizeCombo;

    private ElGamal.ElGamalKeyPair keys;

    @FXML
    private void initialize() {
        keySizeCombo.getItems().addAll("512", "1024", "2048");
        keySizeCombo.setValue("1024");
    }

    @FXML
    private void generateKeys() {
        int bitLength = Integer.parseInt(keySizeCombo.getValue());
        keys = ElGamal.generateKeys(bitLength);

        pField.setText(keys.p.toString());
        gField.setText(keys.g.toString());
        hField.setText(keys.h.toString());

        resultLabel.setText("Keys generated with " + bitLength + " bits.");
    }

    @FXML
    private void signMessage() {
        if (keys == null) {
            resultLabel.setText("Generate keys first.");
            return;
        }
        String message = inputArea.getText();

        if (message.isEmpty()) {
            resultLabel.setText("Input message is empty.");
            return;
        }

        BigInteger msg = new BigInteger(message.getBytes());

        ElGamal.ElGamalSignature signature = ElGamal.sign(msg, keys);

        s1Field.setText(signature.s1.toString());
        s2Field.setText(signature.s2.toString());

        resultLabel.setText("Signature generated.");

    }

    @FXML
    private void verifySignature() {
        if (inputArea.getText().isEmpty()) {
            resultLabel.setText("Message is empty.");
            return;
        }

        BigInteger message = new BigInteger(inputArea.getText().getBytes());
        BigInteger s1 = new BigInteger(s1Field.getText().trim());
        BigInteger s2 = new BigInteger(s2Field.getText().trim());
        BigInteger p = new BigInteger(pField.getText().trim());
        BigInteger g = new BigInteger(gField.getText().trim());
        BigInteger h = new BigInteger(hField.getText().trim());

        ElGamal.ElGamalKeyPair publicKey = new ElGamal.ElGamalKeyPair();
        publicKey.p = p;
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
