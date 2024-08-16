package com.healsmart.services;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.healsmart.daos.IDoctorVisitDao;
import com.healsmart.daos.IEmployeeDao;
import com.healsmart.daos.IUserDao;
import com.healsmart.daos.IWardDao;
import com.healsmart.dtos.DoctorVisitsDataBackinBean;
import com.healsmart.entities.DoctorVisit;

@Service @Transactional
public class DoctorVisitsServices {
	@Autowired
	IUserDao userDao;
	@Autowired
	IEmployeeDao employeeDao;
	@Autowired
	IWardDao wardDao;
	@Autowired 
	IDoctorVisitDao visitsDao;
	
	public int addVisit(DoctorVisitsDataBackinBean visitData)  {
		return visitsDao.insertIntoDoctorVisitsTable(0, visitData.getPatientId(), visitData.getDoctorId(), 0);
	}
	public void increaseVisitCount(DoctorVisitsDataBackinBean visitData) {
		
		DoctorVisit visit=visitsDao.getVisitsByPatIdAndDoctorId(visitData.getPatientId(),visitData.getDoctorId());
		visit.setVisits(visit.getVisits()+1);
		visitsDao.save(visit);
	}
	
	
	
	

}
