package com.uber.driverdataregistry.domain.driver;

import javax.persistence.*;

@Entity
@Table(name = "documents")
public class Document {
    @GeneratedValue
    @Id
    private Long id;

    private final String documentName;

    private final String documentType;

    @Lob
    private final byte[] data;

    public Document() {
        this.documentName = null;
        this.documentType = null;
        this.data = null;
    }

    public Document(String documentName, String documentType, byte[] data) {
        this.documentName = documentName;
        this.documentType = documentType;
        this.data = data;
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

    @Override
    public String toString() {
        return String.format("%s, %s %ss", documentName, documentType, data);
    }

}
