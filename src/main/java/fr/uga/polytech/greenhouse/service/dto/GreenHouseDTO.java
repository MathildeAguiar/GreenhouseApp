package fr.uga.polytech.greenhouse.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link fr.uga.polytech.greenhouse.domain.GreenHouse} entity.
 */
public class GreenHouseDTO implements Serializable {

    private Long id;

    @NotNull
    private String nameG;

    private Float latitude;

    private Float longitude;

    private ProfileDTO observateur;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNameG() {
        return nameG;
    }

    public void setNameG(String nameG) {
        this.nameG = nameG;
    }

    public Float getLatitude() {
        return latitude;
    }

    public void setLatitude(Float latitude) {
        this.latitude = latitude;
    }

    public Float getLongitude() {
        return longitude;
    }

    public void setLongitude(Float longitude) {
        this.longitude = longitude;
    }

    public ProfileDTO getObservateur() {
        return observateur;
    }

    public void setObservateur(ProfileDTO observateur) {
        this.observateur = observateur;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof GreenHouseDTO)) {
            return false;
        }

        GreenHouseDTO greenHouseDTO = (GreenHouseDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, greenHouseDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "GreenHouseDTO{" +
            "id=" + getId() +
            ", nameG='" + getNameG() + "'" +
            ", latitude=" + getLatitude() +
            ", longitude=" + getLongitude() +
            ", observateur=" + getObservateur() +
            "}";
    }
}
