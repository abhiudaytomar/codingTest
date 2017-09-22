package dto;

import constants.ArtType;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

public final class Art {
    @NotNull(message = "Art name must be provided")
    private final String name;
    @NotNull(message = "Art type must be provided")
    private final ArtType artType;
    @NotNull(message = "Artist name must be provided")
    private final String artistName;
    @NotNull(message = "Art creation date must be provided")
    private final LocalDate creationDate;

    private final BigDecimal price;

    private Art(ArtBuilder artBuilder) {
        this.name = artBuilder.name;
        this.artType = artBuilder.artType;
        this.artistName = artBuilder.artistName;
        this.creationDate = artBuilder.creationDate;
        this.price = artBuilder.price;
    }

    public static class ArtBuilder {
        private final String name;
        private final ArtType artType;
        private final String artistName;
        private final LocalDate creationDate;
        private BigDecimal price;

        public ArtBuilder(String name, ArtType artType, String artistName, LocalDate creationDate) {
            this.name = name;
            this.artType = artType;
            this.artistName = artistName;
            this.creationDate = creationDate;
        }

        public ArtBuilder price(BigDecimal price) {
            this.price = price;
            return this;
        }

        public Art build() {
            Art art = new Art(this);
            validate(art);
            return art;
        }

        private void validate(Art art) {
            ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
            Validator validator = factory.getValidator();
            Set<ConstraintViolation<Art>> validationResult = validator.validate(art);

            validationResult.isEmpty();
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (!(obj instanceof Art)) {
            return false;
        }

        Art art = (Art) obj;

        return new EqualsBuilder()
                .append(name, art.name)
                .append(artType, art.artType)
                .append(artistName, art.artistName)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(name)
                .append(artType)
                .append(artistName)
                .toHashCode();
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }

    public String getName() {
        return name;
    }

    public ArtType getArtType() {
        return artType;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public String getArtistName() {
        return artistName;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }
}
