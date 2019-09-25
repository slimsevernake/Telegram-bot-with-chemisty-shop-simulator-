package ru.project.bots.view;

import ru.project.bots.dto.FormDTO;
import ru.project.bots.dto.MessageContainer;
import ru.project.bots.dto.SubstanceDTO;
import ru.project.bots.dto.SubstanceType;

import java.util.List;

public interface MessageView {

    MessageContainer getEmptyMessage(long chatId);

    MessageContainer getAcceptedDocumentMessage(long chatId);

    MessageContainer getUnknownCommandMessage(long chatId);

    MessageContainer getUnknownSubstanceMessage(long chatId);

    MessageContainer getToLongMessage(long chatId);

    MessageContainer getDefaultErrorMessage(long chatId);

    MessageContainer getHelpMessage(long chatId);

    MessageContainer getWelcomeMessage(long chatId);

    MessageContainer getEmptyOrderListMessage(long chatId);

    MessageContainer getOrderListMessage(List<FormDTO> orders, long chatId);

    MessageContainer getCitySelectionMessage(long chatId);

    MessageContainer getNotExistCityMessage(long chatId);

    MessageContainer getPriceListMessage(long chatId, List<SubstanceDTO> substances);

    MessageContainer getWeightSelectionMessage(long chatId, SubstanceType substanceForm);

    MessageContainer getBigWeightMessage(long chatId);

    MessageContainer getBadWeightMessage(long chatId);

    MessageContainer getValidMessage(long chatId, FormDTO formDTO);

    MessageContainer getPaymentMessage(long chatId, FormDTO form);

    MessageContainer getEndDialogMessage(long chatId, long uuid);
}
