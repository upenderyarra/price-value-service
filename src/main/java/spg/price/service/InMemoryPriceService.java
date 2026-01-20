
package spg.price.service;

import org.springframework.stereotype.Service;
import spg.price.model.BatchStatus;
import spg.price.model.PriceRecord;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class InMemoryPriceService implements PriceService, BatchService {

    private final Map<String, PriceRecord> livePrices = new ConcurrentHashMap<>();
    private final Map<UUID, BatchContext> batches = new ConcurrentHashMap<>();

    @Override
    public UUID startBatch() {
        UUID id = UUID.randomUUID();
        batches.put(id, new BatchContext());
        return id;
    }

    @Override
    public void upload(UUID batchId, List<PriceRecord> records) {
        BatchContext ctx = getBatch(batchId);
        ctx.records.addAll(records);
    }

    @Override
    public void complete(UUID batchId) {
        BatchContext ctx = getBatch(batchId);
        ctx.status = BatchStatus.COMPLETED;

        synchronized (livePrices) {
            for (PriceRecord r : ctx.records) {
                livePrices.merge(r.id(), r,
                        (oldV, newV) -> newV.asOf().isAfter(oldV.asOf()) ? newV : oldV);
            }
        }
        batches.remove(batchId);
    }

    @Override
    public void cancel(UUID batchId) {
        getBatch(batchId).status = BatchStatus.CANCELLED;
        batches.remove(batchId);
    }

    @Override
    public Optional<PriceRecord> getLastPrice(String instrumentId) {
        return Optional.ofNullable(livePrices.get(instrumentId));
    }

    private BatchContext getBatch(UUID id) {
        BatchContext ctx = batches.get(id);
        if (ctx == null || ctx.status != BatchStatus.STARTED) {
            throw new IllegalStateException("Invalid batch: " + id);
        }
        return ctx;
    }

    private static class BatchContext {
        List<PriceRecord> records = new ArrayList<>();
        BatchStatus status = BatchStatus.STARTED;
    }
}
