# Kafka Troubleshooting Guide

## Issue: Kafka Connection Failed

If you see errors like:
```
Connection to node -1 (localhost/127.0.0.1:9092) could not be established. Node may not be available.
```

## Root Cause
The application is trying to connect to the wrong Kafka port. Your Docker Compose setup exposes Kafka on port `29092`, but the application was configured to use port `9092`.

## Solution

### 1. Updated Configuration
I've already fixed the configuration files:

- **`application.yml`**: Updated default Kafka bootstrap servers to `localhost:29092`
- **`application-dev.yml`**: Added development-specific Kafka configuration

### 2. Verify Docker Compose is Running
Make sure your Kafka containers are running:

```bash
# Check if containers are running
docker-compose ps

# If not running, start them
docker-compose up -d

# Check Kafka logs
docker-compose logs kafka
```

### 3. Test Kafka Connectivity
Use the provided script to test connectivity:

```bash
# Run the connectivity check script
./scripts/check-kafka.sh

# Or test manually
telnet localhost 29092
```

### 4. Environment Variables
Set the correct environment variables:

```bash
export KAFKA_BOOTSTRAP_SERVERS=localhost:29092
export SPRING_PROFILES_ACTIVE=dev
```

### 5. Application Configuration
The application now uses these settings:

```yaml
spring:
  kafka:
    bootstrap-servers: localhost:29092  # Correct port for Docker Compose
    consumer:
      group-id: partner-service-group-dev
    producer:
      acks: all
      retries: 3
```

## Docker Compose Configuration
Your Docker Compose correctly exposes Kafka:

```yaml
kafka:
  ports:
    - 29092:29092  # External port mapping
  environment:
    KAFKA_ADVERTISED_LISTENERS: PLAINTEXT://kafka:9092,PLAINTEXT_HOST://localhost:29092
```

## Verification Steps

1. **Check if Kafka is accessible:**
   ```bash
   nc -z localhost 29092
   ```

2. **List Kafka topics:**
   ```bash
   docker run --rm -it confluentinc/cp-kafka:latest kafka-topics --bootstrap-server localhost:29092 --list
   ```

3. **Check application logs:**
   ```bash
   # Look for Kafka connection messages
   tail -f logs/foodplus-ordering-partners.log | grep -i kafka
   ```

4. **Test with kafka-console-producer:**
   ```bash
   docker run --rm -it confluentinc/cp-kafka:latest kafka-console-producer --bootstrap-server localhost:29092 --topic test-topic
   ```

## Common Issues and Solutions

### Issue 1: Port Already in Use
```bash
# Check what's using port 29092
sudo netstat -tulpn | grep 29092

# Kill the process if needed
sudo kill -9 <PID>
```

### Issue 2: Docker Compose Not Running
```bash
# Start all services
docker-compose up -d

# Check status
docker-compose ps
```

### Issue 3: Wrong Profile Active
```bash
# Ensure dev profile is active
export SPRING_PROFILES_ACTIVE=dev

# Or set in application.yml
spring:
  profiles:
    active: dev
```

### Issue 4: Network Issues
```bash
# Check Docker network
docker network ls
docker network inspect <network_name>

# Restart Docker if needed
sudo systemctl restart docker
```

## Monitoring Kafka

### 1. Kowl (Web UI)
Access at: http://localhost:8090

### 2. Kafka Manager
Access at: http://localhost:9000

### 3. Schema Registry
Access at: http://localhost:8081

## Application Restart
After fixing the configuration:

```bash
# Stop the application
# Rebuild if needed
mvn clean compile

# Start with correct profile
export SPRING_PROFILES_ACTIVE=dev
java -jar target/partner-service.jar
```

## Expected Behavior
After fixing the configuration, you should see:

```
✅ Kafka connection established
✅ Topics created successfully
✅ Producer/Consumer working
```

## Still Having Issues?

1. Check the application logs for detailed error messages
2. Verify Docker Compose is running: `docker-compose ps`
3. Test Kafka connectivity: `./scripts/check-kafka.sh`
4. Check if the correct profile is active
5. Verify no firewall blocking port 29092 