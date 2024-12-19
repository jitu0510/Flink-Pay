package com.rmgYantra.loginapp.repo;

import com.rmgYantra.loginapp.model.InboundTransactionForBank1;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InboundTransactionRepoForBank1 extends JpaRepository<InboundTransactionForBank1,String> {
}
