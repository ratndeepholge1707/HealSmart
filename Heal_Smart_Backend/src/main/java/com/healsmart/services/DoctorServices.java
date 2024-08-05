package com.healsmart.services;
import static com.healsmart.dtos.DoctorDataBackinBean.createDoctorsList;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.healsmart.custom_exception.NoSuchPatientFoundException;
import com.healsmart.daos.IDoctorDao;
import com.healsmart.daos.IEmployeeDao;
import com.healsmart.daos.IPatientDao;
import com.healsmart.daos.IUserDao;
import com.healsmart.dtos.DoctorDataBackinBean;
import com.healsmart.dtos.PatientDataBacking;
import com.healsmart.entities.Doctor;

@Service @Transactional
public class DoctorServices {
	@Autowired
	IUserDao userDao;
	@Autowired
	IEmployeeDao employeeDao;
	@Autowired
	IDoctorDao doctorDao;
	@Autowired
	IPatientDao patientDao;
	
	public List<DoctorDataBackinBean> getAllDoctors() {
		List<Doctor> doctors=doctorDao.findAll();
		List<DoctorDataBackinBean> doctorDetail=createDoctorsList(doctors);
		
		return doctorDetail;
		
	}

	public void updatePatientDetails(PatientDataBacking patientData) throws NoSuchPatientFoundException  {
		int updateCount;
		if(patientDao.existsById(patientData.getPatId()))
		 updateCount=patientDao.updatePatientPrescription(patientData.getPrescription(),patientData.getPatId());
		else
		throw new NoSuchPatientFoundException("patient  with id "+patientData.getPatId()+" does not exists");
	}
	
	

}
