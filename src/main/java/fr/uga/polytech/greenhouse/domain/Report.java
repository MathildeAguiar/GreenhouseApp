package fr.uga.polytech.greenhouse.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import fr.uga.polytech.greenhouse.domain.enumeration.Language;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Report.
 */
@Entity
@Table(name = "report")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Report implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title_r")
    private String titleR;

    @Column(name = "alerts")
    private String alerts;

    @Lob
    @Column(name = "descript")
    private String descript;

    @NotNull
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @NotNull
    @Column(name = "modified_at", nullable = false)
    private Instant modifiedAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "langue")
    private Language langue;

    @OneToMany(mappedBy = "rapport")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "responsible", "rapport" }, allowSetters = true)
    private Set<Task> tasks = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "user", "works", "documents", "houses" }, allowSetters = true)
    private Profile author;

    @ManyToOne
    @JsonIgnoreProperties(value = { "reports", "observateur" }, allowSetters = true)
    private GreenHouse house;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Report id(Long id) {
        this.id = id;
        return this;
    }

    public String getTitleR() {
        return this.titleR;
    }

    public Report titleR(String titleR) {
        this.titleR = titleR;
        return this;
    }

    public void setTitleR(String titleR) {
        this.titleR = titleR;
    }

    public String getAlerts() {
        return this.alerts;
    }

    public Report alerts(String alerts) {
        this.alerts = alerts;
        return this;
    }

    public void setAlerts(String alerts) {
        this.alerts = alerts;
    }

    public String getDescript() {
        return this.descript;
    }

    public Report descript(String descript) {
        this.descript = descript;
        return this;
    }

    public void setDescript(String descript) {
        this.descript = descript;
    }

    public Instant getCreatedAt() {
        return this.createdAt;
    }

    public Report createdAt(Instant createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getModifiedAt() {
        return this.modifiedAt;
    }

    public Report modifiedAt(Instant modifiedAt) {
        this.modifiedAt = modifiedAt;
        return this;
    }

    public void setModifiedAt(Instant modifiedAt) {
        this.modifiedAt = modifiedAt;
    }

    public Language getLangue() {
        return this.langue;
    }

    public Report langue(Language langue) {
        this.langue = langue;
        return this;
    }

    public void setLangue(Language langue) {
        this.langue = langue;
    }

    public Set<Task> getTasks() {
        return this.tasks;
    }

    public Report tasks(Set<Task> tasks) {
        this.setTasks(tasks);
        return this;
    }

    public Report addTask(Task task) {
        this.tasks.add(task);
        task.setRapport(this);
        return this;
    }

    public Report removeTask(Task task) {
        this.tasks.remove(task);
        task.setRapport(null);
        return this;
    }

    public void setTasks(Set<Task> tasks) {
        if (this.tasks != null) {
            this.tasks.forEach(i -> i.setRapport(null));
        }
        if (tasks != null) {
            tasks.forEach(i -> i.setRapport(this));
        }
        this.tasks = tasks;
    }

    public Profile getAuthor() {
        return this.author;
    }

    public Report author(Profile profile) {
        this.setAuthor(profile);
        return this;
    }

    public void setAuthor(Profile profile) {
        this.author = profile;
    }

    public GreenHouse getHouse() {
        return this.house;
    }

    public Report house(GreenHouse greenHouse) {
        this.setHouse(greenHouse);
        return this;
    }

    public void setHouse(GreenHouse greenHouse) {
        this.house = greenHouse;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Report)) {
            return false;
        }
        return id != null && id.equals(((Report) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Report{" +
            "id=" + getId() +
            ", titleR='" + getTitleR() + "'" +
            ", alerts='" + getAlerts() + "'" +
            ", descript='" + getDescript() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", modifiedAt='" + getModifiedAt() + "'" +
            ", langue='" + getLangue() + "'" +
            "}";
    }
}
