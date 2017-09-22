package service;

import dto.Art;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface GalleryService {

    //add a piece of art to the gallery
    boolean addArt(Art art);

    //remove a piece of art from the gallery
    boolean deleteArt(Art art);

    // returns all art currently in the gallery
    List<Art> getAllArt();

    //returns the names of all of the artists with art currently in the gallery in alphabetical order.
    List<String> getArtists();

    // returns all art by a specific artist.
    List<Art> getArtByArtist(String artistName);

    // returns all art with creation date in the past year.
    List<Art> getRecentArt();

    // returns all art between an upper and lower price limit. Both limits should be optional. Ignore art with no asking price.
    List<Art> getArtByPrice(Long priceLimitMin, Long priceLimitMax);
}
