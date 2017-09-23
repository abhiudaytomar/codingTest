package service;

import com.google.common.collect.ImmutableSet;
import dto.Art;
import exceptions.InvalidPriceRangeException;

import java.math.BigInteger;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * This class implements interface
 *
 * @see service.GalleryService
 */
public class GalleryServiceImpl implements GalleryService {
    private Set<Art> arts;
    private int DAYS_365 = 365;

    public GalleryServiceImpl() {
        arts = new LinkedHashSet<>();
    }

    @Override
    public boolean addArt(Art art) {
        if (art == null) {
            return false;
        }
        return arts.add(art);
    }

    @Override
    public boolean deleteArt(Art art) {
        if (art == null) {
            return false;
        }
        return arts.remove(art);
    }

    @Override
    public Set<Art> getAllArt() {
        return ImmutableSet.copyOf(arts);
    }

    @Override
    public List<String> getArtists() {
        List<String> artistNames = arts.stream()
                .map(art -> art.getArtistName())
                .sorted((artist1, artist2) -> artist1.compareTo(artist2))
                .distinct()
                .collect(Collectors.toList());
        return artistNames;
    }

    @Override
    public Set<Art> getArtByArtist(String artistName) {
        Set<Art> artsByArtist = arts.stream()
                .filter(art -> art.getArtistName()
                        .equals(artistName))
                .collect(Collectors.toSet());
        return artsByArtist;
    }

    @Override
    public Set<Art> getRecentArt() {
        LocalDate currentDate = LocalDate.now();
        Set<Art> artsCreatedTillLastYear = arts.stream().filter(art -> ChronoUnit.DAYS.between(art.getCreationDate(), currentDate) < DAYS_365).collect(Collectors.toSet());
        return artsCreatedTillLastYear;
    }

    // Assumption min and max prices are not to be included.
    @Override
    public Set<Art> getArtByPrice(BigInteger priceLimitMin, BigInteger priceLimitMax) throws InvalidPriceRangeException {
        Predicate<Art> greaterThanOrEqualMinPrice = art -> art.getPrice().compareTo(priceLimitMin) > 0 || art.getPrice().compareTo(priceLimitMin) == 0;

        Predicate<Art> lessThanOrEqualMaxPrice = art -> art.getPrice().compareTo(priceLimitMax) < 0 || art.getPrice().compareTo(priceLimitMax) == 0;

        Predicate<Art> betweenMinAndMaxPrice = art -> greaterThanOrEqualMinPrice.and(lessThanOrEqualMaxPrice).test(art);

        if (priceLimitMin != null && priceLimitMax == null) {
            return filterArtByPrice(greaterThanOrEqualMinPrice);
        }

        if (priceLimitMin == null && priceLimitMax != null) {
            return filterArtByPrice(lessThanOrEqualMaxPrice);
        }

        checkPriceFilterRange(priceLimitMin, priceLimitMax);

        return filterArtByPrice(betweenMinAndMaxPrice);
    }

    private void checkPriceFilterRange(BigInteger priceLimitMin, BigInteger priceLimitMax) {
        if (priceLimitMin.compareTo(priceLimitMax) > 0) {
            throw new InvalidPriceRangeException("priceLimitMax should be higher than priceLimitMin!. Given priceLimitMin::" + priceLimitMin + " priceLimitMax::" + priceLimitMax);
        }
    }

    private Set<Art> filterArtByPrice(Predicate<Art> priceFilter) {
        Set<Art> artsFilteredByPrice = arts.stream().filter(art -> art.getPrice() != null).filter(priceFilter).collect(Collectors.toSet());
        return artsFilteredByPrice;
    }

}
