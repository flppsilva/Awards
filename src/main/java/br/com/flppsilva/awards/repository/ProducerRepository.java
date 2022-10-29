package br.com.flppsilva.awards.repository;

import br.com.flppsilva.awards.model.Producer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProducerRepository extends JpaRepository<Producer, Long> {

    @Query(value = "SELECT p "
            + "FROM Producer p "
            + "LEFT JOIN FETCH Movie m "
            + "WHERE m.winner = true ")
    List<Producer> findByMoviesWinners();
}
