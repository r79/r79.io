package bank;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;

import singleton.HitCounterBean;
import zins.ZinsBean;
import bo.Konto;
import dao.KontoDAOBean;

@Stateless
@Interceptors(ch.zli.m223.interceptor.HelloInterceptor.class)
public class BankBean implements Bank {

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

	@EJB
	private KontoDAOBean dao = null;

	@EJB
	private HitCounterBean hits = null;

	@PostConstruct
	public void afterInstantiation() {
		System.out.println("PostConstruct testet auf zins Instanz > " + zins);
		System.out.println("PostConstruct testet auf dao  Instanz > " + dao);
		System.out.println("PostConstruct testet auf hits Instanz > " + hits);
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

	public List<Konto> getAllKontos() {
		hits.incCount();
		return this.dao.findAll();
	}

	@Override
	public Konto getKontoById(Long id) {
		hits.incCount();
		return this.dao.findById(id);
	}

	@Override
	public Konto getKontoByNr(String nr) {
		hits.incCount();
		return this.dao.findByNr(nr);
	}

	@Override
	public void createKonto(Konto k) {
		hits.incCount();
		this.dao.save(k);
	}

	@Override
	public Object getRemoteReference() {
		return sc.getBusinessObject(Bank.class);
	}

	@Override
	public long tellHits() {
		return this.hits.tellHits();
	}

	@Override
	public void updateKonto(Konto k) {
		hits.incCount();
		this.dao.delete(k);
		this.dao.save(k);

	}

	@Override
	public void removeKonto(Konto k) {
		hits.incCount();
		this.dao.delete(k);

	}
}
