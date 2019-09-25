package ru.project.bots.logic.daemons;

import org.telegram.telegrambots.api.objects.Update;
import ru.project.bots.dto.MessageContainer;
import ru.project.bots.logic.queues.MessageQueue;
import ru.project.bots.logic.services.MessageService;
import ru.project.bots.logic.services.MessageServiceImpl;
import ru.project.bots.model.logs.SimpleLogger;

import java.util.concurrent.ConcurrentLinkedQueue;

public class MessageProcessor implements Runnable{

    private static MessageQueue messageQueue;
    private static ConcurrentLinkedQueue<Update> updates;
    private boolean active;

    private static SimpleLogger logger;

    private final MessageService service;

    public MessageProcessor() {
        this.service = new MessageServiceImpl();
        active = true;
    }

    @Override
    public void run() {

        while (active){

            try {

                Update update;
                synchronized (updates){
                    if(updates.isEmpty())
                        updates.wait();
                    update = updates.poll();
                }

                if(update == null)
                    continue;

                final MessageContainer message = service.getResponse(update);
                messageQueue.putMessage(message);

            }catch (Exception e){
                logger.log(e);
            }

        }

    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public static void setMessageQueue(MessageQueue messageQueue) {
        MessageProcessor.messageQueue = messageQueue;
    }

    public static void setUpdates(ConcurrentLinkedQueue<Update> updates) {
        MessageProcessor.updates = updates;
    }

    public static void setLogger(SimpleLogger _logger){
        logger = _logger;
    }
}
