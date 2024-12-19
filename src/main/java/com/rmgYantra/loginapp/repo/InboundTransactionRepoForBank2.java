package com.rmgYantra.loginapp.repo;

import com.rmgYantra.loginapp.model.InboundTransactionForBank2;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InboundTransactionRepoForBank2 extends JpaRepository<InboundTransactionForBank2,String> {
}
