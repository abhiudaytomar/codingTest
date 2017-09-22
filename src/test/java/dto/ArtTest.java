package dto;

import constants.ArtType;
import dto.Art.ArtBuilder;
import exceptions.ConstraintViolationException;
import static org.hamcrest.core.StringStartsWith.*;
import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.time.LocalDate;

public class ArtTest {
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
        Art art = new ArtBuilder("Mona lisa", ArtType.PAINTING, "Leonard Di Vinci", LocalDate.of(1600,02,23)).build();
        assertNotNull(art);
    }

    @Test
    public void testCreateArtWithAllValues() {}

    @Test
    public void testCreateArtWithAllValuesExceptPrice() {}


}
