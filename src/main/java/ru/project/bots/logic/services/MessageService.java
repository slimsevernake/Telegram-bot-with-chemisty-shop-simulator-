package ru.project.bots.logic.services;

import org.telegram.telegrambots.api.objects.Update;
import ru.project.bots.dto.MessageContainer;

public interface MessageService {

    MessageContainer getResponse(Update update);

}
