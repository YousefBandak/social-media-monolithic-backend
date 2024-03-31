package object_orienters.techspot.message;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Component
public class Sender {

    @Autowired
    private JmsTemplate templ;
    Reciever r;

    // void init() {
    //     r = new Reciever("Husam");
    // }

    public void sendMessage(String message) {

        templ.convertAndSend("a", message);
        System.out.println("SENT");
    }
}
