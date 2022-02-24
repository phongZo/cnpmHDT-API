package com.cnpmHDT.api.service;

import com.cnpmHDT.api.dto.ApiMessageDto;
import com.cnpmHDT.api.constant.cnpmHDTConstant;
import com.cnpmHDT.api.dto.UploadFileDto;
import com.cnpmHDT.api.storage.model.Permission;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
public class cnpmHDTApiService {

    static final String[] UPLOAD_TYPES = new String[]{"LOGO", "AVATAR","IMAGE", "DOCUMENT"};

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    OTPService OTPService;

    @Autowired
    CommonAsyncService commonAsyncService;



    public void deleteFile(String filePath) {
        File file = new File(cnpmHDTConstant.ROOT_DIRECTORY + filePath);
//        file.deleteOnExit();
        if(file.exists()) file.delete();
    }

    public Resource loadFileAsResource(String folder, String fileName) {

        try {
            Path fileStorageLocation = Paths.get(cnpmHDTConstant.ROOT_DIRECTORY + File.separator + folder).toAbsolutePath().normalize();
            Path fP = fileStorageLocation.resolve(fileName).normalize();
            Resource resource = new UrlResource(fP.toUri());
            if (resource.exists()) {
                return resource;
            }
        } catch (MalformedURLException ex) {
            log.error(ex.getMessage(), ex);

        }
        return null;
    }

    public InputStreamResource loadFileAsResourceExt(String folder, String fileName) {

        try {
            File file = new File(cnpmHDTConstant.ROOT_DIRECTORY + File.separator + folder + File.separator + fileName);
            InputStreamResource inputStreamResource = new InputStreamResource(new FileInputStream(file));
            if (inputStreamResource.exists()) {
                return inputStreamResource;
            }
        } catch (FileNotFoundException ex) {
            log.error(ex.getMessage(), ex);

        }
        return null;
    }


    public String getOTPForgetPassword(){
        return OTPService.generate(4);
    }

    public synchronized Long getOrderHash(){
        return Long.parseLong(OTPService.generate(9));
    }

    // create generated referral code
    public synchronized String getReferralCode(){
        return OTPService.generate(6);
    }

    public void pushToFirebase(String url, String data){
        commonAsyncService.pushToFirebase(url,data);
    }

    public void sendEmail(String email, String msg, String subject, boolean html){
        commonAsyncService.sendEmail(email,msg,subject,html);
    }

    public String convertGroupToUri(List<Permission> permissions){
        if(permissions!=null){
            StringBuilder builderPermission = new StringBuilder();
            for(Permission p : permissions){
                builderPermission.append(p.getAction().trim().replace("/v1","")+",");
            }
            return  builderPermission.toString();
        }
        return null;
    }
}
