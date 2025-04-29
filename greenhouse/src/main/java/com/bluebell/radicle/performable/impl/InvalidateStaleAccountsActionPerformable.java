package com.bluebell.radicle.performable.impl;

import com.bluebell.platform.models.core.entities.account.Account;
import com.bluebell.platform.models.core.nonentities.action.ActionData;
import com.bluebell.radicle.performable.ActionPerformable;
import com.bluebell.radicle.services.account.AccountService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

/**
 * Action that marks stale {@link Account}s as inactive
 *
 * @author Stephen Prizio
 * @version 0.1.8
 */
@Component("invalidateStaleAccountsActionPerformable")
public class InvalidateStaleAccountsActionPerformable implements ActionPerformable {

    @Resource(name = "accountService")
    private AccountService accountService;


    //  METHODS

    @Override
    public ActionData perform() {
        try {
            final int count = this.accountService.invalidateStaleAccounts();
            if (count > 0) {
                return ActionData
                        .builder()
                        .success(true)
                        .logs(String.format("%d stale accounts were marked as inactive", count))
                        .build();
            } else {
                return ActionData
                        .builder()
                        .success(true)
                        .data("No stale accounts were found.")
                        .build();
            }
        } catch (Exception e) {
            return ActionData
                    .builder()
                    .success(false)
                    .logs(getStackTraceAsString(e))
                    .build();
        }
    }
}
