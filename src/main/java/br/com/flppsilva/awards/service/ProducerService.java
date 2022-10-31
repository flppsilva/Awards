package br.com.flppsilva.awards.service;

import br.com.flppsilva.awards.configuration.Loggable;
import br.com.flppsilva.awards.dto.AwardsDTO;
import br.com.flppsilva.awards.dto.RangeAwardsDTO;
import br.com.flppsilva.awards.model.Movie;
import br.com.flppsilva.awards.model.Producer;
import br.com.flppsilva.awards.repository.ProducerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ProducerService implements Loggable {

    @Autowired
    ProducerRepository producerRepository;

    static Integer getMinInterval(Integer[] values) {
        Integer minorDifference = null;
        for (int i = 0; i < values.length; i++) {
            for (int j = 0; j < values.length; j++) {
                if (i != j) {
                    int lowerValue = Math.min(values[i], values[j]);
                    int highestValue = Math.max(values[i], values[j]);
                    if (minorDifference == null || highestValue - lowerValue < minorDifference) {
                        minorDifference = highestValue - lowerValue;
                    }
                }
            }
        }
        return minorDifference;
    }

    static Integer getMaxInterval(Integer[] values) {
        Integer largerDifference = null;
        for (int i = 0; i < values.length - 1; i++) {
            int largerValue = Math.min(values[i], values[i + 1]);
            int highestValue = Math.max(values[i], values[i + 1]);
            if (largerDifference == null || highestValue - largerValue > largerDifference) {
                largerDifference = highestValue - largerValue;
            }
        }
        return largerDifference;
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
                    .sorted()
                    .toArray(Integer[]::new);

            // there is only a gap if there are more than two years
            if (years.length >= 2) {
                final int minInterval = getMinInterval(years);
                final int maxInterval = getMaxInterval(years);

                for (int i = 0; i < years.length - 1; i++) {
                    final int previous = years[i];
                    final int follow = years[i + 1];
                    final int interval = follow - previous;

                    if (interval == minInterval || interval == maxInterval) {
                        awards.putIfAbsent(interval, new ArrayList<>());
                        awards.get(interval).add(
                                new RangeAwardsDTO()
                                        .setProducer(producer.getName())
                                        .setInterval(interval)
                                        .setPreviousWin(previous)
                                        .setFollowingWin(follow));
                    }
                }
            }
        });

        // choose the general minimum and maximum
        final Integer min = awards.keySet().stream().min(Integer::compare).orElse(null);
        final Integer max = awards.keySet().stream().max(Integer::compare).orElse(null);

        return new AwardsDTO(
                min != null ? new ArrayList<>(awards.get(min)) : Collections.emptyList(),
                max != null ? new ArrayList<>(awards.get(max)) : Collections.emptyList()
        );
    }
}
