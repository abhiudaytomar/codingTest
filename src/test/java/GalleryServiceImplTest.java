import constants.ArtType;
import dto.Art;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import service.GalleryService;
import service.GalleryServiceImpl;

import java.math.BigDecimal;
import java.time.LocalDate;

public class GalleryServiceImplTest {
    private GalleryService galleryService;

    @Before
    public void setUp() {
        galleryService = new GalleryServiceImpl();
    }

    @Test
    public void testAddNullArt() {
        boolean result = galleryService.addArt(null);
        Assert.assertFalse(result);
    }

    @Test
    public void testAddArt() {
        Art art = new Art.ArtBuilder("Mona Lisa", ArtType.PAINTING, "Leonard Di Vinci", LocalDate.of(1600,02,23)).build();
        Assert.assertTrue(galleryService.addArt(art));
    }

    @Test
    public void testAddArtWithNullName() {
        Art art = new Art.ArtBuilder(null, ArtType.PAINTING, "Leonard Di Vinci", LocalDate.of(1600,02,23)).build();
        Assert.assertTrue(galleryService.addArt(art));
    }
}
