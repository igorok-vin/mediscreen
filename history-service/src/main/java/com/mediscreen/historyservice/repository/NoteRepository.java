package com.mediscreen.historyservice.repository;

import com.mediscreen.historyservice.model.Note;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoteRepository extends MongoRepository<Note, String> {
    List<Note> findAllByPatientIdOrderByDateDesc(long patientId);
    List<Note> findByPatientId(long id);



}
