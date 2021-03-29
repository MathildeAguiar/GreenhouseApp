package fr.uga.polytech.greenhouse.service.dto;

import fr.uga.polytech.greenhouse.domain.enumeration.Language;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import javax.persistence.Lob;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link fr.uga.polytech.greenhouse.domain.Report} entity.
 */
public class ReportDTO implements Serializable {

    private Long id;

    private String titleR;

    private String alerts;

    @Lob
    private String descript;

    @NotNull
    private Instant createdAt;

    @NotNull
    private Instant modifiedAt;

    private Language langue;

    private ProfileDTO author;

    private GreenHouseDTO house;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitleR() {
        return titleR;
    }

    public void setTitleR(String titleR) {
        this.titleR = titleR;
    }

    public String getAlerts() {
        return alerts;
    }

    public void setAlerts(String alerts) {
        this.alerts = alerts;
    }

    public String getDescript() {
        return descript;
    }

    public void setDescript(String descript) {
        this.descript = descript;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getModifiedAt() {
        return modifiedAt;
    }

    public void setModifiedAt(Instant modifiedAt) {
        this.modifiedAt = modifiedAt;
    }

    public Language getLangue() {
        return langue;
    }

    public void setLangue(Language langue) {
        this.langue = langue;
    }

    public ProfileDTO getAuthor() {
        return author;
    }

    public void setAuthor(ProfileDTO author) {
        this.author = author;
    }

    public GreenHouseDTO getHouse() {
        return house;
    }

    public void setHouse(GreenHouseDTO house) {
        this.house = house;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ReportDTO)) {
            return false;
        }

        ReportDTO reportDTO = (ReportDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, reportDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ReportDTO{" +
            "id=" + getId() +
            ", titleR='" + getTitleR() + "'" +
            ", alerts='" + getAlerts() + "'" +
            ", descript='" + getDescript() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", modifiedAt='" + getModifiedAt() + "'" +
            ", langue='" + getLangue() + "'" +
            ", author=" + getAuthor() +
            ", house=" + getHouse() +
            "}";
    }
}
