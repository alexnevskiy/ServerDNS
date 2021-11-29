package model;

import model.resourcerecord.DNSResourceRecord;
import org.apache.commons.lang3.ArrayUtils;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class DNSMessage {
    private DNSHeader header = new DNSHeader();
    private List<DNSQuestion> questions = new ArrayList<>();
    private List<DNSResourceRecord> resourceRecords = new ArrayList<>();

    public DNSMessage() {}

    public DNSMessage(byte[] bytes) {
        header = new DNSHeader(Arrays.copyOfRange(bytes, 0, 12));
        DNSQuestion question = new DNSQuestion(Arrays.copyOfRange(bytes, 12, bytes.length));
        questions = Collections.singletonList(question);    // In practice, only 1 question per message is used
        resourceRecords = new ArrayList<>();
    }

    public DNSHeader getHeader() {
        return header;
    }

    public void setHeader(DNSHeader header) {
        this.header = header;
    }

    public List<DNSQuestion> getQuestions() {
        return questions;
    }

    public void setQuestions(List<DNSQuestion> questions) {
        this.questions = questions;
    }

    public List<DNSResourceRecord> getResourceRecords() {
        return resourceRecords;
    }

    public void setResourceRecords(List<DNSResourceRecord> resourceRecords) {
        this.resourceRecords = resourceRecords;
    }

    public byte[] getMessage() {
        List<Byte> messageList = new ArrayList<>(header.getHeader());
        for (DNSQuestion question : questions) {
            messageList.addAll(question.getQuestion());
        }
        for (DNSResourceRecord resourceRecord : resourceRecords) {
            messageList.addAll(resourceRecord.getResourceRecord());
        }
        Byte[] bytes = messageList.toArray(new Byte[0]);
        return ArrayUtils.toPrimitive(bytes);
    }

    @Override
    public String toString() {
        StringBuilder messageString = new StringBuilder(
                "Header:\n" +
                "\tID: " + header.getId() + "\n" +
                "\tFlags:\n" +
                "\t\tQR: " + (header.getFlags().isQr() ? 1 : 0) + "\n" +
                "\t\tOpCode: " + header.getFlags().getOpCode() + "\n" +
                "\t\tAA: " + (header.getFlags().isAa() ? 1 : 0) + "\n" +
                "\t\tTC: " + (header.getFlags().isTc() ? 1 : 0) + "\n" +
                "\t\tRD: " + (header.getFlags().isRd() ? 1 : 0) + "\n" +
                "\t\tRA: " + (header.getFlags().isRa() ? 1 : 0) + "\n" +
                "\t\tZ: " + header.getFlags().getZ() + "\n" +
                "\t\tRCODE: " + header.getFlags().getRCode() + "\n" +
                "\tQDCOUNT: " + header.getQdCount() + "\n" +
                "\tANCOUNT: " + header.getAnCount() + "\n" +
                "\tNSCOUNT: " + header.getNsCount() + "\n" +
                "\tARCOUNT: " + header.getArCount() + "\n"
        );
        for (DNSQuestion question : questions) {
            messageString.append("Question:\n");
            messageString.append("\tQNAME: ").append(
                    Arrays.toString(question.getQName().getBytes(StandardCharsets.UTF_8))
            ).append("\n");
            messageString.append("\tQTYPE: ").append(question.getQType()).append("\n");
            messageString.append("\tQCLASS: ").append(question.getQClass()).append("\n");
        }
        for (DNSResourceRecord resourceRecord : resourceRecords) {
            messageString.append("Answer:\n");
            messageString.append("\tNAME: ").append(resourceRecord.getRrName()).append("\n");
            messageString.append("\tTYPE: ").append(resourceRecord.getRrType()).append("\n");
            messageString.append("\tCLASS: ").append(resourceRecord.getRrClass()).append("\n");
            messageString.append("\tTTL: ").append(resourceRecord.getRrTtl()).append("\n");
            messageString.append("\tRDLENGTH: ").append(resourceRecord.getRdLength()).append("\n");
            messageString.append("\tRDATA: ").append(resourceRecord.getRData()).append("\n");
        }
        return messageString.toString();
    }
}
