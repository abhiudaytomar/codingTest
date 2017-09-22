package service;

import com.google.common.collect.ImmutableList;
import dto.Art;
import exceptions.IncorrectPriceRangeException;

import java.time.Year;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class GalleryServiceImpl implements GalleryService {
    private List<Art> arts;

    public GalleryServiceImpl() {
        arts = new ArrayList<>();
    }

    // TODO add java comment
    @Override
    public boolean addArt(Art art) {
        if (art == null) {
            return false;
        }
        return arts.add(art);
    }

    //remove a piece of art from the gallery
    @Override
    public boolean deleteArt(Art art) {
        return arts.remove(art);
    }

    // returns all art currently in the gallery
    @Override
    public List<Art> getAllArt() {
        return ImmutableList.copyOf(arts);
    }

    //returns the names of all of the artists with art currently in the gallery in alphabetical order.
    @Override
    public List<String> getArtists() {
        List<String> artistNames = arts.stream()
                                       .map(art -> art.getArtistName())
                                       .sorted((artist1, artist2) -> artist1.compareTo(artist2))
                                       .collect(Collectors.toList());
        return artistNames;
    }

    // returns all art by a specific artist.
    @Override
    public List<Art> getArtByArtist(String artistName) {
        List<Art> artsByArtist = arts.stream()
                                     .filter(art -> art.getArtistName()
                                     .equals(artistName))
                                     .collect(Collectors.toList());
        return artsByArtist;
    }

    // returns all art with creation date in the past year.
    // TOOD Figure out the Creation Date
    @Override
    public List<Art> getRecentArt() {
        int currentYear = Year.now().getValue();
        List<Art> artsCreatedTillLastYear = arts.stream().filter(art -> art.getCreationDate().getYear() < currentYear).collect(Collectors.toList());
        return artsCreatedTillLastYear;
    }

    // returns all art between an upper and lower price limit. Both limits should be optional. Ignore art with no asking price.
    // Assumption min and max prices are not to be included.
    @Override
    public List<Art> getArtByPrice(Long priceLimitMin, Long priceLimitMax) throws IncorrectPriceRangeException {
        Predicate<Art> greaterThanMinPrice = art -> art.getPrice().compareTo(priceLimitMin) > 0;

        Predicate<Art> lessThanMaxPrice = art -> art.getPrice().compareTo(priceLimitMax) < 0;

        Predicate<Art> betweenMinAndMaxPrice = art -> greaterThanMinPrice.and(lessThanMaxPrice).test(art);

        if (priceLimitMin != null && priceLimitMax == null) {
            return filterArtByPrice(greaterThanMinPrice);
        }

        if (priceLimitMin == null && priceLimitMax != null) {
            return filterArtByPrice(lessThanMaxPrice);
        }

        checkPriceFilterRange(priceLimitMin, priceLimitMax);

        return filterArtByPrice(betweenMinAndMaxPrice);
    }

    private void checkPriceFilterRange(Long priceLimitMin, Long priceLimitMax) {
        if (priceLimitMin.compareTo(priceLimitMax) > 0) {
            throw new IncorrectPriceRangeException("priceLimitMax should be higher than priceLimitMin!. Given priceLimitMin::" + priceLimitMin + " priceLimitMax::" + priceLimitMax);
        }
    }

    private List<Art> filterArtByPrice(Predicate<Art> priceFilter) {
        List<Art> artsFilteredByPrice = arts.stream().filter(art -> art.getPrice() != null).filter(priceFilter).collect(Collectors.toList());
        return artsFilteredByPrice;
    }

}
