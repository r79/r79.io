package bank;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
import javax.sql.DataSource;

import zins.ZinsBean;
import bo.Konto;

// @DataSourceDefinition(className = "com.mysql.jdbc.Driver", name = "java:global/jdbc/account", serverName = "localhost", portNumber = 3306, user = "root", password = "", databaseName = "bank")
@Stateless
@Interceptors(ch.zli.m223.interceptor.HelloInterceptor.class)
public class BankBean implements Bank {

	// @Resource(mappedName = "java:global/jdbc/account")
	@Resource(mappedName = "java:/jdbc/bank")
	DataSource ds = null;

	private Connection co = null;
	private Statement st = null;
	private ResultSet rs = null;
	private String query = null;

	@Resource
	protected SessionContext sc;

	/*
	 * The syntax for the EJB Global JNDI is as following:
	 * java:global[/<app-name>l]
	 * /<module-name>/<bean-name>l[!<fully-qualified-interface-name>l]
	 * 
	 * @EJB(lookup = "ejb:Application/ZinsSLSB//RateBean!zins.Zins")
	 * 
	 * For App Scope it is as following:
	 * java:app/<module-name>l/<bean-name>l[!<fully-qualified-interface-name>l]
	 * 
	 * @EJB(lookup = "java:app/MyEJB/ZinsBean")
	 * 
	 * auch:
	 * 
	 * @EJB(lookup="java:module/ZinsBean")
	 * 
	 * @EJB(lookup="java:global/MyEAR/MyEJB/ZinsBean")
	 */

	@EJB
	private ZinsBean zins = null;

	@PostConstruct
	public void afterInstantiation() {
		System.out.println("PostConstruct testet auf ZinsBean Instanz > " + zins);
	}

	@Override
	public double tellRate() {
		return this.zins.getRate();
	}

	@Override
	public double calculateRate(Konto k, int years) {
		Konto konto = getKontoByNr(k.getNr());
		return zins.calculate(konto.getSaldo(), years);
	}

	@Override
	public List<Konto> getAllKontos() {
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
	public Konto getKontoById(Long id) {
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
	public Konto getKontoByNr(String nr) {
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
	public void createKonto(Konto k) {
		openDatabaseConnection();
		query = "INSERT INTO account (nr, name, saldo, currency) VALUES('"
				+ k.getNr() + "','" + k.getName() + "'," + k.getSaldo() + ",'"
				+ k.getCurrency() + "')";
		try {
			st = co.createStatement();
			if (st.executeUpdate(query) != 1)
				throw new EJBException("Could not save Konto");

		} catch (SQLException e) {
			throw new EJBException("Could not find Kontos in DB");
		} finally {
			closeDatabaseConnection();
		}
	}

	@Override
	public void removeKonto(Konto k) {
		openDatabaseConnection();
		query = "DELETE FROM account WHERE nr = '" + k.getNr() + "'";
		try {
			st = co.createStatement();
			if (st.executeUpdate(query) != 1)
				throw new EJBException("Could not delete Konto");
		} catch (SQLException e) {
			throw new EJBException("Could not find Kontos in DB");
		} finally {
			closeDatabaseConnection();
		}
	}

	@Override
	public void updateKonto(Konto k) {
		this.removeKonto(k);
		this.createKonto(k);
	}

	@Override
	public Object getRemoteReference() {
		return sc.getBusinessObject(Bank.class);
	}

	@SuppressWarnings("unused")
	private void openDatabaseConnection() {
		try {
			// BMP - Bean Managed Persistence
			String url = "jdbc:mysql://localhost:3306/bank?user=root";
			// co = DriverManager.getConnection(url);

			// ODER:

			// CMP - Container Managed Persistence
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
}
