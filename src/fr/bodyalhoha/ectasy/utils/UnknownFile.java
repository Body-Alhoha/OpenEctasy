package fr.bodyalhoha.ectasy.utils;

import org.apache.commons.io.IOUtils;

import java.io.InputStream;


public class UnknownFile {
	// The name of the file
	public String name;
	// The contents of the file as a byte array
	public byte[] bytes;

	public UnknownFile(String name, byte[] bytes) {
		this.name = name;
	}

	/**
	 * Creates an instance of the UnknownFile class from an input stream
	 * @param name the name of the file
	 * @param in the input stream to read the file from
	 * @throws Exception if there is an error reading from the input stream
	 */

	public UnknownFile(String name, InputStream in ) throws Exception {
		this.name = name;
		// Wrap the input stream in a BufferedInputStream
		BufferedInputStream bufIn = new BufferedInputStream( in );
		// Read the input stream and convert it to a byte array
		this.bytes = IOUtils.toByteArray(bufIn);
	}
}
