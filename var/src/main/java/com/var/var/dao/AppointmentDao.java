package com.var.var.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.var.var.model.Appointment;


public interface AppointmentDao extends JpaRepository<Appointment, Long> {

    List<Appointment> findAll();
//    Appointment findById(Long id);

//	Appointment findOne(Long id);
    
}
