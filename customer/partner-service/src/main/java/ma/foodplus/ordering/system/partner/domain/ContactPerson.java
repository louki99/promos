package ma.foodplus.ordering.system.partner.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.ZonedDateTime;

/**
 * Contact Person entity representing individual contacts for partners.
 * 
 * <p>This entity manages contact persons associated with partners, including
 * their personal information, position, and contact details.</p>
 * 
 * @author FoodPlus Development Team
 * @version 1.0.0
 */
@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "contact_persons")
public class ContactPerson {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "Contact person name is required")
    @Column(name = "name", nullable = false)
    private String name;
    
    @Column(name = "position")
    private String position;
    
    @Email(message = "Invalid email format")
    @Column(name = "email")
    private String email;
    
    @Pattern(regexp = "^[+]?[0-9\\s\\-()]{8,20}$", message = "Invalid phone number format")
    @Column(name = "phone")
    private String phone;
    
    @Column(name = "is_preferred")
    private Boolean isPreferred = false;
    
    @Column(name = "notes")
    private String notes;
    
    @Column(name = "is_active")
    private Boolean isActive = true;
    
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private ZonedDateTime createdAt;
    
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
     * Checks if this contact person is the preferred contact.
     * 
     * @return true if this is the preferred contact
     */
    public boolean isPreferred() {
        return isPreferred != null && isPreferred;
    }
    
    /**
     * Checks if this contact person is active.
     * 
     * @return true if this contact person is active
     */
    public boolean isActive() {
        return isActive != null && isActive;
    }
    
    /**
     * Sets this contact person as the preferred contact.
     */
    public void setAsPreferred() {
        this.isPreferred = true;
    }
    
    /**
     * Removes preferred status from this contact person.
     */
    public void removePreferred() {
        this.isPreferred = false;
    }
    
    /**
     * Activates this contact person.
     */
    public void activate() {
        this.isActive = true;
    }
    
    /**
     * Deactivates this contact person.
     */
    public void deactivate() {
        this.isActive = false;
    }
} 