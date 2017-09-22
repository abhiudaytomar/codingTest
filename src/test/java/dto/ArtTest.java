package dto;

import constants.ArtType;
import dto.Art.ArtBuilder;
import org.junit.Test;

import java.time.LocalDate;

public class ArtTest {
    @Test
    public void testCreateArtWithNullName() {
        Art art = new ArtBuilder(null, ArtType.PAINTING, "Leonard Di Vinci", LocalDate.of(1600,02,23)).build();
    };

    @Test
    public void testCreateArtWithNullArtType() {};

    @Test
    public void testCreateArtWithNullArtistName() {};

    @Test
    public void testCreateArtWithNullCreationDate() {};

    @Test
    public void testCreateArtWithNullPrice() {};

    @Test
    public void testCreateArtWithAllValues() {};

    @Test
    public void testCreateArtWithAllValuesExceptPrice() {};


}
