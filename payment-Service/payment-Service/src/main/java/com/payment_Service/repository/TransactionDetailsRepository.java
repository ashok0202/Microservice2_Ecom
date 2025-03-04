package com.payment_Service.repository;

import com.payment_Service.entity.TransactionalDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionDetailsRepository extends JpaRepository<TransactionalDetails, Long> {

    TransactionalDetails findByOrderId(long orderId);
}
