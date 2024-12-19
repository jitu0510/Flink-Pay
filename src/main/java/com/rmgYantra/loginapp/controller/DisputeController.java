package com.rmgYantra.loginapp.controller;

import com.rmgYantra.loginapp.model.DisputeForBank1;
import com.rmgYantra.loginapp.model.DisputeForBank2;
import com.rmgYantra.loginapp.model.MoneyTransferForBank1;
import com.rmgYantra.loginapp.model.MoneyTransferForBank2;
import com.rmgYantra.loginapp.repo.DisputeRepositoryForBank1;
import com.rmgYantra.loginapp.repo.DisputeRepositoryForBank2;
import com.rmgYantra.loginapp.service.DisputeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/dispute")
public class DisputeController {

    @Autowired
    private DisputeService disputeService;

    @Autowired
    private DisputeRepositoryForBank1 disputeRepositoryForBank1;

    @Autowired
    private DisputeRepositoryForBank2 disputeRepositoryForBank2;

    @PostMapping("/raise/{username}")
    public ResponseEntity<?> raiseDefect(@RequestParam String referenceNumber,@RequestParam("description") String disputeDescription,@PathVariable String username){
        return disputeService.raiseDefect(referenceNumber,disputeDescription,username);
    }

    @GetMapping("/count/{username}")
    public long countDisputes(@PathVariable String username) {
        if(username.equals("rmgyantra")){
            return disputeRepositoryForBank1.count();
        }
        else if(username.equals("hdfcmaker")){
            return disputeRepositoryForBank2.count();
        }
        return 0;
    }

    @GetMapping("/all/rmgyantra")
    public Page<DisputeForBank1> getAllDisputesForBank1(Pageable pageable) {
        return disputeRepositoryForBank1.findAll(pageable);
    }

    @GetMapping("/all/hdfcmaker")
    public Page<DisputeForBank2> getAllDisputesForBank2(Pageable pageable) {
        return disputeRepositoryForBank2.findAll(pageable);
    }
}
