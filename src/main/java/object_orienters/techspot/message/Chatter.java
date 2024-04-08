package object_orienters.techspot.message;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Chatter {

    private String username;
    private String fullName;
    private Status status;

    public Chatter(String username, Status status) {
        this.username = username;
        this.status = status;
    }


}

enum Status {
    ONLINE,
    OFFLINE
}