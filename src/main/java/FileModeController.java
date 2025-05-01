import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;

import java.io.File;
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

    private void showAlert(Alert.AlertType type, String title, String msg) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
