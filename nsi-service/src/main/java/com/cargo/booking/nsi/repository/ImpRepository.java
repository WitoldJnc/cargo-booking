package com.cargo.booking.nsi.repository;

import com.cargo.booking.nsi.model.Imp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ImpRepository extends JpaRepository<Imp, UUID> {
    Optional<Imp> findFirstByCode(String code);

    @Query(value = """
           select * from nsi.imp imp
           where imp.name ->> 'en' ilike concat(:code, '%')
              or imp.name ->> 'ru' ilike concat(:code, '%')
              or imp.code ilike concat (:code, '%');
            """, nativeQuery = true)
    List<Imp> findAllByCodeOrName(String code);
}
