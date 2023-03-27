package com.uber.driverselfservice.interfaces.dtos.driver;

import javax.validation.constraints.NotEmpty;

import com.uber.driverselfservice.domain.driver.Document;

/**
 * The DocumentDto represents the message payload to change a driver's vehicle.
 */
public class DocumentDto {
    @NotEmpty
    private String documentName;

    @NotEmpty
    private String documentType;

    @NotEmpty
    private byte[] data;

    public DocumentDto() {
    }

    public DocumentDto(String documentName, String documentType, byte[] data) {
        this.documentName = documentName;
        this.documentType = documentType;
        this.data = data;
    }

    public static DocumentDto fromDomainObject(Document document) {
        return new DocumentDto(document.getDocumentName(), document.getDocumentType(), document.getData());
    }

    public Document toDomainObject() {
        return new Document(documentName, documentType, data);
    }

    public String getDocumentName() {
        return documentName;
    }

    public String getDocumentType() {
        return documentType;
    }

    public byte[] getData() {
        return data;
    }

    public void setDocumentName(String vehicleName) {
        this.documentName = documentName;
    }

    public void setDocumentType(String licensePlateNumber) {
        this.documentType = documentType;
    }

    public void setData(String registeredCity) {
        this.data = data;
    }
}