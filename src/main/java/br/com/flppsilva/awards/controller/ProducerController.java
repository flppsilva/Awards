package br.com.flppsilva.awards.controller;

import br.com.flppsilva.awards.dto.AwardsDTO;
import br.com.flppsilva.awards.service.ProducerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProducerController {

    @Autowired
    ProducerService producerService;

    @GetMapping("/reward-interval")
    @Cacheable("rewardInterval")
    public AwardsDTO rewardInterval() {
        return this.producerService.findRewardInterval();
    }
}
