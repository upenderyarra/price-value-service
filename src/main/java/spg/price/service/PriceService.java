
package spg.price.service;

import spg.price.model.PriceRecord;

import java.util.Optional;

public interface PriceService {
    Optional<PriceRecord> getLastPrice(String instrumentId);
}
