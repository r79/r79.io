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
		return amount * Math.pow((rate / 100.0) + 1.0, (double)years); 
	}

	public double calculate(double amount, int years) {
		return amount * Math.pow((0.5 / 100.0) + 1.0, (double) years);
	}

	public double getRate() {
		return rate;
	}
}
