package com.investmenttracker.service;

import com.investmenttracker.dto.CreatePositionRequest;
import com.investmenttracker.dto.PositionDto;
import com.investmenttracker.dto.PositionFilter;
import com.investmenttracker.dto.UpdatePostionRequest;
import com.investmenttracker.entity.Position;
import com.investmenttracker.exception.PositionAccessDeniedException;
import com.investmenttracker.exception.PositionNotFoundException;
import com.investmenttracker.repository.PositionRepository;
import com.investmenttracker.security.SecurityUtils;
import com.investmenttracker.specification.PositionSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PositionService {
    private final PositionRepository positionRepository;
    public PositionService(PositionRepository positionRepository) {
        this.positionRepository = positionRepository;
    }
    public PositionDto createPosition(CreatePositionRequest request) {
        Position position = new Position().builder()
                .userId(SecurityUtils.getCurrentUserId())
                .ticker(request.ticker())
                .averagePrice(request.averagePrice())
                .quantity(request.quantity())
                .currency(request.currency())
                .createdAt(LocalDateTime.now())
                .build();
        return toDto(positionRepository.save(position));

    }
    public Page<PositionDto> getAllPositions(PositionFilter filter, Pageable pageable) {
        Long userId = SecurityUtils.getCurrentUserId();
        PositionFilter securedFilter = new PositionFilter(
                filter.ticker(),
                filter.currency(),
                filter.minAveragePrice(),
                filter.maxAveragePrice(),
                userId
        );
       Specification<Position> specification = PositionSpecification.byFilter(securedFilter);
        return positionRepository.findAll(specification, pageable)
                .map(this::toDto);
    }
    public PositionDto getPositionById(Long id) {
        return toDto(findPositionById(id));
    }
    public void deletePositionById(Long id) {
        Position position = findPositionById(id);
        positionRepository.delete(position);
    }
    public PositionDto updatePosition(Long id, UpdatePostionRequest request) {
        var position = findPositionById(id);
        if (request.ticker() != null) {
            position.setTicker(request.ticker());
        }
        if (request.averagePrice() != null) {
            position.setAveragePrice(request.averagePrice());
        }
        if (request.quantity() != null) {
            position.setQuantity(request.quantity());
        }
        if (request.currency() != null) {
            position.setCurrency(request.currency());
        }
        return toDto(positionRepository.save(position));
    }
    private PositionDto toDto(Position position) {
        return new PositionDto(
                position.getId(),
                position.getUserId(),
                position.getTicker(),
                position.getQuantity(),
                position.getAveragePrice(),
                position.getCurrency(),
                position.getCreatedAt()
        );
    }
    private Position findPositionById(Long id) {
        Position position = positionRepository.findById(id)
                .orElseThrow(() -> new PositionNotFoundException("Position with id " + id + " was not found"));
        Long currentUserId = SecurityUtils.getCurrentUserId();
        if (!position.getUserId().equals(currentUserId)) {
            throw new PositionAccessDeniedException("You are not allowed to access this position");
        }
        return position;
    }
}
