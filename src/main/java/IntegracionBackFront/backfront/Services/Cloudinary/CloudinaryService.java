package IntegracionBackFront.backfront.Services.Cloudinary;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
public class CloudinaryService {

    // Constante para definir el tamaño máxmo permitido para los archivos (5MB)
    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024;

    // Extensiones de archivo permitidas para subir a Cloudinary
    private static final String[] ALLOWED_EXTENSION = {".jpg", ".jpeg", ".png"};

    // Cliente de Cloudinary inyectado como dependencia
    private final Cloudinary cloudinary;

    public CloudinaryService (Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    // Subir imágenes a la raíz de Cloudinary
    public String uploadImage (MultipartFile file) throws IOException {
        validateImage(file);
        Map<?, ?> uploadResult = cloudinary.uploader().upload(
                file.getBytes(),
                ObjectUtils.asMap(
                        "resource_type", "auto",
                        "quality", "auto:good"
                ));

        return (String)uploadResult.get("secure_url");
    }

    public String uploadImage (MultipartFile file, String folder) throws IOException {
        validateImage(file);
        String originalFilename = file.getOriginalFilename();
        String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String uniqueFilename = "img_" + UUID.randomUUID() + fileExtension;

        Map<String, Object> options = ObjectUtils.asMap(
                "folder", folder, // Carpeta de destino
                "public_id", uniqueFilename, // Nombre único para el archivo
                "use_filename", false, // No usar el nombre original
                "unique_filename", false, // No generará nombre único (ya lo hicimos)
                "overwrite", false, // No sobreescribir archivos existentes
                "resource_type", "auto",
                "quality", "auto:good"
        );
        Map<?, ?> uploadResult = cloudinary.uploader().upload(file.getBytes(), options);
        return (String)uploadResult.get("secure_url");
    }

    private void validateImage(MultipartFile file) {
        // Verificar si el archivo esta vacío
        if (file.isEmpty()) throw new IllegalArgumentException("El archivo no puede estar vacío"); // Verificar si el archivo está vacío
        if (file.getSize() > MAX_FILE_SIZE) throw new IllegalArgumentException("El archivo no puede superar los 5MB"); // Verificar si el tamaño del archivo excede el limite permitido
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null) throw new IllegalArgumentException("Nombre de archivo no válido");
        String extension = originalFilename.substring(originalFilename.lastIndexOf(".")).toLowerCase();
        if (!Arrays.asList(ALLOWED_EXTENSION).contains(extension)) throw new IllegalArgumentException("Solo se permiten archivos .jpg, .jpeg o .png");
        if(!file.getContentType().startsWith("image/")) throw  new IllegalArgumentException("El archivo debe ser una imagen válida");

    }


    // Subir imágenes a una carpeta de Cloudinary
}
