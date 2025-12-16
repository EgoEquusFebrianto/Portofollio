#!/bin/bash
echo "Kafka Producer - Starting..."

# Check Java
if ! command -v java &> /dev/null; then
    echo "Java not found. Please install Java 11+"
    exit 1
fi

# Check JAR exists
if [ ! -f "RetailProducerApp.jar" ]; then
    echo "RetailProducerApp.jar not found"
    exit 1
fi

echo "Java: $(java -version 2>&1 | head -1)"
echo "JAR: $(ls -lh RetailProducerApp.jar | awk '{print $5}')"
echo ""
echo "Running: java -jar RetailProducerApp.jar $@"
echo ""

# Run with all arguments
java -jar RetailProducerApp.jar "$@"