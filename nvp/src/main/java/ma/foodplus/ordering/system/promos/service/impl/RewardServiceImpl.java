package ma.foodplus.ordering.system.promos.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.foodplus.ordering.system.promos.dto.RewardDTO;
import ma.foodplus.ordering.system.promos.exception.RewardNotFoundException;
import ma.foodplus.ordering.system.promos.mapper.RewardMapper;
import ma.foodplus.ordering.system.promos.model.Promotion;
import ma.foodplus.ordering.system.promos.model.Reward;
import ma.foodplus.ordering.system.promos.repository.PromotionRepository;
import ma.foodplus.ordering.system.promos.repository.RewardRepository;
import ma.foodplus.ordering.system.promos.service.RewardService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class RewardServiceImpl implements RewardService {

    private final RewardRepository rewardRepository;
    private final PromotionRepository promotionRepository;
    private final RewardMapper rewardMapper;

    @Override
    @Transactional
    public RewardDTO createReward(RewardDTO rewardDTO) {
        Reward reward = rewardMapper.toEntity(rewardDTO);
        Reward savedReward = rewardRepository.save(reward);
        return rewardMapper.toDTO(savedReward);
    }

    @Override
    @Transactional
    public RewardDTO updateReward(RewardDTO rewardDTO) {
        Reward existingReward = rewardRepository.findById(rewardDTO.getId().longValue())
                .orElseThrow(() -> new RewardNotFoundException("Reward not found with id: " + rewardDTO.getId()));
        
        rewardMapper.updateEntityFromDTO(rewardDTO, existingReward);
        Reward updatedReward = rewardRepository.save(existingReward);
        return rewardMapper.toDTO(updatedReward);
    }

    @Override
    @Transactional
    public void deleteReward(Integer id) {
        if (!rewardRepository.existsById(id.longValue())) {
            throw new RewardNotFoundException("Reward not found with id: " + id);
        }
        rewardRepository.deleteById(id.longValue());
    }

    @Override
    @Transactional(readOnly = true)
    public RewardDTO getRewardById(Integer id) {
        Reward reward = rewardRepository.findById(id.longValue())
                .orElseThrow(() -> new RewardNotFoundException("Reward not found with id: " + id));
        return rewardMapper.toDTO(reward);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RewardDTO> getRewardsByPromotionId(Integer promotionId) {
        Promotion promotion = promotionRepository.findById(promotionId)
                .orElseThrow(() -> new RewardNotFoundException("Promotion not found with id: " + promotionId));
        
        return promotion.getRewards().stream()
                .map(rewardMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public RewardDTO addRewardToPromotion(Integer promotionId, RewardDTO rewardDTO) {
        Promotion promotion = promotionRepository.findById(promotionId)
                .orElseThrow(() -> new RewardNotFoundException("Promotion not found with id: " + promotionId));
        
        Reward reward = rewardMapper.toEntity(rewardDTO);
        reward.setPromotion(promotion);
        Reward savedReward = rewardRepository.save(reward);
        return rewardMapper.toDTO(savedReward);
    }

    @Override
    @Transactional
    public void removeRewardFromPromotion(Integer promotionId, Integer rewardId) {
        Promotion promotion = promotionRepository.findById(promotionId)
                .orElseThrow(() -> new RewardNotFoundException("Promotion not found with id: " + promotionId));
        
        Reward reward = rewardRepository.findById(rewardId.longValue())
                .orElseThrow(() -> new RewardNotFoundException("Reward not found with id: " + rewardId));
        
        if (!reward.getPromotion().getId().equals(promotion.getId())) {
            throw new RewardNotFoundException("Reward does not belong to the specified promotion");
        }
        
        rewardRepository.delete(reward);
    }

    @Override
    @Transactional(readOnly = true)
    public List<String> getRewardTypes() {
        return Arrays.stream(Reward.RewardType.values())
                .map(Enum::name)
                .collect(Collectors.toList());
    }
} 