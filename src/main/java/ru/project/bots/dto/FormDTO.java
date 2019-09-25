package ru.project.bots.dto;

public class FormDTO {

    private final long formId;
    private final String city;
    private final SubstanceDTO substance;
    private final int weight;
    private final String type;
    private final float price;
    private final long uuid;

    public FormDTO(long formId, String city, long substanceId, String substanceName, int substancePrice, SubstanceType subtype, int weight, String type, long uuid) {
        this.formId = formId;
        this.city = city;
        this.substance = new SubstanceDTO(substanceId ,substanceName, substancePrice, subtype);
        this.weight = weight;
        this.type = type;
        this.price = weight * substancePrice;
        this.uuid = uuid;
    }

    public String getCity() {
        return city;
    }

    public SubstanceDTO getSubstance() {
        return substance;
    }

    public int getWeight() {
        return weight;
    }

    public String getType() {
        return type;
    }

    public float getPrice() {
        return price;
    }

    public long getFormId() {
        return formId;
    }

    public long getUuid() {
        return uuid;
    }
}
