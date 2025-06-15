package ma.foodplus.ordering.system.promos.service;

import ma.foodplus.ordering.system.promos.dto.*;
import ma.foodplus.ordering.system.promos.mapper.PromotionMapper;
import ma.foodplus.ordering.system.product.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PromotionCalculationServiceTest {

    @Mock
    private PromotionService promotionService;
    @Mock
    private PromotionMapper promotionMapper;
    @Mock
    private ProductService productService;
    @Mock
    private PromoFamilyService promoFamilyService;

    private PromotionCalculationService calculationService;

    @BeforeEach
    void setUp() {
        calculationService = new PromotionCalculationService(
            promotionService,
            promotionMapper,
            productService,
            promoFamilyService
        );
    }

    @Test
    void calculateNestedPromotions_SinglePromotion() {
        // Arrange
        Integer promotionId = 1;
        Map<Long, Integer> basketItems = createBasketItems();
        PromotionDTO promotionDTO = createPromotionDTO(promotionId);

        when(promotionService.getPromotionById(promotionId)).thenReturn(promotionDTO);
        when(promotionService.findByParentPromotionId(promotionId)).thenReturn(Collections.emptyList());

        // Act
        BigDecimal result = calculationService.calculateNestedPromotions(promotionId, basketItems);

        // Assert
        assertNotNull(result);
        assertTrue(result.compareTo(BigDecimal.ZERO) >= 0);
    }

    @Test
    void calculateNestedPromotions_WithNestedPromotions() {
        // Arrange
        Integer promotionId = 1;
        Map<Long, Integer> basketItems = createBasketItems();
        PromotionDTO mainPromotion = createPromotionDTO(promotionId);
        PromotionDTO nestedPromotion = createPromotionDTO(2);

        when(promotionService.getPromotionById(promotionId)).thenReturn(mainPromotion);
        when(promotionService.findByParentPromotionId(promotionId))
            .thenReturn(Collections.singletonList(nestedPromotion));

        // Act
        BigDecimal result = calculationService.calculateNestedPromotions(promotionId, basketItems);

        // Assert
        assertNotNull(result);
        assertTrue(result.compareTo(BigDecimal.ZERO) >= 0);
    }

    @Test
    void calculateProductPoints() {
        // Arrange
        Integer promotionId = 1;
        Long productId = 1L;
        int quantity = 2;
        PromotionDTO promotionDTO = createPromotionDTO(promotionId);
        Map<Long, Integer> productPoints = new HashMap<>();
        productPoints.put(productId, 5);
        promotionDTO.setProductPoints(productPoints);

        when(promotionService.getPromotionById(promotionId)).thenReturn(promotionDTO);

        // Act
        int result = calculationService.calculateProductPoints(promotionId, productId, quantity);

        // Assert
        assertEquals(10, result); // 5 points * 2 quantity
    }

    @Test
    void calculateProductPoints_InactivePromotion() {
        // Arrange
        Integer promotionId = 1;
        Long productId = 1L;
        int quantity = 2;
        PromotionDTO promotionDTO = createPromotionDTO(promotionId);
        promotionDTO.setEndDate(ZonedDateTime.now().minusDays(1));

        when(promotionService.getPromotionById(promotionId)).thenReturn(promotionDTO);

        // Act
        int result = calculationService.calculateProductPoints(promotionId, productId, quantity);

        // Assert
        assertEquals(0, result);
    }

    @Test
    void calculateProductPoints_NoPointsForProduct() {
        // Arrange
        Integer promotionId = 1;
        Long productId = 1L;
        int quantity = 2;
        PromotionDTO promotionDTO = createPromotionDTO(promotionId);
        promotionDTO.setProductPoints(new HashMap<>());

        when(promotionService.getPromotionById(promotionId)).thenReturn(promotionDTO);

        // Act
        int result = calculationService.calculateProductPoints(promotionId, productId, quantity);

        // Assert
        assertEquals(0, result);
    }

    // Helper methods
    private Map<Long, Integer> createBasketItems() {
        Map<Long, Integer> basketItems = new HashMap<>();
        basketItems.put(1L, 2);
        basketItems.put(2L, 3);
        return basketItems;
    }

    private PromotionDTO createPromotionDTO(Integer id) {
        return PromotionDTO.builder()
            .id(id)
            .name("Test Promotion")
            .description("Test Description")
            .startDate(ZonedDateTime.now().minusDays(1))
            .endDate(ZonedDateTime.now().plusDays(1))
            .rules(Collections.singletonList(createPromotionRuleDTO()))
            .build();
    }

    private PromotionRuleDTO createPromotionRuleDTO() {
        return PromotionRuleDTO.builder()
            .id(1)
            .calculationMethod(PromotionRuleDTO.CalculationMethod.BRACKET)
            .breakpointType(PromotionRuleDTO.BreakpointType.AMOUNT)
            .tiers(Collections.singletonList(createPromotionTierDTO()))
            .build();
    }

    private PromotionTierDTO createPromotionTierDTO() {
        return PromotionTierDTO.builder()
            .id(1)
            .minimumThreshold(new BigDecimal("100.00"))
            .discountAmount(new BigDecimal("10.00"))
            .build();
    }
} 