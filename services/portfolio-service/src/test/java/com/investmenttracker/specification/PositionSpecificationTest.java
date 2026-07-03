package com.investmenttracker.specification;

import com.investmenttracker.dto.PositionFilter;
import com.investmenttracker.entity.Currency;
import com.investmenttracker.entity.Position;
import com.investmenttracker.repository.PositionRepository;
import com.investmenttracker.specification.PositionSpecification;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@DataJpaTest
public class PositionSpecificationTest {
    @Autowired
    private PositionRepository positionRepository;
    @Test
    void byFilter_shouldFilterUserById(){
        positionRepository.save(createPosition(1L, "NVDA", Currency.EUR,
                BigDecimal.valueOf(1000), BigDecimal.TEN));
        positionRepository.save(createPosition(2L, "TEST", Currency.USD,
                BigDecimal.valueOf(1000), BigDecimal.valueOf(100)));
        PositionFilter positionFilter = createPositionFilter(null, null, null, null, 1L);
        List<Position> result = positionRepository.findAll(PositionSpecification.byFilter(positionFilter));
        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals(1L, result.get(0).getId());
        Assertions.assertEquals("NVDA", result.get(0).getTicker());
    }
    @Test
    void byFilter_shouldFilterTicker(){
        positionRepository.save(createPosition(1L, "NVDA", Currency.EUR,
                BigDecimal.valueOf(1000), BigDecimal.TEN));
        positionRepository.save(createPosition(2L, "TEST", Currency.USD,
                BigDecimal.valueOf(1000), BigDecimal.valueOf(100)));
        PositionFilter filter = createPositionFilter("NVDA", null, null, null, null);
        List<Position> result = positionRepository.findAll(PositionSpecification.byFilter(filter));
        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals(Currency.EUR, result.get(0).getCurrency());
        Assertions.assertEquals("NVDA", result.get(0).getTicker());
    }
    @Test
    void byFilter_shouldFilterCurrency(){
        positionRepository.save(createPosition(1L, "NVDA", Currency.EUR,
                BigDecimal.valueOf(1000), BigDecimal.TEN));
        positionRepository.save(createPosition(2L, "TEST", Currency.USD,
                BigDecimal.valueOf(1000), BigDecimal.valueOf(100)));
        PositionFilter filter = createPositionFilter(null, Currency.EUR, null, null, null);
        List<Position> result = positionRepository.findAll(PositionSpecification.byFilter(filter));
        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals(Currency.EUR, result.get(0).getCurrency());
        Assertions.assertEquals("NVDA", result.get(0).getTicker());
    }
    @Test
    void byFilter_shouldFilterMinPrice(){
        positionRepository.save(createPosition(1L, "NVDA", Currency.EUR,
                BigDecimal.valueOf(1100), BigDecimal.TEN));
        positionRepository.save(createPosition(2L, "TEST", Currency.USD,
                BigDecimal.valueOf(1000), BigDecimal.valueOf(100)));
        PositionFilter filter = createPositionFilter(null, null, BigDecimal.valueOf(1050), null, null);
        List<Position> result = positionRepository.findAll(PositionSpecification.byFilter(filter));
        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals(Currency.EUR, result.get(0).getCurrency());
        Assertions.assertEquals(BigDecimal.valueOf(1100), result.get(0).getAveragePrice());
    }
    @Test
    void byFilter_shouldFilterMaxPrice(){
        positionRepository.save(createPosition(1L, "NVDA", Currency.EUR,
                BigDecimal.valueOf(1100), BigDecimal.TEN));
        positionRepository.save(createPosition(2L, "TEST", Currency.USD,
                BigDecimal.valueOf(1000), BigDecimal.valueOf(100)));
        PositionFilter filter = createPositionFilter(null, null, null, BigDecimal.valueOf(1050),  null);
        List<Position> result = positionRepository.findAll(PositionSpecification.byFilter(filter));
        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals(Currency.USD, result.get(0).getCurrency());
        Assertions.assertEquals(BigDecimal.valueOf(1000), result.get(0).getAveragePrice());
    }
    @Test
    void byFilter_shouldFilterAllCombined(){
        positionRepository.save(createPosition(1L, "NVDA", Currency.EUR,
                BigDecimal.valueOf(100), BigDecimal.TEN));
        positionRepository.save(createPosition(1L, "TSMC", Currency.EUR,
                BigDecimal.valueOf(100), BigDecimal.valueOf(100)));
        positionRepository.save(createPosition(1L, "NVDA", Currency.USD,
                BigDecimal.valueOf(100), BigDecimal.TEN));
        PositionFilter filter = createPositionFilter("NVDA", Currency.EUR, BigDecimal.valueOf(50), BigDecimal.valueOf(150),  1L);
        List<Position> result = positionRepository.findAll(PositionSpecification.byFilter(filter));
        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals("NVDA", result.get(0).getTicker());
        Assertions.assertEquals(Currency.EUR, result.get(0).getCurrency());
        Assertions.assertEquals(BigDecimal.valueOf(100), result.get(0).getAveragePrice());
        Assertions.assertEquals(1L, result.get(0).getUserId());
    }
    private Position createPosition(Long userId, String ticker, Currency currency, BigDecimal averagePrice, BigDecimal quantity) {
        return Position.builder()
                .userId(userId)
                .ticker(ticker)
                .currency(currency)
                .averagePrice(averagePrice)
                .quantity(quantity)
                .createdAt(LocalDateTime.now())
                .build();
    }
    private PositionFilter createPositionFilter(String ticker, Currency currency, BigDecimal minAveragePrice, BigDecimal maxAveragePrice, Long userId) {
        return new PositionFilter(ticker, currency, minAveragePrice, maxAveragePrice, userId);
    }
}
