package ma.foodplus.ordering.system.promos.service;

import ma.foodplus.ordering.system.customer.dto.CustomerDTO;
import ma.foodplus.ordering.system.customer.model.CustomerType;
import ma.foodplus.ordering.system.customer.service.CustomerService;
import ma.foodplus.ordering.system.order.model.Order;
import ma.foodplus.ordering.system.product.dto.response.ProductResponse;
import ma.foodplus.ordering.system.product.enums.SuiviStock;
import ma.foodplus.ordering.system.product.service.ProductService;
import ma.foodplus.ordering.system.promos.dto.*;
import ma.foodplus.ordering.system.promos.exception.PromotionApplicationException;
import ma.foodplus.ordering.system.promos.model.Promotion;
import ma.foodplus.ordering.system.promos.model.PromotionRule;
import ma.foodplus.ordering.system.promos.repository.PromotionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PromotionApplicationServiceTest {

    @Mock
    private AdvancedPromotionEngine promotionEngine;
    @Mock
    private ProductService productService;
    @Mock
    private PromotionRepository promotionRepository;
    @Mock
    private ConditionEvaluator conditionEvaluator;
    @Mock
    private CustomerService customerService;

    private PromotionApplicationService applicationService;

    @BeforeEach
    void setUp() {
        applicationService = new PromotionApplicationService(
            promotionEngine,
            productService,
            promotionRepository,
            conditionEvaluator,
            customerService
        );
    }

    @Test
    void calculatePromotions_Success() {
        // Arrange
        ApplyPromotionRequest request = createValidRequest();
        Order order = createOrder();
        PromotionContext context = new PromotionContext(order);

        // Only mock what is actually used
        when(promotionEngine.apply(any(Order.class))).thenReturn(context);

        // Act
        ApplyPromotionResponse response = applicationService.calculatePromotions(request);

        // Assert
        assertNotNull(response);
        assertNotNull(response.getOriginalTotal());
        assertNotNull(response.getDiscountTotal());
        assertNotNull(response.getFinalTotal());
        assertFalse(response.getLineItems().isEmpty());
    }

    @Test
    void calculatePromotions_InvalidRequest() {
        // Arrange
        ApplyPromotionRequest request = new ApplyPromotionRequest();

        // Act & Assert
        assertThrows(PromotionApplicationException.class, () -> 
            applicationService.calculatePromotions(request)
        );
    }

    @Test
    void getEligiblePromotions_Success() {
        // Arrange
        ApplyPromotionRequest request = createValidRequest();
        List<Promotion> activePromotions = Arrays.asList(
            createPromotion(1L, "PROMO1"),
            createPromotion(2L, "PROMO2")
        );

        when(promotionRepository.findActivePromotions(any(ZonedDateTime.class)))
            .thenReturn(activePromotions);
        when(conditionEvaluator.evaluate(any(), any(), any())).thenReturn(true);
        when(customerService.getCustomerById(any())).thenReturn(createCustomerDTO());

        // Act
        List<PromotionDTO> result = applicationService.getEligiblePromotions(request);

        // Assert
        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    @Test
    void validatePromotionCode_Valid() {
        ApplyPromotionRequest request = createValidRequest();
        String promotionCode = "VALID_PROMO";
        Promotion promotion = createPromotion(1L, promotionCode);

        when(promotionRepository.findByPromoCode(promotionCode))
            .thenReturn(Optional.of(promotion));
        when(conditionEvaluator.evaluate(any(), any(), any())).thenReturn(true);
        when(customerService.getCustomerById(any())).thenReturn(createCustomerDTO());

        boolean result = applicationService.validatePromotionCode(request, promotionCode);

        assertTrue(result);
    }

    @Test
    void validatePromotionCode_Invalid() {
        // Arrange
        ApplyPromotionRequest request = createValidRequest();
        String promotionCode = "INVALID_PROMO";

        when(promotionRepository.findByPromoCode(promotionCode))
            .thenReturn(Optional.empty());

        // Act
        boolean result = applicationService.validatePromotionCode(request, promotionCode);

        // Assert
        assertFalse(result);
    }

    @Test
    void getPromotionBreakdown_Success() {
        // Arrange
        ApplyPromotionRequest request = createValidRequest();
        String promotionCode = "PROMO1";
        Promotion promotion = createPromotion(1L, promotionCode);
        Order order = createOrder();
        PromotionContext context = new PromotionContext(order);

        when(promotionRepository.findByPromoCode(promotionCode))
            .thenReturn(Optional.of(promotion));
        when(promotionEngine.applyPromotion(any(), any())).thenReturn(context);
        when(conditionEvaluator.evaluate(any(), any(), any())).thenReturn(true);
        when(customerService.getCustomerById(any())).thenReturn(createCustomerDTO());

        // Act
        PromotionBreakdownDTO result = applicationService.getPromotionBreakdown(request, promotionCode);

        // Assert
        assertNotNull(result);
        assertEquals(promotionCode, result.getPromotionCode());
        assertNotNull(result.getOriginalTotal());
        assertNotNull(result.getDiscountTotal());
        assertNotNull(result.getFinalTotal());
    }

    // Helper methods
    private ApplyPromotionRequest createValidRequest() {
        ApplyPromotionRequest request = new ApplyPromotionRequest();
        request.setCustomerId(1L);
        request.setOrderItems(Arrays.asList(
            createOrderItemRequest(1L, 2),
            createOrderItemRequest(2L, 3)
        ));
        return request;
    }

    private OrdertemDto createOrderItemRequest(Long productId, int quantity) {
        OrdertemDto item = new OrdertemDto();
        item.setProductId(productId);
        item.setQuantity(quantity);
        item.setUnitPrice(new BigDecimal("10.00"));
        return item;
    }

    private Order createOrder() {
        Order order = new Order();
        order.setCustomerId(1L);
        order.setItems(Arrays.asList(
            createOrderItem(1L, "Product 1", 2, new BigDecimal("10.00")),
            createOrderItem(2L, "Product 2", 3, new BigDecimal("15.00"))
        ));
        return order;
    }

    private ma.foodplus.ordering.system.order.model.OrderItem createOrderItem(
            Long productId, String name, int quantity, BigDecimal unitPrice) {
        ma.foodplus.ordering.system.order.model.OrderItem item = 
            new ma.foodplus.ordering.system.order.model.OrderItem();
        item.setProductId(productId);
        item.setProductName(name);
        item.setQuantity(quantity);
        item.setUnitPrice(unitPrice);
        return item;
    }

    private Promotion createPromotion(Long id, String promoCode) {
       Promotion promotion = new Promotion();
       promotion.setId(id);
       promotion.setPromoCode(promoCode);
       promotion.setName("Test Promotion");
       promotion.setStartDate(ZonedDateTime.now().minusDays(1));
       promotion.setEndDate(ZonedDateTime.now().plusDays(1));
       promotion.setActive(true);
   
       // Add a dummy rule with required fields
       List<PromotionRule> rules = new ArrayList<>();
       PromotionRule rule = new PromotionRule();
       rule.setCalculationMethod(PromotionRule.CalculationMethod.BRACKET);
   
       // If your rule needs tiers and rewards, add them:
       // (You may need to import and use your actual PromotionTier and Reward classes)
       // List<PromotionTier> tiers = new ArrayList<>();
       // PromotionTier tier = new PromotionTier();
       // tier.setMinimumThreshold(BigDecimal.ZERO);
       // Reward reward = new Reward();
       // reward.setType(Reward.RewardType.FIXED_AMOUNT);
       // reward.setDiscountAmount(BigDecimal.ONE);
       // tier.setReward(reward);
       // tiers.add(tier);
       // rule.setTiers(tiers);
   
       rules.add(rule);
       promotion.setRules(rules);
   
       return promotion;
   }
    private CustomerDTO createCustomerDTO() {
        CustomerDTO customer = new CustomerDTO();
        customer.setId(1L);
        customer.setCustomerType(CustomerType.B2C);
        customer.setCategoryTarifId(1L);
        return customer;
    }
}