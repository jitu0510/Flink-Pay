package com.rmgYantra.loginapp.repo;

import com.rmgYantra.loginapp.model.MoneyTransfer;
import com.rmgYantra.loginapp.model.MoneyTransferForBank1;
import com.rmgYantra.loginapp.model.MoneyTransferForBank2;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AmountTransferRepoForBank2 extends JpaRepository<MoneyTransferForBank2,String> {

    //    @Query("SELECT t FROM MoneyTransfer t WHERE " +
//            "(:username = 'rmgyantra' AND t.senderAccountNumber LIKE '1051%') OR " +
//            "(:username = 'hdfcmaker' AND t.senderAccountNumber LIKE '2101%')")
//    Page<MoneyTransfer> findFilteredTransfers(@Param("username") String username, Pageable pageable);
    MoneyTransferForBank2 findByReferenceNumber(String referenceNumber);
}