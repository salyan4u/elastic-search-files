package ru.solyanin.elasticsearchfiles.util;

import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;

public class ElasticUtil {
    public static SearchRequest getSearchRequest(SearchSourceBuilder sourceBuilder) {
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices("document");
        searchRequest.source(sourceBuilder);
        return searchRequest;
    }

    public static SearchSourceBuilder getSearchSourceBuilder(MatchQueryBuilder matchQueryBuilder, HighlightBuilder highlightBuilder) {
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.highlighter(highlightBuilder);
        sourceBuilder.query(matchQueryBuilder);
        return sourceBuilder;
    }

    public static HighlightBuilder getHighlightBuilder() {
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        HighlightBuilder.Field highlightTitle =
                new HighlightBuilder.Field("title");
        highlightTitle.highlighterType("unified");
        highlightBuilder.field(highlightTitle);
        HighlightBuilder.Field highlightUser =
                new HighlightBuilder.Field("data");
        highlightBuilder.field(highlightUser);
        return highlightBuilder;
    }
}
