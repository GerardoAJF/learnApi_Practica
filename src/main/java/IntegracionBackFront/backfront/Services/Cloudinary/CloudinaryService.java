package IntegracionBackFront.backfront.Services.Cloudinary;

import com.cloudinary.Cloudinary;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.Arrays;

@Slf4j
@Service
public class CloudinaryService {
    // 1. Definir el tamaño de las imágenes en megaBytes
    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024; // 5 MB
    // 2. Definir las extensiones permitidas
    private static final String[] ALLOWER_EXTENSIONS = {
      ".jpg", ".jpeg", ".png"
    };
    // 3. Atributo Cloudinary
    private final Cloudinary cloudinary;

    // Contructor para inyección de dependencias de Cloudinary
    public CloudinaryService(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    public String uploadImage(MultipartFile file) throws IOException {
        validateImage(file);


    }

    private void validateImage(MultipartFile file) {
        // 1. Verificar si el archivo esta vacío
         if (file.isEmpty()) {
             throw new IllegalArgumentException("El archivo está vacío");
         }

         // 2. Verificar si el tamaño excede el limite permitido
        if (file.getSize() > MAX_FILE_SIZE) {
            throw new IllegalArgumentException("El tamaño del archivo no debe ser mayor a 5 MB");
        }

        // 3. Verificar la extensión del archivo
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null) {
            throw  new IllegalArgumentException("Nombre de archivo inválido");
        }

        // 4. Extraer y validar la extensión del archivo
        String extension = originalFilename.substring(originalFilename.lastIndexOf(".")).toLowerCase();
        if (!Arrays.asList(ALLOWER_EXTENSIONS).contains(extension)) {
            throw  new IllegalArgumentException("Solo se permiten archivos JPG, JPEG, PNG");
        }

        // 5. Verficar que el tipo MIME sea una imagen
        if (!file.getContentType().startsWith("image/")) {
            throw new IllegalArgumentException("El archivo debe ser una imagen válida");
        }
    }


}
