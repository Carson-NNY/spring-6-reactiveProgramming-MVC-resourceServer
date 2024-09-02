package guru.springframework.spring6reactive.repositories;

import guru.springframework.spring6reactive.domain.Beer;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

//ReactiveCrudRepository is for the Reactive programming style
public interface BeerRepository extends ReactiveCrudRepository<Beer, Integer> {
}
