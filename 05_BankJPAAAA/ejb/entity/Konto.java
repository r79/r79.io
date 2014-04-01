package entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@SuppressWarnings("serial")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@NamedQueries({
		@NamedQuery(name = "Konto.findAll", query = "from Konto k"),
		@NamedQuery(name = "Konto.findByNr", query = "from Konto k where k.nr = :nr"),
		@NamedQuery(name = "Konto.findByName", query = "from Konto k where k.name = :name") })
public class Konto implements Serializable {
	@Id
	@GeneratedValue
	private Long id;
	@Column(unique = true)
	private String nr;
	private String name;
	private Float saldo;
	private String currency;

	public Konto(String nr, String name, Float saldo, String currency) {
		super();
		this.nr = nr;
		this.name = name;
		this.saldo = saldo;
		this.currency = currency;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Konto other = (Konto) obj;
		if (currency == null) {
			if (other.currency != null)
				return false;
		} else if (!currency.equals(other.currency))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (nr == null) {
			if (other.nr != null)
				return false;
		} else if (!nr.equals(other.nr))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((currency == null) ? 0 : currency.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((nr == null) ? 0 : nr.hashCode());
		return result;
	}

}
