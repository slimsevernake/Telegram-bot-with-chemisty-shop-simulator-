package ru.project.bots.model.configs;

import ru.project.bots.dto.SubstanceDTO;

import java.util.Map;

public interface SubstancesConfig {

    Map<Long, SubstanceDTO> uploadSubstances();

}
