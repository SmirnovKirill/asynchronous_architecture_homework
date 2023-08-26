package asynchomework.accounting.service.domain;

import java.util.List;

public record PersonalAccountInfo(
    Account account,
    List<AccountLog> accountLogs
) {
}
