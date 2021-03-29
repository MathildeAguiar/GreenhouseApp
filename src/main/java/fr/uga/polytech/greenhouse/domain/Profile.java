package fr.uga.polytech.greenhouse.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import fr.uga.polytech.greenhouse.domain.enumeration.Category;
import fr.uga.polytech.greenhouse.domain.enumeration.Filiere;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Profile.
 */
@Entity
@Table(name = "profile")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Profile implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Category status;

    @Enumerated(EnumType.STRING)
    @Column(name = "specialite")
    private Filiere specialite;

    @Size(min = 3)
    @Pattern(regexp = "^[A-Z][a-z]+\\d$")
    @Column(name = "address")
    private String address;

    @Size(max = 7)
    @Column(name = "code_p", length = 7)
    private String codeP;

    @Size(min = 10)
    @Column(name = "ville")
    private String ville;

    @Size(min = 7)
    @Column(name = "phone_number")
    private String phoneNumber;

    @Pattern(regexp = "^([a-zA-Z0-9_\\-\\.]+)@(imag|etu.univ-grenoble-alpes|univ-grenoble-alpes+)\\.fr$")
    @Column(name = "email")
    private String email;

    @Lob
    @Column(name = "indicatif")
    private byte[] indicatif;

    @Column(name = "indicatif_content_type")
    private String indicatifContentType;

    @OneToOne
    @JoinColumn(unique = true)
    private User user;

    @OneToMany(mappedBy = "responsible")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "responsible", "rapport" }, allowSetters = true)
    private Set<Task> works = new HashSet<>();

    @OneToMany(mappedBy = "author")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "tasks", "author", "house" }, allowSetters = true)
    private Set<Report> documents = new HashSet<>();

    @OneToMany(mappedBy = "observateur")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "reports", "observateur" }, allowSetters = true)
    private Set<GreenHouse> houses = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Profile id(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return this.name;
    }

    public Profile name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Category getStatus() {
        return this.status;
    }

    public Profile status(Category status) {
        this.status = status;
        return this;
    }

    public void setStatus(Category status) {
        this.status = status;
    }

    public Filiere getSpecialite() {
        return this.specialite;
    }

    public Profile specialite(Filiere specialite) {
        this.specialite = specialite;
        return this;
    }

    public void setSpecialite(Filiere specialite) {
        this.specialite = specialite;
    }

    public String getAddress() {
        return this.address;
    }

    public Profile address(String address) {
        this.address = address;
        return this;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCodeP() {
        return this.codeP;
    }

    public Profile codeP(String codeP) {
        this.codeP = codeP;
        return this;
    }

    public void setCodeP(String codeP) {
        this.codeP = codeP;
    }

    public String getVille() {
        return this.ville;
    }

    public Profile ville(String ville) {
        this.ville = ville;
        return this;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    public Profile phoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return this.email;
    }

    public Profile email(String email) {
        this.email = email;
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public byte[] getIndicatif() {
        return this.indicatif;
    }

    public Profile indicatif(byte[] indicatif) {
        this.indicatif = indicatif;
        return this;
    }

    public void setIndicatif(byte[] indicatif) {
        this.indicatif = indicatif;
    }

    public String getIndicatifContentType() {
        return this.indicatifContentType;
    }

    public Profile indicatifContentType(String indicatifContentType) {
        this.indicatifContentType = indicatifContentType;
        return this;
    }

    public void setIndicatifContentType(String indicatifContentType) {
        this.indicatifContentType = indicatifContentType;
    }

    public User getUser() {
        return this.user;
    }

    public Profile user(User user) {
        this.setUser(user);
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<Task> getWorks() {
        return this.works;
    }

    public Profile works(Set<Task> tasks) {
        this.setWorks(tasks);
        return this;
    }

    public Profile addWork(Task task) {
        this.works.add(task);
        task.setResponsible(this);
        return this;
    }

    public Profile removeWork(Task task) {
        this.works.remove(task);
        task.setResponsible(null);
        return this;
    }

    public void setWorks(Set<Task> tasks) {
        if (this.works != null) {
            this.works.forEach(i -> i.setResponsible(null));
        }
        if (tasks != null) {
            tasks.forEach(i -> i.setResponsible(this));
        }
        this.works = tasks;
    }

    public Set<Report> getDocuments() {
        return this.documents;
    }

    public Profile documents(Set<Report> reports) {
        this.setDocuments(reports);
        return this;
    }

    public Profile addDocument(Report report) {
        this.documents.add(report);
        report.setAuthor(this);
        return this;
    }

    public Profile removeDocument(Report report) {
        this.documents.remove(report);
        report.setAuthor(null);
        return this;
    }

    public void setDocuments(Set<Report> reports) {
        if (this.documents != null) {
            this.documents.forEach(i -> i.setAuthor(null));
        }
        if (reports != null) {
            reports.forEach(i -> i.setAuthor(this));
        }
        this.documents = reports;
    }

    public Set<GreenHouse> getHouses() {
        return this.houses;
    }

    public Profile houses(Set<GreenHouse> greenHouses) {
        this.setHouses(greenHouses);
        return this;
    }

    public Profile addHouse(GreenHouse greenHouse) {
        this.houses.add(greenHouse);
        greenHouse.setObservateur(this);
        return this;
    }

    public Profile removeHouse(GreenHouse greenHouse) {
        this.houses.remove(greenHouse);
        greenHouse.setObservateur(null);
        return this;
    }

    public void setHouses(Set<GreenHouse> greenHouses) {
        if (this.houses != null) {
            this.houses.forEach(i -> i.setObservateur(null));
        }
        if (greenHouses != null) {
            greenHouses.forEach(i -> i.setObservateur(this));
        }
        this.houses = greenHouses;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Profile)) {
            return false;
        }
        return id != null && id.equals(((Profile) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Profile{" +
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
            ", indicatifContentType='" + getIndicatifContentType() + "'" +
            "}";
    }
}
