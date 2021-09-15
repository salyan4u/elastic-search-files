package ru.solyanin.elasticsearchfiles.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.jodconverter.core.office.OfficeException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.solyanin.elasticsearchfiles.configuration.properties.TempFilesProperties;
import ru.solyanin.elasticsearchfiles.model.DocumentFile;
import ru.solyanin.elasticsearchfiles.repository.DocumentFileRepository;
import ru.solyanin.elasticsearchfiles.service.DocumentFileService;
import ru.solyanin.elasticsearchfiles.util.FileConvertUtil;
import ru.solyanin.elasticsearchfiles.view.SearchResponseDocument;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static ru.solyanin.elasticsearchfiles.util.ElasticUtil.*;

@Service
@Slf4j
public class DocumentFileServiceImpl implements DocumentFileService {
    private final DocumentFileRepository documentFileRepository;
    private final RestHighLevelClient client;
    private final TempFilesProperties tempFilesProperties;

    public DocumentFileServiceImpl(DocumentFileRepository documentFileRepository,
                                   RestHighLevelClient client,
                                   TempFilesProperties tempFilesProperties) {
        this.documentFileRepository = documentFileRepository;
        this.client = client;
        this.tempFilesProperties = tempFilesProperties;
    }

    @Override
    public DocumentFile findById(String id) throws EntityNotFoundException {
        Optional<DocumentFile> optional = documentFileRepository.findById(id);
        if (optional.isPresent()) {
            return optional.get();
        } else throw new EntityNotFoundException();
    }

    @Override
    public Page<SearchResponseDocument> findByData(String data, int number, int size) throws IOException {
        MatchQueryBuilder matchQueryBuilder = new MatchQueryBuilder("data", data);
        HighlightBuilder highlightBuilder = getHighlightBuilder();

        SearchSourceBuilder sourceBuilder = getSearchSourceBuilder(matchQueryBuilder, highlightBuilder);
        SearchRequest searchRequest = getSearchRequest(sourceBuilder);
        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);

        SearchHit[] hits = searchResponse.getHits().getHits();
        List<SearchResponseDocument> list = new ArrayList<>();
        for (SearchHit hit : hits) {
            Map<String, HighlightField> highlightFields = hit.getHighlightFields();
            HighlightField highlight = highlightFields.get("data");
            Text[] fragments = highlight.fragments();
            List<String> matches = new ArrayList<>();
            String fragmentString;
            for (Text fragment : fragments) {
                fragmentString = fragment.string();
                matches.add(fragmentString);
            }
            SearchResponseDocument result = new SearchResponseDocument();
            result.setFragments(matches);
            result.setDownloadUrl((String) hit.getSourceAsMap().get("downloadUrl"));
            list.add(result);
        }
        return new PageImpl<>(list, PageRequest.of(number, size), list.size());
    }

    @Override
    public Iterable<DocumentFile> findAll() {
        return documentFileRepository.findAll();
    }

    @Override
    public DocumentFile create(String downloadUrl) throws IOException, EntityExistsException, OfficeException {
        Optional<DocumentFile> optional = documentFileRepository.findByDownloadUrl(downloadUrl);
        if (optional.isEmpty()) {
            File downloaded = new File(tempFilesProperties.getTempPath());
            File pdf = new File(tempFilesProperties.getPdfPath());

            FileConvertUtil.downloadAndConvert(downloadUrl, downloaded, pdf);
            String data = FileConvertUtil.getStringFromPdf(pdf, downloaded);

            DocumentFile documentFile = new DocumentFile();
            documentFile.setData(data);
            documentFile.setDownloadUrl(downloadUrl);
            log.info("Document created: " + documentFile);
            return documentFileRepository.save(documentFile);
        } else {
            throw new EntityExistsException();
        }
    }

    @Override
    public Iterable<DocumentFile> createList(List<String> downloadUrls) throws IOException, OfficeException {
        List<DocumentFile> addedList = new ArrayList<>();
        for (String downloadUrl : downloadUrls) {
            try {
                DocumentFile documentFile = create(downloadUrl);
                addedList.add(documentFile);
            } catch (EntityExistsException e) {
                log.info("Entity with %s url is already exists: " + downloadUrl);
            }
        }
        return addedList;
    }

    @Override
    public void delete(String id) throws EntityNotFoundException {
        Optional<DocumentFile> optional = documentFileRepository.findById(id);
        if (optional.isPresent()) {
            documentFileRepository.deleteById(id);
            log.info("Document with %s deleted!" + id);
        } else throw new EntityNotFoundException();
    }

    @Override
    public Page<DocumentFile> getPage(int number, int size) {
        Pageable pageable = PageRequest.of(number, size);
        return documentFileRepository.findAll(pageable);
    }
}