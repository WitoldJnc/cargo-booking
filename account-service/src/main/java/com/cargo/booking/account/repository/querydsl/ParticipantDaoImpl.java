package com.cargo.booking.account.repository.querydsl;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.cargo.booking.account.model.*;
import com.cargo.booking.account.repository.ParticipantDao;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;

@Repository
public class ParticipantDaoImpl extends BaseDao implements ParticipantDao {

    private final QParticipant participant = new QParticipant("participant");
    private final QParticipantUser participantUser = new QParticipantUser("participantUser");
    private final QCompany company = new QCompany("company");
    private final QWorkspaceRole workspaceRole = new QWorkspaceRole("workspaceRole");
    private final QWorkspace workspace = new QWorkspace("workspace");

    public ParticipantDaoImpl(EntityManager em) {
        super(em);
    }

    @Override
    public Page<Participant> search(String query, ParticipantStatus participantStatus,
                                    ParticipantType participantType, Pageable pageable) {
        BooleanExpression predicate = participant.type.eq(participantType);
        if (participantStatus != null) {
            predicate = predicate.and(participant.status.eq(participantStatus));
        }
        if (StringUtils.hasLength(query) && participantType == ParticipantType.COMPANY) {
            predicate = predicate.and((company.shortName.containsIgnoreCase(query))
                    .or(company.inn.containsIgnoreCase(query)));
        }
        JPAQuery<Participant> jpaQuery = queryFactory.selectFrom(participant)
                .leftJoin(participant.company, company)
                .leftJoin(participant.participantUsers, participantUser)
                .leftJoin(participantUser.workspaceRoles, workspaceRole)
                .leftJoin(workspaceRole.workspace, workspace)
                .where(predicate);

        long total = jpaQuery.fetch().stream().distinct().count();
        jpaQuery.distinct().limit(pageable.getPageSize()).offset(pageable.getOffset());
        return new PageImpl<>(jpaQuery.fetch(), pageable, total);
    }
}
