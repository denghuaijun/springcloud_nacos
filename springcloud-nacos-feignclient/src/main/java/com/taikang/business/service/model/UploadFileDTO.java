package com.taikang.business.service.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @author dhj
 * @Description
 * @Date Create in 13:52 2019/10/18
 */
@Data
public class UploadFileDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private byte[] bytes;

    private String fileName;

    private String contentType;

    private String originalFilename;

    public byte[] getBytes() {
        return bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getOriginalFilename() {
        return originalFilename;
    }

    public void setOriginalFilename(String originalFilename) {
        this.originalFilename = originalFilename;
    }
}
