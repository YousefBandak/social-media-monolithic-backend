package object_orienters.techspot.security.payload.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JwtResponse {
  private String token;
  private String type = "Bearer";
  private String refreshToken;
  private String username;
  private String email;

  public JwtResponse(String accessToken, String refreshToken, String username, String email) {
    this.token = accessToken;
    this.refreshToken = refreshToken;
    this.username = username;
    this.email = email;
  }

}
