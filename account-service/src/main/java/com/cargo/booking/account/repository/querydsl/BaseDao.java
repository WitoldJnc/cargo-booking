package com.cargo.booking.account.repository.querydsl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;

public abstract class BaseDao {
    JPAQueryFactory queryFactory;

    @Autowired
    protected BaseDao(EntityManager em) {
        queryFactory = new JPAQueryFactory(em);
    }
}
