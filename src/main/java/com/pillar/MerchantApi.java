package com.pillar;

import com.pillar.merchant.Merchant;
import com.pillar.merchant.MerchantRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/merchant")
public class MerchantApi {
    private MerchantRepository repository;

    public MerchantApi(MerchantRepository repository) {
        this.repository = repository;
    }

    @RequestMapping(method = {RequestMethod.GET})
    public List<Merchant> getAll() {
        return repository.findAll();
    }

    @RequestMapping(path = "/{id}", method = {RequestMethod.GET})
    public ResponseEntity<Merchant> getMerchant(@PathVariable Integer id) {
        Merchant merchant = repository.getOne(id);
        if (merchant != null) {
            return new ResponseEntity<>(merchant, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
