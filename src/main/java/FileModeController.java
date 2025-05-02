import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;

public class FileModeController {

    @FXML private TextField filePathInput;
    @FXML private Button signButton;
    @FXML private Button verifyButton;
    private String loadedFilePath;
    private ElGamal.ElGamalKeyPair keys;

    @FXML
    private void loadFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose File");
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            loadedFilePath = file.getPath();
            filePathInput.setText(loadedFilePath);
            showAlert(Alert.AlertType.INFORMATION, "File Loaded", "Loaded file: " + loadedFilePath);
        }
    }

    @FXML
    private void signFile() {
        if (loadedFilePath == null) {
            showAlert(Alert.AlertType.WARNING, "No File", "Load a file first.");
            return;
        }

        try {
            BigInteger hash = FileSigner.hashFile(loadedFilePath);
            keys = ElGamal.generateKeys(512);
            ElGamal.ElGamalSignature sig = ElGamal.sign(hash, keys);

            File out = new File(loadedFilePath + ".sig");
            FileSigner.saveSignature(out.getPath(), sig);

            showAlert(Alert.AlertType.INFORMATION, "Success", "File signed successfully.\nSignature saved as: " + out.getName());

        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", e.getMessage());
        }
    }

    @FXML
    private void verifyFile() {
        if (loadedFilePath == null || keys == null) {
            showAlert(Alert.AlertType.WARNING, "No File or Key", "Make sure file is loaded and signed.");
            return;
        }

        try {
            BigInteger hash = FileSigner.hashFile(loadedFilePath);
            ElGamal.ElGamalSignature sig = FileSigner.loadSignature(loadedFilePath + ".sig");

            boolean valid = ElGamal.verify(hash, sig, keys);
            showAlert(Alert.AlertType.INFORMATION, "Verification", valid ? "Signature is VALID" : "Signature is INVALID");

        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", e.getMessage());
        }
    }

    @FXML
    private void savePublicKeyToFile() {
        if (keys == null) {
            showAlert(Alert.AlertType.WARNING, "No Key", "Generate or load a key first.");
            return;
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Public Key");
        File file = fileChooser.showSaveDialog(null);

        if (file != null) {
            try {
                FileSigner.savePublicKey(file.getAbsolutePath(), keys);
                showAlert(Alert.AlertType.INFORMATION, "Saved", "Public key saved.");
            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "Error", e.getMessage());
            }
        }
    }

    @FXML
    private void loadPublicKeyFromFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Public Key File");
        File file = fileChooser.showOpenDialog(null);

        if (file != null) {
            try {
                keys = FileSigner.loadPublicKey(file.getAbsolutePath());
                showAlert(Alert.AlertType.INFORMATION, "Loaded", "Public key loaded.");
            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "Error", e.getMessage());
            }
        }
    }


    private void showAlert(Alert.AlertType type, String title, String msg) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
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
