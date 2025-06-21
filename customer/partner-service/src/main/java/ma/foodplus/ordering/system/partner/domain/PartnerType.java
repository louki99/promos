package ma.foodplus.ordering.system.partner.domain;

/**
 * Enumeration of partner types in the system.
 * 
 * <p>This enum defines the different types of partners that can be managed:</p>
 * <ul>
 *   <li><strong>B2B</strong>: Business-to-Business partners (customers)</li>
 *   <li><strong>B2C</strong>: Business-to-Consumer partners (individual customers)</li>
 *   <li><strong>SUPPLIER</strong>: Supplier/Fournisseur partners (vendors)</li>
 * </ul>
 * 
 * @author FoodPlus Development Team
 * @version 3.0.0
 */
public enum PartnerType {
    B2B,        // Business partners (customers)
    B2C,        // Individual partners (customers)
    SUPPLIER    // Supplier/Fournisseur partners (vendors)
} 