package fr.uga.polytech.greenhouse.service.dto;

import fr.uga.polytech.greenhouse.domain.enumeration.Category;
import fr.uga.polytech.greenhouse.domain.enumeration.Filiere;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Lob;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link fr.uga.polytech.greenhouse.domain.Profile} entity.
 */
public class ProfileDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    @NotNull
    private Category status;

    private Filiere specialite;

    @Size(min = 3)
    @Pattern(regexp = "^[A-Z][a-z]+\\d$")
    private String address;

    @Size(max = 7)
    private String codeP;

    @Size(min = 10)
    private String ville;

    @Size(min = 7)
    private String phoneNumber;

    @Pattern(regexp = "^([a-zA-Z0-9_\\-\\.]+)@(imag|etu.univ-grenoble-alpes|univ-grenoble-alpes+)\\.fr$")
    private String email;

    @Lob
    private byte[] indicatif;

    private String indicatifContentType;
    private UserDTO user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Category getStatus() {
        return status;
    }

    public void setStatus(Category status) {
        this.status = status;
    }

    public Filiere getSpecialite() {
        return specialite;
    }

    public void setSpecialite(Filiere specialite) {
        this.specialite = specialite;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCodeP() {
        return codeP;
    }

    public void setCodeP(String codeP) {
        this.codeP = codeP;
    }

    public String getVille() {
        return ville;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public byte[] getIndicatif() {
        return indicatif;
    }

    public void setIndicatif(byte[] indicatif) {
        this.indicatif = indicatif;
    }

    public String getIndicatifContentType() {
        return indicatifContentType;
    }

    public void setIndicatifContentType(String indicatifContentType) {
        this.indicatifContentType = indicatifContentType;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProfileDTO)) {
            return false;
        }

        ProfileDTO profileDTO = (ProfileDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, profileDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProfileDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", status='" + getStatus() + "'" +
            ", specialite='" + getSpecialite() + "'" +
            ", address='" + getAddress() + "'" +
            ", codeP='" + getCodeP() + "'" +
            ", ville='" + getVille() + "'" +
            ", phoneNumber='" + getPhoneNumber() + "'" +
            ", email='" + getEmail() + "'" +
            ", indicatif='" + getIndicatif() + "'" +
            ", user=" + getUser() +
            "}";
    }
}
