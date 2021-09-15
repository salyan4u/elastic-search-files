## Elasticsearch text files service

Elasticsearch service that can store and search data from text files.

Technologies used in the project: Java Core, Spring Boot, Hibernate, Swagger, Lombok, Elasticsearch, Maven, JODConverter.

### Start instruction:

1.Install Elasticsearch on your computer with configuration files in resources/elasticinitconfig    
2.Start Elasticsearch as service        
3.Install LibreOffice or OpenOffice and start it as service   
4.Edit values for Elasticsearch database in application.yaml(resource folder)       
5.Edit path values for temp files in application.yaml, file prefix      
6.Edit values for JODConverter in application.yaml, port and count      
7.Specify path to installed office folder in application.yaml   
8.Use Maven to build the jar application - package  
9.Deploy the application in any servlet container like TomCat

### How to use:

1./api/v1/elastic-search-files POST endpoint intended for uploading file to service, you need to specify the download-URL        
2./api/v1/elastic-search-files/search GET endpoint intended for searching on keywords in file

### Additional information:

1.Java version 11   
2.Spring Boot 2     
3.Default port of application - 8080    
4.The project builder is Maven  
5.RESTful API specification described using Swagger     
6.Converter any-txt to pdf file - JODConverter https://github.com/sbraconnier/jodconverter      
7.Files uploaded in service converts to PDF, content parsing in String format and stores in Elasticsearch database      