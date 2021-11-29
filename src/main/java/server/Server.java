package server;

import model.DNSMessage;
import model.DNSType;
import model.resourcerecord.*;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.RandomStringUtils;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static model.DNSType.*;

public class Server extends Thread {
    private final int bufferSize = 512;
    private final int ttl = 43200;
    private final int maxMailAddresses = 5;
    private final int maxNodesCount = 3;
    private final int maxNodeSize = 5;
    private final int maxTextLength = 100;

    private DatagramSocket socket;
    private boolean running;
    private byte[] buffer = new byte[bufferSize];
    private Random random = new Random();

    public Server() throws SocketException {
        socket = new DatagramSocket(53);    // default DNS port
    }

    public void run() {
        running = true;
        DNSMessage message;

        while (running) {
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
            try {
                socket.receive(packet);
                message = new DNSMessage(Arrays.copyOf(buffer, packet.getLength()));
                message.getHeader().getFlags().setQr(true);
                List<DNSResourceRecord> resourceRecords = getResourceRecords(message);
                if (resourceRecords.isEmpty()) {
                    message.getHeader().getFlags().setRCode((byte) 4);  // server cannot fulfill a request of this type
                } else {
                    message.getHeader().setAnCount((short) resourceRecords.size());
                    message.getResourceRecords().addAll(resourceRecords);
                }
                byte[] messageBytes = message.getMessage();
                System.out.println(message.toString());
                System.out.println("Message bytes:");
                StringBuilder stringBuilder = new StringBuilder();
                for (byte messageByte : messageBytes) {
                    stringBuilder.append(messageByte).append(" ");
                }
                System.out.println(stringBuilder.toString() + "\n");

                InetAddress address = packet.getAddress();
                int port = packet.getPort();
                packet = new DatagramPacket(messageBytes, messageBytes.length, address, port);

                if (isInterrupted()) {
                    running = false;
                    continue;
                }

                socket.send(packet);
                buffer = new byte[bufferSize];
            } catch (IOException exception) {
                System.out.println("Server is down");
                running = false;
            }
        }
        socket.close();
    }

    private List<DNSResourceRecord> getResourceRecords(DNSMessage message) {
        List<DNSResourceRecord> resourceRecordList = new ArrayList<>();
        DNSType type;
        try {
            type = DNSType.getType(message.getQuestions().get(0).getQType());
        } catch (IllegalArgumentException exception) {
            exception.getMessage();
            return new ArrayList<>();
        }

        switch (type) {
            case A:
                DNSResourceRecordA resourceRecordA = new DNSResourceRecordA();
                resourceRecordA.setRrName(String.valueOf(0xC00C));  // Link to the qname field in question
                resourceRecordA.setRrType((short) DNSType.getCode(A));
                resourceRecordA.setRrClass((short) 1);
                resourceRecordA.setRrTtl(ttl);
                resourceRecordA.setRdLength((short) 4);
                resourceRecordA.setRData(getRandomResourceDataA(4));
                resourceRecordList.add(resourceRecordA);
                break;
            case MX:
                int mailsCount = random.nextInt(maxMailAddresses) + 1;
                for (int i = 0; i < mailsCount; i++) {
                    DNSResourceRecordMX resourceRecordMX = new DNSResourceRecordMX();
                    resourceRecordMX.setRrName(String.valueOf(0xC00C));  // Link to the qname field in question
                    resourceRecordMX.setRrType((short) DNSType.getCode(MX));
                    resourceRecordMX.setRrClass((short) 1);
                    resourceRecordMX.setRrTtl(ttl);

                    int nodesCount = random.nextInt(maxNodesCount) + 2;
                    String mailAddress = getRandomResourceDataMX(nodesCount, maxNodeSize);

                    resourceRecordMX.setRdLength((short) (mailAddress.length() + 2));
                    resourceRecordMX.setPreference((short) (i + 1));
                    resourceRecordMX.setRData(mailAddress);
                    resourceRecordList.add(resourceRecordMX);
                }
                break;
            case TXT:
                DNSResourceRecordTXT resourceRecordTXT = new DNSResourceRecordTXT();
                resourceRecordTXT.setRrName(String.valueOf(0xC00C));  // Link to the qname field in question
                resourceRecordTXT.setRrType((short) DNSType.getCode(TXT));
                resourceRecordTXT.setRrClass((short) 1);
                resourceRecordTXT.setRrTtl(ttl);

                int textLength = random.nextInt(maxTextLength) + 1;
                String text = getRandomResourceDataTXT(textLength);

                resourceRecordTXT.setRdLength((short) (textLength + 1));
                resourceRecordTXT.setTxtLength((byte) textLength);
                resourceRecordTXT.setRData(text);
                resourceRecordList.add(resourceRecordTXT);
                break;
            case AAAA:
                DNSResourceRecordAAAA resourceRecordAAAA = new DNSResourceRecordAAAA();
                resourceRecordAAAA.setRrName(String.valueOf(0xC00C));  // Link to the qname field in question
                resourceRecordAAAA.setRrType((short) DNSType.getCode(AAAA));
                resourceRecordAAAA.setRrClass((short) 1);
                resourceRecordAAAA.setRrTtl(ttl);
                resourceRecordAAAA.setRdLength((short) 16);
                resourceRecordAAAA.setRData(getRandomResourceDataA(16));
                resourceRecordList.add(resourceRecordAAAA);
                break;
        }
        return resourceRecordList;
    }

    private String getRandomResourceDataA(int size) {
        byte[] bytes = new byte[size];
        random.nextBytes(bytes);
        return new String(Base64.getEncoder().encode(bytes));
    }

    private String getRandomResourceDataMX(int size, int maxNodeSize) {
        int nodeSize;
        List<Byte> list = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            nodeSize = random.nextInt(maxNodeSize) + 2;
            String temp = RandomStringUtils.randomAlphanumeric(nodeSize);
            list.add((byte) nodeSize);
            list.addAll(Arrays.asList(ArrayUtils.toObject(temp.getBytes(StandardCharsets.UTF_8))));
        }
        list.add((byte) 0);
        return new String(ArrayUtils.toPrimitive(list.toArray(new Byte[0])));
    }

    private String getRandomResourceDataTXT(int size) {
        return RandomStringUtils.randomAlphanumeric(size);
    }

    public void close() {
        socket.close();
        interrupt();
    }
}
