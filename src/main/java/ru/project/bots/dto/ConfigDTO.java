package ru.project.bots.dto;

import java.util.List;

public class ConfigDTO {

    private String appName;
    private int messageProcessorThreads;
    private String botName;
    private String botToken;
    private int priceListsNum;
    private List<Long> requiredSubstances;
    private String qiwiWallet;
    private String bitcoin;
    private String logPath;
    private String substancesFile;
    private String bankCard;
    private String mailUsername;
    private String mailPassword;
    private String mailTo;

    public ConfigDTO() {
    }

    public int getMessageProcessorThreads() {
        return messageProcessorThreads;
    }

    public void setMessageProcessorThreads(int messageProcessorThreads) {
        this.messageProcessorThreads = messageProcessorThreads;
    }

    public String getBotName() {
        return botName;
    }

    public void setBotName(String botName) {
        this.botName = botName;
    }

    public String getBotToken() {
        return botToken;
    }

    public void setBotToken(String botToken) {
        this.botToken = botToken;
    }

    public int getPriceListsNum() {
        return priceListsNum;
    }

    public void setPriceListsNum(int priceListsNum) {
        this.priceListsNum = priceListsNum;
    }

    public List<Long> getRequiredSubstances() {
        return requiredSubstances;
    }

    public void setRequiredSubstances(List<Long> requiredSubstances) {
        this.requiredSubstances = requiredSubstances;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getQiwiWallet() {
        return qiwiWallet;
    }

    public void setQiwiWallet(String qiwiWallet) {
        this.qiwiWallet = qiwiWallet;
    }

    public String getLogPath() {
        return logPath;
    }

    public void setLogPath(String logPath) {
        this.logPath = logPath;
    }

    public String getBankCard() {
        return bankCard;
    }

    public void setBankCard(String bankCard) {
        this.bankCard = bankCard;
    }

    public String getBitcoin() {
        return bitcoin;
    }

    public void setBitcoin(String bitcoin) {
        this.bitcoin = bitcoin;
    }

    public String getSubstancesFile() {
        return substancesFile;
    }

    public void setSubstancesFile(String substancesFile) {
        this.substancesFile = substancesFile;
    }

    public String getMailUsername() {
        return mailUsername;
    }

    public void setMailUsername(String mailUsername) {
        this.mailUsername = mailUsername;
    }

    public String getMailPassword() {
        return mailPassword;
    }

    public void setMailPassword(String mailPassword) {
        this.mailPassword = mailPassword;
    }

    public String getMailTo() {
        return mailTo;
    }

    public void setMailTo(String mailTo) {
        this.mailTo = mailTo;
    }
}
