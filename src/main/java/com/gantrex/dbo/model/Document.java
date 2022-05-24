package com.gantrex.dbo.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Type;

/**
 * The persistent class for the document database table.
 * 
 */
@Entity
@Table(name = "document")
@NamedQuery(name = "Document.findAll", query = "SELECT d FROM Document d")
public class Document implements Serializable {
	private static final long serialVersionUID = 1L;
	private String id;
	private Date abandoneddate;
	private String applicationId;
	private String applicationname;
	private String applicationoi;
	private String applicationpath;
	private Date createdate;
	private String customid;
	private String databaseid;
	private Boolean deleted;
	private String documentversion;
	private Date editdate;
	private String editorversion;
	private String engineversion;
	private Date exportdate;
	private String filename;
	private Date importdate;
	private String jobdefinitionid;
	private String packagecreatedate;
	private String packagefilename;
	private String packageversion;
	private String previewpubfile;
	private Date savedate;
	private String servicename;
	private int serviceversion;
	private String userid;
	private DocumentContent documentContent;

	public Document() {
	}

	@Id
	@Column(name="id", unique = true, nullable = false, length = 50)
	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Temporal(TemporalType.TIMESTAMP)
	public Date getAbandoneddate() {
		return this.abandoneddate;
	}

	public void setAbandoneddate(Date abandoneddate) {
		this.abandoneddate = abandoneddate;
	}

	@Column(name = "application_id", length = 50)
	public String getApplicationId() {
		return this.applicationId;
	}

	public void setApplicationId(String applicationId) {
		this.applicationId = applicationId;
	}

	public String getApplicationname() {
		return this.applicationname;
	}

	public void setApplicationname(String applicationname) {
		this.applicationname = applicationname;
	}

	@Column(length = 100)
	public String getApplicationoi() {
		return this.applicationoi;
	}

	public void setApplicationoi(String applicationoi) {
		this.applicationoi = applicationoi;
	}

	public String getApplicationpath() {
		return this.applicationpath;
	}

	public void setApplicationpath(String applicationpath) {
		this.applicationpath = applicationpath;
	}

	@Temporal(TemporalType.TIMESTAMP)
	public Date getCreatedate() {
		return this.createdate;
	}

	public void setCreatedate(Date createdate) {
		this.createdate = createdate;
	}

	public String getCustomid() {
		return this.customid;
	}

	public void setCustomid(String customid) {
		this.customid = customid;
	}

	@Column(length = 50)
	public String getDatabaseid() {
		return this.databaseid;
	}

	public void setDatabaseid(String databaseid) {
		this.databaseid = databaseid;
	}

	@Type(type="numeric_boolean")
	@Column(name = "deleted")
	public Boolean getDeleted() {
		return this.deleted;
	}

	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
	}

	@Column(length = 100)
	public String getDocumentversion() {
		return this.documentversion;
	}

	public void setDocumentversion(String documentversion) {
		this.documentversion = documentversion;
	}

	@Temporal(TemporalType.TIMESTAMP)
	public Date getEditdate() {
		return this.editdate;
	}

	public void setEditdate(Date editdate) {
		this.editdate = editdate;
	}

	@Column(length = 50)
	public String getEditorversion() {
		return this.editorversion;
	}

	public void setEditorversion(String editorversion) {
		this.editorversion = editorversion;
	}

	@Column(length = 100)
	public String getEngineversion() {
		return this.engineversion;
	}

	public void setEngineversion(String engineversion) {
		this.engineversion = engineversion;
	}

	@Temporal(TemporalType.TIMESTAMP)
	public Date getExportdate() {
		return this.exportdate;
	}

	public void setExportdate(Date exportdate) {
		this.exportdate = exportdate;
	}

	public String getFilename() {
		return this.filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	@Temporal(TemporalType.TIMESTAMP)
	public Date getImportdate() {
		return this.importdate;
	}

	public void setImportdate(Date importdate) {
		this.importdate = importdate;
	}

	@Column(length = 50)
	public String getJobdefinitionid() {
		return this.jobdefinitionid;
	}

	public void setJobdefinitionid(String jobdefinitionid) {
		this.jobdefinitionid = jobdefinitionid;
	}

	@Column(length = 1)
	public String getPackagecreatedate() {
		return this.packagecreatedate;
	}

	public void setPackagecreatedate(String packagecreatedate) {
		this.packagecreatedate = packagecreatedate;
	}

	public String getPackagefilename() {
		return this.packagefilename;
	}

	public void setPackagefilename(String packagefilename) {
		this.packagefilename = packagefilename;
	}

	@Column(length = 100)
	public String getPackageversion() {
		return this.packageversion;
	}

	public void setPackageversion(String packageversion) {
		this.packageversion = packageversion;
	}

	public String getPreviewpubfile() {
		return this.previewpubfile;
	}

	public void setPreviewpubfile(String previewpubfile) {
		this.previewpubfile = previewpubfile;
	}

	@Temporal(TemporalType.TIMESTAMP)
	public Date getSavedate() {
		return this.savedate;
	}

	public void setSavedate(Date savedate) {
		this.savedate = savedate;
	}

	public String getServicename() {
		return this.servicename;
	}

	public void setServicename(String servicename) {
		this.servicename = servicename;
	}

	public int getServiceversion() {
		return this.serviceversion;
	}

	public void setServiceversion(int serviceversion) {
		this.serviceversion = serviceversion;
	}

	public String getUserid() {
		return this.userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	// bi-directional one-to-one association to DocumentContent
	@OneToOne(mappedBy = "document")
	public DocumentContent getDocumentContent() {
		return this.documentContent;
	}

	public void setDocumentContent(DocumentContent documentContent) {
		this.documentContent = documentContent;
	}

}