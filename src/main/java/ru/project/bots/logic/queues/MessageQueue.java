package ru.project.bots.logic.queues;

import org.telegram.telegrambots.api.objects.Update;
import ru.project.bots.dto.MessageContainer;

import java.util.concurrent.ConcurrentLinkedQueue;

public interface MessageQueue {

    /**
    * Put updates to process it
    * */
    void putUpdate(Update update);

    /**
    * Put messages to execute
    * */
    void putMessage(MessageContainer message);

    /**
    * Method to put message queue to the bot for execute messages
     * */
    ConcurrentLinkedQueue<MessageContainer> getMessageQueue();

}
