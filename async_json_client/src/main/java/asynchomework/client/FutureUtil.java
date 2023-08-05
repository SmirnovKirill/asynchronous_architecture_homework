package asynchomework.client;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class FutureUtil {
  public static <T> T get(CompletableFuture<T> future) {
    try {
      return future.get();
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new RuntimeException("Thread was interrupted", e);
    } catch (ExecutionException e) {
      throw new RuntimeException("Exception occurred in future", e);
    }
  }
}
