package com.pdfjet.out;

import java.util.zip.*;

class Chunk {

    public long chunkLength;

    public byte[] type;

    public byte[] data;

    public long crc;

    public Chunk() {
    }

    public long getLength() {
        return this.chunkLength;
    }

    public void setLength(long chunkLength) {
        this.chunkLength = chunkLength;
    }

    public void setType(byte[] type) {
        this.type = type;
    }

    public byte[] getData() {
        return this.data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public long getCrc() {
        return this.crc;
    }

    public void setCrc(long crc) {
        this.crc = crc;
    }

    public boolean hasGoodCRC() {
        CRC32 computedCRC = new CRC32();
        computedCRC.update(type, 0, 4);
        computedCRC.update(data, 0, (int) chunkLength);
        return (computedCRC.getValue() == this.crc);
    }
}
