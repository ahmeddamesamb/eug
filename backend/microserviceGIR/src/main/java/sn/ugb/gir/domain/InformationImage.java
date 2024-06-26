package sn.ugb.gir.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A InformationImage.
 */
@Entity
@Table(name = "information_image")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "informationimage")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class InformationImage implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "description")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String description;

    @NotNull
    @Column(name = "chemin_path", nullable = false, unique = true)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String cheminPath;

    @NotNull
    @Column(name = "chemin_file", nullable = false, unique = true)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String cheminFile;

    @Column(name = "en_cours_yn")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String enCoursYN;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public InformationImage id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return this.description;
    }

    public InformationImage description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCheminPath() {
        return this.cheminPath;
    }

    public InformationImage cheminPath(String cheminPath) {
        this.setCheminPath(cheminPath);
        return this;
    }

    public void setCheminPath(String cheminPath) {
        this.cheminPath = cheminPath;
    }

    public String getCheminFile() {
        return this.cheminFile;
    }

    public InformationImage cheminFile(String cheminFile) {
        this.setCheminFile(cheminFile);
        return this;
    }

    public void setCheminFile(String cheminFile) {
        this.cheminFile = cheminFile;
    }

    public String getEnCoursYN() {
        return this.enCoursYN;
    }

    public InformationImage enCoursYN(String enCoursYN) {
        this.setEnCoursYN(enCoursYN);
        return this;
    }

    public void setEnCoursYN(String enCoursYN) {
        this.enCoursYN = enCoursYN;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof InformationImage)) {
            return false;
        }
        return getId() != null && getId().equals(((InformationImage) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "InformationImage{" +
            "id=" + getId() +
            ", description='" + getDescription() + "'" +
            ", cheminPath='" + getCheminPath() + "'" +
            ", cheminFile='" + getCheminFile() + "'" +
            ", enCoursYN='" + getEnCoursYN() + "'" +
            "}";
    }
}
