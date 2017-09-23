package service;

import constants.ArtType;
import dto.Art;
import exceptions.InvalidPriceRangeException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.math.BigInteger;
import java.time.LocalDate;
import java.time.Year;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;

public class GalleryServiceImplTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();
    private GalleryService galleryService;

    @Before
    public void setUp() {
        galleryService = new GalleryServiceImpl();
    }

    @Test
    public void test_addArt_WithNullObject() {
        boolean result = galleryService.addArt(null);

        assertFalse(result);
    }

    @Test
    public void test_addArt() {
        Art art = new Art.ArtBuilder("Mona Lisa", ArtType.PAINTING, "Leonard Di Vinci", LocalDate.of(1600, 02, 23)).build();

        assertTrue(galleryService.addArt(art));
    }

    @Test
    public void test_addArt_SameObjectTwice() {
        Art artOne = new Art.ArtBuilder("Mona Lisa", ArtType.PAINTING, "Leonard Di Vinci", LocalDate.of(1600, 02, 23)).build();

        assertTrue(galleryService.addArt(artOne));
        assertFalse(galleryService.addArt(artOne));
    }

    @Test
    public void test_addArt_SameContentTwoDifferentInstance() {
        Art artOne = new Art.ArtBuilder("Mona Lisa", ArtType.PAINTING, "Leonard Di Vinci", LocalDate.of(1600, 02, 23)).build();
        Art artTwo = new Art.ArtBuilder("Mona Lisa", ArtType.PAINTING, "Leonard Di Vinci", LocalDate.of(1600, 02, 23)).build();

        assertTrue(galleryService.addArt(artOne));
        assertFalse(galleryService.addArt(artTwo));
    }

    @Test
    public void test_deleteArt_WithNullObject() {
        assertFalse(galleryService.deleteArt(null));
    }

    @Test
    public void test_deleteArt_WithEmptyCollection() {
        Art art = new Art.ArtBuilder("Mona Lisa", ArtType.PAINTING, "Leonard Di Vinci", LocalDate.of(1600, 02, 23)).build();

        assertFalse(galleryService.deleteArt(art));
    }

    @Test
    public void test_deleteArt_WithNonEmptyCollection() {
        Art art = new Art.ArtBuilder("Mona Lisa", ArtType.PAINTING, "Leonard Di Vinci", LocalDate.of(1600, 02, 23)).build();

        assertTrue(galleryService.addArt(art));
        assertTrue(galleryService.deleteArt(art));
    }

    @Test
    public void test_deleteArt_Failure() {
        Art artOne = new Art.ArtBuilder("Mona Lisa", ArtType.PAINTING, "Leonard Di Vinci", LocalDate.of(1600, 02, 23)).build();
        Art artTwo = new Art.ArtBuilder("Bust of Nefertiti", ArtType.SCLUPTURE, "Thutmose", LocalDate.of(1600, 02, 23)).build();

        assertTrue(galleryService.addArt(artOne));
        assertFalse(galleryService.deleteArt(artTwo));
    }

    @Test
    public void test_deleteArt_OnlyDateIsDifferent() {
        Art artOne = new Art.ArtBuilder("Mona Lisa", ArtType.PAINTING, "Leonard Di Vinci", LocalDate.of(1600, 02, 23)).build();
        Art artTwo = new Art.ArtBuilder("Mona Lisa", ArtType.PAINTING, "Leonard Di Vinci", LocalDate.of(1923, 02, 12)).build();

        assertTrue(galleryService.addArt(artOne));
        assertTrue(galleryService.deleteArt(artTwo));
    }

    @Test
    public void test_getAllArt() {
        Art artOne = new Art.ArtBuilder("Mona Lisa", ArtType.PAINTING, "Leonard Di Vinci", LocalDate.of(1600, 02, 23)).build();
        Art artTwo = new Art.ArtBuilder("Bust of Nefertiti", ArtType.SCLUPTURE, "Thutmose", LocalDate.of(1600, 02, 23)).build();
        galleryService.addArt(artOne);
        galleryService.addArt(artTwo);
        Set<Art> arts = galleryService.getAllArt();

        assertNotNull(arts);
        assertEquals(2, arts.size());
        assertTrue(arts.contains(artOne));
        assertTrue(arts.contains(artTwo));
    }

    @Test
    public void test_getAllArt_ForImmutability() {
        Art artOne = new Art.ArtBuilder("Mona Lisa", ArtType.PAINTING, "Leonard Di Vinci", LocalDate.of(1600, 02, 23)).build();
        Art artTwo = new Art.ArtBuilder("Bust of Nefertiti", ArtType.SCLUPTURE, "Thutmose", LocalDate.of(1600, 02, 23)).build();
        galleryService.addArt(artOne);
        galleryService.addArt(artTwo);

        Set<Art> arts = galleryService.getAllArt();

        assertNotNull(arts);
        assertEquals(2, arts.size());
        assertTrue(arts.contains(artOne));
        assertTrue(arts.contains(artTwo));

        assertTrue(galleryService.deleteArt(artOne));

        thrown.expect(UnsupportedOperationException.class);
        assertFalse(arts.remove(artOne));


        assertEquals(1, galleryService.getAllArt().size());
        assertEquals(2, arts.size());

    }

    @Test
    public void test_getArtist_InAlphabeticalOrder_AllUniqueArtist() {
        Art artOne = new Art.ArtBuilder("Bust of Nefertiti", ArtType.SCLUPTURE, "cde", LocalDate.of(1600, 02, 23)).build();
        Art artTwo = new Art.ArtBuilder("Bust of Nefertiti", ArtType.SCLUPTURE, "bcd", LocalDate.of(1600, 02, 23)).build();
        Art artThree = new Art.ArtBuilder("Mona Lisa", ArtType.PAINTING, "Abc", LocalDate.of(1600, 02, 23)).build();
        Art artFour = new Art.ArtBuilder("Bust of Nefertiti", ArtType.SCLUPTURE, "bbc", LocalDate.of(1600, 02, 23)).build();

        galleryService.addArt(artOne);
        galleryService.addArt(artTwo);
        galleryService.addArt(artThree);
        galleryService.addArt(artFour);

        List<String> artists = galleryService.getArtists();
        assertEquals(4, artists.size());
        assertEquals("Abc", artists.get(0));
        assertEquals("bbc", artists.get(1));
        assertEquals("bcd", artists.get(2));
        assertEquals("cde", artists.get(3));
    }

    @Test
    public void test_getArtist_InAlphabeticalOrder_OneArtistTwoArt() {
        Art artOne = new Art.ArtBuilder("Bust of Nefertiti", ArtType.SCLUPTURE, "cde", LocalDate.of(1600, 02, 23)).build();
        Art artTwo = new Art.ArtBuilder("Bust of Nefertiti", ArtType.SCLUPTURE, "bcd", LocalDate.of(1600, 02, 23)).build();
        Art artThree = new Art.ArtBuilder("Mona Lisa", ArtType.PAINTING, "Abc", LocalDate.of(1600, 02, 23)).build();
        Art artFour = new Art.ArtBuilder("Bust of Nefertiti", ArtType.SCLUPTURE, "bbc", LocalDate.of(1600, 02, 23)).build();
        Art artFive = new Art.ArtBuilder("Duplicates of life", ArtType.SCLUPTURE, "bbc", LocalDate.of(1600, 02, 23)).build();

        galleryService.addArt(artOne);
        galleryService.addArt(artTwo);
        galleryService.addArt(artThree);
        galleryService.addArt(artFour);
        galleryService.addArt(artFive);

        List<String> artists = galleryService.getArtists();
        assertEquals(4, artists.size());
        assertEquals("Abc", artists.get(0));
        assertEquals("bbc", artists.get(1));
        assertEquals("bcd", artists.get(2));
        assertEquals("cde", artists.get(3));
    }

    @Test
    public void test_getArtByArtist() {
        Art artOne = new Art.ArtBuilder("Bust of Nefertiti", ArtType.SCLUPTURE, "Thutmose", LocalDate.of(1600, 02, 23)).build();
        Art artTwo = new Art.ArtBuilder("Bust of Nefertitin Version 2", ArtType.SCLUPTURE, "Thutmose", LocalDate.of(1600, 02, 23)).build();
        Art artThree = new Art.ArtBuilder("Mona Lisa", ArtType.PAINTING, "Leonard Di Vinci", LocalDate.of(1600, 02, 23)).build();
        Art artFour = new Art.ArtBuilder("Faux Bust of Nefertiti", ArtType.SCLUPTURE, "Elton John", LocalDate.of(1600, 02, 23)).build();
        galleryService.addArt(artOne);
        galleryService.addArt(artTwo);
        galleryService.addArt(artThree);
        galleryService.addArt(artFour);

        Set<Art> arts = galleryService.getArtByArtist("Thutmose");
        assertEquals(2, arts.size());
        assertTrue(arts.contains(artOne));
        assertTrue(arts.contains(artTwo));

        arts = galleryService.getArtByArtist("Leonard Di Vinci");
        assertEquals(1, arts.size());
        assertTrue(arts.contains(artThree));
    }

    @Test
    public void test_getRecentArt() {
        Art artOne = new Art.ArtBuilder("Bust of Nefertiti", ArtType.SCLUPTURE, "Thutmose", LocalDate.of(1600, 02, 23)).build();
        Art artTwo = new Art.ArtBuilder("Bust of Nefertitin Version 2", ArtType.SCLUPTURE, "Thutmose", LocalDate.now().minusDays(36)).build();
        Art artThree = new Art.ArtBuilder("Mona Lisa", ArtType.PAINTING, "Leonard Di Vinci", LocalDate.of(1600, 02, 23)).build();
        Art artFour = new Art.ArtBuilder("Faux Bust of Nefertiti", ArtType.SCLUPTURE, "Elton John", LocalDate.now().minusDays(300)).build();
        galleryService.addArt(artOne);
        galleryService.addArt(artTwo);
        galleryService.addArt(artThree);
        galleryService.addArt(artFour);

        Set<Art> recentArt = galleryService.getRecentArt();
        assertNotNull(recentArt);
        assertEquals(2, recentArt.size());
        assertTrue(recentArt.contains(artTwo));
        assertTrue(recentArt.contains(artFour));
    }

    @Test
    public void test_getArtByPrice_NoMinLimit() {
        Art artOne = new Art.ArtBuilder("Bust of Nefertiti", ArtType.SCLUPTURE, "Thutmose", LocalDate.of(1600, 02, 23))
                .price(new BigInteger("1500000"))
                .build();
        Art artTwo = new Art.ArtBuilder("Bust of Nefertitin Version 2", ArtType.SCLUPTURE, "Thutmose", LocalDate.of(Year.now().getValue(), 02, 23))
                .price(new BigInteger("15000"))
                .build();
        Art artThree = new Art.ArtBuilder("Mona Lisa", ArtType.PAINTING, "Leonard Di Vinci", LocalDate.of(1600, 02, 23))
                .build();
        Art artFour = new Art.ArtBuilder("Faux Bust of Nefertiti", ArtType.SCLUPTURE, "Elton John", LocalDate.of(Year.now().getValue(), 02, 23))
                .price(new BigInteger("1000"))
                .build();
        galleryService.addArt(artOne);
        galleryService.addArt(artTwo);
        galleryService.addArt(artThree);
        galleryService.addArt(artFour);

        Set<Art> artsByPrice = galleryService.getArtByPrice(null, new BigInteger("15000"));
        assertNotNull(artsByPrice);
        assertEquals(2, artsByPrice.size());
        assertTrue(artsByPrice.contains(artTwo));
        assertTrue(artsByPrice.contains(artFour));
    }

    @Test
    public void test_getArtByPrice_NoMaxLimit() {
        Art artOne = new Art.ArtBuilder("Bust of Nefertiti", ArtType.SCLUPTURE, "Thutmose", LocalDate.of(1600, 02, 23))
                .price(new BigInteger("1500000"))
                .build();
        Art artTwo = new Art.ArtBuilder("Bust of Nefertitin Version 2", ArtType.SCLUPTURE, "Thutmose", LocalDate.of(Year.now().getValue(), 02, 23))
                .price(new BigInteger("15000"))
                .build();
        Art artThree = new Art.ArtBuilder("Mona Lisa", ArtType.PAINTING, "Leonard Di Vinci", LocalDate.of(1600, 02, 23))
                .build();
        Art artFour = new Art.ArtBuilder("Faux Bust of Nefertiti", ArtType.SCLUPTURE, "Elton John", LocalDate.of(Year.now().getValue(), 02, 23))
                .price(new BigInteger("1000"))
                .build();
        galleryService.addArt(artOne);
        galleryService.addArt(artTwo);
        galleryService.addArt(artThree);
        galleryService.addArt(artFour);

        Set<Art> artsByPrice = galleryService.getArtByPrice(new BigInteger("15000"), null);
        assertNotNull(artsByPrice);
        assertEquals(2, artsByPrice.size());
        assertTrue(artsByPrice.contains(artOne));
        assertTrue(artsByPrice.contains(artTwo));
    }

    @Test
    public void test_getArtByPrice_MinMaxLimit() {
        Art artOne = new Art.ArtBuilder("Bust of Nefertiti", ArtType.SCLUPTURE, "Thutmose", LocalDate.of(1600, 02, 23))
                .price(new BigInteger("1500000"))
                .build();
        Art artTwo = new Art.ArtBuilder("Bust of Nefertitin Version 2", ArtType.SCLUPTURE, "Thutmose", LocalDate.of(Year.now().getValue(), 02, 23))
                .price(new BigInteger("15000"))
                .build();
        Art artThree = new Art.ArtBuilder("Mona Lisa", ArtType.PAINTING, "Leonard Di Vinci", LocalDate.of(1600, 02, 23))
                .build();
        Art artFour = new Art.ArtBuilder("Faux Bust of Nefertiti", ArtType.SCLUPTURE, "Elton John", LocalDate.of(Year.now().getValue(), 02, 23))
                .price(new BigInteger("1000"))
                .build();
        galleryService.addArt(artOne);
        galleryService.addArt(artTwo);
        galleryService.addArt(artThree);
        galleryService.addArt(artFour);

        Set<Art> artsByPrice = galleryService.getArtByPrice(new BigInteger("10000"), new BigInteger("20000"));
        assertNotNull(artsByPrice);
        assertEquals(1, artsByPrice.size());
        assertTrue(artsByPrice.contains(artTwo));
    }

    @Test
    public void test_getArtByPrice_MinMaxLimit_MinMaxPricedArtInculed() {
        Art artOne = new Art.ArtBuilder("Bust of Nefertiti", ArtType.SCLUPTURE, "Thutmose", LocalDate.of(1600, 02, 23))
                .price(new BigInteger("1500000"))
                .build();
        Art artTwo = new Art.ArtBuilder("Bust of Nefertitin Version 2", ArtType.SCLUPTURE, "Thutmose", LocalDate.of(Year.now().getValue(), 02, 23))
                .price(new BigInteger("15000"))
                .build();

        Art artThree = new Art.ArtBuilder("Mona Lisa", ArtType.PAINTING, "Leonard Di Vinci", LocalDate.of(1600, 02, 23))
                .build();
        Art artFour = new Art.ArtBuilder("Faux Bust of Nefertiti", ArtType.SCLUPTURE, "Elton John", LocalDate.of(Year.now().getValue(), 02, 23))
                .price(new BigInteger("1000"))
                .build();
        Art artFive = new Art.ArtBuilder("Faux Bust of Nefertiti Version 2", ArtType.SCLUPTURE, "Elton John", LocalDate.of(Year.now().getValue(), 02, 23))
                .price(new BigInteger("10000"))
                .build();
        Art artSix = new Art.ArtBuilder("Bust of Nefertitin Version 3", ArtType.SCLUPTURE, "Thutmose", LocalDate.of(Year.now().getValue(), 02, 23))
                .price(new BigInteger("20000"))
                .build();

        galleryService.addArt(artOne);
        galleryService.addArt(artTwo);
        galleryService.addArt(artThree);
        galleryService.addArt(artFour);
        galleryService.addArt(artFive);
        galleryService.addArt(artSix);

        Set<Art> artsByPrice = galleryService.getArtByPrice(new BigInteger("10000"), new BigInteger("20000"));
        assertNotNull(artsByPrice);
        assertEquals(3, artsByPrice.size());
        assertTrue(artsByPrice.contains(artTwo));
        assertTrue(artsByPrice.contains(artFive));
        assertTrue(artsByPrice.contains(artSix));
    }

    @Test
    public void test_getArtByPrice_InvalidLimit() {
        thrown.expect(InvalidPriceRangeException.class);
        galleryService.getArtByPrice(new BigInteger("20000"), new BigInteger("10000"));
    }
}
