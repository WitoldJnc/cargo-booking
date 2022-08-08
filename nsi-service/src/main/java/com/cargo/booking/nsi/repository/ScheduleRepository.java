package com.cargo.booking.nsi.repository;

import com.cargo.booking.nsi.model.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, UUID> {

    @Modifying
    @Transactional
    @Query("delete from Schedule as s where s.airlineId.id = ?1")
    void deleteAllByAirlineId(UUID airlineId);

    @Query(value = """
            select s.* from nsi.schedule s
                    join nsi.airport d on d.id = s.departure_station and d.city_id = :departure
                    join nsi.airport a on a.id = s.arrival_station and a.city_id = :arrive
            """, nativeQuery = true)
    List<Schedule> findAllByDepartureArriveCities(@Param("departure") UUID departureCityId,
                                                  @Param("arrive") UUID arriveCityId);
}
