package ru.project.bots.logic.services.functions;

import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import ru.project.bots.dto.MessageContainer;
import ru.project.bots.model.dao.UserDialogDAO;
import ru.project.bots.view.MessageView;

@FunctionalInterface
public interface Executable {

    /**
    * Functional interface to process different dialog levels
    * */
    MessageContainer execute(Update update, MessageView view, UserDialogDAO userDialogDAO) throws Exception;

}
