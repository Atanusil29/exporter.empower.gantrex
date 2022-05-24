package com.gantrex.dbo.model;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the document_content database table.
 * 
 */
@Entity
@Table(name="document_content")
@NamedQuery(name="DocumentContent.findAll", query="SELECT d FROM DocumentContent d")
public class DocumentContent implements Serializable {
	private static final long serialVersionUID = 1L;
	private String documentId;
	private byte[] contents;
	private Document document;

	public DocumentContent() {
	}


	@Id
	@Column(name="document_id", unique=true, nullable=false, length=50)
	public String getDocumentId() {
		return this.documentId;
	}

	public void setDocumentId(String documentId) {
		this.documentId = documentId;
	}


	public byte[] getContents() {
		return this.contents;
	}

	public void setContents(byte[] contents) {
		this.contents = contents;
	}


	//bi-directional one-to-one association to Document
	@OneToOne
	@JoinColumn(name="document_id", nullable=false, insertable=false, updatable=false)
	public Document getDocument() {
		return this.document;
	}

	public void setDocument(Document document) {
		this.document = document;
	}

}