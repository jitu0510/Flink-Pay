package com.rmgYantra.loginapp.controller;


import com.rmgYantra.loginapp.model.InboundTransactionForBank1;
import com.rmgYantra.loginapp.model.InboundTransactionForBank2;
import com.rmgYantra.loginapp.repo.InboundTransactionRepoForBank1;
import com.rmgYantra.loginapp.repo.InboundTransactionRepoForBank2;
import com.rmgYantra.loginapp.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin
@RequestMapping("/inbound-transaction")
public class InboundTransactionController {

    @Autowired
    private InboundTransactionRepoForBank1 inboundTransactionRepoForBank1;
    @Autowired
    private InboundTransactionRepoForBank2 inboundTransactionRepoForBank2;

    @GetMapping("/count/{username}")
    public long countInboundTransactions(@PathVariable String username) {
        if(username.equals("rmgyantra")){
            return inboundTransactionRepoForBank1.count();
        }else if (username.equals("hdfcmaker")){
            return inboundTransactionRepoForBank2.count();
        }
        return 0;
    }

    @GetMapping("/all/rmgyantra")
    public Page<InboundTransactionForBank1> getAllInboundTransactionsForBank1(Pageable pageable) {
        return inboundTransactionRepoForBank1.findAll(pageable);
    }
    @GetMapping("/all/hdfcmaker")
    public Page<InboundTransactionForBank2> getAllInboundTransactionsForBank2(Pageable pageable) {
        return inboundTransactionRepoForBank2.findAll(pageable);
    }

    @GetMapping("/all-inbounds/{username}")
    public ResponseEntity<?> getAllTransfersWithoutPagination(@PathVariable String username) {
//        Page<MoneyTransfer> transfers = amountTransferRepo.findAll(pageable);
//        List<MoneyTransfer> moneyTransferList = null;
//
//        if(username.equals("rmgyantra")){
//            moneyTransferList = transfers.getContent().stream().filter(t -> t.getSenderAccountNumber().startsWith("1051")).collect(Collectors.toList());
//        }
//        else if(username.equals("hdfcmaker")){
//            moneyTransferList = transfers.getContent().stream().filter(t -> t.getSenderAccountNumber().startsWith("2101")).collect(Collectors.toList());
//        }
//        return new PageImpl<>(moneyTransferList, pageable, moneyTransferList.size());
        List<InboundTransactionForBank1> transactionListForBank1 = null;
        List<InboundTransactionForBank2> transactionListForBank2 = null;

        if(username.equals("rmgyantra")){
            transactionListForBank1 = inboundTransactionRepoForBank1.findAll();
            return new ResponseEntity<>(transactionListForBank1, HttpStatus.OK);
        }
        else if(username.equals("hdfcmaker")){
            transactionListForBank2 = inboundTransactionRepoForBank2.findAll();
            return new ResponseEntity<>(transactionListForBank2, HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
    }
}
