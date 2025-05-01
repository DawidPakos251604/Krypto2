import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.math.BigInteger;

public class TextModeController {

    @FXML private TextArea inputArea;
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
        resultLabel.setText("Keys generated with " + bitLength + " bits.");
    }

    @FXML
    private void signMessage() {
        if (keys == null) {
            resultLabel.setText("Generate keys first.");
            return;
        }
        String message = inputArea.getText();
        BigInteger msg = new BigInteger(message.getBytes());

        ElGamal.ElGamalSignature signature = ElGamal.sign(msg, keys);
        resultLabel.setText("Signature:\nS1: " + signature.s1 + "\nS2: " + signature.s2);

        inputArea.setText(message + "\n" + signature.s1 + "\n" + signature.s2);
    }

    @FXML
    private void verifySignature() {
        if (keys == null) {
            resultLabel.setText("Generate keys first.");
            return;
        }

        String[] lines = inputArea.getText().split("\n");
        if (lines.length < 3) {
            resultLabel.setText("Format: message\\nS1\\nS2");
            return;
        }

        BigInteger message = new BigInteger(lines[0].getBytes());
        BigInteger s1 = new BigInteger(lines[1]);
        BigInteger s2 = new BigInteger(lines[2]);

        ElGamal.ElGamalSignature signature = new ElGamal.ElGamalSignature();
        signature.s1 = s1;
        signature.s2 = s2;

        boolean valid = ElGamal.verify(message, signature, keys);
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
