package com.example.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.entity.Account;
import com.example.entity.Message;
import com.example.service.AccountService;
import com.example.service.MessageService;


/**
 * TODO: You will need to write your own endpoints and handlers for your controller using Spring. The endpoints you will need can be
 * found in readme.md as well as the test cases. You be required to use the @GET/POST/PUT/DELETE/etc Mapping annotations
 * where applicable as well as the @ResponseBody and @PathVariable annotations. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
@RestController
public class SocialMediaController {
    
    
    private AccountService accountService;
    private MessageService messageService;

    @Autowired
    public SocialMediaController(AccountService accountService, MessageService messageService) {
        this.accountService = accountService;
        this.messageService = messageService;
    }

    // POST /register
    @PostMapping("register")
    public ResponseEntity<Account> registerAccount(@RequestBody Account account) {
        Account registeredAccount = accountService.register(account);

        HttpStatus response_status;
        if (registeredAccount == null) {
            response_status = HttpStatus.CONFLICT;
        } else {
            response_status = HttpStatus.OK;
        }

        return ResponseEntity.status(response_status).body(registeredAccount);
    }

    // POST /login
    @PostMapping("login")
    public ResponseEntity<Account> login(@RequestBody Account account) {
        Optional<Account> loginSuccess = accountService.login(account);

        HttpStatus response_status;
        Account response_body;
        if (loginSuccess.isPresent()) {
            response_body = loginSuccess.get();
            response_status = HttpStatus.OK;
        } else {
            response_status = HttpStatus.UNAUTHORIZED;
            response_body = null;
        }
        return ResponseEntity.status(response_status).body(response_body);
    }

    // POST /messages
    @PostMapping("messages")
    public ResponseEntity<Message> postMessage(@RequestBody Message message) {
        Message newMessage = messageService.post(message);
        if (newMessage == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(newMessage);
        }
        return ResponseEntity.status(HttpStatus.OK).body(newMessage);
    }

    // GET /messages
    @GetMapping("messages")
    public ResponseEntity<List<Message>> getMessages() {
        List<Message> msgList = messageService.getAllMessages();
        HttpStatus response_Status = HttpStatus.OK;

        return ResponseEntity.status(response_Status).body(msgList);
    }

    // GET /messages/{message_id}
    @GetMapping("messages/{message_id}")
    public ResponseEntity<Message> getMessageById(@PathVariable Integer message_id) {
        Message message = messageService.getMessageById(message_id);
        return ResponseEntity.status(HttpStatus.OK).body(message);
    }

    // DELETE /messages/{message_id}
    @DeleteMapping("messages/{message_id}")
    public ResponseEntity<Integer> deleteMessageById(@PathVariable Integer message_id) {
        Message deletedMessage = messageService.deleteMessage(message_id);
        Integer response_body;
        if (deletedMessage == null) {
            response_body = null;
        } else {
            response_body = 1;
        }
        return ResponseEntity.status(HttpStatus.OK).body(response_body);
    }

    // PATCH /messages/{message_id}
    @PatchMapping("messages/{message_id}")
    public ResponseEntity<Integer> patchMessageById(@PathVariable Integer message_id, @RequestBody Message message) {
        
        Message rowsUpdated = messageService.patchMessage(message_id, message);
        HttpStatus response_Status;
        if (rowsUpdated != null) {
            response_Status = HttpStatus.OK;
            return ResponseEntity.status(response_Status).body(1);

        }
        response_Status = HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(response_Status).body(null);
    }

    // GET /accounts/{account_id}/messages
    @GetMapping("accounts/{account_id}/messages")
    public ResponseEntity<List<Message>> getAccountById(@PathVariable Integer account_id) {
        List<Message> messageList = accountService.getMessageByUser(account_id);
        return ResponseEntity.status(HttpStatus.OK).body(messageList);
    }

}

