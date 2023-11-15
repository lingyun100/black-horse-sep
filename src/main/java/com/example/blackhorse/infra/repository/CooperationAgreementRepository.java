package com.example.blackhorse.infra.repository;

import com.example.blackhorse.infra.repository.entity.CooperationAgreementEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author linyun.xie
 */
@Repository
public interface CooperationAgreementRepository
    extends JpaRepository<CooperationAgreementEntity, String> {}
