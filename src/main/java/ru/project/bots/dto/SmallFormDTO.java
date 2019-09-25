package ru.project.bots.dto;

public class SmallFormDTO {

    private long uuid;
    private boolean active;

    public SmallFormDTO(long uuid, boolean active) {
        this.uuid = uuid;
        this.active = active;
    }

    public long getUuid() {
        return uuid;
    }

    public void setUuid(long uuid) {
        this.uuid = uuid;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
