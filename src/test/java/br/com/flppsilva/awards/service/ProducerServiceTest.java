package br.com.flppsilva.awards.service;

import br.com.flppsilva.awards.dto.RangeAwardsDTO;
import br.com.flppsilva.awards.model.Movie;
import br.com.flppsilva.awards.model.Producer;
import br.com.flppsilva.awards.repository.ProducerRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class ProducerServiceTest {

    @Mock
    private ProducerRepository producerRepository;

    @Autowired
    @InjectMocks
    private ProducerService producerService;

    private List<Producer> producers;

    @BeforeEach
    void setup() {
        producers = new ArrayList<>(2);
        producers.add(new Producer()
                .setName("Producer 1")
                .setMovies(
                        List.of(
                                new Movie().setYear(2000).setWinner(true),
                                new Movie().setYear(2001).setWinner(false),
                                new Movie().setYear(2002).setWinner(true),
                                new Movie().setYear(2005).setWinner(true)
                        )));
        producers.add(new Producer()
                .setName("Producer 2")
                .setMovies(
                        List.of(
                                new Movie().setYear(2000).setWinner(true),
                                new Movie().setYear(2005).setWinner(true)
                        )));
    }

    @Test
    void shouldReturnMin() {
        Mockito.when(producerRepository.findByMoviesWinners()).thenReturn(producers);
        final List<RangeAwardsDTO> min = producerService.findRewardInterval().min();
        Assertions.assertEquals(1, min.size());

        final RangeAwardsDTO rangeAwards = min.get(0);
        Assertions.assertEquals("Producer 1", rangeAwards.getProducer());
        Assertions.assertEquals(2, rangeAwards.getInterval());
        Assertions.assertEquals(2000, rangeAwards.getPreviousWin());
        Assertions.assertEquals(2002, rangeAwards.getFollowingWin());
    }

    @Test
    void shouldReturnMax() {
        Mockito.when(producerRepository.findByMoviesWinners()).thenReturn(producers);
        final List<RangeAwardsDTO> max = producerService.findRewardInterval().max();
        Assertions.assertEquals(2, max.size());

        Assertions.assertTrue(max.stream().map(RangeAwardsDTO::getProducer).toList().containsAll(List.of("Producer 1", "Producer 2")));
    }

    @Test
    void shouldReturnEmptyWhenEmptyList() {
        Mockito.when(producerRepository.findByMoviesWinners()).thenReturn(Collections.emptyList());
        final List<RangeAwardsDTO> max = producerService.findRewardInterval().max();
        Assertions.assertEquals(0, max.size());
    }

    @Test
    void shouldReceiveNullWhenFewItems() {
        final Integer[] years = {1};
        Assertions.assertNull(ProducerService.getShorterRange(years));
        Assertions.assertNull(ProducerService.getLongerRange(years));
    }

    @Test
    void shouldGetSmallestInterval() {
        final Integer[] years = {1, 3, 9, 11};
        Assertions.assertEquals(Pair.of(1, 3), ProducerService.getShorterRange(years));
    }

    @Test
    void shouldGetLargestInterval() {
        final Integer[] years = {1, 3, 9, 11};
        Assertions.assertEquals(Pair.of(1, 11), ProducerService.getLongerRange(years));
    }
}