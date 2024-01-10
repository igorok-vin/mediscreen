package com.mediscreen.reportservice.proxy;

import com.mediscreen.reportservice.model.Note;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name="${history-service-name}",url = "${history-service-url}")
public interface HistoryProxy {

    @GetMapping("/patHistory/patient/{patientId}")
    List<Note> findListOfNotesByPatientId(@PathVariable("patientId") long patientId);

    @PostMapping( "/patHistory/add")
    Note addNote(@RequestBody Note note);

    @PostMapping("/patHistory/update/{id}")
    Note updateNote(@PathVariable("id") String is, @RequestBody Note note);

    @GetMapping("/patHistory/delete/{id}")
    void delete(@PathVariable String id);

    @GetMapping("/patHistory/note/{id}")
    Note getNoteById(@PathVariable("id") String id);
}
