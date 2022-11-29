/**
 * 
 */
package com.strandls.activity.dao;

import javax.inject.Inject;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.strandls.activity.pojo.CcaPermission;
import com.strandls.activity.util.AbstractDAO;
import com.strandls.activity.util.CCARoles;


/**
 * @author Arun
 *
 * 
 */

public class CcaPermissionRequestDao extends AbstractDAO<CcaPermission, Long>{

	private final Logger logger = LoggerFactory.getLogger(CcaPermissionRequestDao.class);
	
	@Inject
	protected CcaPermissionRequestDao(SessionFactory sessionFactory) {
		super(sessionFactory);
	}

	@Override
	public CcaPermission findById(Long id) {
		Session session = sessionFactory.openSession();
		CcaPermission result = null;
		try {
			result = session.get(CcaPermission.class, id);
		} catch (Exception e) {
			logger.error(e.getMessage());
		} finally {
			session.close();
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	public CcaPermission requestPermissionExist(Long requestorId, Long ccaid , CCARoles roles) {
		String qry = "from CcaPermission where requestorId = :requestorId and ccaid = :ccaid  and role = :role";
		Session session = sessionFactory.openSession();
		CcaPermission result = null;
		try {
			Query<CcaPermission> query = session.createQuery(qry);
			query.setParameter("requestorId", requestorId);
			query.setParameter("ccaid", ccaid);
			query.setParameter("role", roles.getValue());
			result = query.getSingleResult();

		} catch (Exception e) {
			logger.error(e.getMessage());
		} finally {
			session.close();
		}
		return result;
	}

}
