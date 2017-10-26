import java.nio.charset.Charset;
// import java.nio.file.Path;
import java.nio.file.Files;
import java.nio.file.FileSystems;
import java.util.List;

// import sun.applet.AppletIllegalArgumentException;

public class SimpleDriver {
    
    public static void main(String[] args) throws Exception
    {
        if (args.length != 5){
            System.err.println("Usage: java SimpleDriver <inputfile> <bug0> <bug1> <bug2> <bug3>");
            System.exit(1);
        }

        boolean b0 = false;
        boolean b1 = false;
        boolean b2 = false;
        boolean b3 = false;

        try {
            b0 = Boolean.parseBoolean(args[1]);
            b1 = Boolean.parseBoolean(args[2]);
            b2 = Boolean.parseBoolean(args[3]);
            b3 = Boolean.parseBoolean(args[4]);
        }catch (Exception e){
            System.err.println("Invalid boolean arguments provided! (stacktrace follows)");
            e.printStackTrace(System.err);
            System.exit(1);
        }
        
        Charset charset = Charset.forName("UTF-8");
        List<String> lines = null;

        try {
            lines = Files.readAllLines(FileSystems.getDefault().getPath(args[0]), charset);
        }catch (Exception e){
            System.err.println("Invalid input file! (stacktrace follows)");
            e.printStackTrace(System.err);
            System.exit(1);
        }
        HillCipher hillCipher = null;
            // System.out.println("There are this many instructions: " + lines.size());

            hillCipher = new HillCipher(args[0],b0,b1,b2,b3);            
            hillCipher.execute(lines);
            // System.out.println("Program result: " + res + "\n");
			System.out.println("No bug triggered!");


    }
}
