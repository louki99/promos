# Partner Microservice API Documentation

## Overview

The Partner microservice provides comprehensive APIs for managing business partners, customers, and B2B relationships. The service supports both B2B and B2C partner types with specialized operations for each.

## Base URL

```
http://localhost:8080/api/v1
```

## API Endpoints

### 1. General Partner Management

#### Base Path: `/partners`

| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/partners` | Create a new partner |
| `GET` | `/partners/{id}` | Get partner by ID |
| `PUT` | `/partners/{id}` | Update partner |
| `DELETE` | `/partners/{id}` | Delete partner (soft delete) |
| `GET` | `/partners` | Get all partners with pagination |
| `GET` | `/partners/search` | Search partners |
| `GET` | `/partners/by-ct-num/{ctNum}` | Get partner by CT number |
| `GET` | `/partners/by-ice/{ice}` | Get partner by ICE |

#### Business Operations

| Method | Endpoint | Description |
|--------|----------|-------------|
| `GET` | `/partners/active` | Get active partners |
| `GET` | `/partners/vip` | Get VIP partners |
| `POST` | `/partners/{id}/activate` | Activate partner |
| `POST` | `/partners/{id}/deactivate` | Deactivate partner |

#### Loyalty Operations

| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/partners/{id}/loyalty-points` | Update loyalty points |
| `GET` | `/partners/{id}/loyalty-level` | Get loyalty level |

#### Credit Operations

| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/partners/{id}/credit-limit` | Update credit limit |
| `GET` | `/partners/{id}/total-spent` | Get total spent |

#### Group Operations

| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/partners/{partnerId}/groups/{groupId}` | Add partner to group |
| `DELETE` | `/partners/{partnerId}/groups/{groupId}` | Remove partner from group |
| `GET` | `/partners/{partnerId}/groups/{groupId}/check` | Check if partner is in group |

#### Statistics and Reporting

| Method | Endpoint | Description |
|--------|----------|-------------|
| `GET` | `/partners/statistics` | Get partner statistics |
| `GET` | `/partners/top-spenders` | Get top partners by spending |
| `GET` | `/partners/distribution-by-type` | Get partner distribution by type |

### 2. B2B Partner Management

#### Base Path: `/partners/b2b`

| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/partners/b2b` | Create a new B2B partner |
| `PUT` | `/partners/b2b/{id}` | Update B2B partner |
| `GET` | `/partners/b2b` | Get all B2B partners with pagination |
| `GET` | `/partners/b2b/all` | Get all B2B partners (no pagination) |

#### Contract Management

| Method | Endpoint | Description |
|--------|----------|-------------|
| `GET` | `/partners/b2b/expiring-contracts` | Get partners with expiring contracts |
| `POST` | `/partners/b2b/{id}/renew-contract` | Renew B2B partner contract |
| `POST` | `/partners/b2b/{id}/terminate-contract` | Terminate B2B partner contract |
| `GET` | `/partners/b2b/{id}/contract-status` | Get B2B partner contract status |

#### Business Analytics

| Method | Endpoint | Description |
|--------|----------|-------------|
| `GET` | `/partners/b2b/by-annual-turnover` | Get B2B partners by annual turnover range |
| `GET` | `/partners/b2b/by-business-activity` | Get B2B partners by business activity |
| `GET` | `/partners/b2b/overdue-payments` | Get B2B partners with overdue payments |

#### Credit Management

| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/partners/b2b/{id}/process-payment` | Process payment for B2B partner |
| `GET` | `/partners/b2b/{id}/credit-summary` | Get B2B partner credit summary |

#### Validation

| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/partners/b2b/{id}/validate-order` | Validate B2B partner order placement |
| `GET` | `/partners/b2b/{id}/validation-status` | Get B2B partner validation status |

### 3. B2C Partner Management

#### Base Path: `/partners/b2c`

| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/partners/b2c` | Create a new B2C partner |
| `PUT` | `/partners/b2c/{id}` | Update B2C partner |
| `GET` | `/partners/b2c` | Get all B2C partners with pagination |
| `GET` | `/partners/b2c/all` | Get all B2C partners (no pagination) |

#### Personal Information Management

| Method | Endpoint | Description |
|--------|----------|-------------|
| `GET` | `/partners/b2c/by-age-range` | Get B2C partners by age range |
| `GET` | `/partners/b2c/minors` | Get B2C partners who are minors |
| `GET` | `/partners/b2c/by-language` | Get B2C partners by preferred language |

#### Marketing Management

| Method | Endpoint | Description |
|--------|----------|-------------|
| `GET` | `/partners/b2c/marketing-eligible` | Get B2C partners eligible for marketing |
| `POST` | `/partners/b2c/{id}/update-marketing-consent` | Update B2C partner marketing consent |

#### Credit Management

| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/partners/b2c/{id}/process-payment` | Process payment for B2C partner |
| `GET` | `/partners/b2c/{id}/credit-summary` | Get B2C partner credit summary |

#### Validation

| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/partners/b2c/{id}/validate-order` | Validate B2C partner order placement |
| `GET` | `/partners/b2c/{id}/validation-status` | Get B2C partner validation status |

#### Loyalty and Rewards

| Method | Endpoint | Description |
|--------|----------|-------------|
| `GET` | `/partners/b2c/loyalty-leaders` | Get B2C partners with highest loyalty |
| `POST` | `/partners/b2c/{id}/add-loyalty-points` | Add loyalty points to B2C partner |

#### Analytics

| Method | Endpoint | Description |
|--------|----------|-------------|
| `GET` | `/partners/b2c/{id}/performance-metrics` | Get B2C partner performance metrics |
| `GET` | `/partners/b2c/{id}/growth-trends` | Get B2C partner growth trends |

### 4. Bulk Operations

#### Base Path: `/partners/bulk`

| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/partners/bulk/activate` | Bulk activate partners |
| `POST` | `/partners/bulk/deactivate` | Bulk deactivate partners |
| `POST` | `/partners/bulk/update-credit-limits` | Bulk update credit limits |
| `POST` | `/partners/bulk/add-to-group` | Bulk add partners to group |
| `POST` | `/partners/bulk/validate-orders` | Bulk validate order placement |
| `POST` | `/partners/bulk/performance-metrics` | Bulk get performance metrics |
| `POST` | `/partners/bulk/export` | Bulk export partners |
| `POST` | `/partners/bulk/import` | Bulk import partners |
| `POST` | `/partners/bulk/send-notifications` | Bulk send notifications |

### 5. Partner Statistics & Analytics

#### Base Path: `/partner-statistics`

| Method | Endpoint | Description |
|--------|----------|-------------|
| `GET` | `/partner-statistics/overview` | Get partner overview statistics |
| `GET` | `/partner-statistics/top-spenders` | Get top spending partners |
| `GET` | `/partner-statistics/distribution/type` | Get partner distribution by type |
| `GET` | `/partner-statistics/average-order-value` | Get average order value by partner type |
| `GET` | `/partner-statistics/expiring-contracts` | Get partners with expiring contracts |
| `GET` | `/partner-statistics/overdue-payments` | Get partners with overdue payments |
| `GET` | `/partner-statistics/by-credit-rating/{creditRating}` | Get partners by credit rating |
| `GET` | `/partner-statistics/by-business-activity` | Get partners by business activity |
| `GET` | `/partner-statistics/by-annual-turnover` | Get partners by annual turnover range |
| `GET` | `/partner-statistics/vip` | Get VIP partners |
| `GET` | `/partner-statistics/active` | Get active partners |

### 6. Partner Group Management

#### Base Path: `/partner-groups`

| Method | Endpoint | Description |
|--------|----------|-------------|
| `GET` | `/partner-groups/{groupId}/partners` | Get partners in group |
| `POST` | `/partner-groups/{groupId}/partners/{partnerId}` | Add partner to group |
| `DELETE` | `/partner-groups/{groupId}/partners/{partnerId}` | Remove partner from group |
| `GET` | `/partner-groups/{groupId}/partners/{partnerId}/check` | Check partner group membership |

### 7. Partner Audit & History

#### Base Path: `/partners/audit`

| Method | Endpoint | Description |
|--------|----------|-------------|
| `GET` | `/partners/audit/{partnerId}/history` | Get partner audit history |
| `GET` | `/partners/audit/{partnerId}/activity-log` | Get partner activity log |
| `GET` | `/partners/audit/system/changes` | Get system-wide partner changes |
| `GET` | `/partners/audit/system/activity-summary` | Get system activity summary |
| `GET` | `/partners/audit/user/{userId}/activities` | Get user activities |
| `GET` | `/partners/audit/compliance/report` | Generate compliance report |
| `GET` | `/partners/audit/compliance/violations` | Get compliance violations |
| `POST` | `/partners/audit/data-retention/cleanup` | Clean up old audit data |
| `GET` | `/partners/audit/data-retention/status` | Get data retention status |
| `POST` | `/partners/audit/export/audit-trail` | Export audit trail |
| `POST` | `/partners/audit/backup/audit-data` | Backup audit data |

## Data Models

### PartnerDTO
```json
{
  "id": 1,
  "ctNum": "CT123456789",
  "ice": "123456789012345",
  "description": "Partner Description",
  "partnerType": "B2B",
  "telephone": "1234567890",
  "email": "partner@example.com",
  "address": "123 Main St",
  "codePostal": "12345",
  "ville": "City",
  "country": "Country",
  "categoryTarifId": 1,
  "creditLimit": 10000.00,
  "currentCredit": 5000.00,
  "paymentTermDays": 30,
  "creditRating": "A",
  "creditScore": 85,
  "paymentHistory": "Good",
  "outstandingBalance": 2000.00,
  "lastPaymentDate": "2024-01-15T10:30:00Z",
  "preferredPaymentMethod": "Bank Transfer",
  "bankAccountInfo": "Account details",
  "vip": false,
  "loyaltyPoints": 150,
  "lastOrderDate": "2024-01-10T14:20:00Z",
  "totalOrders": 25,
  "totalSpent": 15000.00,
  "averageOrderValue": 600.00,
  "customerSince": "2023-01-01T00:00:00Z",
  "preferredDeliveryTime": "09:00-12:00",
  "preferredDeliveryDays": "Monday,Wednesday,Friday",
  "specialHandlingInstructions": "Handle with care",
  "notes": "Important customer",
  "active": true,
  "lastActivityDate": "2024-01-15T16:45:00Z",
  "createdBy": "admin",
  "updatedBy": "admin",
  "createdAt": "2023-01-01T00:00:00Z",
  "updatedAt": "2024-01-15T16:45:00Z"
}
```

### B2BPartnerDTO
```json
{
  "id": 1,
  "ctNum": "CT123456789",
  "ice": "123456789012345",
  "description": "B2B Partner Description",
  "partnerType": "B2B",
  "companyName": "ABC Corporation",
  "legalForm": "SARL",
  "registrationNumber": "REG123456",
  "taxId": "TAX123456",
  "vatNumber": "VAT123456",
  "businessActivity": "Technology",
  "annualTurnover": 1000000.00,
  "numberOfEmployees": 50,
  "contractNumber": "CON123456",
  "contractStartDate": "2023-01-01T00:00:00Z",
  "contractEndDate": "2024-12-31T23:59:59Z",
  "contractType": "Standard",
  "contractTerms": "Standard terms and conditions",
  "paymentTerms": "Net 30",
  "deliveryTerms": "FOB",
  "specialConditions": "None"
}
```

### B2CPartnerDTO
```json
{
  "id": 2,
  "ctNum": "CT987654321",
  "ice": "987654321098765",
  "description": "B2C Partner Description",
  "partnerType": "B2C",
  "personalIdNumber": "ID123456789",
  "dateOfBirth": "1990-05-15",
  "preferredLanguage": "English",
  "marketingConsent": true
}
```

## Error Responses

### Standard Error Format
```json
{
  "timestamp": "2024-01-15T16:45:00Z",
  "status": 400,
  "error": "Bad Request",
  "message": "Validation failed",
  "path": "/api/v1/partners",
  "details": [
    {
      "field": "ctNum",
      "message": "CT number is required"
    }
  ]
}
```

### Common HTTP Status Codes

| Status Code | Description |
|-------------|-------------|
| `200` | OK - Request successful |
| `201` | Created - Resource created successfully |
| `204` | No Content - Request successful, no content to return |
| `400` | Bad Request - Invalid input data |
| `401` | Unauthorized - Authentication required |
| `403` | Forbidden - Insufficient permissions |
| `404` | Not Found - Resource not found |
| `409` | Conflict - Resource already exists |
| `422` | Unprocessable Entity - Validation failed |
| `500` | Internal Server Error - Server error |

## Authentication

The API uses JWT-based authentication. Include the JWT token in the Authorization header:

```
Authorization: Bearer <jwt_token>
```

## Rate Limiting

- **Standard endpoints**: 1000 requests per hour
- **Bulk operations**: 100 requests per hour
- **Statistics endpoints**: 500 requests per hour

## Pagination

For endpoints that return lists, pagination is supported using Spring Data's Pageable:

```
GET /api/v1/partners?page=0&size=20&sort=id,desc
```

### Pagination Parameters

| Parameter | Type | Description | Default |
|-----------|------|-------------|---------|
| `page` | Integer | Page number (0-based) | 0 |
| `size` | Integer | Page size | 20 |
| `sort` | String | Sort field and direction | `id,desc` |

### Pagination Response Format

```json
{
  "content": [...],
  "pageable": {
    "sort": {
      "sorted": true,
      "unsorted": false
    },
    "pageNumber": 0,
    "pageSize": 20,
    "offset": 0,
    "paged": true,
    "unpaged": false
  },
  "totalElements": 100,
  "totalPages": 5,
  "last": false,
  "first": true,
  "sort": {
    "sorted": true,
    "unsorted": false
  },
  "numberOfElements": 20,
  "size": 20,
  "number": 0
}
```

## Filtering and Search

### Search Parameters

| Parameter | Type | Description |
|-----------|------|-------------|
| `search` | String | Search term for description, CT number, ICE |
| `partnerType` | String | Filter by partner type (B2B, B2C) |
| `active` | Boolean | Filter by active status |
| `vip` | Boolean | Filter by VIP status |
| `creditRating` | String | Filter by credit rating |
| `businessActivity` | String | Filter by business activity (B2B) |
| `preferredLanguage` | String | Filter by preferred language (B2C) |

### Example Search Request

```
GET /api/v1/partners?search=ABC&partnerType=B2B&active=true&page=0&size=10
```

## Bulk Operations

### Bulk Update Request Format

```json
{
  "partnerIds": [1, 2, 3, 4, 5],
  "operation": "UPDATE_STATUS",
  "parameters": {
    "active": true
  }
}
```

### Bulk Operation Response Format

```json
{
  "message": "Bulk operation completed successfully",
  "updatedCount": 5,
  "totalRequested": 5,
  "failedCount": 0,
  "errors": []
}
```

## Webhooks

The service supports webhooks for real-time notifications. Configure webhooks to receive notifications for:

- Partner creation
- Partner updates
- Contract renewals
- Payment processing
- Status changes

### Webhook Payload Format

```json
{
  "event": "PARTNER_CREATED",
  "timestamp": "2024-01-15T16:45:00Z",
  "partnerId": 123,
  "data": {
    "ctNum": "CT123456789",
    "partnerType": "B2B",
    "companyName": "ABC Corporation"
  }
}
```

## SDKs and Libraries

### Java Client

```xml
<dependency>
    <groupId>ma.foodplus</groupId>
    <artifactId>partner-client</artifactId>
    <version>1.0.0</version>
</dependency>
```

### JavaScript/TypeScript Client

```bash
npm install @foodplus/partner-client
```

## Examples

### Creating a B2B Partner

```bash
curl -X POST "http://localhost:8080/api/v1/partners/b2b" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <jwt_token>" \
  -d '{
    "ctNum": "CT123456789",
    "ice": "123456789012345",
    "description": "ABC Corporation",
    "partnerType": "B2B",
    "companyName": "ABC Corporation",
    "legalForm": "SARL",
    "contractNumber": "CON123456",
    "contractStartDate": "2024-01-01T00:00:00Z",
    "contractEndDate": "2024-12-31T23:59:59Z"
  }'
```

### Creating a B2C Partner

```bash
curl -X POST "http://localhost:8080/api/v1/partners/b2c" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <jwt_token>" \
  -d '{
    "ctNum": "CT987654321",
    "ice": "987654321098765",
    "description": "John Doe",
    "partnerType": "B2C",
    "personalIdNumber": "ID123456789",
    "dateOfBirth": "1990-05-15",
    "preferredLanguage": "English",
    "marketingConsent": true
  }'
```

### Bulk Activating Partners

```bash
curl -X POST "http://localhost:8080/api/v1/partners/bulk/activate" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <jwt_token>" \
  -d '[1, 2, 3, 4, 5]'
```

### Getting Partner Statistics

```bash
curl -X GET "http://localhost:8080/api/v1/partner-statistics/overview" \
  -H "Authorization: Bearer <jwt_token>"
```

## Support

For API support and questions:

- **Email**: api-support@foodplus.com
- **Documentation**: https://docs.foodplus.com/api
- **Status Page**: https://status.foodplus.com
- **Developer Portal**: https://developers.foodplus.com 