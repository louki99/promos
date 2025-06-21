# Partner Mapper Refactoring

## 🎯 **Problem Identified**

You correctly identified that the **mapper layer needed refactoring** to align with the type-specific controllers and DTOs:

### **Original Problems:**
1. **❌ Single generic mapper** - `PartnerMapper` tried to handle all partner types
2. **❌ Complex mappings** - All embedded object mappings in one place
3. **❌ No type-specific mappers** - No dedicated mappers for B2B and B2C
4. **❌ Inconsistent with controllers** - Controllers were type-specific but mappers weren't
5. **❌ MapStruct limitations** - Abstract types caused compilation issues

## ✅ **Solution Implemented**

### **New Mapper Architecture:**

```
mapper/
├── PartnerMapper.java              # Main mapper (backward compatibility)
├── PartnerMapperFactory.java       # Factory for unified mapping
├── B2BPartnerMapper.java           # Type-specific B2B mapper
└── B2CPartnerMapper.java           # Type-specific B2C mapper
```

### **1. Type-Specific Mappers**

#### **B2BPartnerMapper**
- **Purpose**: Handles B2B-specific mappings including company and contract information
- **Features**:
  - Maps `B2BPartner` ↔ `B2BPartnerDTO`
  - Maps `B2BPartner` → `PartnerDTO` (for backward compatibility)
  - Handles embedded objects: `CompanyInfo`, `ContractInfo`
  - Type-safe with proper validation

#### **B2CPartnerMapper**
- **Purpose**: Handles B2C-specific mappings including personal information
- **Features**:
  - Maps `B2CPartner` ↔ `B2CPartnerDTO`
  - Maps `B2CPartner` → `PartnerDTO` (for backward compatibility)
  - Handles direct properties: `personalIdNumber`, `dateOfBirth`, etc.
  - Type-safe with proper validation

### **2. Factory Pattern**

#### **PartnerMapperFactory**
- **Purpose**: Provides unified interface for mapping operations
- **Features**:
  - Delegates to appropriate type-specific mapper
  - Handles type detection and conversion
  - Provides backward compatibility methods
  - Supports both individual and bulk operations

### **3. Backward Compatibility**

#### **PartnerMapper (Updated)**
- **Purpose**: Maintains existing interface for backward compatibility
- **Features**:
  - Delegates to `PartnerMapperFactory`
  - Preserves existing method signatures
  - Handles type-specific updates manually
  - Supports gradual migration

## 🚀 **Benefits of New Architecture**

### **✅ Type Safety**
- **B2BPartnerMapper** - Only handles B2B partners and DTOs
- **B2CPartnerMapper** - Only handles B2C partners and DTOs
- **Compile-time validation** - Type mismatches caught at compile time
- **Proper validation** - Each mapper validates its specific type

### **✅ Separation of Concerns**
- **B2B mappings** - Company info, contract details, business metrics
- **B2C mappings** - Personal info, marketing preferences, individual data
- **Common mappings** - Contact, credit, loyalty, audit information
- **Clear boundaries** - Each mapper handles its specific domain

### **✅ Maintainability**
- **Easier testing** - Type-specific mappers are easier to test
- **Better debugging** - Clear separation makes issues easier to trace
- **Reduced complexity** - Each mapper has focused responsibility
- **Easier extensions** - Adding new partner types is straightforward

### **✅ Performance**
- **Optimized mappings** - Type-specific mappers are more efficient
- **Reduced overhead** - No unnecessary type checking at runtime
- **Better caching** - MapStruct can optimize type-specific mappings
- **Faster compilation** - Smaller, focused mapper classes

### **✅ Developer Experience**
- **Clear API** - Developers know which mapper to use
- **Intuitive usage** - Type-specific methods make intent clear
- **Better IDE support** - Autocomplete and validation work better
- **Consistent patterns** - Similar operations follow consistent patterns

## 📋 **Usage Guide**

### **For New Code (Recommended)**

#### **B2B Partner Operations:**
```java
@Service
@RequiredArgsConstructor
public class B2BPartnerService {
    private final B2BPartnerMapper b2bPartnerMapper;
    
    public B2BPartnerDTO createB2BPartner(B2BPartnerDTO dto) {
        B2BPartner entity = b2bPartnerMapper.toEntity(dto);
        // ... business logic
        return b2bPartnerMapper.toDTO(savedEntity);
    }
    
    public List<B2BPartnerDTO> getAllB2BPartners(List<B2BPartner> entities) {
        return b2bPartnerMapper.toDTOList(entities);
    }
}
```

#### **B2C Partner Operations:**
```java
@Service
@RequiredArgsConstructor
public class B2CPartnerService {
    private final B2CPartnerMapper b2cPartnerMapper;
    
    public B2CPartnerDTO createB2CPartner(B2CPartnerDTO dto) {
        B2CPartner entity = b2cPartnerMapper.toEntity(dto);
        // ... business logic
        return b2cPartnerMapper.toDTO(savedEntity);
    }
    
    public List<B2CPartnerDTO> getAllB2CPartners(List<B2CPartner> entities) {
        return b2cPartnerMapper.toDTOList(entities);
    }
}
```

#### **Unified Operations (Factory):**
```java
@Service
@RequiredArgsConstructor
public class PartnerService {
    private final PartnerMapperFactory partnerMapperFactory;
    
    public Object getPartner(Partner entity) {
        // Returns appropriate DTO type based on partner type
        return partnerMapperFactory.toDTO(entity);
    }
    
    public PartnerDTO getGenericPartner(Partner entity) {
        // Always returns PartnerDTO for backward compatibility
        return partnerMapperFactory.toGenericDTO(entity);
    }
    
    public Partner createPartner(PartnerDTO dto) {
        // Creates appropriate entity type based on DTO type
        return partnerMapperFactory.toEntity(dto);
    }
}
```

### **For Existing Code (Backward Compatibility)**

#### **Existing Service Code:**
```java
@Service
@RequiredArgsConstructor
public class PartnerService {
    private final PartnerMapper partnerMapper; // Still works!
    
    public PartnerDTO getPartner(Partner partner) {
        return partnerMapper.toDTO(partner); // Delegates to factory
    }
    
    public Partner createPartner(PartnerDTO dto) {
        return partnerMapper.toEntity(dto); // Delegates to factory
    }
    
    public void updatePartner(PartnerDTO dto, Partner partner) {
        partnerMapper.updateEntityFromDTO(dto, partner); // Manual updates
    }
}
```

## 🔄 **Migration Strategy**

### **Phase 1: Gradual Migration**
1. **Keep existing code** - All existing code continues to work
2. **Use new mappers** - Start using type-specific mappers in new code
3. **Test thoroughly** - Ensure both approaches work correctly

### **Phase 2: Service Layer Updates**
1. **Update B2B services** - Use `B2BPartnerMapper` directly
2. **Update B2C services** - Use `B2CPartnerMapper` directly
3. **Update controllers** - Use type-specific DTOs and mappers

### **Phase 3: Complete Migration**
1. **Remove old code** - Once all code is migrated
2. **Optimize performance** - Remove factory overhead where not needed
3. **Update documentation** - Reflect new architecture

## 🎯 **Best Practices**

### **✅ Use Type-Specific Mappers**
```java
// ✅ Good - Type-specific
B2BPartnerDTO dto = b2bPartnerMapper.toDTO(b2bPartner);

// ❌ Avoid - Generic (unless needed for compatibility)
PartnerDTO dto = partnerMapper.toDTO(partner);
```

### **✅ Use Appropriate DTOs**
```java
// ✅ Good - Type-specific DTOs
B2BPartnerDTO b2bDto = new B2BPartnerDTO();
b2bDto.setCompanyName("Acme Corp");
b2bDto.setContractNumber("CON-001");

// ❌ Avoid - Generic DTOs for type-specific operations
PartnerDTO genericDto = new PartnerDTO();
genericDto.setCompanyName("Acme Corp"); // Less type-safe
```

### **✅ Leverage Factory for Mixed Operations**
```java
// ✅ Good - Factory for mixed types
List<Partner> mixedPartners = getMixedPartners();
List<PartnerDTO> dtos = partnerMapperFactory.toDTOList(mixedPartners);

// ✅ Good - Type-specific for known types
List<B2BPartner> b2bPartners = getB2BPartners();
List<B2BPartnerDTO> b2bDtos = b2bPartnerMapper.toDTOList(b2bPartners);
```

### **✅ Handle Type-Specific Updates**
```java
// ✅ Good - Type-specific updates
B2BPartner b2bPartner = getB2BPartner();
B2BPartnerDTO updates = getUpdates();
b2bPartnerMapper.updateEntityFromDTO(updates, b2bPartner);

// ✅ Good - Manual updates for complex scenarios
Partner partner = getPartner();
PartnerDTO updates = getUpdates();
partnerMapper.updateEntityFromDTO(updates, partner);
```

## 🎯 **Conclusion**

The mapper refactoring addresses the fundamental issues you identified:

### **✅ Problems Solved:**
- **Type safety** - Each mapper handles its specific type
- **Separation of concerns** - Clear boundaries between B2B and B2C
- **Maintainability** - Easier to test, debug, and extend
- **Performance** - Optimized for specific types
- **Developer experience** - Clear, intuitive API

### **✅ Architecture Now:**
- **Type-specific mappers** - `B2BPartnerMapper` and `B2CPartnerMapper`
- **Factory pattern** - `PartnerMapperFactory` for unified operations
- **Backward compatibility** - Existing code continues to work
- **Future-ready** - Easy to add new partner types
- **Production-ready** - Enterprise-grade mapping architecture

This refactoring makes the Partner microservice much more maintainable and type-safe while preserving all existing functionality and providing a clear migration path. 