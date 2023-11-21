package com.mediscreen.uiservice.controller;

import com.mediscreen.uiservice.model.Note;
import com.mediscreen.uiservice.proxy.HistoryProxy;
import com.mediscreen.uiservice.service.IPatientService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
public class NoteController {

    private static final Logger logger = LoggerFactory.getLogger(NoteController.class);
    HistoryProxy historyProxy;
    IPatientService patientService;

    @Autowired
    public NoteController(HistoryProxy historyProxy, IPatientService patientService) {
        this.historyProxy = historyProxy;
        this.patientService = patientService;
    }

    @GetMapping("/patHistory/notes/{patientId}")
    public String showNotesList(@ModelAttribute("noteUi") Note note, @PathVariable("patientId") long patientId, Model model) {
        model.addAttribute("notesPatient", historyProxy.findListOfNotesByPatientId(patientId));

        model.addAttribute("patientFirstName", patientService.getPatientById(patientId).getFirstName());
        model.addAttribute("patientLastName", patientService.getPatientById(patientId).getLastName());
        logger.info("UI-SERVICE: NoteController - Get notes list by patient ID");
        return "note/list";
    }

    @GetMapping("/patHistory/notes/delete/{id}/{patientId}")
    public String deleteNote(@PathVariable("id") String id, @PathVariable("patientId") long patientId) {
        historyProxy.delete(id);
        logger.info("UI-SERVICE: NoteController - Delete note by note ID");
        return "redirect:/patHistory/notes/" + patientId;
        //return "redirect:/patient/list";
    }

    @GetMapping("/patHistory/addNotes/{patientId}")
    public String showAddNotesForm(@ModelAttribute("noteUi") Note note, @PathVariable("patientId") long patientId, Model model) {
        model.addAttribute("noteUi", note);
        model.addAttribute("patientFirstName", patientService.getPatientById(patientId).getFirstName());
        model.addAttribute("patientLastName", patientService.getPatientById(patientId).getLastName());
        logger.info("UI-SERVICE: NoteController - Get add note form by patient ID");
        return "note/add";
    }

    @PostMapping("/patHistory/addNotes/{patientId}")
    public String createNote(@Valid @ModelAttribute("noteUi") Note note, BindingResult bindingResult, @PathVariable("patientId") int patientId, Model model) {
        if (bindingResult.hasFieldErrors()) {
            model.addAttribute("noteUi", note);
            logger.error("UI-SERVICE: NoteController - Add note form has errors");
            return "note/add";
        }
        historyProxy.addNote(note);
        model.addAttribute("noteUi", note);
        logger.info("UI-SERVICE: NoteController - The note has been created successfully.");
        return "redirect:/patHistory/notes/" + patientId;
    }

    @GetMapping("/patHistory/updateNote/{id}/{patientId}")
    public String displayNoteUpdateForm(@ModelAttribute("noteUi")Note note, @PathVariable("patientId") int patientId, @PathVariable("id") String id, Model model) {
        Note noteById = historyProxy.getNoteById(note.getId());
        model.addAttribute("noteUi", noteById);
        logger.info("UI-SERVICE: NoteController - Get note update form by note ID");
        return "note/update";
    }

    @PostMapping("/patHistory/updateNote/{id}/{patientId}")
    public String updateNote(@PathVariable("id") String id, @Valid @ModelAttribute("noteUi") Note note, BindingResult bindingResult, @PathVariable("patientId") long patientId, Model model) {
        if (bindingResult.hasFieldErrors()) {
            model.addAttribute("noteUi", note);
            model.addAttribute("patientId", patientId);
            logger.error("UI-SERVICE: NoteController - Update note form has errors");
            return "note/update";
        }
        model.addAttribute("noteUi", note);
        model.addAttribute("patientId", patientId);
        historyProxy.updateNote(id, note);
        logger.info("UI-SERVICE: NoteController - The note has been updated successfully.");
        return "redirect:/patHistory/notes/" + patientId;
    }
}
