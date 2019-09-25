package ru.project.bots.model.dao;

import ru.project.bots.dto.SubstanceDTO;
import ru.project.bots.dto.SubstanceType;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface SubstancesDAO {

    List<SubstanceDTO> getSubstancesByIDs(Set<Long> ids);

    long getSubstancesCount();

    long getIdByName(String substance);

    List<SubstanceDTO> getAllSubstances();

    List<Long> getAllSubstanceIDs();

    void removeSubstances(Set<Long> ids);

    SubstanceType getSubstanceFormByChatId(long chatId);

    void updateSubstances(Collection<SubstanceDTO> substances);
}
