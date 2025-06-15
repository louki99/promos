package ma.foodplus.ordering.system.promos.service;

import ma.foodplus.ordering.system.promos.dto.RewardDTO;
import java.util.List;

public interface RewardService {
    
    RewardDTO createReward(RewardDTO rewardDTO);
    
    RewardDTO updateReward(RewardDTO rewardDTO);
    
    void deleteReward(Integer id);
    
    RewardDTO getRewardById(Integer id);
    
    List<RewardDTO> getRewardsByPromotionId(Integer promotionId);
    
    RewardDTO addRewardToPromotion(Integer promotionId, RewardDTO rewardDTO);
    
    void removeRewardFromPromotion(Integer promotionId, Integer rewardId);
    
    List<String> getRewardTypes();
} 