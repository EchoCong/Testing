import java.io.*;
import java.util.*;
import java.lang.*;

public class Fuzzer {

    // Modify a map by change one item's value
    public static void modify(Map map, int index, String target){
        map.put(index, target);
    }

    public static void delete(Map map, int index){
        map.put(index,"");

    }

    // Randomly generate a string in a range of 0-3 characters
    public static String generateRandomString(){
        Random rng = new Random();
        int length = (int)(Math.random()*5);
        //System.out.println("replaceS length: "+length);
        char[] text = new char[length];
        String beitai = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890+-=~!@#$%^&*()abcdefghijklmnopqrstuvwxyz{}|';?/.,><\"' ";
        for (int i = 0; i < length; i++)
        {
            text[i] = characters.charAt(rng.nextInt(characters.length()));
        }
        //System.out.println("randomStr: "+ new String(text));
        return new String(text);

    }

    //  Join up all the map's value to a string
    public static String mapToString(Map map){
        String newStr = "";
        Iterator it = map.entrySet().iterator();
        while(it.hasNext()){
            Map.Entry pair = (Map.Entry)it.next();
            //System.out.println(pair.getKey() + " = " + pair.getValue());
            newStr += pair.getValue();
            it.remove();
        }
        return newStr;
    }


    // Convert a string to a map
    public static Map stringToMap(String string){
        Map<Integer, String> map = new HashMap<Integer, String>();
        String[] characters = string.split("");
        //System.out.println(characters.length);
        for(int i = 0; i<characters.length; i++) {
            map.put(i, characters[i]);
        }
        return map;
    }


    // Treat the whole file as a part and apply mutation
    public static String generateLine(String string, double rate){
        // Have 3% probability to generate an empty string
        if(Math.random()<0.03){
            return "\n";
        }else{
            //Generate a set contains the indice for modification
            Set<Integer> set = new HashSet<Integer>();
            int length = string.length();
            int range = (int)(length * rate);
            while(set.size()<range){
                int index = (int)(Math.random() * length);
                set.add(index);
            }
            Map<Integer, String> map = stringToMap(string);
            for(int i: set){
                modify(map, i, generateRandomString());
            }
            String target = mapToString(map);
            return target+"\n";
        }

    }

    public static void generateFuzzTypeOne(String string, int lines, String filename){
        //File fileRead = new File(fileName);
        File fileWritten = new File(filename);

        BufferedReader reader = null;
        //ufferedWriter writer = null;
        try {
            //reader = new BufferedReader(new FileReader(fileRead));
            BufferedWriter writer = new BufferedWriter(new FileWriter(fileWritten.getAbsoluteFile()));
            String tempString = null;
            int line = 1;
            tempString = string;
            //tempString = reader.readLine();
            for(int i = 0; i < lines; i++){
                writer.write(generateLine(tempString, 0.2));
            }
            //reader.close();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
    }

    public static void generateFuzzTypeTwo(){
        //File fileRead = new File(fileName);

        try{
            for(int i =0; i <100; i++){
                File fileWritten = new File("inkey"+i+".txt");
                BufferedWriter writer = new BufferedWriter(new FileWriter(fileWritten.getAbsoluteFile()));
                writer.write(""+(int)(Math.random()*500)+"\n");
                for(int j = 0; j<3; j++){
                    writer.write(""+(int)(Math.random()*500)+" "+(int)(Math.random()*500)+" "+(int)(Math.random()*500)+"\n");
                }

                writer.close();
            }
        }catch(Exception e){

        }

    }

    public static void main(String[] args){

        boolean aosen = false;
        boolean lin = false;
        boolean lisha = false;

        try {
            aosen = Boolean.parseBoolean(args[0]);
            lin = Boolean.parseBoolean(args[1]);
            lisha = Boolean.parseBoolean(args[2]);
        }catch (Exception e){
            System.err.println("Invalid boolean arguments provided! (stacktrace follows)");
            e.printStackTrace(System.err);
            System.exit(1);
        }

        if(aosen){
            for(int i = 0; i < 4; i++){
                generateFuzzTypeOne("6;true;WelcomeToBeijing2008", 100, "fuzz_Shikai_Aosen_"+i+".s");
            }

        }

        if(lin){
            for(int i = 0; i < 100; i++){
                generateFuzzTypeOne("ABCDEFGHIJKLMNOPQRSTUVWXYZ 0123456789~!@#$%^&*()_+a", 1,"infile"+i+".txt");
            }
            generateFuzzTypeTwo();


        }

        if(lisha){
            for(int i = 0; i < 4; i++) {
                generateFuzzTypeOne("Asdfkjij[ndfjskf sdlfjsef sdfsleosf     sdlfslfkjsldfsdfadf sdfeslaqpk", 100, "fuzz_Shikai_Lisha"+i+".s");
            }
        }


    }

}
