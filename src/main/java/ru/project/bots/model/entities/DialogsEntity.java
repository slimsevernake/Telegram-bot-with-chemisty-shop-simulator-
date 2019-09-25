package ru.project.bots.model.entities;

import ru.project.bots.dto.DialogLevel;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "dialogs", schema = "telegramDB")
@org.hibernate.annotations.NamedQueries({
        @org.hibernate.annotations.NamedQuery(name = "getDialogStatus",
                query = "select status from DialogsEntity where chatId = :id"),
        @org.hibernate.annotations.NamedQuery(name = "updateDialogStatus",
                query = "update DialogsEntity set status = :status where chatId = :id"),
        @org.hibernate.annotations.NamedQuery(name = "existDialog",
                query = "select chatId from DialogsEntity where chatId = :id"),
        @org.hibernate.annotations.NamedQuery(name = "countDialogs",
                query = "select count(chatId) from DialogsEntity")
})
public class DialogsEntity {

    public static final String GET_DIALOG_STATUS = "getDialogStatus";
    public static final String UPDATE_DIALOG_STATUS = "updateDialogStatus";
    public static final String COUNT_DIALOGS = "countDialogs";

    private long chatId;
    private DialogLevel status;
    private List<FormEntity> forms;

    public DialogsEntity() {
    }

    public DialogsEntity(long chatId, DialogLevel status) {
        this.chatId = chatId;
        this.status = status;
    }

    @Id
    @Column(name = "chat_id", unique = true, nullable = false)
    public long getChatId() {
        return chatId;
    }

    public void setChatId(long chatId) {
        this.chatId = chatId;
    }

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.ORDINAL)
    public DialogLevel getStatus() {
        return status;
    }

    public void setStatus(DialogLevel status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DialogsEntity that = (DialogsEntity) o;
        return chatId == that.chatId;
    }

    @Override
    public int hashCode() {

        return Objects.hash(chatId, status);
    }


    @OneToMany(mappedBy = "dialog",orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    public List<FormEntity> getForm() {
        return forms;
    }

    public void setForm(List<FormEntity> form) {
        this.forms = form;
    }
}
