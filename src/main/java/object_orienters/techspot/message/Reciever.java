package object_orienters.techspot.message;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class Reciever {

    // final String a = "";
    // String b;

    // Reciever(String b) {
    //     this.b = b;
    //     a.concat(this.b);
    // }

    @JmsListener(destination = "a")
    public void receiveMessage(String message) {
        System.out.println("The message was " + message);
        System.out.println("RECIEVED");
    }

    // public String getA() {
    //     return a;
    // }

}
