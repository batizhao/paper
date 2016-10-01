package io.github.batizhao.service.iml;

import io.github.batizhao.domain.Account;
import io.github.batizhao.repository.AccountRepository;
import io.github.batizhao.service.AccountService;
import io.github.batizhao.util.Digests;
import io.github.batizhao.util.Encodes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

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
    public Account save(Account account) {
        //设定安全的密码，生成 SHA-1 hash
        byte[] hashPassword = Digests.sha1("123456".getBytes());
        account.setPassword(Encodes.encodeHex(hashPassword));

        account.setTime(new Date());
        return accountRepository.save(account);
    }

    @Override
    public Account findOne(Long id) {
        return accountRepository.findOne(id);
    }

    @Override
    public Account update(Account account) {
        byte[] hashPassword = Digests.sha1("123456".getBytes());
        account.setPassword(Encodes.encodeHex(hashPassword));

        account.setTime(new Date());
        return accountRepository.save(account);
    }

    @Override
    public void delete(Long id) {
        accountRepository.delete(id);
    }

    @Override
    public Iterable<Account> findByRoles(String role) {
        return accountRepository.findByRoles(role);
    }
}
