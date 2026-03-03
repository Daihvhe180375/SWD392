package com.apartment.repository;

import com.apartment.model.Annoucement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnnoucementRepository extends JpaRepository<Annoucement, Integer> {
}
