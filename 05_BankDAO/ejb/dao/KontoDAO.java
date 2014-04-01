package dao;

import java.util.List;

import bo.Konto;

public interface KontoDAO extends GenericDAO<Konto> {
	public Konto findByNr(String nr);

	public List<Konto> findByName(String name);

}
