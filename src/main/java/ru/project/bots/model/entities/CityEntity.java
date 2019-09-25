package ru.project.bots.model.entities;

import javax.persistence.*;

@Entity
@Table(name = "cities", schema = "telegramDB")
@org.hibernate.annotations.NamedQueries({
        @org.hibernate.annotations.NamedQuery(name = "getCityByName",
                query = "select c from CityEntity c where lower(c.title) like lower(:title)")
})
public class CityEntity {

    public static final String GET_CITY_BY_NAME = "getCityByName";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "city_id", nullable = false, unique = true)
    private long cityId;

    @Basic
    @Column(name = "title", nullable = false, unique = true)
    private String title;

    public CityEntity() {
    }

    public CityEntity(String title) {
        this.title = title;
    }

    public long getCityId() {
        return cityId;
    }

    public void setCityId(long cityId) {
        this.cityId = cityId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
