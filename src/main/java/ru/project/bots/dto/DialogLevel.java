package ru.project.bots.dto;

public enum DialogLevel {

    DIALOG_START("START"),
    DIALOG_CITY_SELECTION("CITY_SELECTION"),
    DIALOG_PRICE_LIST("PRICE_LIST"),
    DIALOG_WEIGHT_SELECTION("WEIGHT_SELECTION"),
    DIALOG_VALID("VALID"),
    DIALOG_PAYMENT("PAYMENT"),
    DIALOG_END("END");

    private final String name;

    DialogLevel(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
