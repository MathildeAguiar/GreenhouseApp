package fr.uga.polytech.greenhouse.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.InstantFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link fr.uga.polytech.greenhouse.domain.Task} entity. This class is used
 * in {@link fr.uga.polytech.greenhouse.web.rest.TaskResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /tasks?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class TaskCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter titleT;

    private StringFilter description;

    private InstantFilter startTime;

    private InstantFilter endTime;

    private InstantFilter createdAt;

    private LongFilter responsibleId;

    private LongFilter rapportId;

    public TaskCriteria() {}

    public TaskCriteria(TaskCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.titleT = other.titleT == null ? null : other.titleT.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.startTime = other.startTime == null ? null : other.startTime.copy();
        this.endTime = other.endTime == null ? null : other.endTime.copy();
        this.createdAt = other.createdAt == null ? null : other.createdAt.copy();
        this.responsibleId = other.responsibleId == null ? null : other.responsibleId.copy();
        this.rapportId = other.rapportId == null ? null : other.rapportId.copy();
    }

    @Override
    public TaskCriteria copy() {
        return new TaskCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getTitleT() {
        return titleT;
    }

    public StringFilter titleT() {
        if (titleT == null) {
            titleT = new StringFilter();
        }
        return titleT;
    }

    public void setTitleT(StringFilter titleT) {
        this.titleT = titleT;
    }

    public StringFilter getDescription() {
        return description;
    }

    public StringFilter description() {
        if (description == null) {
            description = new StringFilter();
        }
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
    }

    public InstantFilter getStartTime() {
        return startTime;
    }

    public InstantFilter startTime() {
        if (startTime == null) {
            startTime = new InstantFilter();
        }
        return startTime;
    }

    public void setStartTime(InstantFilter startTime) {
        this.startTime = startTime;
    }

    public InstantFilter getEndTime() {
        return endTime;
    }

    public InstantFilter endTime() {
        if (endTime == null) {
            endTime = new InstantFilter();
        }
        return endTime;
    }

    public void setEndTime(InstantFilter endTime) {
        this.endTime = endTime;
    }

    public InstantFilter getCreatedAt() {
        return createdAt;
    }

    public InstantFilter createdAt() {
        if (createdAt == null) {
            createdAt = new InstantFilter();
        }
        return createdAt;
    }

    public void setCreatedAt(InstantFilter createdAt) {
        this.createdAt = createdAt;
    }

    public LongFilter getResponsibleId() {
        return responsibleId;
    }

    public LongFilter responsibleId() {
        if (responsibleId == null) {
            responsibleId = new LongFilter();
        }
        return responsibleId;
    }

    public void setResponsibleId(LongFilter responsibleId) {
        this.responsibleId = responsibleId;
    }

    public LongFilter getRapportId() {
        return rapportId;
    }

    public LongFilter rapportId() {
        if (rapportId == null) {
            rapportId = new LongFilter();
        }
        return rapportId;
    }

    public void setRapportId(LongFilter rapportId) {
        this.rapportId = rapportId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final TaskCriteria that = (TaskCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(titleT, that.titleT) &&
            Objects.equals(description, that.description) &&
            Objects.equals(startTime, that.startTime) &&
            Objects.equals(endTime, that.endTime) &&
            Objects.equals(createdAt, that.createdAt) &&
            Objects.equals(responsibleId, that.responsibleId) &&
            Objects.equals(rapportId, that.rapportId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, titleT, description, startTime, endTime, createdAt, responsibleId, rapportId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TaskCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (titleT != null ? "titleT=" + titleT + ", " : "") +
            (description != null ? "description=" + description + ", " : "") +
            (startTime != null ? "startTime=" + startTime + ", " : "") +
            (endTime != null ? "endTime=" + endTime + ", " : "") +
            (createdAt != null ? "createdAt=" + createdAt + ", " : "") +
            (responsibleId != null ? "responsibleId=" + responsibleId + ", " : "") +
            (rapportId != null ? "rapportId=" + rapportId + ", " : "") +
            "}";
    }
}
