package com.rmgYantra.loginapp.repo;

import com.rmgYantra.loginapp.model.MoneyTransfer;
import com.rmgYantra.loginapp.model.MoneyTransferForBank1;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AmountTransferRepoForBank1 extends JpaRepository<MoneyTransferForBank1,String> {

//    @Query("SELECT t FROM MoneyTransfer t WHERE " +
//            "(:username = 'rmgyantra' AND t.senderAccountNumber LIKE '1051%') OR " +
//            "(:username = 'hdfcmaker' AND t.senderAccountNumber LIKE '2101%')")
//    Page<MoneyTransfer> findFilteredTransfers(@Param("username") String username, Pageable pageable);
    MoneyTransferForBank1 findByReferenceNumber(String referenceNumber);
}
