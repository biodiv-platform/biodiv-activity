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

import com.strandls.activity.pojo.CCAPermission;
import com.strandls.activity.util.AbstractDAO;
import com.strandls.activity.util.CCARoles;


/**
 * @author Arun
 *
 * 
 */

public class CcaPermissionRequestDao extends AbstractDAO<CCAPermission, Long>{

	private final Logger logger = LoggerFactory.getLogger(CcaPermissionRequestDao.class);
	
	@Inject
	protected CcaPermissionRequestDao(SessionFactory sessionFactory) {
		super(sessionFactory);
	}

	@Override
	public CCAPermission findById(Long id) {
		Session session = sessionFactory.openSession();
		CCAPermission result = null;
		try {
			result = session.get(CCAPermission.class, id);
		} catch (Exception e) {
			logger.error(e.getMessage());
		} finally {
			session.close();
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	public CCAPermission requestPermissionExist(Long requestorId, Long ccaId, CCARoles roles) {
		String qry = "from SpeciesPermissionRequest where requestorId = :requestorId and ccaId = :ccaId and role = :role ";
		Session session = sessionFactory.openSession();
		CCAPermission result = null;
		try {
			Query<CCAPermission> query = session.createQuery(qry);
			query.setParameter("requestorId", requestorId);
			query.setParameter("ccaId", ccaId);
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
