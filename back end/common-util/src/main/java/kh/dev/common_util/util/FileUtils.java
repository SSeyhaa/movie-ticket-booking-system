package kh.dev.common_util.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class FileUtils {

  public static String saveImage(String target, MultipartFile file) {
    validateImage(file);

    String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
    Path targetLocation = Paths.get(target).resolve(fileName);

    try {
      Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
      return targetLocation.toString();
    } catch (IOException e) {
      throw new RuntimeException("Could not store file " + fileName, e);
    }
  }

  public static void validateImage(MultipartFile file) {
    if (file.isEmpty()) {
      throw new IllegalArgumentException("File is empty");
    }

    String contentType = file.getContentType();
    if (contentType == null
        || (!contentType.equals("image/png") && !contentType.equals("image/jpeg"))) {
      throw new IllegalArgumentException("Invalid file type. Only PNG and JPEG are allowed.");
    }

    if (file.getSize() > 2_000_000) { // 2MB limit
      throw new IllegalArgumentException("File size exceeds 2MB limit");
    }
  }
}
