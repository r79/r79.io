package dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.EJBException;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.sql.DataSource;

import bo.Konto;

@Stateless
@Resource(mappedName = "java:/jdbc/bank", name = "bankDB")
public class KontoDAOBean extends KontoDAOImpl {

	private DataSource ds = null;
	private Connection co = null;
	private Statement st = null;
	private ResultSet rs = null;
	private String query = null;

	@Resource
	protected SessionContext sc;

	private void openDatabaseConnection() {

		try {
			// CMP - Container Managed Connection
			ds = (DataSource) sc.lookup("bankDB");
			co = ds.getConnection();

		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException("Could not open Database Connection");
		}
	}

	private void closeDatabaseConnection() {
		try {
			if (st != null)
				st.close();
			if (co != null)
				co.close();
		} catch (SQLException e) {
			throw new EJBException("Could not close Database Connection");
		}
	}

	@Override
	public Konto findById(Long id) {
		openDatabaseConnection();
		Konto konto = new Konto();
		query = "SELECT * FROM account WHERE id = " + id;
		try {
			st = co.createStatement();
			rs = st.executeQuery(query);
			if (rs.next()) {
				konto.setNr(rs.getString("nr"));
				konto.setName(rs.getString("name"));
				konto.setSaldo(rs.getFloat("saldo"));
				konto.setCurrency(rs.getString("currency"));
			}
		} catch (SQLException e) {
			throw new EJBException("Could not find Konto mit ID " + id + " in DB");
		} finally {
			closeDatabaseConnection();
		}
		return konto;
	}

	@Override
	public Konto findByNr(String nr) {
		openDatabaseConnection();
		Konto konto = new Konto();
		query = "SELECT * FROM account WHERE nr = '" + nr + "'";
		try {
			st = co.createStatement();
			rs = st.executeQuery(query);
			if (rs.next()) {
				konto.setNr(rs.getString("nr"));
				konto.setName(rs.getString("name"));
				konto.setSaldo(rs.getFloat("saldo"));
				konto.setCurrency(rs.getString("currency"));
			}
		} catch (SQLException e) {
			throw new EJBException("Could not find Konto mit Nr " + nr + " in DB");
		} finally {
			closeDatabaseConnection();
		}
		return konto;
	}

	@Override
	public List<Konto> findAll() {
		openDatabaseConnection();
		List<Konto> kontos = new ArrayList<Konto>();
		query = "SELECT * FROM account";
		try {
			st = co.createStatement();
			rs = st.executeQuery(query);
			while (rs.next()) {
				Konto konto = new Konto();
				konto.setNr(rs.getString("nr"));
				konto.setName(rs.getString("name"));
				konto.setSaldo(rs.getFloat("saldo"));
				konto.setCurrency(rs.getString("currency"));
				kontos.add(konto);
			}
		} catch (SQLException e) {
			throw new EJBException("Could not find Kontos in DB");
		} finally {
			closeDatabaseConnection();
		}
		return kontos;
	}

	@Override
	public Konto save(Konto entity) {
		openDatabaseConnection();
		query = "INSERT INTO account (nr, name, saldo, currency) VALUES('"
				+ entity.getNr() + "','" + entity.getName() + "'," + entity.getSaldo()
				+ ",'" + entity.getCurrency() + "')";
		try {
			st = co.createStatement();
			if (st.executeUpdate(query) != 1)
				throw new EJBException("Could not save Konto");

		} catch (SQLException e) {
			throw new EJBException("Could not find Kontos in DB");
		} finally {
			closeDatabaseConnection();
		}
		return null;
	}

	@Override
	public Konto delete(Konto entity) {
		openDatabaseConnection();
		query = "DELETE FROM account WHERE nr = '" + entity.getNr() + "'";
		try {
			st = co.createStatement();
			if (st.executeUpdate(query) != 1)
				throw new EJBException("Could not delete Konto");
		} catch (SQLException e) {
			throw new EJBException("Could not find Kontos in DB");
		} finally {
			closeDatabaseConnection();
		}
		return null;
	}

	@Override
	public long count() {
		return (long)findAll().size();
	}

	@Override
	public List<Konto> findByName(String name) {
		openDatabaseConnection();
		List<Konto> kontos = new ArrayList<Konto>();
		query = "SELECT * FROM account  WHERE nr = '%" + name + "%'";
		try {
			st = co.createStatement();
			rs = st.executeQuery(query);
			while (rs.next()) {
				Konto konto = new Konto();
				konto.setNr(rs.getString("nr"));
				konto.setName(rs.getString("name"));
				konto.setSaldo(rs.getFloat("saldo"));
				konto.setCurrency(rs.getString("currency"));
				kontos.add(konto);
			}
		} catch (SQLException e) {
			throw new EJBException("Could not find Kontos in DB");
		} finally {
			closeDatabaseConnection();
		}
		return kontos;
	}
}
