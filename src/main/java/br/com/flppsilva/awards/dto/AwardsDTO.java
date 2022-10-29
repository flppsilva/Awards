package br.com.flppsilva.awards.dto;

import java.util.List;

public record AwardsDTO(List<RangeAwardsDTO> min, List<RangeAwardsDTO> max) {
}
