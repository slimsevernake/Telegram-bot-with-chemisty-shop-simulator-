package ru.project.bots.model.configs;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import ru.project.bots.dto.ConfigDTO;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JSONConfigFile implements ConfigFile{

    @Override
    public ConfigDTO getConfig() {
        return loadJSON();
    }

    private ConfigDTO loadJSON(){

        ConfigDTO configDTO = new ConfigDTO();
        try {

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode node = objectMapper.readTree(JSONConfigFile.class.getClassLoader().getResource("config.json"));

            configDTO.setAppName(node.get("App").asText());
            /*
            * Parse init properties
            * */
            node = node.get("properties");

            configDTO.setMessageProcessorThreads(node.get("processorThreads").asInt());
            configDTO.setBotName(node.get("botName").asText());
            configDTO.setBotToken(node.get("botToken").asText());
            configDTO.setPriceListsNum(node.get("priceListsNum").asInt());
            configDTO.setRequiredSubstances(getLongArray("requiredSubstances", node));
            configDTO.setQiwiWallet(node.get("qiwi").asText());
            configDTO.setBitcoin(node.get("bitcoin").asText());
            configDTO.setBankCard(node.get("card").asText());
            configDTO.setSubstancesFile(node.get("substancesFile").asText());
            configDTO.setLogPath(node.get("logfile").asText());

            configDTO.setMailTo(node.get("mailTo").asText());
            configDTO.setMailUsername(node.get("mailUsername").asText());
            configDTO.setMailPassword(node.get("mailPassword").asText());

        } catch (IOException e) {
            System.out.println("Cannot load config file: "+e.getMessage());
            System.exit(1);
        }

        return configDTO;
    }

    private List<Long> getLongArray(String name, JsonNode node){

        List<Long> list = new ArrayList<>();
        node.get(name).elements().forEachRemaining( e -> list.add(e.asLong()));

        return list;
    }
}
