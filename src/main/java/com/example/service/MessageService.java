package com.example.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.Account;
import com.example.entity.Message;
import com.example.repository.AccountRepository;
import com.example.repository.MessageRepository;

@Service
public class MessageService {
    // connection to the Account table
    AccountRepository accountRepository;

    // connection to the Message table
    MessageRepository messageRepository;

    //constructor injection
    @Autowired
    public MessageService(MessageRepository messageRepository, AccountRepository accountRepository) {
        this.messageRepository = messageRepository;
        this.accountRepository = accountRepository;
    }

    // message length shoulg be over 0 and under 256
    private boolean messageChecker(String message) {
        return (message.length() > 0 && message.length() <= 255);
    }

    // 3) Our API should be albe to process the creation of new messages
    @Transactional
    public Message post(Message message) {
        // check if the message is valid
        String messageText = message.getMessageText();
        boolean isMessageValid = messageChecker(messageText);
        // get user's id
        Integer posted_by = message.getPostedBy();
        // if user exists in account table, user won't be null
        Account user = accountRepository.findById(posted_by).orElse(null);

        // messageText is valid and user exist in Account table
        if (isMessageValid && user != null) {
            Message newMessage = new Message(posted_by, messageText, message.getTimePostedEpoch());
            return messageRepository.save(newMessage);
        }
        return null;
    }

    // 4) Our API should be able to retrieve all message
    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    }

    // 5) Our API should be able to retrieve a message by its id
    public Message getMessageById(Integer message_id) {
        return messageRepository.findById(message_id).orElse(null);
    }

    // 6) Our API should be able to delete a message given a message id
    public Message deleteMessage(Integer message_id) {
        Message output = getMessageById(message_id);

        if (output != null) {
            // deletes the message
            messageRepository.deleteById(message_id);
        }
        return output;
    }

    // 7) Our API should be albe toupdate a message text given a message id
    @Transactional
    public Message patchMessage(Integer message_id, String newText) {
        Message message = getMessageById(message_id);
        // validate message
        boolean isNewTextValid = messageChecker(newText);

        // if the new message is valid and the message with id exists in the table
        if (isNewTextValid && message != null) {
            message.setMessageText(newText);
            return messageRepository.save(message);
        }
        return null;
    }

}
