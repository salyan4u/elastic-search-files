package ru.solyanin.elasticsearchfiles.service;

import org.jodconverter.core.office.OfficeException;
import org.springframework.data.domain.Page;
import ru.solyanin.elasticsearchfiles.model.DocumentFile;
import ru.solyanin.elasticsearchfiles.view.SearchResponseDocument;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.io.IOException;
import java.util.List;

public interface DocumentFileService {
    DocumentFile findById(String id) throws EntityNotFoundException;

    Page<SearchResponseDocument> findByData(String data, int number, int size) throws IOException;

    Iterable<DocumentFile> findAll();

    @Transactional
    DocumentFile create(String downloadUrl) throws IOException, OfficeException;

    @Transactional
    Iterable<DocumentFile> createList(List<String> downloadUrls) throws IOException, OfficeException;

    @Transactional
    void delete(String id) throws EntityNotFoundException;

    Page<DocumentFile> getPage(int number, int size);
}