package com.investmenttracker.creditservice.service;

import com.investmenttracker.creditservice.entity.Credit;
import com.investmenttracker.creditservice.entity.CreditStatus;
import com.investmenttracker.creditservice.exception.CreditAlreadyExistsException;
import com.investmenttracker.creditservice.exception.CreditNotFoundException;
import com.investmenttracker.creditservice.repository.CreditRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreditService {
    private final CreditRepository creditRepository;

   public Credit createCredit(Credit credit) {
       if (creditRepository.existsById(credit.getUserId())) {
           throw new CreditAlreadyExistsException("User already has credit associated with this id");
       }
       credit.setStatus(CreditStatus.ACTIVE);
       return creditRepository.save(credit);
   }

    public Credit getCreditByUserId(Long userId) {
       return creditRepository.findByUserId(userId)
               .orElseThrow(()-> new CreditNotFoundException("Credit for user was not found"));
    }
}

