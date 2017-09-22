package service;

import dto.Art;
import exceptions.IncorrectPriceRangeException;

import java.math.BigDecimal;
import java.time.Year;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class GalleryServiceImpl implements GalleryService {
    private List<Art> arts;

    public GalleryServiceImpl() {
        arts = new ArrayList<>();
    }

    //add a piece of art to the gallery
    @Override
    public boolean addArt(Art art) {
        if(art == null) {
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
        return Collections.unmodifiableList(arts);
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
    public List<String> getArtByArtist(String artistName) {
        List<String> artsByArtist = arts.stream().filter(art -> art.getArtistName().equals(artistName)).map(art -> artistName).collect(Collectors.toList());
        return artsByArtist;
    }

    // returns all art with creation date in the past year.
    @Override
    public List<Art> getRecentArt() {
        int currentYear = Year.now().getValue();
        List<Art> artsCreatedTillLastYear = arts.stream().filter(art -> art.getCreationDate().getYear() < currentYear).collect(Collectors.toList());
        return artsCreatedTillLastYear;
    }

    // returns all art between an upper and lower price limit. Both limits should be optional. Ignore art with no asking price.
    @Override
    public List<Art> getArtByPrice(BigDecimal priceLimitMin, BigDecimal priceLimitMax) throws IncorrectPriceRangeException {
        Predicate<Art> priceFilter = null;
        if (priceLimitMin != null && priceLimitMax == null) {
            priceFilter = createMinPriceFilter(priceLimitMin);
            return filterArtBy(priceFilter);
        }

        if (priceLimitMin == null && priceLimitMax != null) {
            priceFilter = createMaxPriceFilter(priceLimitMax);
            return filterArtBy(priceFilter);
        }

        checkPriceFilterRange(priceLimitMin, priceLimitMax);
        priceFilter = createMinMaxPriceFilter(priceLimitMin, priceLimitMax);
        return filterArtBy(priceFilter);
    }

    private Predicate<Art> createMinPriceFilter(BigDecimal priceLimitMin) {
        return art -> art.getPrice().compareTo(priceLimitMin) > 0;
    }

    private Predicate<Art> createMaxPriceFilter(BigDecimal priceLimitMax) {
        return art -> priceLimitMax.compareTo(art.getPrice()) > 0;
    }

    private void checkPriceFilterRange(BigDecimal priceLimitMin, BigDecimal priceLimitMax) {
        if(priceLimitMin.compareTo(priceLimitMax) > 0) {
            throw new IncorrectPriceRangeException("priceLimitMax should be higher than priceLimitMin!. Given priceLimitMin::" + priceLimitMin + " priceLimitMax::" + priceLimitMax);
        }
    }

    private Predicate<Art> createMinMaxPriceFilter(BigDecimal priceLimitMin, BigDecimal priceLimitMax) {
        return art -> art.getPrice().compareTo(priceLimitMin) > 0 && art.getPrice().compareTo(priceLimitMax) < 0;
    }

    private List<Art> filterArtBy(Predicate<Art> priceFilter) {
        List<Art> artsFilteredByPrice = arts.stream().filter(art -> art.getPrice() != null).filter(priceFilter).collect(Collectors.toList());
        return artsFilteredByPrice;
    }

}
