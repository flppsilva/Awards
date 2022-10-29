package br.com.flppsilva.awards.service;

import br.com.flppsilva.awards.configuration.Loggable;
import br.com.flppsilva.awards.dto.AwardsDTO;
import br.com.flppsilva.awards.dto.RangeAwardsDTO;
import br.com.flppsilva.awards.model.Movie;
import br.com.flppsilva.awards.model.Producer;
import br.com.flppsilva.awards.repository.ProducerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class ProducerService implements Loggable {

    @Autowired
    ProducerRepository producerRepository;

    static Pair<Integer, Integer> getShorterRange(Integer[] values) {
        Pair<Integer, Integer> result = null;
        Integer minorDifference = null;
        for (int i = 0; i < values.length; i++) {
            for (int j = 0; j < values.length; j++) {
                if (i != j) {
                    int lowerValue = Math.min(values[i], values[j]);
                    int highestValue = Math.max(values[i], values[j]);
                    if (minorDifference == null || highestValue - lowerValue < minorDifference) {
                        minorDifference = highestValue - lowerValue;
                        result = Pair.of(lowerValue, highestValue);
                    }
                }
            }
        }
        return result;
    }

    static Pair<Integer, Integer> getLongerRange(Integer[] values) {
        if (values.length < 2) {
            return null;
        }

        return Pair.of(
                Stream.of(values).min(Integer::compare).get(),
                Stream.of(values).max(Integer::compare).get()
        );
    }

    public AwardsDTO findRewardInterval() {
        this.getLogger().debug("Loading producers with award movies");
        final List<Producer> producers = producerRepository.findByMoviesWinners();
        final Map<Integer, List<RangeAwardsDTO>> awards = new HashMap<>();

        // keep the minimum and maximum of each producer
        producers.forEach(producer -> {
            final Integer[] years = producer.getMovies().stream()
                    .filter(Movie::isWinner)
                    .map(Movie::getYear)
                    .distinct()
                    .toArray(Integer[]::new);

            // there is only a gap if there are more than two years
            if (years.length >= 2) {

                final Pair<Integer, Integer> minRange = getShorterRange(years);
                final int minInterval = minRange.getSecond() - minRange.getFirst();
                awards.putIfAbsent(minInterval, new ArrayList<>());
                awards.get(minInterval).add(
                        new RangeAwardsDTO()
                                .setProducer(producer.getName())
                                .setInterval(minInterval)
                                .setPreviousWin(minRange.getFirst())
                                .setFollowingWin(minRange.getSecond())
                );

                final Pair<Integer, Integer> maxRange = getLongerRange(years);
                final int maxInterval = maxRange.getSecond() - maxRange.getFirst();
                awards.putIfAbsent(maxInterval, new ArrayList<>());
                awards.get(maxInterval).add(
                        new RangeAwardsDTO()
                                .setProducer(producer.getName())
                                .setInterval(maxInterval)
                                .setPreviousWin(maxRange.getFirst())
                                .setFollowingWin(maxRange.getSecond()));
            }
        });

        // choose the general minimum and maximum
        final Integer min = awards.keySet().stream().min(Integer::compare).orElse(null);
        final Integer max = awards.keySet().stream().max(Integer::compare).orElse(null);

        return new AwardsDTO(
                min != null ? awards.get(min).stream().distinct().collect(Collectors.toList()) : Collections.emptyList(),
                max != null ? awards.get(max).stream().distinct().collect(Collectors.toList()) : Collections.emptyList()
        );
    }
}
