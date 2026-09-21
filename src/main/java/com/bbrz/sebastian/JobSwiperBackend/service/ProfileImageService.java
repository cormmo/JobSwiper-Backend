package com.bbrz.sebastian.JobSwiperBackend.service;

import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.Iterator;
import java.util.Locale;

/**
 * Validates and decodes Base64 encoded profile images.
 *
 * <p>Checks file size, image dimensions and supported image formats.</p>
 */
@Service
public class ProfileImageService {

    static final int MAX_IMAGE_BYTES = 5 * 1024 * 1024;
    static final int MAX_IMAGE_DIMENSION = 4096;

    /**
     * Decodes and validates a Base64 encoded image.
     *
     * @param base64Image Base64 image data
     * @return validated image data and media type
     */
    public ValidatedImage validateAndDecode(String base64Image) {
        String declaredMediaType = null;
        String encodedImage = base64Image.trim();

        if (encodedImage.regionMatches(true, 0, "data:", 0, 5)) {
            int separator = encodedImage.indexOf(',');

            if (separator < 0) {
                throw new IllegalArgumentException("Invalid image data URL");
            }

            String metadata = encodedImage.substring(5, separator);
            String[] metadataParts = metadata.split(";");

            if (metadataParts.length != 2
                    || !"base64".equalsIgnoreCase(metadataParts[1])) {
                throw new IllegalArgumentException(
                        "Image data URL must contain Base64 data"
                );
            }

            declaredMediaType = metadataParts[0].toLowerCase(Locale.ROOT);
            encodedImage = encodedImage.substring(separator + 1);
        }

        if (encodedImage.length() > ((MAX_IMAGE_BYTES + 2L) / 3L) * 4L) {
            throw new IllegalArgumentException("Image must not exceed 5 MB");
        }

        final byte[] image;

        try {
            image = Base64.getDecoder().decode(encodedImage);
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException(
                    "Image contains invalid Base64 data"
            );
        }

        if (image.length == 0) {
            throw new IllegalArgumentException("Image must not be empty");
        }

        if (image.length > MAX_IMAGE_BYTES) {
            throw new IllegalArgumentException("Image must not exceed 5 MB");
        }

        String actualMediaType = inspectImage(image);

        if (declaredMediaType != null
                && !declaredMediaType.equals(actualMediaType)) {
            throw new IllegalArgumentException(
                    "Declared image media type does not match its content"
            );
        }

        return new ValidatedImage(image, actualMediaType);
    }

    /**
     * Checks the image format and dimensions.
     *
     * @param image decoded image data
     * @return detected media type
     */
    private String inspectImage(byte[] image) {
        try (ImageInputStream input =
                     ImageIO.createImageInputStream(new ByteArrayInputStream(image))) {

            if (input == null) {
                throw new IllegalArgumentException(
                        "Unsupported or invalid image"
                );
            }

            Iterator<ImageReader> readers = ImageIO.getImageReaders(input);

            if (!readers.hasNext()) {
                throw new IllegalArgumentException(
                        "Unsupported or invalid image"
                );
            }

            ImageReader reader = readers.next();

            try {
                reader.setInput(input, true, true);

                int width = reader.getWidth(0);
                int height = reader.getHeight(0);

                if (width <= 0 || height <= 0
                        || width > MAX_IMAGE_DIMENSION
                        || height > MAX_IMAGE_DIMENSION) {
                    throw new IllegalArgumentException(
                            "Image dimensions must be between 1 and 4096 pixels"
                    );
                }

                reader.read(0);

                return mediaType(reader.getFormatName());
            } finally {
                reader.dispose();
            }

        } catch (IOException ex) {
            throw new IllegalArgumentException(
                    "Unsupported or invalid image"
            );
        }
    }

    /**
     * Converts an image format name to its media type.
     *
     * @param formatName image format name
     * @return matching media type
     */
    private String mediaType(String formatName) {
        return switch (formatName.toLowerCase(Locale.ROOT)) {
            case "jpeg", "jpg" -> "image/jpeg";
            case "png" -> "image/png";
            case "gif" -> "image/gif";
            default -> throw new IllegalArgumentException(
                    "Only JPEG, PNG, and GIF images are supported"
            );
        };
    }

    /**
     * Stores validated image data and its media type.
     *
     * @param data image data
     * @param mediaType image media type
     */
    public record ValidatedImage(byte[] data, String mediaType) {

        public ValidatedImage {
            data = data.clone();
        }

        @Override
        public byte[] data() {
            return data.clone();
        }
    }
}
