package asynchomework.tracker.service.service;

import java.math.BigDecimal;
import java.util.Random;
import org.springframework.stereotype.Service;

@Service
public class TaskPriceService {
  private static final Random RANDOM = new Random();

  public BigDecimal calculateAssignFee() {
    return new BigDecimal(RANDOM.nextInt(21) % 21 + 20);
  }

  public BigDecimal calculateResolvePrice() {
    return new BigDecimal(RANDOM.nextInt(11) % 11 + 10);
  }
}
