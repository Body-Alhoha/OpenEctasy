package fr.bodyalhoha.ectasy.utils;

import org.apache.commons.io.IOUtils;

import java.io.InputStream;

public class UnknownFile {
    public String name;
    public byte[] bytes;

    public UnknownFile(String name, byte[] bytes) {
        this.name = name;
    }

    public UnknownFile(String name, InputStream in) throws Exception {
        this.name = name;
        this.bytes = IOUtils.toByteArray(in);
    }
}