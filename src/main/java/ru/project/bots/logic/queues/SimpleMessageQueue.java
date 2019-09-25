package ru.project.bots.logic.queues;

import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import ru.project.bots.dto.MessageContainer;
import ru.project.bots.logic.daemons.MessageProcessor;
import ru.project.bots.dto.ConfigDTO;

import java.util.concurrent.ConcurrentLinkedQueue;

public class SimpleMessageQueue implements MessageQueue {

    /*
    * Concurrent queue to keep updates until it process
    * */
    private final ConcurrentLinkedQueue<Update> updates = new ConcurrentLinkedQueue<>();

    /*
    * Concurrent queue to complete messages before it send
    * */
    private final ConcurrentLinkedQueue<MessageContainer> messages = new ConcurrentLinkedQueue<>();

    public SimpleMessageQueue(ConfigDTO configDTO) {
        init(configDTO);
    }

    private void init(ConfigDTO config){

        MessageProcessor.setUpdates(updates);
        MessageProcessor.setMessageQueue(this);
        for (int i = 0; i < config.getMessageProcessorThreads(); i++) {
            new Thread(new MessageProcessor()).start();
        }

    }


    @Override
    public void putUpdate(Update update) {

        /*
        * Put new update to queue and notufi one thread
        * */
        synchronized (updates){
            updates.add(update);
            updates.notify();
        }

    }

    @Override
    public void putMessage(MessageContainer message) {

        /*
        * Put ready message to executor and notify 1 thread
        * */
        synchronized (messages){
            messages.add(message);
            messages.notify();
        }

    }

    @Override
    public ConcurrentLinkedQueue<MessageContainer> getMessageQueue(){
        return this.messages;
    }

}
