package singleton;

import javax.annotation.PostConstruct;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Singleton;

@Singleton
public class HitCounterBean {

	private long hits;

	@PostConstruct
	public void resetCounts() {
		hits = 0;
	}

	public void incCount() {
		hits++;
	}

	@Lock(LockType.READ)
	public long tellHits() {
		return this.hits;
	}
}
