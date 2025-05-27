#!/bin/bash

# Comprehensive Java Spring Boot Quality Check Script
# This script runs all code quality checks for the MCP NTools Java project

echo "========================================"
echo "   MCP NTools Java - Quality Checks"
echo "========================================"
echo

# Set project directory
PROJECT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
cd "$PROJECT_DIR"

echo "🔍 Running comprehensive quality checks..."
echo

# Function to print status
print_status() {
    if [ $1 -eq 0 ]; then
        echo "✅ $2 - PASSED"
    else
        echo "❌ $2 - FAILED"
        return 1
    fi
}

# Function to run quality check phase
run_quality_phase() {
    local phase_name="$1"
    local command="$2"
    
    echo "📊 Running $phase_name..."
    echo "Command: $command"
    echo "----------------------------------------"
    
    if eval "$command"; then
        print_status 0 "$phase_name"
        echo
        return 0
    else
        print_status 1 "$phase_name"
        echo
        return 1
    fi
}

# Initialize counters
TOTAL_CHECKS=0
PASSED_CHECKS=0

# 1. Maven Clean & Compile
TOTAL_CHECKS=$((TOTAL_CHECKS + 1))
if run_quality_phase "Maven Clean & Compile" "mvn clean compile -q"; then
    PASSED_CHECKS=$((PASSED_CHECKS + 1))
fi

# 2. Checkstyle Analysis
TOTAL_CHECKS=$((TOTAL_CHECKS + 1))
if run_quality_phase "Checkstyle Code Style Check" "mvn checkstyle:check -q"; then
    PASSED_CHECKS=$((PASSED_CHECKS + 1))
fi

# 3. PMD Analysis
TOTAL_CHECKS=$((TOTAL_CHECKS + 1))
if run_quality_phase "PMD Static Analysis" "mvn pmd:check -q"; then
    PASSED_CHECKS=$((PASSED_CHECKS + 1))
fi

# 4. SpotBugs Analysis
TOTAL_CHECKS=$((TOTAL_CHECKS + 1))
if run_quality_phase "SpotBugs Security & Bug Analysis" "mvn spotbugs:check -q"; then
    PASSED_CHECKS=$((PASSED_CHECKS + 1))
fi

# 5. Unit Tests
TOTAL_CHECKS=$((TOTAL_CHECKS + 1))
if run_quality_phase "Unit Tests" "mvn test -q"; then
    PASSED_CHECKS=$((PASSED_CHECKS + 1))
fi

# 6. JaCoCo Code Coverage
TOTAL_CHECKS=$((TOTAL_CHECKS + 1))
if run_quality_phase "JaCoCo Code Coverage Check" "mvn jacoco:check -q"; then
    PASSED_CHECKS=$((PASSED_CHECKS + 1))
fi

# 7. Full Maven Verify (combines all checks)
TOTAL_CHECKS=$((TOTAL_CHECKS + 1))
if run_quality_phase "Maven Verify (Full Pipeline)" "mvn verify -q"; then
    PASSED_CHECKS=$((PASSED_CHECKS + 1))
fi

echo "========================================"
echo "           QUALITY REPORT"
echo "========================================"
echo
echo "📈 Results Summary:"
echo "   ✅ Passed: $PASSED_CHECKS/$TOTAL_CHECKS checks"
echo "   ❌ Failed: $((TOTAL_CHECKS - PASSED_CHECKS))/$TOTAL_CHECKS checks"
echo

if [ $PASSED_CHECKS -eq $TOTAL_CHECKS ]; then
    echo "🎉 ALL QUALITY CHECKS PASSED!"
    echo "   Your code meets enterprise-grade quality standards."
    echo
    echo "📊 Reports Generated:"
    echo "   • Checkstyle: target/checkstyle-result.xml"
    echo "   • PMD: target/pmd.xml & target/site/pmd.html"
    echo "   • SpotBugs: target/spotbugsXml.xml"
    echo "   • JaCoCo Coverage: target/site/jacoco/index.html"
    echo
    echo "🚀 Ready for production deployment!"
    exit 0
else
    echo "⚠️  QUALITY CHECKS FAILED!"
    echo "   Please review the failed checks above and fix the issues."
    echo
    echo "💡 Tips:"
    echo "   • Check target/checkstyle-result.xml for style violations"
    echo "   • Review target/pmd.xml for code quality issues"
    echo "   • Examine test failures in target/surefire-reports/"
    echo "   • Verify code coverage in target/site/jacoco/index.html"
    exit 1
fi
