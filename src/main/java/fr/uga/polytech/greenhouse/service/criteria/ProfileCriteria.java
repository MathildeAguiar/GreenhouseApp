package fr.uga.polytech.greenhouse.service.criteria;

import fr.uga.polytech.greenhouse.domain.enumeration.Category;
import fr.uga.polytech.greenhouse.domain.enumeration.Filiere;
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
 * Criteria class for the {@link fr.uga.polytech.greenhouse.domain.Profile} entity. This class is used
 * in {@link fr.uga.polytech.greenhouse.web.rest.ProfileResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /profiles?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ProfileCriteria implements Serializable, Criteria {

    /**
     * Class for filtering Category
     */
    public static class CategoryFilter extends Filter<Category> {

        public CategoryFilter() {}

        public CategoryFilter(CategoryFilter filter) {
            super(filter);
        }

        @Override
        public CategoryFilter copy() {
            return new CategoryFilter(this);
        }
    }

    /**
     * Class for filtering Filiere
     */
    public static class FiliereFilter extends Filter<Filiere> {

        public FiliereFilter() {}

        public FiliereFilter(FiliereFilter filter) {
            super(filter);
        }

        @Override
        public FiliereFilter copy() {
            return new FiliereFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private CategoryFilter status;

    private FiliereFilter specialite;

    private StringFilter address;

    private StringFilter codeP;

    private StringFilter ville;

    private StringFilter phoneNumber;

    private StringFilter email;

    private LongFilter userId;

    private LongFilter workId;

    private LongFilter documentId;

    private LongFilter houseId;

    public ProfileCriteria() {}

    public ProfileCriteria(ProfileCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.status = other.status == null ? null : other.status.copy();
        this.specialite = other.specialite == null ? null : other.specialite.copy();
        this.address = other.address == null ? null : other.address.copy();
        this.codeP = other.codeP == null ? null : other.codeP.copy();
        this.ville = other.ville == null ? null : other.ville.copy();
        this.phoneNumber = other.phoneNumber == null ? null : other.phoneNumber.copy();
        this.email = other.email == null ? null : other.email.copy();
        this.userId = other.userId == null ? null : other.userId.copy();
        this.workId = other.workId == null ? null : other.workId.copy();
        this.documentId = other.documentId == null ? null : other.documentId.copy();
        this.houseId = other.houseId == null ? null : other.houseId.copy();
    }

    @Override
    public ProfileCriteria copy() {
        return new ProfileCriteria(this);
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

    public StringFilter getName() {
        return name;
    }

    public StringFilter name() {
        if (name == null) {
            name = new StringFilter();
        }
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public CategoryFilter getStatus() {
        return status;
    }

    public CategoryFilter status() {
        if (status == null) {
            status = new CategoryFilter();
        }
        return status;
    }

    public void setStatus(CategoryFilter status) {
        this.status = status;
    }

    public FiliereFilter getSpecialite() {
        return specialite;
    }

    public FiliereFilter specialite() {
        if (specialite == null) {
            specialite = new FiliereFilter();
        }
        return specialite;
    }

    public void setSpecialite(FiliereFilter specialite) {
        this.specialite = specialite;
    }

    public StringFilter getAddress() {
        return address;
    }

    public StringFilter address() {
        if (address == null) {
            address = new StringFilter();
        }
        return address;
    }

    public void setAddress(StringFilter address) {
        this.address = address;
    }

    public StringFilter getCodeP() {
        return codeP;
    }

    public StringFilter codeP() {
        if (codeP == null) {
            codeP = new StringFilter();
        }
        return codeP;
    }

    public void setCodeP(StringFilter codeP) {
        this.codeP = codeP;
    }

    public StringFilter getVille() {
        return ville;
    }

    public StringFilter ville() {
        if (ville == null) {
            ville = new StringFilter();
        }
        return ville;
    }

    public void setVille(StringFilter ville) {
        this.ville = ville;
    }

    public StringFilter getPhoneNumber() {
        return phoneNumber;
    }

    public StringFilter phoneNumber() {
        if (phoneNumber == null) {
            phoneNumber = new StringFilter();
        }
        return phoneNumber;
    }

    public void setPhoneNumber(StringFilter phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public StringFilter getEmail() {
        return email;
    }

    public StringFilter email() {
        if (email == null) {
            email = new StringFilter();
        }
        return email;
    }

    public void setEmail(StringFilter email) {
        this.email = email;
    }

    public LongFilter getUserId() {
        return userId;
    }

    public LongFilter userId() {
        if (userId == null) {
            userId = new LongFilter();
        }
        return userId;
    }

    public void setUserId(LongFilter userId) {
        this.userId = userId;
    }

    public LongFilter getWorkId() {
        return workId;
    }

    public LongFilter workId() {
        if (workId == null) {
            workId = new LongFilter();
        }
        return workId;
    }

    public void setWorkId(LongFilter workId) {
        this.workId = workId;
    }

    public LongFilter getDocumentId() {
        return documentId;
    }

    public LongFilter documentId() {
        if (documentId == null) {
            documentId = new LongFilter();
        }
        return documentId;
    }

    public void setDocumentId(LongFilter documentId) {
        this.documentId = documentId;
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
        final ProfileCriteria that = (ProfileCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(status, that.status) &&
            Objects.equals(specialite, that.specialite) &&
            Objects.equals(address, that.address) &&
            Objects.equals(codeP, that.codeP) &&
            Objects.equals(ville, that.ville) &&
            Objects.equals(phoneNumber, that.phoneNumber) &&
            Objects.equals(email, that.email) &&
            Objects.equals(userId, that.userId) &&
            Objects.equals(workId, that.workId) &&
            Objects.equals(documentId, that.documentId) &&
            Objects.equals(houseId, that.houseId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, status, specialite, address, codeP, ville, phoneNumber, email, userId, workId, documentId, houseId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProfileCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (name != null ? "name=" + name + ", " : "") +
            (status != null ? "status=" + status + ", " : "") +
            (specialite != null ? "specialite=" + specialite + ", " : "") +
            (address != null ? "address=" + address + ", " : "") +
            (codeP != null ? "codeP=" + codeP + ", " : "") +
            (ville != null ? "ville=" + ville + ", " : "") +
            (phoneNumber != null ? "phoneNumber=" + phoneNumber + ", " : "") +
            (email != null ? "email=" + email + ", " : "") +
            (userId != null ? "userId=" + userId + ", " : "") +
            (workId != null ? "workId=" + workId + ", " : "") +
            (documentId != null ? "documentId=" + documentId + ", " : "") +
            (houseId != null ? "houseId=" + houseId + ", " : "") +
            "}";
    }
}
