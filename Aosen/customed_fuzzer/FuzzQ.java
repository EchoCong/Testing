import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class FuzzQ {
    private static final String VALID_INPUT = "Asdfkjij[ndfjskf sdlfjsef sdfsleosf     sdlfslfkjsldfsdfadf sdfeslaqpk";
    public static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890+-=~!@#$%^&*()abcdefghijklmnopqrstuvwxyz{}|';?/.,><\"' ";
    private static final int LEN = VALID_INPUT.length();
    private static final int NUMOFTESTS = 100;   // generate x testcase
    private static final int PARTS = LEN / 10;
    public static final int WORDS_EACHPART = LEN / PARTS;
    public static Random random = new Random();

    public static void main(String[] args) throws IOException {
        FuzzQ fuzzq = new FuzzQ();
        File file = fuzzq.createFile();
        for (int i = 0; i < NUMOFTESTS; i++) {
            String mutated_msg = fuzzq.control_mutate();
            fuzzq.writeFuzz(file, mutated_msg);
        }
    }

    private File createFile() throws IOException {
        File file = new File("fuzzQ.s");
        if (!file.exists()) file.createNewFile();
        return file;
    }

    private String control_mutate() throws IOException {
        StringBuilder sb = new StringBuilder(VALID_INPUT);
        int start = random.nextInt(PARTS) * WORDS_EACHPART;
        System.out.println(start);
        for (int i = start; i < start + WORDS_EACHPART; i++) {
            Operation op = randomOperation();
            op.mutate(sb, start);
        }
        String mutated_msg = sb.toString();
        return mutated_msg;
    }

    private void writeFuzz(File file, String message) throws IOException {
        FileWriter fileWriter = new FileWriter(file, true);
        fileWriter.write(message + "\n");
        fileWriter.close();
    }

    private Operation randomOperation() {
        int pick = random.nextInt(Operation.values().length);
        return Operation.values()[pick];
    }
}

