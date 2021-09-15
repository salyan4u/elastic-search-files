package ru.solyanin.elasticsearchfiles.view;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchResponseDocument {
    private List<String> fragments;
    private String downloadUrl;
}