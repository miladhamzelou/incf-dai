package org.incf.atlas.central.xml;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class ResourceBackUp {

	private Class<? extends Object> anchor = null;
	private final String ANCHOR_MODE = "anchor";
	private final String DIRECTORY_MODE = "directory";
	private String findableName = null;
	private String findMode = null;
	private boolean isReadable = false;
	private boolean isWritable = false;
	private File resourceFile = null;

	/*
	 * Constructor should announce whether resource was found. Use URL objects.
	 * Get rid of readable writable attributes? possibly?
	 */
	public ResourceBackUp(String fileName) {
		this.anchor = this.getClass();
		this.findableName = fileName.replace('\\', '/');
		this.isReadable = true;
		this.isWritable = false;
		this.findMode = this.ANCHOR_MODE;
	}

	public ResourceBackUp(Class<? extends Object> anchor, String fileName) {
		this.anchor = anchor;
		this.findableName = fileName.replace('\\', '/');
		this.isReadable = true;
		this.isWritable = false;
		this.findMode = this.ANCHOR_MODE;
	}

	public ResourceBackUp(String varName, String fileName) throws Exception {
		if (varName == null) {
			this.findableName = fileName.replace('\\', '/');
		} else {
			EnvVarsExtractor extractor = new EnvVarsExtractor();
			this.findableName = (extractor.var(varName) + "/" + fileName)
					.replace('\\', '/');
		}
		this.resourceFile = new File(this.findableName);
		if (this.resourceFile.isDirectory()) {
			this.isReadable = false;
			this.isWritable = false;
		} else {
			this.isReadable = true;
			this.isWritable = true;
		}
		this.findMode = this.DIRECTORY_MODE;
	}

	public String getFindableName() {
		return this.findableName;
	}

	public long getLastUpdate() {
		if (this.resourceFile != null) {
			return this.resourceFile.lastModified();
		} else {
			return 0;
		}
	}

	public InputStream getReadStream() throws Exception {
		if (!this.isReadable) {
			throw (new Exception(
					"Attempted to retrieve a readable object from a non-readable "
							+ "resource."));
		}
		InputStream result = null;
		if (this.findMode.equals(this.ANCHOR_MODE)) {
			result = new BufferedInputStream(
					this.anchor.getResourceAsStream(this.findableName));
		} else {
			result = new BufferedInputStream(new FileInputStream(
					this.findableName));
		}
		return result;
	}

	public File getResourceFile() throws Exception {
		if (this.findMode.equals(this.ANCHOR_MODE)) {
			throw (new Exception(
					"Attempted to retrieve a writable object form a non-writable "
							+ "resource."));
		}
		return this.resourceFile;
	}

	public OutputStream getWriteStream() throws Exception {
		if (!this.isWritable) {
			throw (new Exception(
					"Attempted to retrieve a writable object form a non-writable "
							+ "resource."));
		}
		return new FileOutputStream(this.findableName);
	}
}
