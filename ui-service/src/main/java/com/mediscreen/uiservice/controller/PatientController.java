package com.mediscreen.uiservice.controller;

import com.mediscreen.uiservice.exception.BirthdateException;
import com.mediscreen.uiservice.exception.PatientNotFoundException;
import com.mediscreen.uiservice.model.Patient;
import com.mediscreen.uiservice.proxy.PatientProxy;
import com.mediscreen.uiservice.service.PatientService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@Controller
public class PatientController {

    private static final Logger logger = LoggerFactory.getLogger(PatientController.class);

    private PatientProxy patientProxy;

    private PatientService patientService;

    @Autowired
    public PatientController(PatientProxy patientProxy, PatientService patientService) {
        this.patientProxy = patientProxy;
        this.patientService = patientService;
    }

    @GetMapping("/patient/add")
    public String displayPatientForm(@ModelAttribute("patientUI") Patient patient, Model model) {
        return "patient/add";
    }

    @PostMapping("/patient/add")
    public String createPatient(@Valid @ModelAttribute("patientUI") Patient patient, BindingResult bindingResult, Model model) {
        if (bindingResult.hasFieldErrors()) {
            logger.error("UI-SERVICE: PatientController - Add patient form has errors");
            return "patient/add";
        }
        try {
            patientService.createPatient(patient);
        } catch (BirthdateException e) {
            bindingResult.rejectValue("dob", "BirthDateIsIncorrect", e.getMessage());
            model.addAttribute("patientUI", patient);
            logger.warn("UI-SERVICE: PatientController - The birth date is incorrect.");
            return "patient/add";
        }
        logger.info("UI-SERVICE: PatientController - The patient has been created successfully.");
        return "redirect:/patient/list";
    }
    @GetMapping("/patient/update/{id}")
    public String displayPatientUpdateForm(@PathVariable("id") long id, Model model) {
        Patient patientUpdate = patientProxy.getPatientById(id);
        model.addAttribute("patientUI", patientUpdate);
        logger.info("UI-SERVICE: PatientController - Get update patient form by patient ID");
        return "patient/update";
    }

    @PostMapping("/patient/update/{id}")
    public String updatePatient(@PathVariable("id") long id, @ModelAttribute("patientUI")  @Valid Patient patient, BindingResult bindingResult, Model model) throws BirthdateException {
        if (bindingResult.hasFieldErrors()) {
            model.addAttribute("patientUI", patient);
            logger.error("UI-SERVICE: PatientController - Update patient form has errors");
            return "patient/update";
        }
        try {
            model.addAttribute("patientUI", patient);
            patientService.updatePatient(id, patient);
        } catch (BirthdateException e) {
            model.addAttribute("patientUI", patient);
            bindingResult.rejectValue("dob", "BirthDateIsIncorrect", e.getMessage());
            logger.warn("UI-SERVICE: PatientController - The birth date is incorrect.");
            return "patient/update";
        }
        logger.info("UI-SERVICE: PatientController - The patient has been updated successfully.");
        return "redirect:/patient/list";
    }

    @GetMapping("/patient/list")
    public String getPatientsList(Model model) {
        return paginatedPatients(1,new Patient(), model);
    }

    @GetMapping("/page/{pageNumber}")
    public String paginatedPatients(@PathVariable("pageNumber") Integer pageNumber, Patient patient, Model model) {
        int pageSize = 4;
        Page<Patient> page = patientService.getPaginatedPatientList(pageNumber,pageSize);

        model.addAttribute("currentPage", pageNumber);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("patientsList", page);
        model.addAttribute("patient", patient);
        logger.info("UI-SERVICE: PatientController - Get patients list");
        return "patient/list";
    }

    @PostMapping("/patient/list")
    public String searchPatientByLastName(@Valid String lastName, Patient patient, BindingResult bindingResult, Model model) {
        List<Patient> patientsList = null;
        try {
            patientsList = patientService.getPatientsByLastNameForSearch(lastName);
        } catch (PatientNotFoundException e) {
            bindingResult.rejectValue("lastName", "PatientWasNotFoundBySuchALastName", e.getMessage());
            logger.warn("UI-SERVICE: PatientController - Search patient by last name. Patient not found.");
            return "patient/list";
        }
        logger.warn("UI-SERVICE: PatientController - Search patient by last name. The search is successful.");
        model.addAttribute("patientsList", patientsList);
        model.addAttribute("patient", patient);
        System.out.println(patientsList);
        return "patient/list";
    }

    @PostMapping("/page/{pageNumber}")
    public String searchPatientByLastNameWithPageNumber(@Valid String lastName, Patient patient, BindingResult bindingResult, @PathVariable("pageNumber") int pageNumber,Model model) {
        List<Patient> patientsList = null;
        int pageSize = 5;
        try {
            patientsList = patientService.getPatientsByLastNameForSearch(lastName);
        } catch (PatientNotFoundException e) {
            bindingResult.rejectValue("lastName", "PatientWasNotFoundBySuchALastName", e.getMessage());
            logger.warn("UI-SERVICE: PatientController - Search patient by last name. Patient not found.");
            return "patient/list";
        }
        Page<Patient> page = patientService.getPaginatedPatientList(pageNumber,pageSize);
        model.addAttribute("currentPage", pageNumber);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("patientsList", page);
        model.addAttribute("patientsList", patientsList);
        model.addAttribute("patient", patient);
        logger.warn("UI-SERVICE: PatientController - Search patient by last name. The search is successful.");
        System.out.println(patientsList);
        return "patient/list";
    }

    @GetMapping("/patient/delete/{id}")
    public String deletePatient(@PathVariable long id, HttpServletRequest request) {
        patientProxy.deletePatient(id);
        String referer = request.getHeader("Referer");
        logger.info("UI-SERVICE: PatientController - Delete patient by patient ID");
        return "redirect:" + referer;
    }
}
