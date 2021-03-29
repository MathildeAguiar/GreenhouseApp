package fr.uga.polytech.greenhouse.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Task.
 */
@Entity
@Table(name = "task")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Task implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "title_t", nullable = false)
    private String titleT;

    @Size(min = 8)
    @Column(name = "description")
    private String description;

    @NotNull
    @Column(name = "start_time", nullable = false)
    private Instant startTime;

    @Column(name = "end_time")
    private Instant endTime;

    @NotNull
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @ManyToOne
    @JsonIgnoreProperties(value = { "user", "works", "documents", "houses" }, allowSetters = true)
    private Profile responsible;

    @ManyToOne
    @JsonIgnoreProperties(value = { "tasks", "author", "house" }, allowSetters = true)
    private Report rapport;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Task id(Long id) {
        this.id = id;
        return this;
    }

    public String getTitleT() {
        return this.titleT;
    }

    public Task titleT(String titleT) {
        this.titleT = titleT;
        return this;
    }

    public void setTitleT(String titleT) {
        this.titleT = titleT;
    }

    public String getDescription() {
        return this.description;
    }

    public Task description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Instant getStartTime() {
        return this.startTime;
    }

    public Task startTime(Instant startTime) {
        this.startTime = startTime;
        return this;
    }

    public void setStartTime(Instant startTime) {
        this.startTime = startTime;
    }

    public Instant getEndTime() {
        return this.endTime;
    }

    public Task endTime(Instant endTime) {
        this.endTime = endTime;
        return this;
    }

    public void setEndTime(Instant endTime) {
        this.endTime = endTime;
    }

    public Instant getCreatedAt() {
        return this.createdAt;
    }

    public Task createdAt(Instant createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Profile getResponsible() {
        return this.responsible;
    }

    public Task responsible(Profile profile) {
        this.setResponsible(profile);
        return this;
    }

    public void setResponsible(Profile profile) {
        this.responsible = profile;
    }

    public Report getRapport() {
        return this.rapport;
    }

    public Task rapport(Report report) {
        this.setRapport(report);
        return this;
    }

    public void setRapport(Report report) {
        this.rapport = report;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Task)) {
            return false;
        }
        return id != null && id.equals(((Task) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Task{" +
            "id=" + getId() +
            ", titleT='" + getTitleT() + "'" +
            ", description='" + getDescription() + "'" +
            ", startTime='" + getStartTime() + "'" +
            ", endTime='" + getEndTime() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            "}";
    }
}
