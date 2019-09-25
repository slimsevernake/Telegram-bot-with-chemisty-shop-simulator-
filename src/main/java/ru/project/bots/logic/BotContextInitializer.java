package ru.project.bots.logic;

import ru.project.bots.logic.daemons.MessageProcessor;
import ru.project.bots.logic.queues.MessageQueue;
import ru.project.bots.logic.queues.SimpleMessageQueue;
import ru.project.bots.logic.services.MailService;
import ru.project.bots.logic.services.MessageServiceImpl;
import ru.project.bots.logic.services.PriceListService;
import ru.project.bots.logic.services.SubstanceUpdaterService;
import ru.project.bots.model.configs.ConfigFile;
import ru.project.bots.model.configs.JSONConfigFile;
import ru.project.bots.model.logs.FileSimpleLogger;
import ru.project.bots.model.logs.SimpleLogger;
import ru.project.bots.bot.SimpleBot;
import ru.project.bots.dto.ConfigDTO;
import ru.project.bots.view.MessageViewImpl;

public class BotContextInitializer {

    private static SimpleBot bot;
    private static PriceListService priceListService;
    private static SimpleLogger logger;

    public static void init(){

        /*
         * Load configs with constants
         * */
        ConfigFile configFile = new JSONConfigFile();
        ConfigDTO config = configFile.getConfig();

        /*
        * logs
        * */
        logger = new FileSimpleLogger(config.getLogPath());
        MessageServiceImpl.setLogger(logger);
        MessageProcessor.setLogger(logger);
        /*
        * Initialize Singletons
        * */
        priceListService = new PriceListService(config.getPriceListsNum(), config.getRequiredSubstances(),
                logger);
        new SubstanceUpdaterService(logger, config.getSubstancesFile());
        new MailService(config.getMailTo(), config.getMailUsername(), config.getMailPassword(), logger);
        /*
         * Load bot to check updates and query to process it
         * */
        MessageQueue queue = new SimpleMessageQueue(config);
        bot = new SimpleBot(queue, queue.getMessageQueue(),
                config.getBotName(), config.getBotToken(), logger);

        /*
        * Set view properties
        * */
        MessageViewImpl.setQiwiWallet(config.getQiwiWallet());
        MessageViewImpl.setBitcoin(config.getBitcoin());
        MessageViewImpl.setShopName(config.getAppName());
        MessageViewImpl.setBankCard(config.getBankCard());

    }

    public static SimpleBot getBot() {
        return bot;
    }

    public static PriceListService getPriceListService() { return priceListService; }

    public static SimpleLogger getLogger(){
        return logger;
    }
}
