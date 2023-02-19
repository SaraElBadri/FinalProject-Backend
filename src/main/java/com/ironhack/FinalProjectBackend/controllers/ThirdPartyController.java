package com.ironhack.FinalProjectBackend.controllers;


import com.ironhack.FinalProjectBackend.services.impl.ThirdPartyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
public class ThirdPartyController {
    @Autowired
    ThirdPartyService thirdPartyService;

    @PostMapping("/transfer-send/{hashedkey}")
    @ResponseStatus(HttpStatus.OK)
    public void sendMoney(@PathVariable String hashedKey, @RequestParam Long accountId,@RequestParam Long secretKey,@RequestParam BigDecimal money){
        thirdPartyService.sendMoney(hashedKey, accountId, secretKey, money);
    }

    @PostMapping ("/transfer-receive/{hashedkey}")
    @ResponseStatus(HttpStatus.OK)
    public void receiveMoney(@PathVariable String hashedKey, @RequestParam Long accountId,@RequestParam Long secretKey,@RequestParam BigDecimal money){
        thirdPartyService.receiveMoney(hashedKey, accountId, secretKey, money);
    }

}
