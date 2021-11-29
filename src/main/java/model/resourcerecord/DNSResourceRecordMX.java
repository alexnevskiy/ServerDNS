package model.resourcerecord;

import org.apache.commons.lang3.ArrayUtils;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.List;

import static model.Util.*;

public class DNSResourceRecordMX extends DNSResourceRecord {
    private short preference = 0;

    public DNSResourceRecordMX() {
        super();
    }

    public short getPreference() {
        return preference;
    }

    public void setPreference(short preference) {
        this.preference = preference;
    }

    @Override
    public void setRData(String rData) {
        super.setRData(new String(ByteBuffer.allocate(Short.BYTES).putShort(preference).array()) + rData);
    }

    public DNSResourceRecordMX(byte[] bytes) {
        super(bytes);
        int rDataStart = getRrName().length() + 10;
        preference = convertToShort(Arrays.copyOfRange(bytes, rDataStart, rDataStart + 2));
        super.setRData(new String(Arrays.copyOfRange(bytes, rDataStart, rDataStart + getRdLength())));
    }

    @Override
    public List<Byte> getResourceRecord() {
        List<Byte> resourceRecordAList = super.getResourceRecord();
        resourceRecordAList.addAll(Arrays.asList(ArrayUtils.toObject(getRData().getBytes())));
        return resourceRecordAList;
    }
}
