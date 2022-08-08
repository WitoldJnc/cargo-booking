package com.cargo.booking.nsi.repository;

import com.cargo.booking.nsi.model.Rate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Repository
public interface RateRepository extends JpaRepository<Rate, UUID> {

    List<Rate> findAllByParticipantIdOrderByDtUpload(UUID participantId);

    @Modifying
    @Transactional
    @Query("update Rate as r set r.isActive = false where r.participantId = ?1")
    void markOldRecordsAsInactive(UUID participantId);

    @Query(value = """
            select r.* from nsi.rate r
                     join nsi.airport a on a.id = r.arrival_id and a.city_id = :arrive
                     join nsi.airport d on d.id = r.departure_id and d.city_id = :departure
            where r.is_active
                        """, nativeQuery = true)
    List<Rate> findAllActiveByArriveAndDepartureCities(@Param("departure") UUID departureCityId,
                                                       @Param("arrive") UUID arriveCityIdl);

}