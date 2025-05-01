//import java.math.BigInteger;
//import java.nio.file.Files;
//import java.nio.file.Paths;
//import java.util.Scanner;
//
//public class Main {
//    public static void main(String[] args) {
//        Scanner scanner = new Scanner(System.in);
//        String input = "";
//
//        // Wybór źródła wiadomości
//        System.out.println("Wybierz sposób wprowadzenia wiadomości:");
//        System.out.println("1 - Wprowadzenie z klawiatury");
//        System.out.println("2 - Wczytanie z pliku");
//        System.out.print("Twój wybór: ");
//        String choice = scanner.nextLine();
//
//        if (choice.equals("1")) {
//            System.out.print("Wprowadź wiadomość do podpisania: ");
//            input = scanner.nextLine();
//        } else if (choice.equals("2")) {
//            String filePath = "1.txt";
//            System.out.println("Próba otwarcia pliku: " + filePath);
//
//            try {
//                input = new String(Files.readAllBytes(Paths.get(filePath)));
//            } catch (Exception e) {
//                System.out.println("Błąd podczas wczytywania pliku: ");
//                e.printStackTrace();
//                return;
//            }
//        } else {
//            System.out.println("Nieprawidłowy wybór. Program zakończony.");
//            return;
//        }
//
//        // Konwersja wiadomości na BigInteger
//        BigInteger message = new BigInteger(1, input.getBytes());
//
//        // Generowanie kluczy i podpisywanie wiadomości
//        ElGamal.ElGamalKeyPair keys = ElGamal.generateKeys(512);
//        ElGamal.ElGamalSignature signature = ElGamal.sign(message, keys);
//
//        // Wyświetlenie danych
//        System.out.println("\n=== Klucze i podpis ===");
//        System.out.println("p: " + keys.p);
//        System.out.println("g: " + keys.g);
//        System.out.println("h: " + keys.h);
//        System.out.println("a (klucz prywatny): " + keys.a);
//        System.out.println("Podpis s1: " + signature.s1);
//        System.out.println("Podpis s2: " + signature.s2);
//
//        // Weryfikacja podpisu
//        boolean valid = ElGamal.verify(message, signature, keys);
//        System.out.println("Weryfikacja podpisu: " + (valid ? "POPRAWNA" : "BŁĘDNA"));
//    }
//}


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

