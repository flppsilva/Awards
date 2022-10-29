package br.com.flppsilva.awards.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "ano", nullable = false)
    private int year;

    @Column(name = "title", nullable = false)
    private String title;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(
            name = "movie_producer",
            joinColumns = @JoinColumn(name = "movie_id"),
            inverseJoinColumns = @JoinColumn(name = "producer_id"))
    private List<Producer> producers;

    @Column(name = "winner", nullable = false)
    private boolean winner;

    public long getId() {
        return id;
    }

    public Movie setId(long id) {
        this.id = id;
        return this;
    }

    public int getYear() {
        return year;
    }

    public Movie setYear(int year) {
        this.year = year;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public Movie setTitle(String title) {
        this.title = title;
        return this;
    }

    public List<Producer> getProducers() {
        return producers;
    }

    public Movie setProducers(List<Producer> producers) {
        this.producers = producers;
        return this;
    }

    public boolean isWinner() {
        return winner;
    }

    public Movie setWinner(boolean winner) {
        this.winner = winner;
        return this;
    }
}
