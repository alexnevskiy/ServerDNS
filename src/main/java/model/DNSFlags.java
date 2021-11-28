package model;

public class DNSFlags {
    private boolean qr = false;     // 1 bit | 0
    private byte opCode = 0;        // 4 bit | 1-4
    private boolean aa = false;     // 1 bit | 5
    private boolean tc = false;     // 1 bit | 6
    private boolean rd = false;     // 1 bit | 7
    private boolean ra = false;     // 1 bit | 8
    private byte z = 0;       // 3 bit | 9-11
    private byte rCode = 0;         // 4 bit | 12-15
    private int flags = 0;

    DNSFlags() {}

    DNSFlags(short flagsBytes) {
        qr = pollBit(flagsBytes,0);
        opCode = pollByte(flagsBytes, 1, 4);
        aa = pollBit(flagsBytes, 5);
        tc = pollBit(flagsBytes, 6);
        rd = pollBit(flagsBytes, 7);
        ra = pollBit(flagsBytes, 8);
        z = pollByte(flagsBytes, 9, 11);
        rCode = pollByte(flagsBytes, 12, 15);
    }

    public boolean isQr() {
        return qr;
    }

    public void setQr(boolean qr) {
        this.qr = qr;
    }

    public byte getOpCode() {
        return opCode;
    }

    public void setOpCode(byte opCode) {
        this.opCode = opCode;
    }

    public boolean isAa() {
        return aa;
    }

    public void setAa(boolean aa) {
        this.aa = aa;
    }

    public boolean isTc() {
        return tc;
    }

    public void setTc(boolean tc) {
        this.tc = tc;
    }

    public boolean isRd() {
        return rd;
    }

    public void setRd(boolean rd) {
        this.rd = rd;
    }

    public boolean isRa() {
        return ra;
    }

    public void setRa(boolean ra) {
        this.ra = ra;
    }

    public byte getZ() {
        return z;
    }

    public byte getRCode() {
        return rCode;
    }

    public void setRCode(byte rCode) {
        this.rCode = rCode;
    }

    public short getFlags() {
        putBit(qr);
        putByte(opCode, 4);
        putBit(aa);
        putBit(tc);
        putBit(rd);
        putBit(ra);
        putByte(z, 3);
        putByte(rCode, 4);
        return (short) flags;
    }

    private void putBit(boolean bitFlag) {
        flags = flags << 1;
        if (bitFlag) {
            flags += 1;
        }
    }

    private void putByte(byte byteFlag, int shift) {
        flags = flags << shift;
        flags += byteFlag & 0xFFFF;
    }

    private boolean pollBit(int flagsByte, int bitNumber) {
        return (flagsByte & (int) Math.pow(2, bitNumber)) == 1;
    }

    private byte pollByte(int flagsByte, int startBitNumber, int endBitNumber) {
        int mask = 0;
        for (int i = startBitNumber; i <= endBitNumber; i++) {
            mask += Math.pow(2, i);
        }
        return (byte) (flagsByte & mask);
    }
}
