package ru.project.bots.bot;

import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import ru.project.bots.dto.MessageContainer;
import ru.project.bots.logic.queues.MessageQueue;
import ru.project.bots.model.logs.SimpleLogger;

import java.util.concurrent.ConcurrentLinkedQueue;

public class SimpleBot extends TelegramLongPollingBot implements Runnable{

    private final String botName;
    private final String botToken;

    private final MessageQueue queue;
    private final ConcurrentLinkedQueue<MessageContainer> messages;
    private boolean active = false;

    private final SimpleLogger logger;

    public SimpleBot(MessageQueue queue, ConcurrentLinkedQueue<MessageContainer> messages,
                     String botName, String botToken, SimpleLogger logger) {
        super();        

        this.queue = queue;
        this.messages = messages;
        this.botName = botName;
        this.botToken = botToken;
        this.logger = logger;

        init();
    }

    @Override
    public void onUpdateReceived(Update update) {

        queue.putUpdate(update);

    }

    @Override
    public String getBotUsername() {
        return this.botName;
    }

    @Override
    public String getBotToken() {
        return this.botToken;
    }

    private void init(){

        Thread thread = new Thread(this);
        thread.setPriority(1);
        this.active = true;
        thread.start();
    }

    @Override
    public void run() {

        while (active){
            try {

                MessageContainer message;
                synchronized (messages){
                    if(messages.isEmpty())
                        messages.wait();
                    message = messages.poll();
                }
                if(message == null)
                    continue;

                switch (message.getType()){
                    case TEXT:
                        execute(message.getSendMessage());
                        break;
                    case PHOTO:
                        sendPhoto(message.getSendPhoto());
                        break;
                }

            }catch (Exception e){

                logger.log(e);

                Thread thread = new Thread(this);
                thread.setPriority(1);
                this.active = true;
                thread.start();

                Thread.currentThread().interrupt();
            }

        }

    }

}
