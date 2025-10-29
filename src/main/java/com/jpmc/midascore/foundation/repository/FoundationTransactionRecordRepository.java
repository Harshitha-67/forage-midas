package com.jpmc.midascore.foundation.repository;

import com.jpmc.midascore.foundation.FoundationTransactionRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FoundationTransactionRecordRepository extends JpaRepository<FoundationTransactionRecord, Long> {
}
