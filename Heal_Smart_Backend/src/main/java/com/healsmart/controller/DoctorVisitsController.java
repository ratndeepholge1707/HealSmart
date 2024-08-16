package com.healsmart.controller;

import javax.annotation.security.RolesAllowed;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.healsmart.dtos.DoctorVisitsDataBackinBean;
import com.healsmart.dtos.Response;
import com.healsmart.services.DoctorVisitsServices;

@CrossOrigin("*")
@RestController@RolesAllowed("ROLE_DOCTOR")
@RequestMapping("/api/doctorVisit")
public class DoctorVisitsController {
	@Autowired
	DoctorVisitsServices visitServices;



	@PostMapping("/addVisit")
	public ResponseEntity<?> addWard(@RequestBody DoctorVisitsDataBackinBean visitData) {
		int updateCount = visitServices.addVisit(visitData);
		if (updateCount == 1)
			return Response.success("VISIT_ADDED");
		return Response.success("FAILED");
	}

	

}
