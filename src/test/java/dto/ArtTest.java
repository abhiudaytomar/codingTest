package dto;

import constants.ArtType;
import dto.Art.ArtBuilder;
import exceptions.ConstraintViolationException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.math.BigInteger;
import java.time.LocalDate;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.core.StringStartsWith.startsWith;
import static org.junit.Assert.*;

public class ArtTest {
    private final int YEAR_1600 = 1600;
    private final int MONTH_02 = 2;
    private final int DAY_23 = 23;
    private final BigInteger PRICE_IN_PENCE = new BigInteger("123400");

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void testCreateArtWithNullName() {
        thrown.expect(ConstraintViolationException.class);
        thrown.expectMessage(startsWith("Validation failed"));
        thrown.expectMessage(containsString("Art name must be provided"));

        Art art = new ArtBuilder(null, ArtType.PAINTING, "Leonard Di Vinci", LocalDate.now()).build();
    }

    @Test
    public void testCreateArtWithNullArtType() {
        thrown.expect(ConstraintViolationException.class);
        thrown.expectMessage(startsWith("Validation failed"));
        thrown.expectMessage(containsString("Art type must be provided"));
        Art art = new ArtBuilder("Mona lisa", null, "Leonard Di Vinci", LocalDate.now()).build();
    }

    @Test
    public void testCreateArtWithNullArtistName() {
        thrown.expect(ConstraintViolationException.class);
        thrown.expectMessage(startsWith("Validation failed"));
        thrown.expectMessage(containsString("Artist name must be provided"));
        Art art = new ArtBuilder("Mona lisa", ArtType.PAINTING, null, LocalDate.now()).build();
    }

    @Test
    public void testCreateArtWithNullCreationDate() {
        thrown.expect(ConstraintViolationException.class);
        thrown.expectMessage(startsWith("Validation failed"));
        thrown.expectMessage(containsString("Please provide creation date or use right constructor"));
        Art art = new ArtBuilder("Mona lisa", ArtType.PAINTING, "Leonard Di Vinci", null).build();
    }

    @Test
    public void testCreateArtWithAllNullValues() {
        thrown.expect(ConstraintViolationException.class);
        thrown.expectMessage(startsWith("Validation failed"));
        thrown.expectMessage(containsString("Artist name must be provided"));
        thrown.expectMessage(containsString("Art type must be provided"));
        thrown.expectMessage(containsString("Art name must be provided"));
        thrown.expectMessage(containsString("Please provide creation date or use right constructor"));
        Art art = new ArtBuilder(null, null, null, null).build();
    }

    @Test
    public void testCreateArtWithNullPrice() {
        Art art = new ArtBuilder("Mona lisa", ArtType.PAINTING, "Leonard Di Vinci", LocalDate.of(YEAR_1600, MONTH_02, DAY_23)).build();
        assertNotNull(art);
        assertEquals("Mona lisa", art.getName());
        assertEquals(ArtType.PAINTING, art.getArtType());
        assertEquals("Leonard Di Vinci", art.getArtistName());
        assertNull(art.getPrice());
        assertTrue(LocalDate.of(YEAR_1600, MONTH_02, DAY_23).compareTo(art.getCreationDate()) == 0);
    }

    /*
     WARNING : This test might fail if time of the day is 23 hour 59 min 59 sec.
     */
    @Test
    public void testCreateArtWithoutDateWithNullPrice() {
        Art art = new ArtBuilder("Mona lisa", ArtType.PAINTING, "Leonard Di Vinci").build();
        assertNotNull(art);
        assertTrue(LocalDate.now().compareTo(art.getCreationDate()) == 0);
        assertEquals("Mona lisa", art.getName());
        assertEquals(ArtType.PAINTING, art.getArtType());
        assertEquals("Leonard Di Vinci", art.getArtistName());
        assertNull(art.getPrice());
    }

    /*
     WARNING : This test might fail if time of the day is 23 hour 59 min 59 sec.
    */
    @Test
    public void testCreateArtWithoutDateWithPriceSet() {
        Art art = new ArtBuilder("Mona lisa", ArtType.PAINTING, "Leonard Di Vinci").price(PRICE_IN_PENCE).build();
        assertNotNull(art);
        assertTrue(LocalDate.now().compareTo(art.getCreationDate()) == 0);
        assertTrue(PRICE_IN_PENCE.compareTo(art.getPrice()) == 0);
        assertEquals("Mona lisa", art.getName());
        assertEquals(ArtType.PAINTING, art.getArtType());
        assertEquals("Leonard Di Vinci", art.getArtistName());
    }

    @Test
    public void testCreateArtWithAllValues() {
        Art art = new ArtBuilder("Mona lisa", ArtType.PAINTING, "Leonard Di Vinci", LocalDate.of(YEAR_1600, MONTH_02, DAY_23)).price(PRICE_IN_PENCE).build();
        assertNotNull(art);
        assertEquals("Mona lisa", art.getName());
        assertEquals(ArtType.PAINTING, art.getArtType());
        assertEquals("Leonard Di Vinci", art.getArtistName());
        assertTrue(LocalDate.of(YEAR_1600, MONTH_02, DAY_23).compareTo(art.getCreationDate()) == 0);
        assertTrue(PRICE_IN_PENCE.compareTo(art.getPrice()) == 0);
    }

    @Test
    public void testCreateArtEqualsMethodEqualScenario() {
        Art artOne = new ArtBuilder("Mona lisa", ArtType.PAINTING, "Leonard Di Vinci", LocalDate.of(YEAR_1600, MONTH_02, DAY_23)).price(PRICE_IN_PENCE).build();
        Art artTwo = new ArtBuilder("Mona lisa", ArtType.PAINTING, "Leonard Di Vinci", LocalDate.now()).price(PRICE_IN_PENCE).build();
        assertNotNull(artOne);
        assertNotNull(artTwo);
        assertTrue(artOne.equals(artTwo));
    }

    @Test
    public void testCreateEqualsMethodNotEqualScenario() {
        Art artOne = new ArtBuilder("Mona lisa", ArtType.PAINTING, "Leonard Di Vinci", LocalDate.of(YEAR_1600, MONTH_02, DAY_23)).price(PRICE_IN_PENCE).build();
        Art artTwo = new ArtBuilder("Faux De Mona lisa!", ArtType.PAINTING, "Leonard Di Vinci", LocalDate.of(YEAR_1600, MONTH_02, DAY_23)).price(PRICE_IN_PENCE).build();
        assertNotNull(artOne);
        assertNotNull(artTwo);
        assertFalse(artOne.equals(artTwo));
    }
}
