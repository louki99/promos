package ma.foodplus.ordering.system.promos.mapper;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import ma.foodplus.ordering.system.promos.dto.PromotionDTO;
import ma.foodplus.ordering.system.promos.dto.PromotionRuleDTO;
import ma.foodplus.ordering.system.promos.model.Promotion;
import ma.foodplus.ordering.system.promos.model.PromotionRule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-06-09T15:23:22+0100",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.42.0.v20250514-1000, environment: Java 21.0.7 (Eclipse Adoptium)"
)
@Component
public class PromotionMapperImpl implements PromotionMapper {

    @Autowired
    private PromotionRuleMapper promotionRuleMapper;

    @Override
    public PromotionDTO toDTO(Promotion promotion) {
        if ( promotion == null ) {
            return null;
        }

        PromotionDTO.PromotionDTOBuilder promotionDTO = PromotionDTO.builder();

        promotionDTO.rules( promotionRuleListToPromotionRuleDTOList( promotion.getRules() ) );
        promotionDTO.combinabilityGroup( promotion.getCombinabilityGroup() );
        promotionDTO.description( promotion.getDescription() );
        promotionDTO.endDate( promotion.getEndDate() );
        promotionDTO.id( promotion.getId() );
        promotionDTO.name( promotion.getName() );
        promotionDTO.priority( promotion.getPriority() );
        promotionDTO.promoCode( promotion.getPromoCode() );
        promotionDTO.startDate( promotion.getStartDate() );

        return promotionDTO.build();
    }

    @Override
    public Promotion toEntity(PromotionDTO dto) {
        if ( dto == null ) {
            return null;
        }

        Promotion promotion = new Promotion();

        promotion.setRules( promotionRuleDTOListToPromotionRuleList( dto.getRules() ) );
        promotion.setActive( dto.isActive() );
        promotion.setCombinabilityGroup( dto.getCombinabilityGroup() );
        promotion.setDescription( dto.getDescription() );
        promotion.setEndDate( dto.getEndDate() );
        promotion.setExclusive( dto.isExclusive() );
        promotion.setId( dto.getId() );
        promotion.setName( dto.getName() );
        promotion.setPriority( dto.getPriority() );
        promotion.setPromoCode( dto.getPromoCode() );
        promotion.setStartDate( dto.getStartDate() );

        return promotion;
    }

    @Override
    public void updateEntityFromDTO(PromotionDTO dto, Promotion promotion) {
        if ( dto == null ) {
            return;
        }

        promotion.setActive( dto.isActive() );
        promotion.setCombinabilityGroup( dto.getCombinabilityGroup() );
        promotion.setDescription( dto.getDescription() );
        promotion.setEndDate( dto.getEndDate() );
        promotion.setExclusive( dto.isExclusive() );
        promotion.setName( dto.getName() );
        promotion.setPriority( dto.getPriority() );
        promotion.setPromoCode( dto.getPromoCode() );
        if ( promotion.getRules() != null ) {
            List<PromotionRule> list = promotionRuleDTOListToPromotionRuleList1( dto.getRules() );
            if ( list != null ) {
                promotion.getRules().clear();
                promotion.getRules().addAll( list );
            }
            else {
                promotion.setRules( null );
            }
        }
        else {
            List<PromotionRule> list = promotionRuleDTOListToPromotionRuleList1( dto.getRules() );
            if ( list != null ) {
                promotion.setRules( list );
            }
        }
        promotion.setStartDate( dto.getStartDate() );
    }

    protected List<PromotionRuleDTO> promotionRuleListToPromotionRuleDTOList(List<PromotionRule> list) {
        if ( list == null ) {
            return null;
        }

        List<PromotionRuleDTO> list1 = new ArrayList<PromotionRuleDTO>( list.size() );
        for ( PromotionRule promotionRule : list ) {
            list1.add( promotionRuleMapper.toDTO( promotionRule ) );
        }

        return list1;
    }

    protected List<PromotionRule> promotionRuleDTOListToPromotionRuleList(List<PromotionRuleDTO> list) {
        if ( list == null ) {
            return null;
        }

        List<PromotionRule> list1 = new ArrayList<PromotionRule>( list.size() );
        for ( PromotionRuleDTO promotionRuleDTO : list ) {
            list1.add( promotionRuleMapper.toEntity( promotionRuleDTO ) );
        }

        return list1;
    }

    protected List<PromotionRule> promotionRuleDTOListToPromotionRuleList1(List<PromotionRuleDTO> list) {
        if ( list == null ) {
            return null;
        }

        List<PromotionRule> list1 = new ArrayList<PromotionRule>( list.size() );
        for ( PromotionRuleDTO promotionRuleDTO : list ) {
            list1.add( promotionRuleMapper.toEntity( promotionRuleDTO ) );
        }

        return list1;
    }
}
