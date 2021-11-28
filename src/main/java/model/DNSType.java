package model;

public enum DNSType {
    A(1),
    MX(15),
    TXT(16),
    AAAA(28);

    private final int code;

    DNSType(int code) {
        this.code = code;
    }

    public static int getCode(DNSType dnsType) {
        return dnsType.code;
    }

    public static DNSType getType(int code) {
        for (DNSType dnsType : values()) {
            if (dnsType.code == code) {
                return dnsType;
            }
        }
        throw new IllegalArgumentException("This type is not supported or does not exist.");
    }
}
