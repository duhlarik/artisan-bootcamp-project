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
public class MerchantApiController {
    private MerchantRepository repository;

    public MerchantApiController(MerchantRepository repository) {
        this.repository = repository;
    }

    @RequestMapping(method = {RequestMethod.GET})
    public List<Merchant> getAll() {
        return repository.findAll();
    }

    @RequestMapping(path = "/{id}", method = {RequestMethod.GET})
    public ResponseEntity<Merchant> getMerchant(@PathVariable Integer id) {
        return repository.findById(id)
                .map((merchant) -> new ResponseEntity<>(merchant, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
