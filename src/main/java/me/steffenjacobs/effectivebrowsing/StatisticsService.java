package me.steffenjacobs.effectivebrowsing;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/** @author Steffen Jacobs */
@Component
public class StatisticsService {

	@PersistenceContext
	EntityManager entityManager;

	@Transactional
	public void executeUpdateQuery(long id) {
		Query query = entityManager.createQuery("Update track t set t.listencount=(t.listencount + 1) where t.id =:id");
		query.setParameter("id", id);
		query.executeUpdate();
	}
}
