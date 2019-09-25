package ru.project.bots.model.dao;

import ru.project.bots.dto.DialogLevel;
import ru.project.bots.dto.FormDTO;
import ru.project.bots.dto.SmallFormDTO;

import java.util.List;

public interface UserDialogDAO {

    DialogLevel getDialogLevel(long chatId);

    FormDTO getForm(long chatId);

    boolean startDialog(long chatId);

    boolean endDialog(long chatId);

    boolean completeForm(long chatId);

    List<FormDTO> getOrders(long chatId);

    boolean updateDialogLevel(long chatId, DialogLevel level);

    boolean updateWeight(long chatId, int value);

    boolean updateSubstance(long chatId, long substanceId);

    boolean updateCity(long chatId, String name);

    String getCityName(long chatId);

    boolean existCity(String name);

    List<SmallFormDTO> existActiveForm(long chatId);

    long getUuid(long chatId);

}
