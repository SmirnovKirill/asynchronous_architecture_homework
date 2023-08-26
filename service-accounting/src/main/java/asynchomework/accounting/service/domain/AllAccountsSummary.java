package asynchomework.accounting.service.domain;

import java.util.List;

public record AllAccountsSummary(
    List<AllAccountsSummaryDay> summaryDays
) {
}
