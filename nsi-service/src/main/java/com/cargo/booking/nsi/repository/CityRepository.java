package com.cargo.booking.nsi.repository;

import com.cargo.booking.nsi.model.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface CityRepository extends JpaRepository<City, UUID> {

    @Query(value = """
            select * from nsi.city ci
                     where ci.name ->> 'en' ilike concat(:name, '%')
                        or ci.name ->> 'ru' ilike concat(:name, '%');
            """, nativeQuery = true)
    List<City> findAllByNameLikeIgnoreCase(@Param("name") String cityName);
}
