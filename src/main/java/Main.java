//--------------------------------------//
//  Autorzy:                            //
//  Dawid Pakos 251604                  //
//  Aleksandra Szczepocka 251642        //
//  Zespol 6                            //
//--------------------------------------//

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/mode_selection.fxml"));
        AnchorPane root = loader.load();

        Scene scene = new Scene(root);
        primaryStage.setTitle("Mode selection");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}

