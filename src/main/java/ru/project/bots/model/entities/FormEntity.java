package ru.project.bots.model.entities;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "forms", schema = "telegramDB")
@org.hibernate.annotations.NamedQueries({
        @org.hibernate.annotations.NamedQuery(name = "updateWeight",
                query = "update FormEntity set weight = :weight where dialog = :dialog and active = true"),
        @org.hibernate.annotations.NamedQuery(name = "updateCity",
                query = "update FormEntity set city = :city where dialog = :dialog and active = true"),
        @org.hibernate.annotations.NamedQuery(name = "updateSubstance",
                query = "update FormEntity set substance = :substance where dialog = :dialog and active = true"),
        @org.hibernate.annotations.NamedQuery(name = "deleteForm",
                query = "delete from FormEntity where dialog = :dialog"),
        @org.hibernate.annotations.NamedQuery(name = "getCityName",
                query = "select city from FormEntity where dialog = :dialog and active = true"),
        @org.hibernate.annotations.NamedQuery(name = "getForm",
                query = "select new ru.project.bots.dto.FormDTO(f.formId, f.city, s.substanceId, s.title, s.price, s.type, f.weight, f.type, f.uuid) " +
                        " from FormEntity f" +
                        " left join f.substance s " +
                        " where f.dialog = :dialog and active = :active"),
        @org.hibernate.annotations.NamedQuery(name = "getLastForm",
                query = "select new ru.project.bots.dto.FormDTO(f.formId, f.city, s.substanceId, s.title, s.price, s.type, f.weight, f.type, f.uuid) " +
                        " from FormEntity f" +
                        " left join f.substance s " +
                        " where f.dialog = :dialog order by f.uuid desc"),
        @org.hibernate.annotations.NamedQuery(name = "getUUID",
                query = "select uuid from FormEntity where dialog = :dialog and active = true"),
        @org.hibernate.annotations.NamedQuery(name = "getActiveForm",
                query = "select new ru.project.bots.dto.SmallFormDTO(uuid, active) from FormEntity where dialog = :dialog order by uuid desc"),
        @org.hibernate.annotations.NamedQuery(name = "completeForm",
                query = "update FormEntity set active = false where dialog = :dialog and active = true"),
        @org.hibernate.annotations.NamedQuery(name = "countOrders",
                query = "select count(distinct formId) from FormEntity formId ")
})
public class FormEntity {

    public static final String UPDATE_WEIGHT = "updateWeight";
    public static final String UPDATE_CITY = "updateCity";
    public static final String UPDATE_SUBSTANCE = "updateSubstance";
    public static final String GET_CITY_NAME= "getCityName";
    public static final String GET_FORM = "getForm";
    public static final String GET_UUID = "getUUID";
    public static final String GET_ACTIVE_FORM = "getActiveForm";
    public static final String COMPLETE_FORM = "completeForm";
    public static final String COUNT_ORDERS = "countOrders";
    public static final String GET_LAST_FORM = "getLastForm";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "form_id", nullable = false, unique = true)
    private long formId;

    @Basic
    @Column(name = "city")
    private String city;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "substance_id", foreignKey = @ForeignKey(name = "form_substance_fk"))
    private SubstanceEntity substance;

    @Basic
    @Column(name = "weight")
    private int weight;

    @Basic
    @Column(name = "type")
    private String type;

    @ManyToOne(optional = false, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "chat_id", nullable = false, foreignKey = @ForeignKey(name = "form_dialog_fk"))
    private DialogsEntity dialog;

    @Column(name = "uuid", nullable = false)
    private long uuid;

    @Column(name = "active", nullable = false)
    @Basic
    private boolean active;

    public long getFormId() {
        return formId;
    }

    public void setFormId(long formId) {
        this.formId = formId;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setSubstance(SubstanceEntity substance) {
        this.substance = substance;
    }

    public SubstanceEntity getSubstance() {
        return substance;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public DialogsEntity getDialog() {
        return dialog;
    }

    public void setDialog(DialogsEntity dialog) {
        this.dialog = dialog;
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
