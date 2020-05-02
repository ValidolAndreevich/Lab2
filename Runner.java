import java.io.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;

public class Runner {

    public static void main(String[] args) throws IOException {
        RandomAccessFile aFile = new RandomAccessFile(
                "MFT", "r");
        FileChannel inChannel = aFile.getChannel();
        long fileSize = inChannel.size();
        ByteBuffer buffer = ByteBuffer.allocate((int) fileSize);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        inChannel.read(buffer);
        System.out.println("Offset to fixup array: " + buffer.getShort(4));
        System.out.println("Number of entries in fixup array: " + buffer.getShort(6));
        System.out.println("$LogFile sequence number: " + buffer.getLong(8));
        System.out.println("Sequence value: " + buffer.getShort(16));
        System.out.println("Link count: " + buffer.getShort(18));
        System.out.println("Offset to first attribute: " + buffer.getShort(20));
        System.out.println("Flags: " + buffer.getShort(22));
        System.out.println("Used size of MFT entry: " + buffer.getInt(24));
        System.out.println("Allocated size of MFT entry: " + buffer.getInt(28));
        System.out.println("File reference to base record: " + buffer.getLong(32));
        System.out.println("Next attribute identifier: " + buffer.getShort(40));

        int offset = buffer.getShort(20);
        int start = offset;
        System.out.println(offset);
        while (buffer.getInt(start) != -1) {
            offset = start;
            System.out.println("___________________________________");
            System.out.println("Attribute Type Identifier: " + buffer.getInt(offset));
            offset += 4;
            System.out.println("Length of Attribute: " + buffer.getInt(offset));
            int attrLen = buffer.getInt(offset);
            offset += 4;
            System.out.println("Non-resident flag: " + buffer.get(offset));
            boolean resident = buffer.getShort(offset) != 0;
            offset += 1;
            System.out.println("Lenght of name: " + buffer.get(offset));
            offset += 1;
            System.out.println("Offset to name: " + buffer.getShort(offset));
            offset += 2;
            System.out.println("Flags: " + buffer.getShort(offset));
            offset += 2;
            System.out.println("Attribute Identifier: " + buffer.getShort(offset));
            offset += 2;
            if (resident) {
                System.out.println("Size of content.: " + buffer.getInt(offset));
                offset += 4;
                System.out.println("Offset to content: " + buffer.getShort(offset));
                offset += 2;
            } else {
                System.out.println("Starting Virtual Cluster Number of the runlist: " + buffer.getLong(offset));
                offset += 8;
                System.out.println("Ending Virtual Cluster Number of the runlist: " + buffer.getLong(offset));
                offset += 8;
                System.out.println("Offset to the runlist: " + buffer.getShort(offset));
                offset += 2;
                System.out.println("Compression unit size: " + buffer.getShort(offset));
                offset += 2;
                System.out.println("unused: " + buffer.getInt(offset));
                offset += 4;
                System.out.println("Allocated size of the attribute content: " + buffer.getLong(offset));
                offset += 8;
                System.out.println("Actual size of attribute content: " + buffer.getLong(offset));
                offset += 8;
                System.out.println("Initialized size of the attribute content.: " + buffer.getLong(offset));
                offset += 8;
            }
            start += attrLen;
        }
    }

}
