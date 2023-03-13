package com.itheima.reggie.controller;

import com.itheima.reggie.common.R;
import com.itheima.reggie.entity.Account;
import com.itheima.reggie.entity.Details;
import com.itheima.reggie.service.AccountService;
import com.itheima.reggie.service.DetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequestMapping("/details")
public class DetailsController {
    @Autowired
    private DetailsService detailsService;
    @Autowired
    private AccountService accountService;

    /**
     * @param account
     * @return
     */
    @PostMapping
    public R<String> add(@RequestBody Account account) {

        Long accountId = account.getId();
        Account accountById = accountService.getById(accountId);
        if (accountById == null) {
            return R.error("账户不存在");
        }
        Integer status = accountById.getStatus();
        if (status == 0) {
            return R.error("账户已被冻结充值失败");
        }
        accountById.setBalance(accountById.getBalance().add(account.getBalance()));
        accountService.updateById(accountById);
        Account accountById2 = accountService.getById(accountId);
        Details details = new Details();
        details.setBalance(account.getBalance());
        details.setAccountId(accountId);
        detailsService.save(details);
        return R.success("充值成功后的余额为" + accountById2.getBalance() + "元");
    }

    @PostMapping("/reduce")
    public R<String> reduce(@RequestBody Account account) {
        Long id = account.getId();
        Account serviceById = accountService.getById(id);
        if (serviceById == null) {
            return R.error("账户不存在,请从新输入");
        }
        Integer status = serviceById.getStatus();
        if (status == 0) {
            return R.error("账户已被冻结");
        }
        if (serviceById.getBalance().compareTo(account.getBalance()) == -1) {
            return R.error("账户余额不足,卡内剩余" + serviceById.getBalance() + "元");
        }
        serviceById.setBalance(serviceById.getBalance().subtract(account.getBalance()));
        accountService.updateById(serviceById);

        Details details = new Details();
        details.setAccountId(id);
        details.setBalance(account.getBalance().multiply(new BigDecimal(-1)));
        detailsService.save(details);

        Account account1 = accountService.getById(id);
        return R.success("本次消费" + account.getBalance() + "元,卡内余额为" + account1.getBalance() + "元");

    }
}