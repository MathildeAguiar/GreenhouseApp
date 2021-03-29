package fr.uga.polytech.greenhouse.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A GreenHouse.
 */
@Entity
@Table(name = "green_house")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class GreenHouse implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "name_g", nullable = false)
    private String nameG;

    @Column(name = "latitude")
    private Float latitude;

    @Column(name = "longitude")
    private Float longitude;

    @OneToMany(mappedBy = "house")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "tasks", "author", "house" }, allowSetters = true)
    private Set<Report> reports = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "user", "works", "documents", "houses" }, allowSetters = true)
    private Profile observateur;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public GreenHouse id(Long id) {
        this.id = id;
        return this;
    }

    public String getNameG() {
        return this.nameG;
    }

    public GreenHouse nameG(String nameG) {
        this.nameG = nameG;
        return this;
    }

    public void setNameG(String nameG) {
        this.nameG = nameG;
    }

    public Float getLatitude() {
        return this.latitude;
    }

    public GreenHouse latitude(Float latitude) {
        this.latitude = latitude;
        return this;
    }

    public void setLatitude(Float latitude) {
        this.latitude = latitude;
    }

    public Float getLongitude() {
        return this.longitude;
    }

    public GreenHouse longitude(Float longitude) {
        this.longitude = longitude;
        return this;
    }

    public void setLongitude(Float longitude) {
        this.longitude = longitude;
    }

    public Set<Report> getReports() {
        return this.reports;
    }

    public GreenHouse reports(Set<Report> reports) {
        this.setReports(reports);
        return this;
    }

    public GreenHouse addReport(Report report) {
        this.reports.add(report);
        report.setHouse(this);
        return this;
    }

    public GreenHouse removeReport(Report report) {
        this.reports.remove(report);
        report.setHouse(null);
        return this;
    }

    public void setReports(Set<Report> reports) {
        if (this.reports != null) {
            this.reports.forEach(i -> i.setHouse(null));
        }
        if (reports != null) {
            reports.forEach(i -> i.setHouse(this));
        }
        this.reports = reports;
    }

    public Profile getObservateur() {
        return this.observateur;
    }

    public GreenHouse observateur(Profile profile) {
        this.setObservateur(profile);
        return this;
    }

    public void setObservateur(Profile profile) {
        this.observateur = profile;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof GreenHouse)) {
            return false;
        }
        return id != null && id.equals(((GreenHouse) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "GreenHouse{" +
            "id=" + getId() +
            ", nameG='" + getNameG() + "'" +
            ", latitude=" + getLatitude() +
            ", longitude=" + getLongitude() +
            "}";
    }
}
