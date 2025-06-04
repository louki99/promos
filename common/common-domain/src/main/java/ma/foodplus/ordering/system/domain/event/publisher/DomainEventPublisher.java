package ma.foodplus.ordering.system.domain.event.publisher;

import ma.foodplus.ordering.system.domain.event.DomainEvent;

public interface DomainEventPublisher<T extends DomainEvent> {

    void publish(T domainEvent);
}
