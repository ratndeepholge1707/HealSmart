package com.healsmart.services;
import static com.healsmart.dtos.ChargesCalculationBeanPatient.calculateCharges;
import static com.healsmart.dtos.MedicineAssignedDataBackinBean.createMedicineListForPatient;
import static com.healsmart.dtos.PatientDataBacking.createPatient;
import static com.healsmart.dtos.PatientDataBacking.createPatientsOfDoctor;
import static com.healsmart.dtos.PatientDataBacking.getByIdPatient;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.healsmart.custom_exception.NoSuchPatientFoundException;
import com.healsmart.custom_exception.PatientAlreadyExistsException;
import com.healsmart.daos.IDoctorDao;
import com.healsmart.daos.IDoctorVisitDao;
import com.healsmart.daos.IEmployeeDao;
import com.healsmart.daos.IPatientDao;
import com.healsmart.daos.IUserDao;
import com.healsmart.daos.IWardDao;
import com.healsmart.dtos.ChargesCalculationBeanPatient;
import com.healsmart.dtos.MedicineAssignedDataBackinBean;
import com.healsmart.dtos.PatientDataBacking;
import com.healsmart.entities.Doctor;
import com.healsmart.entities.DoctorVisit;
import com.healsmart.entities.MedicineAssigned;
import com.healsmart.entities.Patient;
import com.healsmart.entities.User;
import com.healsmart.entities.Ward;

@Service @Transactional
public class PatientServices {
	@Autowired
	IUserDao userDao;
	@Autowired
	IEmployeeDao employeeDao;
	@Autowired
	IPatientDao patientDao;
	@Autowired
	IDoctorDao doctorDao;
	@Autowired
	IDoctorVisitDao visitsDao;
	
	@Autowired
	IWardDao wardDao;
	
	public int addPatient(PatientDataBacking patientDetails) throws PatientAlreadyExistsException {
		if(!userDao.existsByEmail(patientDetails.getEmail())) {
			userDao.insertIntoUsers(0, patientDetails.getFirstName(), patientDetails.getLastName(), patientDetails.getEmail(), patientDetails.getPassword(), patientDetails.getCellNo(), patientDetails.getRole(), patientDetails.getSecurityQuestion(), patientDetails.getSecurityAnswer());
			User user=userDao.findByEmail(patientDetails.getEmail());
			int updateCount=patientDao.insertIntoPatients(0, user.getId(),patientDetails.getDoctorId() , patientDetails.getWardId(), patientDetails.getDateOfAdmission(), patientDetails.getBloodGroup(), patientDetails.getDob(), patientDetails.getPrescription(), patientDetails.getBedAlloted(), patientDetails.getPaymentStatus(), patientDetails.getPatientProblem());
			Patient patient=patientDao.findByUserId(user.getId());
			visitsDao.insertIntoDoctorVisitsTable(0, patient.getId(), patientDetails.getDoctorId(),0);
			return updateCount;
		}else {
			throw new PatientAlreadyExistsException("patient with email = "+patientDetails.getEmail()+" exists!!!");
			
		}
		
		
	}
	
	public List<PatientDataBacking> getAllPatients(){
		List<Patient> patients=patientDao.findAll();
		List<PatientDataBacking> patientList=createPatient(patients);
		return patientList;
		
	}
	
	public PatientDataBacking getPatientById(int id) throws NoSuchPatientFoundException {
		if(patientDao.existsById(id)) {
			Patient patient=patientDao.getById(id);
			PatientDataBacking patientDetailsToSend=getByIdPatient(patient);
			return patientDetailsToSend;
		}else {
			throw new NoSuchPatientFoundException("patient with id = "+id+" does not exists!!!");
		}
		
		
				
	}
	//get patients medicines by patient id
	public List<MedicineAssignedDataBackinBean> getMedicineByPatId(int id) {
		Patient patient=patientDao.getById(id);
		List<MedicineAssigned> medicines=patient.getMedicines();
		List<MedicineAssignedDataBackinBean> medicineToSend=createMedicineListForPatient(medicines);
		return medicineToSend;
		
				
	}
	
	//update patient details
	public void updatePatientDetails(PatientDataBacking patientDetails) throws NoSuchPatientFoundException {
		
		if(patientDao.existsById(patientDetails.getPatId())) {
			Patient updatePatient = patientDao.getById(patientDetails.getPatId());
			//to add to visit table
			
				DoctorVisit initVisit=visitsDao.getVisitsByPatIdAndDoctorId(patientDetails.getPatId(), patientDetails.getDoctorId());
				System.out.println("------------------>initvisit"+initVisit);
				if(initVisit==null) {
					visitsDao.insertIntoDoctorVisitsTable(0, patientDetails.getPatId(), patientDetails.getDoctorId(), 0);
				}
			Ward updateWard=wardDao.getById(patientDetails.getWardId());//got corrusponding ward by ward Id
			updateWard.addPatient(updatePatient);//added patient in ward list
			updatePatient.setWard(updateWard);//new ward set in patient
			//======================================================================
			
			Doctor updatedDoctor=doctorDao.getById(patientDetails.getDoctorId());//got new doctor by id 
			
			
			
			updatedDoctor.addPatient(updatePatient);//patient added in doctor list 
			updatePatient.setDoctor(updatedDoctor);//doctor added to patient list
			
			updatePatient.getUser().setFirstName(patientDetails.getFirstName());
			updatePatient.getUser().setLastName(patientDetails.getLastName());
			updatePatient.getUser().setCellNo(patientDetails.getCellNo());
			
			updatePatient.setDob(patientDetails.getDob());
			updatePatient.setBedAlloted(patientDetails.getBedAlloted());
			updatePatient.setBloodGroup(patientDetails.getBloodGroup());
			//=======================================
			//update visits
			Patient savedPatient = patientDao.save(updatePatient);
			
			
		}else {
			throw new NoSuchPatientFoundException("patient with id = "+patientDetails.getPatId()+" does not exists!!!");
		}
		
	}
	
	public int removePatientById(int id) throws NoSuchPatientFoundException {
		if(patientDao.existsById(id)) {
			patientDao.deleteById(id);
			return 1;
		}else {
			throw new NoSuchPatientFoundException("patient with id = "+id+" does not exists!!!");
		}
		
	}
	
	public ChargesCalculationBeanPatient calculateChargesByPatId(int patId) {
		ChargesCalculationBeanPatient patientCharges=new ChargesCalculationBeanPatient();
		int daysStayed = patientDao.calculateDaysOfStayOfPatient(patId);
		Patient patient=patientDao.getById(patId);
		patientCharges=calculateCharges(patient, daysStayed);
		return patientCharges;
	}
	
	public void updatePaymentStatusByPatId(PatientDataBacking patientData) throws NoSuchPatientFoundException {
		if(patientDao.existsById(patientData.getPatId())) {
			Patient patient=patientDao.getById(patientData.getPatId());
			patient.setPaymentStatus(patientData.getPaymentStatus());
			patientDao.save(patient);
		}else {
			throw new NoSuchPatientFoundException("patient with id = "+patientData.getPatId()+" does not exists!!!");
		}
		
	}
	//check if bedalloted exits
	public Boolean checkIfBedAvailable(PatientDataBacking bedData) {
		
		return patientDao.existsByBedAllotedAndWardId(bedData.getBedAlloted(),bedData.getWardId()); 
	}

	public List<PatientDataBacking> getPatientsOfDocter(int id) {
		List<Patient> patients=patientDao.findAll();
		List<PatientDataBacking> patientList=createPatientsOfDoctor(patients,id);
		return patientList;
		
	}
	
	
	
}
