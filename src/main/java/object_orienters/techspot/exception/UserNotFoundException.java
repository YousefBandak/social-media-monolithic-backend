package object_orienters.techspot.exception;

public class UserNotFoundException extends RuntimeException {
   public UserNotFoundException(String username){
       super("User With Username:" + username + " Could Not Be Found.");
   }
}
