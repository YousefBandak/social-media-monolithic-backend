//package object_orienters.techspot.message;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.jms.core.JmsTemplate;
//import org.springframework.stereotype.Component;
//
//@Component
//public class Sender {
//
//    @Autowired
//    private JmsTemplate templ;
//
//    public void sendMessage(String message) {
//        templ.convertAndSend("test-message", message);
//        System.out.println("SENT");
//    }
//}
