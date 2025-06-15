package ma.foodplus.ordering.system.promos.mapper;

import ma.foodplus.ordering.system.promos.dto.RewardDTO;
import ma.foodplus.ordering.system.promos.model.Reward;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

@Mapper(componentModel = "spring", unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE, uses = {})
public interface RewardMapper {
    
    @Mapping(target = "type", source = "type", qualifiedByName = "toRewardType")
    @Mapping(target = "targetEntityType", source = "targetEntityType", qualifiedByName = "toTargetEntityType")
    Reward toEntity(RewardDTO dto);

    @Mapping(target = "type", source = "type", qualifiedByName = "toRewardTypeString")
    @Mapping(target = "targetEntityType", source = "targetEntityType", qualifiedByName = "toTargetEntityTypeString")
    RewardDTO toDTO(Reward entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "type", source = "type", qualifiedByName = "toRewardType")
    @Mapping(target = "targetEntityType", source = "targetEntityType", qualifiedByName = "toTargetEntityType")
    void updateEntityFromDTO(RewardDTO dto, @MappingTarget Reward entity);

    @Named("toRewardType")
    default Reward.RewardType toRewardType(String type) {
        return type != null ? Reward.RewardType.valueOf(type) : null;
    }

    @Named("toRewardTypeString")
    default String toRewardTypeString(Reward.RewardType type) {
        return type != null ? type.name() : null;
    }

    @Named("toTargetEntityType")
    default Reward.TargetEntityType toTargetEntityType(String type) {
        return type != null ? Reward.TargetEntityType.valueOf(type) : null;
    }

    @Named("toTargetEntityTypeString")
    default String toTargetEntityTypeString(Reward.TargetEntityType type) {
        return type != null ? type.name() : null;
    }
}