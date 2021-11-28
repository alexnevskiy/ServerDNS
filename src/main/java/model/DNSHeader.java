package model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static model.Util.convertToBytes;
import static model.Util.convertToShort;

public class DNSHeader {
    private short id = 0;
    private DNSFlags flags = new DNSFlags();
    private short qdCount = 0;
    private short anCount = 0;
    private short nsCount = 0;
    private short arCount = 0;

    public DNSHeader() {}

    public DNSHeader(byte[] bytes) {
        if (bytes.length < 12) {
            throw new IllegalArgumentException("The number of bytes does not match 12.");
        }

        id = convertToShort(Arrays.copyOfRange(bytes, 0, 2));
        flags = new DNSFlags(convertToShort(Arrays.copyOfRange(bytes, 2, 4)));
        qdCount = convertToShort(Arrays.copyOfRange(bytes, 4, 6));
        anCount = convertToShort(Arrays.copyOfRange(bytes, 6, 8));
        nsCount = convertToShort(Arrays.copyOfRange(bytes, 8, 10));
        arCount = convertToShort(Arrays.copyOfRange(bytes, 10, 12));
    }

    public short getId() {
        return id;
    }

    public void setId(short id) {
        this.id = id;
    }

    public DNSFlags getFlags() {
        return flags;
    }

    public void setFlags(DNSFlags flags) {
        this.flags = flags;
    }

    public short getQdCount() {
        return qdCount;
    }

    public void setQdCount(short qdCount) {
        this.qdCount = qdCount;
    }

    public short getAnCount() {
        return anCount;
    }

    public void setAnCount(short anCount) {
        this.anCount = anCount;
    }

    public short getNsCount() {
        return nsCount;
    }

    public void setNsCount(short nsCount) {
        this.nsCount = nsCount;
    }

    public short getArCount() {
        return arCount;
    }

    public void setArCount(short arCount) {
        this.arCount = arCount;
    }

    public List<Byte> getHeader() {
        List<Byte> headerList = new ArrayList<>();
        headerList.addAll(convertToBytes(id));
        headerList.addAll(convertToBytes(flags.getFlags()));
        headerList.addAll(convertToBytes(qdCount));
        headerList.addAll(convertToBytes(anCount));
        headerList.addAll(convertToBytes(nsCount));
        headerList.addAll(convertToBytes(arCount));
        return headerList;
    }
}
