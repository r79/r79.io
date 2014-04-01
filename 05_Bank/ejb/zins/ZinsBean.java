package zins;

import javax.annotation.Resource;
import javax.ejb.Stateless;

@Stateless
public class ZinsBean {
	@Resource(name = "Rate")
	private float rate = 0f;

	public double calculate(double amount, double rate, int years) {
		return amount * Math.pow((rate / 100.0) + 1.0, (double) years);
	}

	public double calculate(double amount, int years) {
		return amount * Math.pow((rate / 100.0) + 1.0, (double) years);
	}

	public float getRate() {
		return rate;
	}
}
