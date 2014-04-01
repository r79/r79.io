package zins;

import javax.ejb.Remote;

@Remote
public interface Zins {
	double calculate(double amount, double rate, int years);

	double calculate(double amount, int years);

	public double getRate();
}
