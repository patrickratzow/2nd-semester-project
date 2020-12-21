# Introduction
Second semester project product for DMAA0220 - Group 3 at University College Nordjylland (UCN).

This is a Java project utilising multithreading, databases, and a multi-tier architecture. It's capable of creating a project, customer, and an assosicated order with products.

The products are found via _Specifications_, which are aggregates of requirement types and links to specific product types. It works by putting in some requirements for a product, and out comes the cheapest product that fits all the criteria.
If no product is found, it'll give an error.

## Setup
You will need to setup the credentials to your Microsoft SQL Server in ``res/config.properties``. Please be aware that upon running the application it will not setup the database schema automatically unless it's the test database.

If you wish it to setup everything as it should be for the main database, go to ``src/datasource/mssql/DBConnectionPoolMsSql.java`` line 46, and comment out the if statement around setupDatabase()
