package ma.foodplus.ordering.system.partner.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

/**
 * DTO for Document operations.
 * 
 * <p>This DTO contains document information for creating and updating
 * documents associated with partners.</p>
 * 
 * @author FoodPlus Development Team
 * @version 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DocumentDTO {
    
    private Long id;
    
    @NotBlank(message = "Document type is required")
    private String type;
    
    @NotBlank(message = "File URL is required")
    private String fileUrl;
    
    private String fileName;
    
    private Long fileSize;
    
    private String mimeType;
    
    private String description;
    
    private Boolean verified = false;
    
    private String verifiedBy;
    
    private ZonedDateTime verifiedAt;
    
    private ZonedDateTime expiryDate;
    
    private Boolean active = true;
    
    private ZonedDateTime uploadedAt;
    
    private ZonedDateTime updatedAt;
    
    private String createdBy;
    
    private String updatedBy;
    
    private Long partnerId;
    
    /**
     * Checks if this document is verified.
     * 
     * @return true if this document is verified
     */
    public boolean isVerified() {
        return verified != null && verified;
    }
    
    /**
     * Checks if this document is active.
     * 
     * @return true if this document is active
     */
    public boolean isActive() {
        return active != null && active;
    }
    
    /**
     * Checks if this document has expired.
     * 
     * @return true if this document has expired
     */
    public boolean isExpired() {
        if (expiryDate == null) {
            return false;
        }
        return ZonedDateTime.now().isAfter(expiryDate);
    }
    
    /**
     * Checks if this document will expire soon (within the specified days).
     * 
     * @param daysThreshold the number of days threshold
     * @return true if this document will expire soon
     */
    public boolean isExpiringSoon(int daysThreshold) {
        if (expiryDate == null) {
            return false;
        }
        ZonedDateTime thresholdDate = ZonedDateTime.now().plusDays(daysThreshold);
        return expiryDate.isBefore(thresholdDate) && !isExpired();
    }
    
    /**
     * Gets the file extension from the file URL.
     * 
     * @return the file extension
     */
    public String getFileExtension() {
        if (fileUrl == null || fileUrl.isEmpty()) {
            return "";
        }
        int lastDotIndex = fileUrl.lastIndexOf('.');
        if (lastDotIndex == -1) {
            return "";
        }
        return fileUrl.substring(lastDotIndex + 1).toLowerCase();
    }
    
    /**
     * Gets the file name from the file URL.
     * 
     * @return the file name
     */
    public String getFileNameFromUrl() {
        if (fileUrl == null || fileUrl.isEmpty()) {
            return "";
        }
        int lastSlashIndex = fileUrl.lastIndexOf('/');
        if (lastSlashIndex == -1) {
            return fileUrl;
        }
        return fileUrl.substring(lastSlashIndex + 1);
    }
} 