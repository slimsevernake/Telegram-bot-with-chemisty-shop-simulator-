package ru.project.bots.logic.services;

import org.telegram.telegrambots.api.objects.Update;
import ru.project.bots.dto.DialogLevel;
import ru.project.bots.dto.MessageContainer;
import ru.project.bots.logic.services.functions.DefaultDialogFunctions;
import ru.project.bots.logic.services.functions.Executable;
import ru.project.bots.model.dao.UserDialogDAO;
import ru.project.bots.model.hibernate.GenericDAO;
import ru.project.bots.model.hibernate.HibernateUserDialogDAO;
import ru.project.bots.model.logs.SimpleLogger;
import ru.project.bots.view.MessageView;
import ru.project.bots.view.MessageViewImpl;

import java.util.concurrent.ConcurrentHashMap;

public class MessageServiceImpl implements MessageService {

    private final MessageView view;
    private final UserDialogDAO dialogDAO;
    private final static ConcurrentHashMap<DialogLevel, Executable>
            functions = new ConcurrentHashMap<>();

    private static SimpleLogger logger;

    static {

        /*
        * Add functions to process any dialog lvl
        * */
        functions.put(DialogLevel.DIALOG_START, DefaultDialogFunctions.onDialogStart());
        functions.put(DialogLevel.DIALOG_CITY_SELECTION, DefaultDialogFunctions.onCitySelection());
        functions.put(DialogLevel.DIALOG_PRICE_LIST, DefaultDialogFunctions.onPriceList());
        functions.put(DialogLevel.DIALOG_WEIGHT_SELECTION, DefaultDialogFunctions.onWeightSelection());
        functions.put(DialogLevel.DIALOG_VALID, DefaultDialogFunctions.onValid());
        functions.put(DialogLevel.DIALOG_PAYMENT, DefaultDialogFunctions.onPayment());
        functions.put(DialogLevel.DIALOG_END, DefaultDialogFunctions.onEnd());

    }

    public MessageServiceImpl() {
        this.view = new MessageViewImpl();
        this.dialogDAO = new HibernateUserDialogDAO();
    }

    @Override
    public MessageContainer getResponse(Update update) {

        MessageContainer sendMessage;
        try {
            /*
            * Check message on valid
            * */
            if(!update.hasMessage())

                return view.getEmptyMessage(update.getMessage().getChatId());
            else if(update.getMessage().hasPhoto() || update.getMessage().hasDocument()
                    || update.getMessage().hasLocation() || update.getMessage().hasSuccessfulPayment())

                return view.getAcceptedDocumentMessage(update.getMessage().getChatId());
            else if(update.getMessage().getText().trim().equals(""))

                return view.getEmptyMessage(update.getMessage().getChatId());

            String text = update.getMessage().getText().trim();
            if(text.length() > 128)
                return view.getToLongMessage(update.getMessage().getChatId());
            text = text.toLowerCase();

            if(text.equals("помощь"))
                return view.getHelpMessage(update.getMessage().getChatId());

            if(text.charAt(0) == '/'){
                if(text.equals("/start")){

                    GenericDAO dao = (GenericDAO) dialogDAO;
                    dao.beginTx();
                    boolean created = dialogDAO.startDialog(update.getMessage().getChatId());
                    dao.commitTx();

                    if(!created)
                        return view.getDefaultErrorMessage(update.getMessage().getChatId());

                    return view.getWelcomeMessage(update.getMessage().getChatId());

                } else if(text.equals("/help")) {

                    return view.getHelpMessage(update.getMessage().getChatId());
                } else {

                    return view.getUnknownCommandMessage(update.getMessage().getChatId());
                }
            }


            GenericDAO dao = (GenericDAO) dialogDAO;
            dao.beginTx();

            DialogLevel level = dialogDAO.getDialogLevel(update.getMessage().getChatId());
            sendMessage = functions.get(level).execute(update, view, dialogDAO);

            dao.commitTx();

        }catch (Exception e){
            ((GenericDAO) dialogDAO).rollback();
            logger.log(e);
            sendMessage = view.getDefaultErrorMessage(update.getMessage().getChatId());
        }

        return sendMessage;
    }


    public static void setLogger(SimpleLogger logger) {
        MessageServiceImpl.logger = logger;
    }

}
