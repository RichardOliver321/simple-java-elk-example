package com.elkdemo.demo.service;

import com.elkdemo.demo.model.MovieRating;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpHost;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
            throw new RuntimeException("Couldn't connect to Elastic Search with connection details: http://" + elasticSearchURL + Integer.toString(elasticSearchPort));
        }
    }

    /**
     * How to retrieve data from Elastic Search via the RestHighLevelClient
     */
    public List<MovieRating> findMoviesWithPropertiesLike(String property, String propertyItem) {

        try (RestHighLevelClient elasticSearchClient = new RestHighLevelClient(RestClient.builder(new HttpHost(elasticSearchURL, elasticSearchPort, "http")))) {

            SearchRequest searchRequest = new SearchRequest(indexName);
            SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
            sourceBuilder.query(createMatcher(property, propertyItem));

            searchRequest.source(sourceBuilder);

            SearchResponse searchResponse = elasticSearchClient.search(searchRequest);

            return buildResponse(searchResponse, elasticSearchClient);

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Couldn't connect to Elastic search with connection details: http://" + elasticSearchURL + Integer.toString(elasticSearchPort));
        }

    }

    private List<MovieRating> buildResponse(SearchResponse searchResponse, RestHighLevelClient client) {

        List<MovieRating> movieRatingList = new ArrayList<>();

        searchResponse.getHits().forEach(movie -> {
            GetRequest getRequest = new GetRequest(movie.getIndex(), movie.getType(), movie.getId());

            try {
                GetResponse getResponse = client.get(getRequest);
                if (getResponse.isExists())
                    movieRatingList.add(objectMapper.readValue(getResponse.getSourceAsString(), MovieRating.class));
            } catch (IOException e) {
                e.printStackTrace();
            }

        });
        return movieRatingList;
    }

    private MatchQueryBuilder createMatcher(String property, String propertyItem) {
        MatchQueryBuilder matchQueryBuilder = new MatchQueryBuilder(property, propertyItem);

        matchQueryBuilder.fuzziness(Fuzziness.AUTO);
        matchQueryBuilder.prefixLength(3);
        matchQueryBuilder.maxExpansions(10);

        return matchQueryBuilder;
    }
}
