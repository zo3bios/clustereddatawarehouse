# Define variables
JAVA := java
MAVEN := mvn
MAIN_CLASS := com.progresssoft.ClusteredDataWarehouse.ClusteredDataWarehouseApplication

# Define targets
.PHONY: run
run:
	$(JAVA) -jar target/clustereddatawarehouse-0.0.1-SNAPSHOT.jar

.PHONY: test
test:
	$(MAVEN) test

.PHONY: clean
clean:
	$(MAVEN) clean

.PHONY: build
build:
	$(MAVEN) package

.PHONY: all
all: clean build run

.PHONY: help
help:
	@echo "Available targets:"
	@echo "- run: Run the application"
	@echo "- test: Run tests"
	@echo "- clean: Clean build artifacts"
	@echo "- build: Build the application"
	@echo "- all: Clean, build, and run the application"
