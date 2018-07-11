# Simple ELK Stack Example
## Overview
Shows how to use the RestHighLevelClient to push data to ElasticSearch

### Required Information

#### ELK Stack
Any ELK stack can be used. The one I used for the demo was run in a docker container found here: 
https://hub.docker.com/r/sebp/elk/

To download the image:
"docker pull sebp/elk:<version>"
e.g. docker pull sebp/elk:611 

Run container:
"docker run -i sebp/elk:<version>"

The docker pull command above will get a docker image of the ELK stack at version 6.1.1 (611)
The version is important as it corresponds to the dependency version for the RestHighLevelClient. It is somewhat 
"resilient" and should work with minor changes but it is safer to match the version of your ELK stack to your dependency 
version to avoid unnecessary hassle.

#### Swagger
I used swagger to allow for easy REST calls (So there is no front end)


##### URLS

0.0.0.0:5601                 -> Kibana (Visual front end to view data )
0.0.0.0:9200                 -> Elastic Search endpoint (The data will be pushed to this URL via the RestHighLevelClient)
0.0.0.0:8080/swagger-ui.html -> Swagger UI 
