package asynchomework.auth.client;

import asynchomework.auth.api.UserDto;
import asynchomework.auth.api.create.CreateUserRequestDto;
import asynchomework.auth.api.create.CreateUserResponseDto;
import asynchomework.client.AsyncJsonHttpClient;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import org.springframework.util.MultiValueMapAdapter;

public class UserClient {
  private final AsyncJsonHttpClient httpClient = new AsyncJsonHttpClient("localhost", 8080, "user");

  public CompletableFuture<UserDto> getUser(long userId) {
    return httpClient.get("", new MultiValueMapAdapter<>(Map.of("user_id", List.of(String.valueOf(userId)))), UserDto.class);
  }

  public CompletableFuture<CreateUserResponseDto> createUser(CreateUserRequestDto createUserRequest) {
    return httpClient.post("", createUserRequest, CreateUserResponseDto.class);
  }
}
