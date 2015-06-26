package be.vdab.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "soort")
@Table(name = "artikels")
public abstract class Artikel implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	private long id;
	private String naam;
	private BigDecimal aankoopprijs;
	private BigDecimal verkoopprijs;

	public Artikel(String naam, BigDecimal aankoopprijs, BigDecimal verkoopprijs) {
		setNaam(naam);
		setAankoopprijs(aankoopprijs);
		setVerkoopprijs(verkoopprijs);
	}

	protected Artikel() {
	}// default constructor is vereiste voor JPA

	public static boolean isNaamValid(String naam) {
		return naam != null && !naam.isEmpty();
	}

	public static boolean isAankoopprijsValid(BigDecimal aankoopprijs) {
		return aankoopprijs != null
				&& aankoopprijs.compareTo(BigDecimal.ZERO) >= 0;
	}

	public static boolean isVerkoopprijsValid(BigDecimal verkoopprijs) {
		return verkoopprijs != null
				&& verkoopprijs.compareTo(BigDecimal.ONE) >= 0;
	}

	public static boolean isAankoopprijsVerkoopprijsValid(
			BigDecimal aankoopprijs, BigDecimal verkoopprijs) {
		return aankoopprijs.compareTo(verkoopprijs) <= 0;
	}

	public long getId() {
		return id;
	}

	public String getNaam() {
		return naam;
	}

	public BigDecimal getAankoopprijs() {
		return aankoopprijs;
	}

	public BigDecimal getVerkoopprijs() {
		return verkoopprijs;
	}

	public void setNaam(String naam) {
		this.naam = naam;
	}

	public void setAankoopprijs(BigDecimal aankoopprijs) {
		if (!isAankoopprijsValid(aankoopprijs)) {
			throw new IllegalArgumentException();
		}
		this.aankoopprijs = aankoopprijs;
	}

	public void setVerkoopprijs(BigDecimal verkoopprijs) {
		if (!isVerkoopprijsValid(verkoopprijs)) {
			throw new IllegalArgumentException();
		}
		this.verkoopprijs = verkoopprijs;
	}

	public BigDecimal getWinstPercentage() {
		return verkoopprijs.subtract(aankoopprijs)
				.divide(aankoopprijs, 2, RoundingMode.HALF_UP)
				.multiply(BigDecimal.valueOf(100));
	}

}
