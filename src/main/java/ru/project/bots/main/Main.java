package ru.project.bots.main;

import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import ru.project.bots.logic.BotContextInitializer;

public class Main {

    public static void main(String[] args) {

        ApiContextInitializer.init();
        TelegramBotsApi botsApi = new TelegramBotsApi();

        BotContextInitializer.init();

        try {

            botsApi.registerBot(BotContextInitializer.getBot());

        }catch (Exception e){
            BotContextInitializer.getLogger().log(e);
        }

    }

}
