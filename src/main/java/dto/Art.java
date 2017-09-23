package dto;

import constants.ArtType;
import exceptions.ConstraintViolationException;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.constraints.NotNull;
import java.math.BigInteger;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * This Class stores information about an Art
 * creationDate :- If no date is provided then today's date is assigned
 */
public final class Art {
    @NotNull(message = "Art name must be provided")
    private final String name;

    @NotNull(message = "Art type must be provided")
    private final ArtType artType;

    @NotNull(message = "Artist name must be provided")
    private final String artistName;

    private final BigInteger price;

    @NotNull(message = "Please provide creation date or use right constructor")
    private LocalDate creationDate;

    private Art(ArtBuilder artBuilder) {
        this.name = artBuilder.name;
        this.artType = artBuilder.artType;
        this.artistName = artBuilder.artistName;
        this.creationDate = artBuilder.creationDate;
        this.price = artBuilder.price;
    }

    public String getName() {
        return name;
    }

    public ArtType getArtType() {
        return artType;
    }

    public BigInteger getPrice() {
        return price;
    }

    public String getArtistName() {
        return artistName;
    }

    public LocalDate getCreationDate() {
        return creationDate;
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

    public static class ArtBuilder {
        private final String name;
        private final ArtType artType;
        private final String artistName;
        private LocalDate creationDate;
        private BigInteger price;

        public ArtBuilder(String name, ArtType artType, String artistName) {
            this.name = name;
            this.artType = artType;
            this.artistName = artistName;
            this.creationDate = LocalDate.now();
        }

        public ArtBuilder(String name, ArtType artType, String artistName, LocalDate creationDate) {
            this.name = name;
            this.artType = artType;
            this.artistName = artistName;
            this.creationDate = creationDate;
        }

        public ArtBuilder creationDate(LocalDate creationDate) {
            this.creationDate = creationDate;
            return this;
        }

        public ArtBuilder price(BigInteger price) {
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
            if (validationResult.size() > 0) {
                Set<String> violationMessages = new HashSet<>();

                for (ConstraintViolation<Art> constraintViolation : validationResult) {
                    violationMessages.add(constraintViolation.getPropertyPath() + ": " + constraintViolation.getMessage());
                }

                throw new ConstraintViolationException("Validation failed:\n" + StringUtils.join(violationMessages, "\n"));
            }
        }
    }
}
