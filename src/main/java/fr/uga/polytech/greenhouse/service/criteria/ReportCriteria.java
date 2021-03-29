package fr.uga.polytech.greenhouse.service.criteria;

import fr.uga.polytech.greenhouse.domain.enumeration.Language;
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
 * Criteria class for the {@link fr.uga.polytech.greenhouse.domain.Report} entity. This class is used
 * in {@link fr.uga.polytech.greenhouse.web.rest.ReportResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /reports?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ReportCriteria implements Serializable, Criteria {

    /**
     * Class for filtering Language
     */
    public static class LanguageFilter extends Filter<Language> {

        public LanguageFilter() {}

        public LanguageFilter(LanguageFilter filter) {
            super(filter);
        }

        @Override
        public LanguageFilter copy() {
            return new LanguageFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter titleR;

    private StringFilter alerts;

    private InstantFilter createdAt;

    private InstantFilter modifiedAt;

    private LanguageFilter langue;

    private LongFilter taskId;

    private LongFilter authorId;

    private LongFilter houseId;

    public ReportCriteria() {}

    public ReportCriteria(ReportCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.titleR = other.titleR == null ? null : other.titleR.copy();
        this.alerts = other.alerts == null ? null : other.alerts.copy();
        this.createdAt = other.createdAt == null ? null : other.createdAt.copy();
        this.modifiedAt = other.modifiedAt == null ? null : other.modifiedAt.copy();
        this.langue = other.langue == null ? null : other.langue.copy();
        this.taskId = other.taskId == null ? null : other.taskId.copy();
        this.authorId = other.authorId == null ? null : other.authorId.copy();
        this.houseId = other.houseId == null ? null : other.houseId.copy();
    }

    @Override
    public ReportCriteria copy() {
        return new ReportCriteria(this);
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

    public StringFilter getTitleR() {
        return titleR;
    }

    public StringFilter titleR() {
        if (titleR == null) {
            titleR = new StringFilter();
        }
        return titleR;
    }

    public void setTitleR(StringFilter titleR) {
        this.titleR = titleR;
    }

    public StringFilter getAlerts() {
        return alerts;
    }

    public StringFilter alerts() {
        if (alerts == null) {
            alerts = new StringFilter();
        }
        return alerts;
    }

    public void setAlerts(StringFilter alerts) {
        this.alerts = alerts;
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

    public InstantFilter getModifiedAt() {
        return modifiedAt;
    }

    public InstantFilter modifiedAt() {
        if (modifiedAt == null) {
            modifiedAt = new InstantFilter();
        }
        return modifiedAt;
    }

    public void setModifiedAt(InstantFilter modifiedAt) {
        this.modifiedAt = modifiedAt;
    }

    public LanguageFilter getLangue() {
        return langue;
    }

    public LanguageFilter langue() {
        if (langue == null) {
            langue = new LanguageFilter();
        }
        return langue;
    }

    public void setLangue(LanguageFilter langue) {
        this.langue = langue;
    }

    public LongFilter getTaskId() {
        return taskId;
    }

    public LongFilter taskId() {
        if (taskId == null) {
            taskId = new LongFilter();
        }
        return taskId;
    }

    public void setTaskId(LongFilter taskId) {
        this.taskId = taskId;
    }

    public LongFilter getAuthorId() {
        return authorId;
    }

    public LongFilter authorId() {
        if (authorId == null) {
            authorId = new LongFilter();
        }
        return authorId;
    }

    public void setAuthorId(LongFilter authorId) {
        this.authorId = authorId;
    }

    public LongFilter getHouseId() {
        return houseId;
    }

    public LongFilter houseId() {
        if (houseId == null) {
            houseId = new LongFilter();
        }
        return houseId;
    }

    public void setHouseId(LongFilter houseId) {
        this.houseId = houseId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ReportCriteria that = (ReportCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(titleR, that.titleR) &&
            Objects.equals(alerts, that.alerts) &&
            Objects.equals(createdAt, that.createdAt) &&
            Objects.equals(modifiedAt, that.modifiedAt) &&
            Objects.equals(langue, that.langue) &&
            Objects.equals(taskId, that.taskId) &&
            Objects.equals(authorId, that.authorId) &&
            Objects.equals(houseId, that.houseId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, titleR, alerts, createdAt, modifiedAt, langue, taskId, authorId, houseId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ReportCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (titleR != null ? "titleR=" + titleR + ", " : "") +
            (alerts != null ? "alerts=" + alerts + ", " : "") +
            (createdAt != null ? "createdAt=" + createdAt + ", " : "") +
            (modifiedAt != null ? "modifiedAt=" + modifiedAt + ", " : "") +
            (langue != null ? "langue=" + langue + ", " : "") +
            (taskId != null ? "taskId=" + taskId + ", " : "") +
            (authorId != null ? "authorId=" + authorId + ", " : "") +
            (houseId != null ? "houseId=" + houseId + ", " : "") +
            "}";
    }
}
