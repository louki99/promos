package ma.foodplus.ordering.system.promos.mapper;

import ma.foodplus.ordering.system.promos.dto.RewardDTO;
import ma.foodplus.ordering.system.promos.model.Reward;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface RewardMapper {
    
    @Mapping(target = "rewardType", source = "rewardType", qualifiedByName = "toRewardType")
    Reward toEntity(RewardDTO dto);

    @Mapping(target = "rewardType", source = "rewardType", qualifiedByName = "toRewardTypeString")
    RewardDTO toDTO(Reward entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "rewardType", source = "rewardType", qualifiedByName = "toRewardType")
    void updateEntityFromDTO(RewardDTO dto, @MappingTarget Reward entity);

    @Named("toRewardType")
    default Reward.RewardType toRewardType(String type) {
        return type != null ? Reward.RewardType.valueOf(type) : null;
    }

    @Named("toRewardTypeString")
    default String toRewardTypeString(Reward.RewardType type) {
        return type != null ? type.name() : null;
    }
} 