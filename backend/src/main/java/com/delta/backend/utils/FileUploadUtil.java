package com.delta.backend.utils;

import java.io.*;
import java.nio.file.*;
import java.util.UUID;


import org.apache.commons.io.FileUtils;
import org.springframework.web.multipart.MultipartFile;

public class FileUploadUtil {

    private static FileUploadUtil instance;

    public static void saveFile(String uploadDir, String fileName,
                                MultipartFile multipartFile) throws IOException {
        Path uploadPath = Paths.get(uploadDir);

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        try (InputStream inputStream = multipartFile.getInputStream()) {
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ioe) {
            throw new IOException("Não foi possível salvar o arquivo: " + fileName, ioe);
        }
    }

    public static FileUploadUtil getInstance() {

        if (instance == null) {
            instance = new FileUploadUtil();
        }

        return instance;
    }


    public File getFile(String fileKey, String diretorio) {

        File file;
        String str = diretorio + fileKey;
        file = new File(str);

        if (file.exists())
            return file;

        return null;
    }



    public byte[] getFileBytes(File file) throws IOException {


        try(ByteArrayOutputStream ous = new ByteArrayOutputStream(); InputStream ios = new FileInputStream(file)) {
            byte[] buffer = new byte[4096];
            int read = 0;

            while ((read = ios.read(buffer)) != -1) {
                ous.write(buffer, 0, read);
            }

            return ous.toByteArray();
        } catch (IOException ex) {
            throw new IOException();
        }
    }




    public void deleteFile(String caminho) {
        File arquivo = new File(caminho);

        arquivo.deleteOnExit();
    }






}
