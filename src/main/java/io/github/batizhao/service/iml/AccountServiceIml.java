package io.github.batizhao.service.iml;

import io.github.batizhao.domain.Account;
import io.github.batizhao.repository.AccountRepository;
import io.github.batizhao.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

/**
 * @author batizhao
 * @since 2016/9/28
 */
@Service
public class AccountServiceIml implements AccountService {

    @Autowired
    AccountRepository accountRepository;

    @Override
    public Account findByUsername(String username) {
        return accountRepository.findByUsername(username);
    }

    @Override
    public Iterable<Account> findAll() {
        return accountRepository.findAll();
    }

    @Override
    public Optional<Account> findOne(Long id) {
        return accountRepository.findById(id);
    }

    @Override
    public Account save(Account account) {
        account.setTime(new Date());
        return accountRepository.save(account);
    }

    @Override
    public Account update(Account account) {
        account.setTime(new Date());
        return accountRepository.save(account);
    }

    @Override
    public void delete(Long id) {
        accountRepository.deleteById(id);
    }

    @Override
    public Iterable<Account> findByRoles(String role) {
        return accountRepository.findByRoles(role);
    }

}
