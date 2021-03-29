package fr.uga.polytech.greenhouse.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link fr.uga.polytech.greenhouse.domain.GreenHouse} entity. This class is used
 * in {@link fr.uga.polytech.greenhouse.web.rest.GreenHouseResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /green-houses?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class GreenHouseCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter nameG;

    private FloatFilter latitude;

    private FloatFilter longitude;

    private LongFilter reportId;

    private LongFilter observateurId;

    public GreenHouseCriteria() {}

    public GreenHouseCriteria(GreenHouseCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.nameG = other.nameG == null ? null : other.nameG.copy();
        this.latitude = other.latitude == null ? null : other.latitude.copy();
        this.longitude = other.longitude == null ? null : other.longitude.copy();
        this.reportId = other.reportId == null ? null : other.reportId.copy();
        this.observateurId = other.observateurId == null ? null : other.observateurId.copy();
    }

    @Override
    public GreenHouseCriteria copy() {
        return new GreenHouseCriteria(this);
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

    public StringFilter getNameG() {
        return nameG;
    }

    public StringFilter nameG() {
        if (nameG == null) {
            nameG = new StringFilter();
        }
        return nameG;
    }

    public void setNameG(StringFilter nameG) {
        this.nameG = nameG;
    }

    public FloatFilter getLatitude() {
        return latitude;
    }

    public FloatFilter latitude() {
        if (latitude == null) {
            latitude = new FloatFilter();
        }
        return latitude;
    }

    public void setLatitude(FloatFilter latitude) {
        this.latitude = latitude;
    }

    public FloatFilter getLongitude() {
        return longitude;
    }

    public FloatFilter longitude() {
        if (longitude == null) {
            longitude = new FloatFilter();
        }
        return longitude;
    }

    public void setLongitude(FloatFilter longitude) {
        this.longitude = longitude;
    }

    public LongFilter getReportId() {
        return reportId;
    }

    public LongFilter reportId() {
        if (reportId == null) {
            reportId = new LongFilter();
        }
        return reportId;
    }

    public void setReportId(LongFilter reportId) {
        this.reportId = reportId;
    }

    public LongFilter getObservateurId() {
        return observateurId;
    }

    public LongFilter observateurId() {
        if (observateurId == null) {
            observateurId = new LongFilter();
        }
        return observateurId;
    }

    public void setObservateurId(LongFilter observateurId) {
        this.observateurId = observateurId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final GreenHouseCriteria that = (GreenHouseCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(nameG, that.nameG) &&
            Objects.equals(latitude, that.latitude) &&
            Objects.equals(longitude, that.longitude) &&
            Objects.equals(reportId, that.reportId) &&
            Objects.equals(observateurId, that.observateurId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nameG, latitude, longitude, reportId, observateurId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "GreenHouseCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (nameG != null ? "nameG=" + nameG + ", " : "") +
            (latitude != null ? "latitude=" + latitude + ", " : "") +
            (longitude != null ? "longitude=" + longitude + ", " : "") +
            (reportId != null ? "reportId=" + reportId + ", " : "") +
            (observateurId != null ? "observateurId=" + observateurId + ", " : "") +
            "}";
    }
}
