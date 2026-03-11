package com.concept.conceptbridge.material;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MaterialService {

    @Autowired
    private MaterialRepository materialRepository;

    @Value("${pdf.storage.path:uploads/pdfs}")
    private String storagePath;
    private static final String UPLOAD_DIR = "uploads/pdfs/";

    public List<Material> getAvailablePdfsForStudent(String branch, Integer semester) {
        return materialRepository.findByBranchAndSemesterAndVisibility(
                branch, semester, "PUBLIC");
    }

    public Resource openPdf(Long pdfId) throws Exception {
        Material material = materialRepository.findById(pdfId)
                .orElseThrow(() -> new RuntimeException("PDF not found with id: " + pdfId));

        Path filePath = Paths.get(storagePath).resolve(material.getFileUrl()).normalize();
        System.out.println("Looking for PDF at: " + filePath.toString());

        Resource resource = new UrlResource(filePath.toUri());

        if (!resource.exists() || !resource.isReadable()) {
            throw new RuntimeException("PDF file not found at: " + filePath);
        }

        return resource;
    }


    public void deleteMaterial(Long id) {

        Material material = materialRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Material not found"));

        String fileName = material.getFileUrl(); // this is only "1773130623147_1.pdf"

        try {

            Path filePath = Paths.get("uploads", "pdfs", fileName);

            System.out.println("Deleting file: " + filePath.toAbsolutePath());

            Files.deleteIfExists(filePath);

        } catch (Exception e) {
            e.printStackTrace();
        }

        materialRepository.delete(material);
    }
    public Map<String, Object> getPdfBase64(Long pdfId) throws Exception {
        Material material = materialRepository.findById(pdfId)
                .orElseThrow(() -> new RuntimeException("PDF not found with id: " + pdfId));

        // Try multiple possible locations
        String[] possiblePaths = {
                storagePath,
                "uploads/pdfs",
                "src/main/resources/static/pdfs"
        };

        Path filePath = null;
        Resource resource = null;

        for (String path : possiblePaths) {
            filePath = Paths.get(path).resolve(material.getFileUrl()).normalize();
            resource = new UrlResource(filePath.toUri());
            if (resource.exists() && resource.isReadable()) {
                break;
            }
        }

        if (resource == null || !resource.exists()) {
            throw new RuntimeException("PDF file not found: " + material.getFileUrl() +
                    ". Checked in: " + String.join(", ", possiblePaths));
        }

        byte[] pdfBytes = Files.readAllBytes(filePath);
        String base64 = Base64.getEncoder().encodeToString(pdfBytes);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("title", material.getTitle());
        response.put("data", base64);
        response.put("size", pdfBytes.length);

        return response;
    }


    // ============ NEW TEACHER METHODS ============

    public List<Material> getMaterialsByTeacher(Long uploadedBy) {
        return materialRepository.findByUploadedByOrderByCreatedAtDesc(uploadedBy);
    }

    public Material uploadMaterial(MultipartFile file,
                                   String title,
                                   String description,
                                   String subject,
                                   String branch,
                                   Integer semester,
                                   Long uploadedBy) throws IOException {

        // Create upload directory
        File uploadDir = new File(UPLOAD_DIR);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        // Generate unique filename
        String originalFilename = file.getOriginalFilename();
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String uniqueFilename = System.currentTimeMillis() + "_" + uploadedBy + extension;

        // Save file
        String filePath = UPLOAD_DIR + uniqueFilename;
        Path path = Paths.get(filePath);
        Files.copy(file.getInputStream(), path);

        // Create material record
        Material material = new Material();
        material.setTitle(title);
        material.setDescription(description);
        material.setSubject(subject);
        material.setBranch(branch);
        material.setSemester(semester);
        material.setUploadedBy(uploadedBy);
        material.setFileUrl(uniqueFilename);
        material.setVisibility("PUBLIC");
        material.setCreatedAt(LocalDateTime.now());

        return materialRepository.save(material);
    }



    public List<Material> getAllMaterials() {
        return materialRepository.findAll();
    }
}