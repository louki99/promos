# Database Scripts

This directory contains database management scripts for the FoodPlus POS System.

## Scripts

### reset-database.sql
**Purpose**: Resets the database schema for development purposes.

**Usage**:
```bash
# Connect to PostgreSQL and run the script
psql -h 10.113.181.84 -U manager -d allinone -f scripts/reset-database.sql
```

**Warning**: This script will DROP and RECREATE the entire 'pos' schema. Use only in development environments!

**What it does**:
1. Drops the existing 'pos' schema and all its contents
2. Creates a fresh 'pos' schema
3. Sets the search path to the pos schema
4. Logs the completion

## When to Use

### Development Environment
- When you want to start fresh with a clean database
- After making changes to migration files
- When testing new features that require schema changes

### Production Environment
- **NEVER** use reset-database.sql in production
- Use proper backup and restore procedures
- Follow the migration system for schema changes

## Alternative Approaches

### Using Flyway Clean (Development Only)
If you have Flyway clean enabled, you can also use:

```yaml
# In application.yml (development only)
spring:
  flyway:
    clean-disabled: false
```

Then run:
```bash
./mvnw flyway:clean
./mvnw spring-boot:run
```

### Manual Schema Reset
If you prefer manual control:

```sql
-- Connect to PostgreSQL and run manually
DROP SCHEMA IF EXISTS pos CASCADE;
CREATE SCHEMA pos;
```

## Best Practices

1. **Always backup before resetting** (even in development)
2. **Use version control** for all schema changes
3. **Test migrations** after reset
4. **Document any manual changes** made outside of migrations
5. **Use Flyway for all schema changes** in production

## Troubleshooting

### Permission Issues
If you get permission errors:
```sql
-- Grant necessary permissions
GRANT ALL PRIVILEGES ON SCHEMA pos TO manager;
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA pos TO manager;
```

### Connection Issues
If you can't connect to the database:
1. Check if PostgreSQL is running
2. Verify connection parameters
3. Ensure firewall allows connections
4. Check user permissions

### Migration Issues
After reset, if migrations fail:
1. Check Flyway configuration
2. Verify migration files are in correct location
3. Ensure migration naming follows conventions
4. Check for syntax errors in migration files 