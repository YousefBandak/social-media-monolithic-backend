package object_orienters.techspot.message;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import org.slf4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.io.FileInputStream;
import java.io.IOException;

@Configuration
public class FirebaseConfig {

    Logger logger = org.slf4j.LoggerFactory.getLogger(FirebaseConfig.class);

    @SuppressWarnings("deprecation")
    @PostConstruct
    public void initializeFirebase() throws IOException {
        logger.info("Initializing Firebase");
        FileInputStream serviceAccount =
                new FileInputStream("src/main/resources/techspot-objectorienters-firebase-adminsdk-bqho2-f5981f1058.json");


        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .build();

        FirebaseApp.initializeApp(options);
        logger.info("Firebase Initialized");
    }

    @Bean
    public Firestore firestore() {
        return FirestoreClient.getFirestore();
    }

}
