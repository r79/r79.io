package zins;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;

@Stateless(mappedName = "ejb/zins", name = "RateBean")
@Interceptors(ch.zli.m223.interceptor.HelloInterceptor.class)
public class ZinsBean implements Zins {
	@Resource(name = "Rate")
	private float rate;

	public double calculate(double amount, double rate, int years) {
		return 0.0;
	}

	public double calculate(double amount, int years) {
		return 0.0;
	}

	public double getRate() {
		return rate;
	}
}
