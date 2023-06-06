Clustered Data Warehouse
------------------------
The Clustered Data Warehouse is a project that involves importing data into a clustered data warehouse environment. It uses PostgreSQL as the underlying database and utilizes JPA (Java Persistence API) for handling queries and database operations. The project provides functionality to validate the structure of the imported data, prevents duplicate imports, and ensures that no rollback occurs during the import process.

Request Fields
--------------
The request data should include the following fields:

Deal Unique Id: A unique identifier for the deal.  
From Currency ISO Code: The ISO code of the currency from which the deal is made (ordering currency).  
To Currency ISO Code: The ISO code of the currency to which the deal is made.  
Deal Timestamp: The timestamp of the deal.  
Deal Amount in Ordering Currency: The amount of the deal in the ordering currency.  

Validation
----------
The application performs validation on the structure of each imported record. It checks for the following:

Missing fields: Each record in the import dataset must include all the required fields mentioned above. If any field is missing, the record is considered invalid and will not be imported.  
Type format: The application ensures that the data types of the fields are correct. For example, if From Currency ISO Code length is expected to be three, the application validates that the length of From Currency ISO Code is three.

Please note that this is a simplified explanation of the validation process, and actual implementation may require additional validation rules and error handling.

Logging
-------
There will be logging for each request received through API.  

Please note that this is a simplified explanation of the logging, and actual implementation contains additional logging to log info or errors.

Preventing Duplicate Imports
----------------------------
The application implements a mechanism to prevent importing the same dataset twice. When a dataset is received for import, the system checks if the Record Unique Ids already exist in the data warehouse. If any of the Record Unique Ids already exist, the dataset is considered a duplicate and will not be imported. This ensures that only unique datasets are imported into the system.

No Rollback Allowed
-------------------
To ensure that every imported record is saved in the data warehouse and no rollback occurs, the application follows a no-rollback policy. This means that even if an error occurs while saving a record, the transaction will not be rolled back, and the previously saved records will remain in the data warehouse. This approach guarantees that all successfully imported records are persisted, irrespective of any subsequent errors.

Makefile
--------
To automate the build process, manage dependencies, and improve productivity.

As Makefile simplifies the execution of complex tasks, reduces the chance of errors, and helps streamline the development workflow.

Docker
------
Created a Dockerfile and docker-compose.yml to build the image.