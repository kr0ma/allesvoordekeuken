package be.vdab.entities;

import java.io.Serializable;
import java.util.Collections;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

@Entity
@Table(name = "Artikelgroepen")
public class Artikelgroep implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	private long id;

	private String naam;

	@OneToMany(mappedBy = "artikelgroep")
	@OrderBy("naam")
	private Set<Artikel> artikels;

	public Set<Artikel> getArtikels() {
		return Collections.unmodifiableSet(artikels);
	}

	public String getNaam() {
		return naam;
	}

	public void setId(long id) {
		this.id = id;
	}

	public void setNaam(String naam) {
		this.naam = naam;
	}

	public long getId() {
		return id;
	}

	public void addArtikel(Artikel artikel) {
		artikels.add(artikel);
		if (artikel.getArtikelgroep() != this) {
			artikel.setArtikelgroep(this);
		}
	}

	public void removeArtikel(Artikel artikel) {
		artikels.remove(artikel);
		if (artikel.getArtikelgroep() == this) {
			artikel.setArtikelgroep(null);
		}
	}

	@Override
	public boolean equals(Object object) {
		if (!(object instanceof Artikelgroep)) {
			return false;
		}
		Artikelgroep andereArtikelgroep = (Artikelgroep) object;
		return naam.equalsIgnoreCase(andereArtikelgroep.naam);
	}

	@Override
	public int hashCode() {
		return naam.toUpperCase().hashCode();
	}

}
