package br.com.flppsilva.awards.configuration;

import br.com.flppsilva.awards.model.Movie;
import br.com.flppsilva.awards.model.Producer;
import br.com.flppsilva.awards.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class DataInicialization implements ApplicationRunner, Loggable {

    private static final String CSV_DELIMITER = ";";
    private static final int CSV_COLUMN_YEAR = 0;
    private static final int CSV_COLUMN_TITLE = 1;
    private static final int CSV_COLUMN_PRODUCERS = 3;
    private static final int CSV_COLUMN_WINNER = 4;

    @Autowired
    MovieRepository movieRepository;

    @Override
    public void run(ApplicationArguments args) {
        this.getLogger().info("Started movie loading process");
        try {
            final List<Movie> movies = new ArrayList<>();
            final Map<String, Producer> producers = new HashMap<>();

            final File movieListFile = ResourceUtils.getFile("classpath:movielist.csv");
            this.getLogger().debug("[{}] Importing file ...", movieListFile.getName());

            final List<String> lines = Files.readAllLines(movieListFile.toPath());
            this.getLogger().debug("[{}] {} lines found ...", movieListFile.getName(), lines.size());
            for (final String line : lines.subList(1, lines.size())) {
                final String[] columns = line.split(CSV_DELIMITER);

                final Movie movie = new Movie()
                        .setYear(Integer.parseInt(columns[CSV_COLUMN_YEAR]))
                        .setTitle(columns[CSV_COLUMN_TITLE])
                        .setWinner(columns.length > CSV_COLUMN_WINNER && "yes".equals(columns[CSV_COLUMN_WINNER]))
                        .setProducers(new ArrayList<>());

                for (final String producerName : columns[CSV_COLUMN_PRODUCERS].replace(" and ", ",").split(",")) {
                    final String producerNameNormalized = producerName.trim();
                    producers.putIfAbsent(producerNameNormalized, new Producer().setName(producerNameNormalized));
                    movie.getProducers().add(producers.get(producerNameNormalized));
                }

                movies.add(movie);
                this.getLogger().debug("[{}] {} ({}) imported!", movieListFile.getName(), movie.getTitle(), movie.getYear());
            }
            movieRepository.saveAll(movies);
            this.getLogger().info("Movies loaded successfully!");
        } catch (Exception e) {
            this.getLogger().error("Error while loading movies!", e);
        }
    }
}
