package ma.foodplus.ordering.system.product.service.domain.ports.output.message.publisher;

import ma.foodplus.ordering.system.product.service.domain.event.product.ProductCreatedEvent;

public interface ProductMessagePublisher{

    void publish(ProductCreatedEvent productCreatedEvent);

}