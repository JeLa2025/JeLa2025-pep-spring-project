package com.example.service;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.Account;
import com.example.entity.Message;
import com.example.repository.AccountRepository;
import com.example.repository.MessageRepository;

@Service
public class AccountService {
    //connection to Account table
    AccountRepository accountRepository;

    // connection to Message table
    MessageRepository messageRepository;

    // constructor injection
    @Autowired
    public AccountService(AccountRepository accountRepository, MessageRepository messageRepository) {
        this.accountRepository = accountRepository;
        this.messageRepository = messageRepository;
    }

    // check username and password
    private boolean userNameAndPasswordCheck(String username, String password) {
        return (username.length() > 0 && password.length() >= 4);
    }

    // 1) Our API should be able to process new User registrations
    @Transactional
    public Account register(Account account) {
        String username = account.getUsername();
        String password = account.getPassword();
        // check if the username and password are valid
        boolean isValidName = userNameAndPasswordCheck(username, password);

        // check if account already exists by username
        Account doesExists = accountRepository.findAccountByUsername(username);

        // username and password is valid, user doesn't exist in the table
        if (isValidName && doesExists == null) {
            Account newAccount = new Account(username, password);
            return accountRepository.save(newAccount);
        }
        return null;
    }

    // 2) Our API should be albe to process User logins
    public Optional<Account> login(Account account) {
        String username = account.getUsername();
        String password = account.getPassword();

        // check if account is valid by username and password
        return accountRepository.findAccountByUsernameAndPassword(username, password);
    }

    // 8) Our API should be able to retrieve all messages written by a particular user
    public List<Message> getMessageByUser(Integer account_id) {
        return messageRepository.findMessageByPostedBy(account_id);
    }

    public List<Account> getAllAccount() {
        return accountRepository.findAll();
    }
}