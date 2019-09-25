package ru.project.bots.model.entities;

import ru.project.bots.dto.SubstanceType;

import javax.persistence.*;

@Entity
@Table(name = "substances", schema = "telegramDB")
@org.hibernate.annotations.NamedQueries({
        @org.hibernate.annotations.NamedQuery(name = "getSubstancesByIDs",
                query = "select new ru.project.bots.dto.SubstanceDTO(s.substanceId, s.title, s.price, s.type) from SubstanceEntity s " +
                        "where s.substanceId in :ids"),
        @org.hibernate.annotations.NamedQuery(name = "removeSubstances",
                query = "delete from SubstanceEntity where substanceId in (:ids)"),
        @org.hibernate.annotations.NamedQuery(name = "getSubstanceIdByName",
                query = "select substanceId from SubstanceEntity where lower(title) like lower(:title)"),
        @org.hibernate.annotations.NamedQuery(name = "countSubstances",
                query = "select distinct count(substanceId) from SubstanceEntity"),
        @org.hibernate.annotations.NamedQuery(name = "getAllSubstances",
                query = "select new ru.project.bots.dto.SubstanceDTO(s.substanceId, s.title, s.price, s.type) from SubstanceEntity s"),
        @org.hibernate.annotations.NamedQuery(name = "getAllSubstanceIDs",
                query = "select substanceId from SubstanceEntity"),
        @org.hibernate.annotations.NamedQuery(name = "getSubstanceFormByChat",
                query = " select se.type from DialogsEntity de " +
                        " left join de.form fe left join fe.substance se " +
                        " where de.chatId = :id")
})
public class SubstanceEntity {

    public static final String GET_SUBSTANCES_BY_IDS = "getSubstancesByIDs";
    public static final String GET_ALL_SUBSTANCES = "getAllSubstances";
    public static final String GET_ALL_SUBSTANCE_IDS = "getAllSubstanceIDs";
    public static final String GET_SUBSTANCE_ID_BY_NAME = "getSubstanceIdByName";
    public static final String GET_SUBSTANCE_FORM_BY_CHAT = "getSubstanceFormByChat";
    public static final String COUNT_SUBSTANCES = "countSubstances";
    public static final String REMOVE_SUBSTANCES = "removeSubstances";

    @Id
    @Column(name = "substance_id", nullable = false, unique = true)
    private long substanceId;

    @Basic
    @Column(name = "title", nullable = false, unique = true)
    private String title;

    @Basic
    @Column(name = "price", nullable = false)
    private int price;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "type")
    private SubstanceType type;


    public SubstanceEntity() {
    }

    public SubstanceEntity(long substanceId, String title, int price, SubstanceType type) {
        this.substanceId = substanceId;
        this.title = title;
        this.price = price;
        this.type = type;
    }

    public SubstanceEntity(String title, int price, SubstanceType type) {
        this.title = title;
        this.price = price;
        this.type = type;
    }

    public long getSubstanceId() {
        return substanceId;
    }

    public void setSubstanceId(long substanseId) {
        this.substanceId = substanseId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public SubstanceType getType() {
        return type;
    }

    public void setType(SubstanceType type) {
        this.type = type;
    }

}
