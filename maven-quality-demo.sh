#!/bin/bash

# Maven Quality Checks Demo Script
# Demonstrates all Maven-based quality checks for MCP NTools Java

echo "========================================"
echo "  Maven Quality Checks Demonstration"
echo "========================================"
echo

cd "$(dirname "$0")"

echo "🏗️  Maven-Based Quality Pipeline"
echo "All quality checks are integrated into Maven lifecycle:"
echo

echo "1. 📋 Standard Quality Pipeline:"
echo "   mvn clean verify"
echo "   ├── validate: Checkstyle"
echo "   ├── compile: Java compilation"
echo "   ├── test: Unit tests + JaCoCo"
echo "   └── verify: PMD + SpotBugs + Coverage check"
echo

echo "2. 🔍 Enhanced Quality Profile:"
echo "   mvn clean verify -Pquality"
echo "   ├── Enhanced PMD with copy-paste detection"
echo "   ├── Enhanced SpotBugs with max effort"
echo "   └── Comprehensive reporting"
echo

echo "3. ⚡ Quick Quality Check:"
echo "   mvn clean compile -Pquick-quality"
echo "   └── Fast Checkstyle validation only"
echo

echo "4. 📊 Individual Quality Tools:"
echo "   mvn checkstyle:check    # Code style"
echo "   mvn pmd:check          # Static analysis"
echo "   mvn spotbugs:check     # Bug detection"
echo "   mvn jacoco:check       # Coverage verification"
echo "   mvn test               # Run tests"
echo

echo "5. 📈 Report Generation:"
echo "   mvn site -Pquality     # Generate HTML reports"
echo "   mvn jacoco:report      # Coverage report"
echo

echo "==============================================="
echo "Quality Reports Location:"
echo "==============================================="
echo "• Checkstyle: target/checkstyle-result.xml"
echo "• PMD:        target/pmd.xml & target/site/pmd.html"
echo "• SpotBugs:   target/spotbugsXml.xml"
echo "• JaCoCo:     target/site/jacoco/index.html"
echo "• Tests:      target/surefire-reports/"
echo

echo "==============================================="
echo "Maven Profiles Available:"
echo "==============================================="
echo "• quality:       Enhanced quality checks"
echo "• quick-quality: Fast Checkstyle-only check"
echo

echo "Example Usage:"
echo "mvn clean verify                    # Standard quality pipeline"
echo "mvn clean verify -Pquality          # Enhanced quality checks"
echo "mvn clean compile -Pquick-quality   # Quick style check"
