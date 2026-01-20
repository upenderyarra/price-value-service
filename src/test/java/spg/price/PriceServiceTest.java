
package spg.price;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import spg.price.model.PriceRecord;
import spg.price.service.BatchService;
import spg.price.service.PriceService;

import java.time.Instant;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PriceServiceTest {

    @Autowired
    BatchService batchService;

    @Autowired
    PriceService priceService;

    @Test
    void fullFlowTest() {
        var batchId = batchService.startBatch();

        batchService.upload(batchId, List.of(
                new PriceRecord("GOOG", Instant.now(), Map.of("price", 1200))
        ));

        batchService.complete(batchId);

        assertTrue(priceService.getLastPrice("GOOG").isPresent());
    }
}
