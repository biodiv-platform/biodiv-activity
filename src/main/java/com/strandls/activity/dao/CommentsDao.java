/**
 * 
 */
package com.strandls.activity.dao;

import javax.inject.Inject;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.strandls.activity.pojo.Comments;
import com.strandls.activity.util.AbstractDAO;

/**
 * @author Abhishek Rudra
 *
 */
public class CommentsDao extends AbstractDAO<Comments, Long> {

	private final Logger logger = LoggerFactory.getLogger(CommentsDao.class);

	/**
	 * @param sessionFactory
	 */
	@Inject
	protected CommentsDao(SessionFactory sessionFactory) {
		super(sessionFactory);
	}

	@Override
	public Comments findById(Long id) {
		Session session = sessionFactory.openSession();
		Comments entity = null;
		try {
			entity = session.get(Comments.class, id);
		} catch (Exception e) {
			logger.error(e.getMessage());
		} finally {
			session.close();
		}
		return entity;
	}

}
