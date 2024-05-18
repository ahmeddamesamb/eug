package sn.ugb.gir.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A InformationPersonnelle.
 */
@Entity
@Table(name = "information_personnelle")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class InformationPersonnelle implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "nom_etu", nullable = false)
    private String nomEtu;

    @Column(name = "nom_jeune_fille_etu")
    private String nomJeuneFilleEtu;

    @NotNull
    @Column(name = "prenom_etu", nullable = false)
    private String prenomEtu;

    @NotNull
    @Column(name = "statut_marital", nullable = false)
    private String statutMarital;

    @Column(name = "regime")
    private Integer regime;

    @Column(name = "profession")
    private String profession;

    @NotNull
    @Column(name = "adresse_etu", nullable = false)
    private String adresseEtu;

    @Column(name = "tel_etu")
    private String telEtu;

    @Column(name = "email_etu")
    private String emailEtu;

    @Column(name = "adresse_parent")
    private String adresseParent;

    @Column(name = "tel_parent")
    private String telParent;

    @Column(name = "email_parent")
    private String emailParent;

    @Column(name = "nom_parent")
    private String nomParent;

    @Column(name = "prenom_parent")
    private String prenomParent;

    @Column(name = "handicap_yn")
    private Integer handicapYN;

    @Column(name = "ordi_perso_yn")
    private Integer ordiPersoYN;

    @Column(name = "derniere_modification")
    private LocalDate derniereModification;

    @Column(name = "email_user")
    private String emailUser;

    @JsonIgnoreProperties(value = { "region", "typeSelection", "lycee", "informationPersonnelle", "baccalaureat" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(unique = true)
    private Etudiant etudiant;

    @ManyToOne(fetch = FetchType.LAZY)
    private TypeHandicap typeHadique;

    @ManyToOne(fetch = FetchType.LAZY)
    private TypeBourse typeBourse;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public InformationPersonnelle id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomEtu() {
        return this.nomEtu;
    }

    public InformationPersonnelle nomEtu(String nomEtu) {
        this.setNomEtu(nomEtu);
        return this;
    }

    public void setNomEtu(String nomEtu) {
        this.nomEtu = nomEtu;
    }

    public String getNomJeuneFilleEtu() {
        return this.nomJeuneFilleEtu;
    }

    public InformationPersonnelle nomJeuneFilleEtu(String nomJeuneFilleEtu) {
        this.setNomJeuneFilleEtu(nomJeuneFilleEtu);
        return this;
    }

    public void setNomJeuneFilleEtu(String nomJeuneFilleEtu) {
        this.nomJeuneFilleEtu = nomJeuneFilleEtu;
    }

    public String getPrenomEtu() {
        return this.prenomEtu;
    }

    public InformationPersonnelle prenomEtu(String prenomEtu) {
        this.setPrenomEtu(prenomEtu);
        return this;
    }

    public void setPrenomEtu(String prenomEtu) {
        this.prenomEtu = prenomEtu;
    }

    public String getStatutMarital() {
        return this.statutMarital;
    }

    public InformationPersonnelle statutMarital(String statutMarital) {
        this.setStatutMarital(statutMarital);
        return this;
    }

    public void setStatutMarital(String statutMarital) {
        this.statutMarital = statutMarital;
    }

    public Integer getRegime() {
        return this.regime;
    }

    public InformationPersonnelle regime(Integer regime) {
        this.setRegime(regime);
        return this;
    }

    public void setRegime(Integer regime) {
        this.regime = regime;
    }

    public String getProfession() {
        return this.profession;
    }

    public InformationPersonnelle profession(String profession) {
        this.setProfession(profession);
        return this;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public String getAdresseEtu() {
        return this.adresseEtu;
    }

    public InformationPersonnelle adresseEtu(String adresseEtu) {
        this.setAdresseEtu(adresseEtu);
        return this;
    }

    public void setAdresseEtu(String adresseEtu) {
        this.adresseEtu = adresseEtu;
    }

    public String getTelEtu() {
        return this.telEtu;
    }

    public InformationPersonnelle telEtu(String telEtu) {
        this.setTelEtu(telEtu);
        return this;
    }

    public void setTelEtu(String telEtu) {
        this.telEtu = telEtu;
    }

    public String getEmailEtu() {
        return this.emailEtu;
    }

    public InformationPersonnelle emailEtu(String emailEtu) {
        this.setEmailEtu(emailEtu);
        return this;
    }

    public void setEmailEtu(String emailEtu) {
        this.emailEtu = emailEtu;
    }

    public String getAdresseParent() {
        return this.adresseParent;
    }

    public InformationPersonnelle adresseParent(String adresseParent) {
        this.setAdresseParent(adresseParent);
        return this;
    }

    public void setAdresseParent(String adresseParent) {
        this.adresseParent = adresseParent;
    }

    public String getTelParent() {
        return this.telParent;
    }

    public InformationPersonnelle telParent(String telParent) {
        this.setTelParent(telParent);
        return this;
    }

    public void setTelParent(String telParent) {
        this.telParent = telParent;
    }

    public String getEmailParent() {
        return this.emailParent;
    }

    public InformationPersonnelle emailParent(String emailParent) {
        this.setEmailParent(emailParent);
        return this;
    }

    public void setEmailParent(String emailParent) {
        this.emailParent = emailParent;
    }

    public String getNomParent() {
        return this.nomParent;
    }

    public InformationPersonnelle nomParent(String nomParent) {
        this.setNomParent(nomParent);
        return this;
    }

    public void setNomParent(String nomParent) {
        this.nomParent = nomParent;
    }

    public String getPrenomParent() {
        return this.prenomParent;
    }

    public InformationPersonnelle prenomParent(String prenomParent) {
        this.setPrenomParent(prenomParent);
        return this;
    }

    public void setPrenomParent(String prenomParent) {
        this.prenomParent = prenomParent;
    }

    public Integer getHandicapYN() {
        return this.handicapYN;
    }

    public InformationPersonnelle handicapYN(Integer handicapYN) {
        this.setHandicapYN(handicapYN);
        return this;
    }

    public void setHandicapYN(Integer handicapYN) {
        this.handicapYN = handicapYN;
    }

    public Integer getOrdiPersoYN() {
        return this.ordiPersoYN;
    }

    public InformationPersonnelle ordiPersoYN(Integer ordiPersoYN) {
        this.setOrdiPersoYN(ordiPersoYN);
        return this;
    }

    public void setOrdiPersoYN(Integer ordiPersoYN) {
        this.ordiPersoYN = ordiPersoYN;
    }

    public LocalDate getDerniereModification() {
        return this.derniereModification;
    }

    public InformationPersonnelle derniereModification(LocalDate derniereModification) {
        this.setDerniereModification(derniereModification);
        return this;
    }

    public void setDerniereModification(LocalDate derniereModification) {
        this.derniereModification = derniereModification;
    }

    public String getEmailUser() {
        return this.emailUser;
    }

    public InformationPersonnelle emailUser(String emailUser) {
        this.setEmailUser(emailUser);
        return this;
    }

    public void setEmailUser(String emailUser) {
        this.emailUser = emailUser;
    }

    public Etudiant getEtudiant() {
        return this.etudiant;
    }

    public void setEtudiant(Etudiant etudiant) {
        this.etudiant = etudiant;
    }

    public InformationPersonnelle etudiant(Etudiant etudiant) {
        this.setEtudiant(etudiant);
        return this;
    }

    public TypeHandicap getTypeHadique() {
        return this.typeHadique;
    }

    public void setTypeHadique(TypeHandicap typeHandicap) {
        this.typeHadique = typeHandicap;
    }

    public InformationPersonnelle typeHadique(TypeHandicap typeHandicap) {
        this.setTypeHadique(typeHandicap);
        return this;
    }

    public TypeBourse getTypeBourse() {
        return this.typeBourse;
    }

    public void setTypeBourse(TypeBourse typeBourse) {
        this.typeBourse = typeBourse;
    }

    public InformationPersonnelle typeBourse(TypeBourse typeBourse) {
        this.setTypeBourse(typeBourse);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof InformationPersonnelle)) {
            return false;
        }
        return getId() != null && getId().equals(((InformationPersonnelle) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "InformationPersonnelle{" +
            "id=" + getId() +
            ", nomEtu='" + getNomEtu() + "'" +
            ", nomJeuneFilleEtu='" + getNomJeuneFilleEtu() + "'" +
            ", prenomEtu='" + getPrenomEtu() + "'" +
            ", statutMarital='" + getStatutMarital() + "'" +
            ", regime=" + getRegime() +
            ", profession='" + getProfession() + "'" +
            ", adresseEtu='" + getAdresseEtu() + "'" +
            ", telEtu='" + getTelEtu() + "'" +
            ", emailEtu='" + getEmailEtu() + "'" +
            ", adresseParent='" + getAdresseParent() + "'" +
            ", telParent='" + getTelParent() + "'" +
            ", emailParent='" + getEmailParent() + "'" +
            ", nomParent='" + getNomParent() + "'" +
            ", prenomParent='" + getPrenomParent() + "'" +
            ", handicapYN=" + getHandicapYN() +
            ", ordiPersoYN=" + getOrdiPersoYN() +
            ", derniereModification='" + getDerniereModification() + "'" +
            ", emailUser='" + getEmailUser() + "'" +
            "}";
    }
}
