package com.healsmart.services;
import static com.healsmart.dtos.WardDataBackinBean.*;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.healsmart.custom_exception.NoSuchMedicineExistsException;
import com.healsmart.daos.IDoctorDao;
import com.healsmart.daos.IEmployeeDao;
import com.healsmart.daos.IMedicineAssignedDao;
import com.healsmart.daos.IMedicineDao;
import com.healsmart.daos.IUserDao;
import com.healsmart.daos.IWardDao;
import com.healsmart.dtos.DoctorDataBackinBean;
import com.healsmart.dtos.MedicineAssignedDataBackinBean;
import com.healsmart.dtos.WardDataBackinBean;
import com.healsmart.entities.User;
import com.healsmart.entities.Ward;

@Service @Transactional
public class MedicineAssignedServices {
	@Autowired
	IUserDao userDao;
	@Autowired
	IEmployeeDao employeeDao;
	@Autowired
	IWardDao wardDao;
	@Autowired
	IMedicineAssignedDao medicineAssingedDao;
	@Autowired
	IMedicineDao medicineDao;
	
	public void addMedicineToPatient(MedicineAssignedDataBackinBean medicineData) throws NoSuchMedicineExistsException {
		
			medicineAssingedDao.addIntoMedicineAssigned(medicineData.getPatId(), medicineData.getMedicineId(), medicineData.getMedicinePrescription(), medicineData.getMedicineQty());
		
		
		
	}
	
	public void removeMedicineOfPatient(int medicineAssignId) {
		medicineAssingedDao.deleteById(medicineAssignId);
	}
	
	
	
	

}
