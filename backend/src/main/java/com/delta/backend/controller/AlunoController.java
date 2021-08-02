package com.delta.backend.controller;

import com.delta.backend.model.Aluno;


import com.delta.backend.repository.AlunoRepository;
import com.delta.backend.service.FileService;
import com.delta.backend.utils.FileUploadUtil;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Random;

@CrossOrigin
@RestController
@RequestMapping({"/aluno"})
public class AlunoController {

    @Autowired
    private FileService filesService;

    @Autowired
    private AlunoRepository alunoRepository;

    @GetMapping
    public List<Aluno> findAll(){
        return alunoRepository.findAll();
    }

    @GetMapping(path = {"/{id}"})
    public ResponseEntity<Aluno> findById(@PathVariable int id){
        return alunoRepository.findById(id)
                .map(record -> ResponseEntity.ok().body(record))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Aluno create(@RequestBody Aluno aluno){
        return alunoRepository.save(aluno);
    }

    @PutMapping(value="/{id}")
    public ResponseEntity<Aluno> update(@PathVariable("id") int id,
                                 @RequestBody Aluno aluno) {
        return alunoRepository.findById(id)
                .map(registro -> {
                    registro.setNome(aluno.getNome());
                    registro.setPath_foto(aluno.getPath_foto());
                    registro.setEndereco(aluno.getEndereco());
                    Aluno updated = alunoRepository.save(aluno);
                    return ResponseEntity.ok().body(updated);
                }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping(path ={"/{id}"})
    public ResponseEntity <?> delete(@PathVariable int id) {
        return alunoRepository.findById(id)
                .map(registro -> {
                    alunoRepository.deleteById(id);
                    return ResponseEntity.ok().build();
                }).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping(path ={"/foto"},consumes = "multipart/form-data")
    public String salvaFoto(@RequestParam("file") MultipartFile multipartFile) throws IOException {
        Random rand = new Random();
        int r = Math.abs(rand.nextInt());
        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename()).replaceAll("\\s","");
        String uploadCaminho = "c:/home/fotos/"+r;
        String relPath = "/fotos/"+r + "/" + fileName;
        FileUploadUtil.saveFile(uploadCaminho, fileName,multipartFile);

        return relPath;
    }

    @GetMapping(path={"/foto/{path}"},produces = MediaType.IMAGE_JPEG_VALUE)
    public void getFoto(HttpServletResponse response, @PathVariable String path) throws IOException{
        String _path = path.replaceAll("-","/");
        System.out.println(_path);
        var in = new ClassPathResource(_path);
        //@ResponseBody byte[]
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(in.getInputStream(), response.getOutputStream());
        //return IOUtils.toByteArray(in);
    }

    @GetMapping(path = "/getFileByKey/{key}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public @ResponseBody
    byte[] getFileByKey(@PathVariable String key) throws IOException {
        String _path = key.replaceAll("-","/");
        System.out.println(_path);
        return FileUploadUtil.getInstance().getFileBytes(filesService.getFile(_path));
    }

}
