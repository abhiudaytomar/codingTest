package service;

import dto.Art;

import java.math.BigInteger;
import java.util.List;
import java.util.Set;

public interface GalleryService {
    /**
     * add a piece of art to the gallery
     *
     * @param art
     * @return
     */

    boolean addArt(Art art);

    /**
     * remove a piece of art from the gallery
     *
     * @param art
     * @return
     */

    boolean deleteArt(Art art);

    /**
     * returns all art currently in the gallery
     *
     * @return
     */
    Set<Art> getAllArt();

    /**
     * returns the names of all of the artists with art currently in the gallery in alphabetical order.
     * returns distinct names.
     * @return
     */
    List<String> getArtists();

    /**
     * returns all art by a specific artist.
     *
     * @param artistName
     * @return
     */
    Set<Art> getArtByArtist(String artistName);

    /**
     * returns all art with creation date in the past year.
     *
     * @return
     */

    Set<Art> getRecentArt();

    /**
     * returns all art between an upper and lower price limit. Both limits should be optional. Ignore art with no asking price.
     *
     * @param priceLimitMin
     * @param priceLimitMax
     * @return
     */

    Set<Art> getArtByPrice(BigInteger priceLimitMin, BigInteger priceLimitMax);
}
