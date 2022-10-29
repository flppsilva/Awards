package br.com.flppsilva.awards.dto;

import java.util.Objects;

public class RangeAwardsDTO {

    private String producer;
    int interval;
    int previousWin;
    int followingWin;

    public String getProducer() {
        return producer;
    }

    public RangeAwardsDTO setProducer(String producer) {
        this.producer = producer;
        return this;
    }

    public int getInterval() {
        return interval;
    }

    public RangeAwardsDTO setInterval(int interval) {
        this.interval = interval;
        return this;
    }

    public int getPreviousWin() {
        return previousWin;
    }

    public RangeAwardsDTO setPreviousWin(int previousWin) {
        this.previousWin = previousWin;
        return this;
    }

    public int getFollowingWin() {
        return followingWin;
    }

    public RangeAwardsDTO setFollowingWin(int followingWin) {
        this.followingWin = followingWin;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RangeAwardsDTO that = (RangeAwardsDTO) o;
        return interval == that.interval && previousWin == that.previousWin && followingWin == that.followingWin && Objects.equals(producer, that.producer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(producer, interval, previousWin, followingWin);
    }
}
