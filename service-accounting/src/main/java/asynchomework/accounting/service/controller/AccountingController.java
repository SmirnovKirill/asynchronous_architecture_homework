package asynchomework.accounting.service.controller;

import asynchomework.accounting.service.domain.AllAccountsSummary;
import asynchomework.accounting.service.domain.AuthException;
import asynchomework.accounting.service.domain.PersonalAccountInfo;
import asynchomework.accounting.service.service.AccountingService;
import asynchomework.accounting.service.service.AuthService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(
    path = "/accounting",
    consumes = MediaType.APPLICATION_JSON_VALUE,
    produces = MediaType.APPLICATION_JSON_VALUE
)
public class AccountingController {
  private final AuthService authService;
  private final AccountingService accountingService;

  public AccountingController(AuthService authService, AccountingService accountingService) {
    this.authService = authService;
    this.accountingService = accountingService;
  }

  @GetMapping("personal_info")
  public PersonalAccountInfo getPersonalAccountInfo(@CookieValue(value = "userPublicId", required = false) String userPublicId) {
    if (StringUtils.isBlank(userPublicId) || !authService.canViewPersonalAccountInfo(userPublicId)) {
      throw new AuthException("Incorrect role for viewing personal account info");
    }

    return accountingService.getPersonalAccountInfo(authService.getCurrentUser(userPublicId));
  }

  @GetMapping("all_accounts_summary")
  public AllAccountsSummary getAllAccountsSummary(@CookieValue(value = "userPublicId", required = false) String userPublicId) {
    if (StringUtils.isBlank(userPublicId) || !authService.canViewAllAccountsSummary(userPublicId)) {
      throw new AuthException("Incorrect role for viewing all accounts summary");
    }

    return accountingService.getAllAccountsSummary();
  }
}
