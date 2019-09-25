package ru.project.bots.dto;

public class SubstanceDTO {

    private final long id;
    private final String name;
    private final int price;
    private final SubstanceType type;

    public SubstanceDTO(long id, String name, int price, SubstanceType type) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public long getId() {
        return id;
    }

    public SubstanceType getType() {
        return type;
    }
}
