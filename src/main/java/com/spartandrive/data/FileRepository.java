package com.spartandrive.data;

import com.spartandrive.web.request.SharedFileDetail;
import org.apache.lucene.util.QueryBuilder;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.spartandrive.data.SharedFile.*;
import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

/**
 * Created by mrugen on 11/28/15.
 */
@Repository
public class FileRepository {

    @Autowired
    TransportClient client;

    private static String FILE = "file";
    private static String SHARED = "shared";

    public String saveFileDetails(SharedFileDetail sharedFileDetail) throws IOException {
        final IndexResponse sharedFileIndexResponse = client.prepareIndex(FILE, SHARED)
                .setSource(
                        jsonBuilder()
                                .startObject()
                                .field(OWNER_ID.toString(), sharedFileDetail.getOwnerUID())
                                .field(OWNER_EMAIL.toString(), sharedFileDetail.getOwnerEmail())
                                .field(IS_DIRECTORY.toString(), sharedFileDetail.getIsDirectory())
                                .field(PATH.toString(), sharedFileDetail.getPath())
                                .field(MIME_TYPE.toString(), sharedFileDetail.getMimeType())
                                .field(ICON.toString(), sharedFileDetail.getIcon())
                                .field(FILE_NAME.toString(), sharedFileDetail.getFileName())
                                .field(FOLDER_NAME.toString(), sharedFileDetail.getFolderName())
                                .field(SHARED_WITH_EMAIL.toString(), sharedFileDetail.getSharedWithEmail())
                                .field(SHARED_FILE_URL.toString(), sharedFileDetail.getSharedFileUrl())
                                .field(CONTENTS.toString(), sharedFileDetail.getDirectoryContents())
                                .endObject()
                ).get();
        return sharedFileIndexResponse.getId();
    }

    public List<SharedFileDetail> findBySharedEmail(String email) {
        final SearchResponse searchResponse = searchByEmailId(email);
        final SearchHits hits = searchResponse.getHits();
        if (hits != null && hits.totalHits() > 0) {
            return Arrays.asList(hits.getHits()).stream().map(hit -> new SharedFileDetail(hit.sourceAsMap())).collect(Collectors.toList());
        }
        return null;
    }

    private SearchResponse searchByEmailId(String email) {
        return client.prepareSearch(FILE)
                .setQuery(QueryBuilders.matchQuery(SHARED_WITH_EMAIL.toString(), email)).setPostFilter(QueryBuilders.termQuery(SHARED_WITH_EMAIL.toString(),email))
                .execute().actionGet();
    }

    /**
     * @Returns Boolean If the document was found and deleted.
     **/
    public boolean deleteSharedFileRecord(String emailId, String filePath) throws FailedToDeleteFileException {
        final SearchResponse searchResponse = searchByEmailId(emailId);
        final SearchHits hits = searchResponse.getHits();
        if (hits != null && hits.getTotalHits() > 0) {
            for (SearchHit hit : hits) {
                final Map<String, Object> source = hit.getSource();
                final String sharedWithEmail = source.get(SHARED_WITH_EMAIL.toString()).toString();
                final String path = source.get(PATH.toString()).toString();
                final String id = hit.getId();
                if (sharedWithEmail.equals(emailId) && path.equals(filePath)) {
                    final DeleteResponse deleteResponse = client.prepareDelete(FILE, SHARED, id).get();
                    if (!deleteResponse.isFound()) {
                        throw new FailedToDeleteFileException("Failed to delete file record " + filePath + " for " + emailId);
                    } else {
                        return true;
                    }

                }
            }
        }
        return false;
    }
}
