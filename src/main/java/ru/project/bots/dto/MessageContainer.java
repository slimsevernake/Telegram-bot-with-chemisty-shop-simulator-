package ru.project.bots.dto;

import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.methods.send.SendPhoto;

public class MessageContainer {

    private final SendMessage sendMessage;
    private final SendPhoto sendPhoto;

    private final MessageType type;

    public MessageContainer(SendMessage sendMessage) {
        this.sendMessage = sendMessage;
        type = MessageType.TEXT;
        sendPhoto = null;
    }

    public MessageContainer(SendPhoto sendPhoto) {
        this.sendPhoto = sendPhoto;
        type = MessageType.PHOTO;
        this.sendMessage = null;
    }

    public SendMessage getSendMessage() {
        return sendMessage;
    }

    public SendPhoto getSendPhoto() {
        return sendPhoto;
    }

    public MessageType getType() {
        return type;
    }
}
