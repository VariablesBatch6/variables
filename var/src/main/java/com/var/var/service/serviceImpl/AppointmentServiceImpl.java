package com.var.var.service.serviceImpl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.var.var.dao.AppointmentDao;
import com.var.var.model.Appointment;
import com.var.var.service.AppointmentService;


@Service
public class AppointmentServiceImpl implements AppointmentService {

    @Autowired
    private AppointmentDao appointmentDao;

    public Appointment createAppointment(Appointment appointment) {
       return appointmentDao.save(appointment);
    }

    public List<Appointment> findAll() {
        return appointmentDao.findAll();
    }

    public Optional<Appointment> findAppointment(Long id) {
        return appointmentDao.findById(id);
    }

    public void confirmAppointment(Long id) {
        Optional<Appointment> appointment = findAppointment(id);
        if(appointment.isPresent()) {
        	appointment.get().setConfirmed(true);
        	appointmentDao.save(appointment.get());
        }
        
    }
}
