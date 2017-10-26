import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public enum Instruction {
	ADD("add"), 
	DELETE("delete"), 
	MUTATE("mutate"),
	EMPTY("empty");

	private final String opcode;

	Instruction(String opcode) {
		this.opcode = opcode;
	}

	public String getOpcode() {
		return opcode;
	}


	// ADD
	private static final List<Instruction> VALUES = Collections.unmodifiableList(Arrays.asList(values()));
	private static final int SIZE = VALUES.size();
	private static final Random RANDOM = new Random();

	public static Instruction randomInstruction() {
		return VALUES.get(RANDOM.nextInt(SIZE));
	}

}
