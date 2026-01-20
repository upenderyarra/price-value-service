
package spg.price.controller;

import org.springframework.web.bind.annotation.*;
import spg.price.model.PriceRecord;
import spg.price.service.BatchService;
import spg.price.service.PriceService;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/prices")
public class PriceController {

    private final BatchService batchService;
    private final PriceService priceService;

    public PriceController(BatchService batchService, PriceService priceService) {
        this.batchService = batchService;
        this.priceService = priceService;
    }

    /*@PostMapping("/batch/start")
    public UUID startBatch() {


        return batchService.startBatch();
    }*/
    @PostMapping("/batch/start")
    public Map<String, String> startBatch() {
        return Map.of("batchId", batchService.startBatch().toString());
    }

    @PostMapping("/batch/{id}/upload")
    public void upload(@PathVariable UUID id, @RequestBody List<PriceRecord> records) {
        batchService.upload(id, records);
    }

    @PostMapping("/batch/{id}/complete")
    public void complete(@PathVariable UUID id) {
        batchService.complete(id);
    }

    @PostMapping("/batch/{id}/cancel")
    public void cancel(@PathVariable UUID id) {
        batchService.cancel(id);
    }

    @GetMapping("/{instrumentId}")
    public PriceRecord getLast(@PathVariable String instrumentId) {
        return priceService.getLastPrice(instrumentId).orElse(null);
    }
}
