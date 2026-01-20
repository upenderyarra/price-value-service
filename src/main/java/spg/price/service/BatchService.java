
package spg.price.service;

import spg.price.model.PriceRecord;

import java.util.List;
import java.util.UUID;

public interface BatchService {
    UUID startBatch();
    void upload(UUID batchId, List<PriceRecord> records);
    void complete(UUID batchId);
    void cancel(UUID batchId);
}
