package com.healsmart.services;
import static com.healsmart.dtos.MedicineAssignedDataBackinBean.createAllMedicineList;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.healsmart.daos.IEmployeeDao;
import com.healsmart.daos.IMedicineAssignedDao;
import com.healsmart.daos.IMedicineDao;
import com.healsmart.daos.IUserDao;
import com.healsmart.daos.IWardDao;
import com.healsmart.dtos.MedicineAssignedDataBackinBean;
import com.healsmart.entities.Medicine;

@Service @Transactional
public class MedicineServices {
	@Autowired
	IUserDao userDao;
	@Autowired
	IEmployeeDao employeeDao;
	@Autowired
	IWardDao wardDao;
	@Autowired
	IMedicineDao medicineDao;
	@Autowired
	IMedicineAssignedDao medicineAssingedDao;
	
	public List<MedicineAssignedDataBackinBean> getAllMedicines(){
		List<Medicine> medicine=medicineDao.findAll();
		List<MedicineAssignedDataBackinBean> medicinesTosend=createAllMedicineList(medicine);
		return medicinesTosend;
		
	}

	public int addMedicine(MedicineAssignedDataBackinBean medicineData) {
		return  medicineDao.insertIntoMedicineTable(0, medicineData.getMedicineName(), medicineData.getMedicinePrice());
		
	}

	public void removeMedicine(int medicineId) {
		medicineDao.deleteById(medicineId);
		
	}
	
	
	
	
	

}
