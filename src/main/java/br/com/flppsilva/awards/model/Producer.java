package br.com.flppsilva.awards.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class Producer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, unique = true)
    private String name;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "movie_producer",
            joinColumns = @JoinColumn(name = "producer_id"),
            inverseJoinColumns = @JoinColumn(name = "movie_id"))
    private List<Movie> movies;

    public long getId() {
        return id;
    }

    public Producer setId(long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public Producer setName(String name) {
        this.name = name;
        return this;
    }

    public List<Movie> getMovies() {
        return movies;
    }

    public Producer setMovies(List<Movie> movies) {
        this.movies = movies;
        return this;
    }
}
