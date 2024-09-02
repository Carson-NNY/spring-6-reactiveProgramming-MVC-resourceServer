package guru.springframework.spring6reactive.services;

import guru.springframework.spring6reactive.mappers.BeerMapper;
import guru.springframework.spring6reactive.models.BeerDTO;
import guru.springframework.spring6reactive.repositories.BeerRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor //@RequiredArgsConstructor: The @RequiredArgsConstructor annotation from Lombok automatically generates a
// constructor with final fields as parameters. Since your BeerRepository and BeerMapper are marked as final, Lombok generates a constructor that takes these as parameters. Spring then automatically uses this constructor to inject the dependencies.
public class BeerServiceImpl implements BeerService {

    //When a Spring-managed bean (like BeerServiceImpl) has only one constructor, Spring
    // automatically uses that constructor to instantiate the bean and inject the required dependencies.
    // This makes the @Autowired annotation redundant when there is only one constructor.
    private final BeerRepository beerRepository;
    private final BeerMapper beerMapper;

    @Override
    public Flux<BeerDTO> listBeers() {
        return beerRepository.findAll().map(beerMapper::beerToBeerDTO);
    }

    @Override
    public Mono<BeerDTO> getBeerById(Integer beerId) {
        return beerRepository.findById(beerId).map(beerMapper::beerToBeerDTO);
    }

    @Override
    public Mono<BeerDTO> saveNewBeer(BeerDTO beerDTO) {
        return beerRepository.save(beerMapper.beerDTOToBeer(beerDTO))
                .map(beerMapper::beerToBeerDTO);
    }

    @Override
    public Mono<BeerDTO> updateBeer(Integer beerId, BeerDTO beerDTO) {

        return beerRepository.findById(beerId)
                .map(existingBeer -> {
                    existingBeer.setBeerName(beerDTO.getBeerName());
                    existingBeer.setBeerStyle(beerDTO.getBeerStyle());
                    existingBeer.setPrice(beerDTO.getPrice());
                    existingBeer.setUpc(beerDTO.getUpc());
                    existingBeer.setQuantityOnHand(beerDTO.getQuantityOnHand());
                    return existingBeer;
                })
                .flatMap(beerRepository::save)
                .map(beerMapper::beerToBeerDTO);

    }

    @Override
    public Mono<BeerDTO> patchBeer(Integer beerId, BeerDTO beerDTO) {

        return beerRepository.findById(beerId)
                .map(existingBeer -> {
                    if(StringUtils.hasText(beerDTO.getBeerName())) {
                        existingBeer.setBeerName(beerDTO.getBeerName());
                    }
                    if(StringUtils.hasText(beerDTO.getBeerStyle())) {
                        existingBeer.setBeerStyle(beerDTO.getBeerStyle());
                    }
                    if(beerDTO.getPrice() != null) {
                        existingBeer.setPrice(beerDTO.getPrice());
                    }
                    if(beerDTO.getQuantityOnHand() != null) {
                        existingBeer.setQuantityOnHand(beerDTO.getQuantityOnHand());
                    }
                    if(StringUtils.hasText(beerDTO.getUpc())) {
                        existingBeer.setUpc(beerDTO.getUpc());
                    }

                    return existingBeer;
                })
                .flatMap(beerRepository::save)
                .map(beerMapper::beerToBeerDTO);
    }

    @Override
    public Mono<Void> deleteById(Integer beerId) {
        return beerRepository.deleteById(beerId);
    }


}
