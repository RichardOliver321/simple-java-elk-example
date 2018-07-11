package com.elkdemo.demo.service;

import com.elkdemo.demo.model.MovieRating;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpHost;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class MovieService {

    @Value("${elastic.search.url}")
    private String elasticSearchURL;

    @Value("${elastic.search.index.name}")
    private String indexName;

    @Value("${elastic.search.index.type}")
    private String indexType;

    @Value("${elastic.search.port}")
    private int elasticSearchPort;

    private ObjectMapper objectMapper;

    public MovieService() {
        this.objectMapper = new ObjectMapper();
    }

    /**
     * An example of how to push data into Elastic Search by using the RestHighLevelClient
     */
    public MovieRating saveMovieRating(MovieRating movieRating) {

        try (RestHighLevelClient elasticSearchClient = new RestHighLevelClient(RestClient.builder(new HttpHost(elasticSearchURL, elasticSearchPort, "http")))) {

            IndexRequest indexRequest = new IndexRequest(indexName, indexType);
            indexRequest.source(objectMapper.writeValueAsString(movieRating), XContentType.JSON);

            elasticSearchClient.index(indexRequest);

            return movieRating;

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Couldn't connect to Elastic Search with connection details: http" + elasticSearchURL + Integer.toString(elasticSearchPort));
        }
    }

    /**
     * How to retrieve data from Elastic Search via the RestHighLevelClient
     */
    public MovieRating getAverageRating(String title) {
        return new MovieRating();
    }
}
