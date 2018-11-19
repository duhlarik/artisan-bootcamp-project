package com.pillar.transaction;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/transaction")
public class TransactionController {

    @RequestMapping(path = "/create", method = {RequestMethod.POST})
    public HttpStatus createTransaction(@RequestBody Transaction transaction) {

        return HttpStatus.OK;
    }

}
