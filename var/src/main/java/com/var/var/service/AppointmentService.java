package com.var.var.service;

import java.util.List;

import com.var.var.model.Appointment;


public interface AppointmentService {
    
	Appointment createAppointment(Appointment appointment);

    List<Appointment> findAll();

//    Appointment findAppointment(Long id);
//
    void confirmAppointment(Long id);
}
