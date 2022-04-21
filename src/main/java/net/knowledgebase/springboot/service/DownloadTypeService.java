package net.knowledgebase.springboot.service;

import net.knowledgebase.springboot.model.DownloadType;

import java.util.List;

public interface DownloadTypeService {
    List<DownloadType> getAllDownloadTypes();

    DownloadType saveDownloadType(DownloadType downloadType);

    DownloadType getDownloadTypeById(Long id);

    DownloadType updateDownloadType(DownloadType downloadType);

    void deleteDownloadTypeById(Long id);
}
