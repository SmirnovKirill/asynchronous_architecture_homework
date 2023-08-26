package asynchomework.accounting.service.domain;

import java.math.BigDecimal;
import java.time.LocalDate;

public record AllAccountsSummaryDay(
    LocalDate day,
    BigDecimal amount
) {
}
