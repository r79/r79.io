package bank;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import zins.ZinsBean;
import entity.Konto;

@Stateless
@Interceptors(ch.zli.m223.interceptor.HelloInterceptor.class)
public class BankBean implements Bank {

	@PersistenceContext(unitName = "persistenceContext_Bank")
	private EntityManager em = null;

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

	@SuppressWarnings("unchecked")
	public List<Konto> getAllKontos() {
		return (List<Konto>) new ArrayList<Konto>(em
				.createNamedQuery("Konto.findAll").getResultList());
	}

	@Override
	public Konto getKontoById(Long id) {
		return em.getReference(Konto.class, id);
	}

	@Override
	public Konto getKontoByNr(String nr) {
		return (Konto) em.createNamedQuery("Konto.findByNr").setParameter("nr", nr)
				.getSingleResult();
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public Long createKonto(Konto k) {
		em.persist(k);
		return k.getId();
	}

	@Override
	public Object getRemoteReference() {
		return sc.getBusinessObject(Bank.class);
	}


	@Override
	public void removeKonto(Konto k) {
		System.out.println("kontoToDelete : " + k);
		Konto kontoToDelete = (Konto) em.createNamedQuery("Konto.findByNr")
				.setParameter("nr", k.getNr()).getSingleResult();
		em.remove(kontoToDelete);
	}


	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	private void updateAmount(Konto k, Float amount) {
			Konto kontoToUpdate = (Konto) em.createNamedQuery("Konto.findByNr")
					.setParameter("nr", k.getNr()).getSingleResult();
			kontoToUpdate.setSaldo(kontoToUpdate.getSaldo() + amount);
	}

	@Override
	public void updateKonto(Konto k) {
		// TODO Auto-generated method stub
	}
}