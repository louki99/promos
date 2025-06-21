# Partner Domain Model Refactoring Documentation

## Overview

The Partner domain model has been successfully refactored to implement a clean inheritance structure following Domain-Driven Design (DDD) best practices. This refactoring separates B2B and B2C partner concerns while maintaining a unified interface for common operations.

## Architecture

### Inheritance Structure

```
Partner (Abstract Base Class)
├── B2BPartner (Concrete Implementation)
└── B2CPartner (Concrete Implementation)
```

### JPA Inheritance Strategy

- **Strategy**: `InheritanceType.SINGLE_TABLE`
- **Discriminator**: `partner_type` column with values "B2B" and "B2C"
- **Rationale**: Better performance for queries and simpler database structure

## Domain Model Components

### 1. Abstract Base Class: `Partner`

**Purpose**: Contains common attributes and behavior shared by all partner types.

**Key Features**:
- Common identification fields (`ctNum`, `ice`, `description`)
- Embedded objects for related data:
  - `ContactInfo` - Contact details
  - `CreditInfo` - Financial information
  - `LoyaltyInfo` - Loyalty and VIP status
  - `DeliveryPreference` - Delivery preferences
  - `AuditInfo` - Audit and tracking information
- Abstract methods that enforce type-specific behavior:
  - `getPartnerType()` - Returns the partner type
  - `canPlaceOrder()` - Type-specific order validation
  - `isValid()` - Type-specific validation rules

**Common Business Methods**:
- `isB2B()` / `isB2C()` - Type checking
- `hasSufficientCredit(BigDecimal amount)` - Credit validation
- `getAvailableCredit()` - Available credit calculation
- `isVip()` - VIP status check
- `getLoyaltyLevel()` - Loyalty level calculation (0-5)
- `addLoyaltyPoints(int points)` - Loyalty points management
- `updateOrderStats(BigDecimal orderValue)` - Order statistics update

### 2. Concrete Implementation: `B2BPartner`

**Purpose**: Represents business-to-business partners with company-specific attributes and validation rules.

**B2B-Specific Attributes**:
- `CompanyInfo` - Company details (name, legal form, registration, etc.)
- `ContractInfo` - Contract management (number, dates, terms, etc.)

**B2B-Specific Business Methods**:
- `hasValidContract()` - Contract validity check
- `isContractExpiringSoon(int daysThreshold)` - Contract expiration warning
- `getDaysUntilContractExpiration()` - Days until contract expires
- `hasOverduePayments()` - Payment status check

**B2B Validation Rules**:
- Active status required
- Valid contract required
- Sufficient credit limit required
- Company information mandatory
- Contract information mandatory

### 3. Concrete Implementation: `B2CPartner`

**Purpose**: Represents business-to-consumer partners with individual-specific attributes and validation rules.

**B2C-Specific Attributes**:
- `personalIdNumber` - Personal identification
- `dateOfBirth` - Date of birth
- `preferredLanguage` - Language preference
- `marketingConsent` - Marketing communication consent

**B2C-Specific Business Methods**:
- `isEligibleForMarketing()` - Marketing consent check
- `getAge()` - Age calculation
- `isMinor()` - Minor status check
- `hasValidPersonalIdNumber()` - ID number validation

**B2C Validation Rules**:
- Active status required
- Valid personal information required
- Contact information mandatory
- Credit limit optional but validated if present

## Embedded Objects

### ContactInfo
- `telephone`, `telecopie`, `email`
- `address`, `city`, `country`, `region`, `postalCode`

### CreditInfo
- `creditLimit`, `creditRating`, `creditScore`
- `outstandingBalance`, `paymentHistory`
- `lastPaymentDate`, `paymentTermDays`
- `preferredPaymentMethod`, `bankAccountInfo`

### LoyaltyInfo
- `isVip`, `loyaltyPoints`
- `totalOrders`, `totalSpent`, `averageOrderValue`
- `lastOrderDate`, `partnerSince`

### DeliveryPreference
- `preferredDeliveryTime`
- `preferredDeliveryDays`
- `specialHandlingInstructions`

### AuditInfo
- `notes`, `active`, `lastActivityDate`
- `createdBy`, `updatedBy`
- `createdAt`, `updatedAt`

### CompanyInfo (B2B-specific)
- `companyName`, `legalForm`, `registrationNumber`
- `taxId`, `vatNumber`, `businessActivity`
- `annualTurnover`, `numberOfEmployees`

### ContractInfo (B2B-specific)
- `contractNumber`, `contractStartDate`, `contractEndDate`
- `contractType`, `contractTerms`
- `paymentTerms`, `deliveryTerms`, `specialConditions`

## DTO Structure

### BasePartnerDTO (Abstract)
- Common attributes for all partner types
- Validation annotations for shared fields

### B2BPartnerDTO
- Extends `BasePartnerDTO`
- B2B-specific fields with validation
- Company and contract information

### B2CPartnerDTO
- Extends `BasePartnerDTO`
- B2C-specific fields with validation
- Personal information and preferences

### PartnerDTO (Legacy)
- Deprecated for backward compatibility
- Contains all possible fields for both types
- Should be replaced with type-specific DTOs

## Business Logic Separation

### Order Placement Logic

**B2B Partners**:
```java
public boolean canPlaceOrder() {
    return isActive() && hasValidContract() && hasSufficientCredit(orderAmount);
}
```

**B2C Partners**:
```java
public boolean canPlaceOrder() {
    return isActive() && hasValidPersonalIdNumber() && 
           (creditLimit == null || hasSufficientCredit(orderAmount));
}
```

### Validation Logic

**B2B Partners**:
- Company information required
- Contract information required
- Contract dates validation
- Business-specific rules

**B2C Partners**:
- Personal information required
- Contact information required
- Age validation (for minors)
- Consumer-specific rules

## Repository Layer

The `PartnerRepository` provides type-specific queries:
- `findByPartnerType(PartnerType type)`
- `countByPartnerType(PartnerType type)`
- B2B-specific queries for contracts and business metrics
- B2C-specific queries for personal information

## Service Layer

The `PartnerService` implements comprehensive business logic:
- CRUD operations with type-specific validation
- Business operations (credit management, loyalty points)
- Type-specific operations (contract management for B2B)
- Search and filtering capabilities

## Benefits of the Refactoring

### 1. **Clean Separation of Concerns**
- B2B and B2C logic are clearly separated
- Each type has its own validation rules
- Domain logic is encapsulated within the appropriate classes

### 2. **Type Safety**
- Compile-time type checking
- Prevents mixing B2B and B2C attributes
- Clear interfaces for each partner type

### 3. **Maintainability**
- Easy to add new partner types
- Changes to one type don't affect the other
- Clear responsibility boundaries

### 4. **Performance**
- Single table inheritance for efficient queries
- Proper indexing on discriminator column
- Optimized for common use cases

### 5. **Extensibility**
- Easy to add new partner types
- Simple to extend with new attributes
- Flexible business rule implementation

## Migration Strategy

### Database Migration
- Existing data is preserved in the single table
- New discriminator column added
- Backward compatibility maintained

### Code Migration
- Legacy `PartnerDTO` marked as deprecated
- New type-specific DTOs introduced
- Gradual migration to new structure

## Best Practices Implemented

### 1. **Domain-Driven Design**
- Rich domain models with business logic
- Value objects for related data
- Clear aggregate boundaries

### 2. **SOLID Principles**
- Single Responsibility Principle
- Open/Closed Principle
- Liskov Substitution Principle
- Interface Segregation Principle
- Dependency Inversion Principle

### 3. **JPA Best Practices**
- Proper inheritance mapping
- Embedded objects for related data
- Optimized query strategies

### 4. **Validation**
- Type-specific validation rules
- Comprehensive error handling
- Clear validation messages

## Future Enhancements

### 1. **Additional Partner Types**
- Easy to add new partner types (e.g., B2G for government)
- Extend the inheritance hierarchy
- Add type-specific validation rules

### 2. **Enhanced Business Rules**
- Complex credit scoring algorithms
- Advanced loyalty programs
- Dynamic pricing based on partner type

### 3. **Integration Points**
- Event-driven architecture
- External system integrations
- Real-time validation services

## Conclusion

The refactored Partner domain model successfully implements a clean inheritance structure that separates B2B and B2C concerns while maintaining a unified interface. The architecture is future-proof, maintainable, and follows DDD best practices. The implementation provides excellent performance, type safety, and extensibility for future enhancements. 