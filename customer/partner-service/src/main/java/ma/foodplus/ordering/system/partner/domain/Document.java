package ma.foodplus.ordering.system.partner.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.ZonedDateTime;

/**
 * Document entity representing documents associated with partners.
 * 
 * <p>This entity manages various types of documents such as contracts,
 * registration certificates, tax documents, etc. associated with partners.</p>
 * 
 * @author FoodPlus Development Team
 * @version 1.0.0
 */
@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "partner_documents")
public class Document {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "Document type is required")
    @Column(name = "document_type", nullable = false)
    private String type;
    
    @NotBlank(message = "File URL is required")
    @Column(name = "file_url", nullable = false)
    private String fileUrl;
    
    @Column(name = "file_name")
    private String fileName;
    
    @Column(name = "file_size")
    private Long fileSize;
    
    @Column(name = "mime_type")
    private String mimeType;
    
    @Column(name = "description")
    private String description;
    
    @Column(name = "is_verified")
    private Boolean isVerified = false;
    
    @Column(name = "verified_by")
    private String verifiedBy;
    
    @Column(name = "verified_at")
    private ZonedDateTime verifiedAt;
    
    @Column(name = "expiry_date")
    private ZonedDateTime expiryDate;
    
    @Column(name = "is_active")
    private Boolean isActive = true;
    
    @CreationTimestamp
    @Column(name = "uploaded_at", nullable = false, updatable = false)
    private ZonedDateTime uploadedAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at")
    private ZonedDateTime updatedAt;
    
    @Column(name = "created_by")
    private String createdBy;
    
    @Column(name = "updated_by")
    private String updatedBy;
    
    // Relationship with Partner
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "partner_id", nullable = false)
    private Partner partner;
    
    /**
     * Checks if this document is verified.
     * 
     * @return true if this document is verified
     */
    public boolean isVerified() {
        return isVerified != null && isVerified;
    }
    
    /**
     * Checks if this document is active.
     * 
     * @return true if this document is active
     */
    public boolean isActive() {
        return isActive != null && isActive;
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
     * Verifies this document.
     * 
     * @param verifiedBy the user who verified the document
     */
    public void verify(String verifiedBy) {
        this.isVerified = true;
        this.verifiedBy = verifiedBy;
        this.verifiedAt = ZonedDateTime.now();
    }
    
    /**
     * Unverifies this document.
     */
    public void unverify() {
        this.isVerified = false;
        this.verifiedBy = null;
        this.verifiedAt = null;
    }
    
    /**
     * Activates this document.
     */
    public void activate() {
        this.isActive = true;
    }
    
    /**
     * Deactivates this document.
     */
    public void deactivate() {
        this.isActive = false;
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