package me.steffenjacobs.effectivebrowsing;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import me.steffenjacobs.effectivebrowsing.domain.orm.TrackInfo;

/** @author Steffen Jacobs */

@Component
public class SearchService {

	@PersistenceContext
	EntityManager entityManager;

	@SuppressWarnings("unchecked")
	@Transactional
	public List<TrackInfo> search(String searchTerms) {

		int paramCount = 0;

		String[] arr = searchTerms.split(" ");

		StringBuilder queryString = new StringBuilder();
		queryString.append("SELECT DISTINCT d FROM ");
		queryString.append("track");
		queryString.append(" d ");
		queryString.append("WHERE ");
		queryString.append("(d.title <> '' AND (");
		queryString.append("UPPER(d.title) LIKE UPPER(CONCAT('%',:param" + paramCount++ + ",'%')) OR UPPER(:param" + paramCount++ + ") LIKE UPPER(CONCAT('%',d.title,'%')) ");
		for (int i = 1; i < arr.length; i++) {
			queryString
					.append("OR UPPER(d.title) LIKE UPPER(CONCAT('%',:param" + paramCount++ + ",'%')) OR UPPER(:param" + paramCount++ + ") LIKE UPPER(CONCAT('%',d.title,'%')) ");
		}

		queryString.append(")) OR (d.artist <> '' AND (");
		queryString.append("UPPER(d.artist) LIKE UPPER(CONCAT('%',:param" + paramCount++ + ",'%')) OR UPPER(:param" + paramCount++ + ") LIKE UPPER(CONCAT('%',d.artist,'%')) ");
		for (int i = 1; i < arr.length; i++) {
			queryString
					.append("OR UPPER(d.artist) LIKE UPPER(CONCAT('%',:param" + paramCount++ + ",'%')) OR UPPER(:param" + paramCount++ + ") LIKE UPPER(CONCAT('%',d.artist,'%')) ");
		}

		queryString.append(")) OR (d.album <> '' AND (");
		queryString.append("UPPER(d.album) LIKE UPPER(CONCAT('%',:param" + paramCount++ + ",'%')) OR UPPER(:param" + paramCount++ + ") LIKE UPPER(CONCAT('%',d.album,'%')) ");
		for (int i = 1; i < arr.length; i++) {
			queryString
					.append("OR UPPER(d.album) LIKE UPPER(CONCAT('%',:param" + paramCount++ + ",'%')) OR UPPER(:param" + paramCount++ + ") LIKE UPPER(CONCAT('%',d.album,'%')) ");
		}
		queryString.append("))");

		Query q = entityManager.createQuery(queryString.toString(), TrackInfo.class);

		// 3 loops
		for (int k = 0; k < 3; k++) {
			// each array element per loop
			for (int i = 0; i < arr.length; i++) {
				// arr.length * 2 * k -> offset for second loop
				// i * 2 -> offset 2 times per line
				// j -> offset for each line

				// each array element twice per loop
				for (int j = 0; j < 2; j++) {
					int position = arr.length * 2 * k + i * 2 + j;
					q.setParameter("param" + position, arr[i]);
				}
			}
		}
		return q.getResultList();
	}
}
