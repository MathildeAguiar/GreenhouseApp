package fr.uga.polytech.greenhouse.service.criteria;

import fr.uga.polytech.greenhouse.domain.enumeration.AlertLevel;
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
 * Criteria class for the {@link fr.uga.polytech.greenhouse.domain.Alert} entity. This class is used
 * in {@link fr.uga.polytech.greenhouse.web.rest.AlertResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /alerts?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class AlertCriteria implements Serializable, Criteria {

    /**
     * Class for filtering AlertLevel
     */
    public static class AlertLevelFilter extends Filter<AlertLevel> {

        public AlertLevelFilter() {}

        public AlertLevelFilter(AlertLevelFilter filter) {
            super(filter);
        }

        @Override
        public AlertLevelFilter copy() {
            return new AlertLevelFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private AlertLevelFilter level;

    private InstantFilter createdAt;

    private InstantFilter modifiedAt;

    public AlertCriteria() {}

    public AlertCriteria(AlertCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.level = other.level == null ? null : other.level.copy();
        this.createdAt = other.createdAt == null ? null : other.createdAt.copy();
        this.modifiedAt = other.modifiedAt == null ? null : other.modifiedAt.copy();
    }

    @Override
    public AlertCriteria copy() {
        return new AlertCriteria(this);
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

    public AlertLevelFilter getLevel() {
        return level;
    }

    public AlertLevelFilter level() {
        if (level == null) {
            level = new AlertLevelFilter();
        }
        return level;
    }

    public void setLevel(AlertLevelFilter level) {
        this.level = level;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final AlertCriteria that = (AlertCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(level, that.level) &&
            Objects.equals(createdAt, that.createdAt) &&
            Objects.equals(modifiedAt, that.modifiedAt)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, level, createdAt, modifiedAt);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AlertCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (level != null ? "level=" + level + ", " : "") +
            (createdAt != null ? "createdAt=" + createdAt + ", " : "") +
            (modifiedAt != null ? "modifiedAt=" + modifiedAt + ", " : "") +
            "}";
    }
}
