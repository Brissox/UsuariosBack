package cl.bakery.Usuarios.Config;

import java.io.FileInputStream;
import java.io.IOException;

import org.springframework.context.annotation.Configuration;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

import jakarta.annotation.PostConstruct;

@Configuration
public class FirebaseConfig {

    @PostConstruct
    public void initializeFirebase() throws IOException {
        if (FirebaseApp.getApps().isEmpty()) {
            FileInputStream serviceAccount = new FileInputStream(
                    "C:\\Users\\briss\\OneDrive\\Escritorio\\BackEnd\\BackEnd_BackeryAPP\\Usuarios\\Usuarios\\src\\main\\resources\\bakerymobile-5a946-firebase-adminsdk-fbsvc-cd5ea0ce55 (1).json");

            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();

            FirebaseApp.initializeApp(options);
            System.out.println("Firebase Admin inicializado correctamente");
        }
    }
}

// INICIALIZACION DE FIREBASE PARA PODER USARLO EN EL PROYECTO