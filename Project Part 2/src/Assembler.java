import java.io.*;
import java.util.ArrayList;
import java.util.Objects;


public class Assembler {

    //ALL COMPONENTS TO BUILD FINAL INSTRUCTION
    int LOC;
    String OpCode;
    String R;
    String IX;
    String I;
    String Address;
    String Instruction = OpCode + R + IX + I + Address;

    //FLAG TO CHECK IF GIVEN INPUT DOES NOT FOLLOW INPUT SYNTAX AS IN CLASS PROJECT PDF
    Boolean codeValid = true;

    //LIST TO STORE INPUT ROWS FROM INPUT FILE
    ArrayList<FileData> inputRows = new ArrayList<FileData>();

    //LIST TO STORE OUTPUT ROWS AND PRINT TO OUTPUT FILE
    ArrayList<FileData> outputData = new ArrayList<FileData>();

    //INITIAL FUNCTION TO PARSE THE INPUT FILE AND STORE IN INPUT ROWS
    public void readInit() {

        // File file = new File("/Users/yashwanth/Documents/GWU/Sem 1/Computer System Architecture/Project/Part 2/Assembler/src/input_data.txt");
        File file = new File("src/input_data.txt");

        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(file));
        } catch (FileNotFoundException ex) {
            System.out.println("File not Found!! " + ex);
        }
        String row;
        try {
            short i = 0; //GENERIC FLAG VARIABLE TO STORE ADDRESS INTO THE PC FOR THE FIRST TIME ONLY
            while ((row = br.readLine()) != null)
            {
                String[] content = row.split(" ");

                if(content.length == 1){
                    inputRows.add(new FileData(content[0]));

                }
                if(content.length == 2){
                    if(Objects.equals(content[0], "LOC") && i ==0 ){
                        LOC = Integer.parseInt(content[1]);
                    }
                    else{
                        inputRows.add(new FileData(content[0], content[1]));
                    }
                }
                else {
                    System.out.println("Invalid Data. Please check your input");
                }
                i++;

            }
        } catch (IOException ex) {
            System.out.println("FILE READ ERROR!! " + ex);
        }

    }


    public static void main(String[] args) {

        Assembler assembler = new Assembler();
        System.out.println(assembler.genLeftPaddedString(Integer.toHexString(13), 4));
        assembler.readInit();
        assembler.encodeInstructions();

        //GENERATING OUTPUT FILE, ONLY GIVEN INPUT IS VALID
        if(assembler.codeValid){

            try{
                //NAME OF OUTPUT FILE IS "output.txt"
                FileWriter fileWriter = new FileWriter("output_data.txt");
                for (FileData r :
                        assembler.outputData) {
                    String line = r.getRow1() + " ";
                    System.out.print(r.getRow1() + " ");
                    if(r.getRow2() != null){
                        System.out.println(r.getRow2());
                        line += r.getRow2();
                    }
                    line += "\n";
                    fileWriter.write(line);
                }
                fileWriter.close();
            } catch (Exception e){
                System.out.println("File Writing Error "  + e.getMessage());
            }
        }
    }

    //BUILD FINAL INSTRUCTION USING R, IX, I & ADDRESS AS PER THE GIVEN OP CODE TO TRANSLATE HEXADECIMAL
    public void generateInstruction(String x, String y){

        String[] content = y.split(",");

        if ("LDX".equals(x)) {
            OpCode = genLeftPaddedString(octalToBinary(Integer.toString(41)), 6);
            if (content.length == 2 || content.length == 3) {
                IX = genLeftPaddedString(Integer.toBinaryString(Integer.parseInt(content[0])), 2);
                Address = genLeftPaddedString(Integer.toBinaryString(Integer.parseInt(content[1])), 5);
                R = "00";
                // Set 'I' based on content length
                I = (content.length == 2) ? "0" : Integer.toBinaryString(Integer.parseInt(content[2]));
            } else {
                codeValid = false;
            }
        }

        if ("STX".equals(x)) {
            OpCode = genLeftPaddedString(octalToBinary(Integer.toString(42)), 6);
            if (content.length == 2 || content.length == 3) {
                IX = genLeftPaddedString(Integer.toBinaryString(Integer.parseInt(content[0])), 2);
                Address = genLeftPaddedString(Integer.toBinaryString(Integer.parseInt(content[1])), 5);
                R = "00";
                // Set 'I' based on content length
                I = (content.length == 2) ? "0" : Integer.toBinaryString(Integer.parseInt(content[2]));
            } else {
                codeValid = false;
            }
        }


        if ("LDR".equals(x)) {
            OpCode = genLeftPaddedString(octalToBinary(Integer.toString(1)), 6);
            if (content.length >= 3 && content.length <= 4) {
                R = genLeftPaddedString(Integer.toBinaryString(Integer.parseInt(content[0])), 2);
                IX = genLeftPaddedString(Integer.toBinaryString(Integer.parseInt(content[1])), 2);
                Address = genLeftPaddedString(Integer.toBinaryString(Integer.parseInt(content[2])), 5);
                I = (content.length == 3) ? "0" : Integer.toBinaryString(Integer.parseInt(content[3]));
            } else {
                codeValid = false;
            }
        }

        if ("STR".equals(x)) {
            OpCode = genLeftPaddedString(octalToBinary(Integer.toString(2)), 6);
            if (content.length >= 3 && content.length <= 4) {
                R = genLeftPaddedString(Integer.toBinaryString(Integer.parseInt(content[0])), 2);
                IX = genLeftPaddedString(Integer.toBinaryString(Integer.parseInt(content[1])), 2);
                Address = genLeftPaddedString(Integer.toBinaryString(Integer.parseInt(content[2])), 5);
                I = (content.length == 3) ? "0" : Integer.toBinaryString(Integer.parseInt(content[3]));
            } else {
                codeValid = false;
            }
        }

        if ("LDA".equals(x)) {
            OpCode = genLeftPaddedString(octalToBinary(Integer.toString(3)), 6);
            if (content.length >= 3 && content.length <= 4) {
                R = genLeftPaddedString(Integer.toBinaryString(Integer.parseInt(content[0])), 2);
                IX = genLeftPaddedString(Integer.toBinaryString(Integer.parseInt(content[1])), 2);
                Address = genLeftPaddedString(Integer.toBinaryString(Integer.parseInt(content[2])), 5);
                I = (content.length == 3) ? "0" : Integer.toBinaryString(Integer.parseInt(content[3]));
            } else {
                codeValid = false;
            }
        }

        if ("JZ".equals(x)) {
            OpCode = genLeftPaddedString(octalToBinary(Integer.toString(10)), 6);
            if (content.length >= 3 && content.length <= 4) {
                R = genLeftPaddedString(Integer.toBinaryString(Integer.parseInt(content[0])), 2);
                IX = genLeftPaddedString(Integer.toBinaryString(Integer.parseInt(content[1])), 2);
                Address = genLeftPaddedString(Integer.toBinaryString(Integer.parseInt(content[2])), 5);
                I = (content.length == 3) ? "0" : Integer.toBinaryString(Integer.parseInt(content[3]));
            } else {
                codeValid = false;
            }
        }

        if ("JCC".equals(x)) {
            OpCode = genLeftPaddedString(octalToBinary(Integer.toString(12)), 6);
            if (content.length >= 3 && content.length <= 4) {
                R = genLeftPaddedString(Integer.toBinaryString(Integer.parseInt(content[0])), 2);
                IX = genLeftPaddedString(Integer.toBinaryString(Integer.parseInt(content[1])), 2);
                Address = genLeftPaddedString(Integer.toBinaryString(Integer.parseInt(content[2])), 5);
                I = (content.length == 3) ? "0" : Integer.toBinaryString(Integer.parseInt(content[3]));
            } else {
                codeValid = false;
            }
        }

        if ("JNE".equals(x)) {
            OpCode = genLeftPaddedString(octalToBinary(Integer.toString(11)), 6);
            if (content.length >= 3 && content.length <= 4) {
                R = genLeftPaddedString(Integer.toBinaryString(Integer.parseInt(content[0])), 2);
                IX = genLeftPaddedString(Integer.toBinaryString(Integer.parseInt(content[1])), 2);
                Address = genLeftPaddedString(Integer.toBinaryString(Integer.parseInt(content[2])), 5);
                I = (content.length == 3) ? "0" : Integer.toBinaryString(Integer.parseInt(content[3]));
            } else {
                codeValid = false;
            }
        }

        if ("JMA".equals(x)) {
            OpCode = genLeftPaddedString(octalToBinary(Integer.toString(13)), 6);
            if (content.length >= 2 && content.length <= 3) {
                IX = genLeftPaddedString(Integer.toBinaryString(Integer.parseInt(content[0])), 2);
                Address = genLeftPaddedString(Integer.toBinaryString(Integer.parseInt(content[1])), 5);
                R = "00";
                I = (content.length == 2) ? "0" : Integer.toBinaryString(Integer.parseInt(content[2]));
            } else {
                codeValid = false;
            }
        }

        if ("JSR".equals(x)) {
            OpCode = genLeftPaddedString(octalToBinary(Integer.toString(14)), 6);
            if (content.length >= 2 && content.length <= 3) {
                IX = genLeftPaddedString(Integer.toBinaryString(Integer.parseInt(content[0])), 2);
                Address = genLeftPaddedString(Integer.toBinaryString(Integer.parseInt(content[1])), 5);
                R = "00";
                I = (content.length == 2) ? "0" : Integer.toBinaryString(Integer.parseInt(content[2]));
            } else {
                codeValid = false;
            }
        }

        if ("SOB".equals(x)) {
            OpCode = genLeftPaddedString(octalToBinary(Integer.toString(16)), 6);
            if (content.length >= 3 && content.length <= 4) {
                R = genLeftPaddedString(Integer.toBinaryString(Integer.parseInt(content[0])), 2);
                IX = genLeftPaddedString(Integer.toBinaryString(Integer.parseInt(content[1])), 2);
                Address = genLeftPaddedString(Integer.toBinaryString(Integer.parseInt(content[2])), 5);
                I = (content.length == 3) ? "0" : Integer.toBinaryString(Integer.parseInt(content[3]));
            } else {
                codeValid = false;
            }
        }

        if ("JGE".equals(x)) {
            OpCode = genLeftPaddedString(octalToBinary(Integer.toString(17)), 6);
            if (content.length >= 3 && content.length <= 4) {
                R = genLeftPaddedString(Integer.toBinaryString(Integer.parseInt(content[0])), 2);
                IX = genLeftPaddedString(Integer.toBinaryString(Integer.parseInt(content[1])), 2);
                Address = genLeftPaddedString(Integer.toBinaryString(Integer.parseInt(content[2])), 5);
                I = (content.length == 3) ? "0" : Integer.toBinaryString(Integer.parseInt(content[3]));
            } else {
                codeValid = false;
            }
        }

        if("AMR".equals(x)){
            OpCode = genLeftPaddedString(octalToBinary(Integer.toString(4)), 6);
            if(content.length == 3){
                R = genLeftPaddedString(Integer.toBinaryString(Integer.parseInt(content[0])), 2);
                IX = genLeftPaddedString(Integer.toBinaryString(Integer.parseInt(content[1])), 2);
                Address = genLeftPaddedString(Integer.toBinaryString(Integer.parseInt(content[2])), 5);
                I = "0";
            } else if (content.length == 4){
                R = genLeftPaddedString(Integer.toBinaryString(Integer.parseInt(content[0])), 2);
                IX = genLeftPaddedString(Integer.toBinaryString(Integer.parseInt(content[1])), 2);
                Address = genLeftPaddedString(Integer.toBinaryString(Integer.parseInt(content[2])), 5);
                I = Integer.toBinaryString(Integer.parseInt(content[3]));
            } else {
                codeValid = false;
            }
        }

        if("AMR".equals(x)){
            OpCode = genLeftPaddedString(octalToBinary(Integer.toString(5)), 6);
            if(content.length == 3){
                R = genLeftPaddedString(Integer.toBinaryString(Integer.parseInt(content[0])), 2);
                IX = genLeftPaddedString(Integer.toBinaryString(Integer.parseInt(content[1])), 2);
                Address = genLeftPaddedString(Integer.toBinaryString(Integer.parseInt(content[2])), 5);
                I = "0";
            } else if (content.length == 4){
                R = genLeftPaddedString(Integer.toBinaryString(Integer.parseInt(content[0])), 2);
                IX = genLeftPaddedString(Integer.toBinaryString(Integer.parseInt(content[1])), 2);
                Address = genLeftPaddedString(Integer.toBinaryString(Integer.parseInt(content[2])), 5);
                I = Integer.toBinaryString(Integer.parseInt(content[3]));
            } else {
                codeValid = false;
            }
        }

        if ("MLT".equals(x)) {
            OpCode = genLeftPaddedString(octalToBinary(Integer.toString(20)), 6);
            R = content.length == 2 ? genLeftPaddedString(Integer.toBinaryString(Integer.parseInt(content[0])), 2) : "00"; // rx
            IX = content.length == 2 ? genLeftPaddedString(Integer.toBinaryString(Integer.parseInt(content[1])), 2) : "00"; // ry
            Address = "00000";
            I = "0";
            codeValid = content.length == 2;
        }

        if ("DVD".equals(x)) {
            OpCode = genLeftPaddedString(octalToBinary(Integer.toString(21)), 6);
            R = content.length == 2 ? genLeftPaddedString(Integer.toBinaryString(Integer.parseInt(content[0])), 2) : "00"; // rx
            IX = content.length == 2 ? genLeftPaddedString(Integer.toBinaryString(Integer.parseInt(content[1])), 2) : "00"; // ry
            Address = "00000";
            I = "0";
            codeValid = content.length == 2;
        }

        if ("TRR".equals(x)) {
            OpCode = genLeftPaddedString(octalToBinary(Integer.toString(22)), 6);
            R = content.length == 2 ? genLeftPaddedString(Integer.toBinaryString(Integer.parseInt(content[0])), 2) : "00"; // rx
            IX = content.length == 2 ? genLeftPaddedString(Integer.toBinaryString(Integer.parseInt(content[1])), 2) : "00"; // ry
            Address = "00000";
            I = "0";
            codeValid = content.length == 2;
        }

        if ("AND".equals(x)) {
            OpCode = genLeftPaddedString(octalToBinary(Integer.toString(23)), 6);
            R = content.length == 2 ? genLeftPaddedString(Integer.toBinaryString(Integer.parseInt(content[0])), 2) : "00"; // rx
            IX = content.length == 2 ? genLeftPaddedString(Integer.toBinaryString(Integer.parseInt(content[1])), 2) : "00"; // ry
            Address = "00000";
            I = "0";
            codeValid = content.length == 2;
        }

        if ("ORR".equals(x)) {
            OpCode = genLeftPaddedString(octalToBinary(Integer.toString(24)), 6);
            R = content.length == 2 ? genLeftPaddedString(Integer.toBinaryString(Integer.parseInt(content[0])), 2) : "00"; // rx
            IX = content.length == 2 ? genLeftPaddedString(Integer.toBinaryString(Integer.parseInt(content[1])), 2) : "00"; // ry
            Address = "00000";
            I = "0";
            codeValid = content.length == 2;
        }


        if ("NOT".equals(x)) {
            OpCode = genLeftPaddedString(octalToBinary(Integer.toString(25)), 6);
            R = content.length == 1 ? genLeftPaddedString(Integer.toBinaryString(Integer.parseInt(content[0])), 2) : "00"; // rx
            IX = "00"; // ry
            Address = "00000";
            I = "0";
            codeValid = content.length == 1;
        }

        if ("SRC".equals(x)) {
            OpCode = genLeftPaddedString(octalToBinary(Integer.toString(31)), 6);
            codeValid = content.length == 4;
            if (codeValid) {
                R = genLeftPaddedString(Integer.toBinaryString(Integer.parseInt(content[0])), 2);
                // Combining the a/l and a/r into IX, assuming they are single digit binary.
                IX = Integer.toBinaryString(Integer.parseInt(content[3])) + Integer.toBinaryString(Integer.parseInt(content[2]));
                I = "0";
                // Address needs only 4 bits, hence the substring to ensure it does not exceed the length.
                Address = "0" + genLeftPaddedString(Integer.toBinaryString(Integer.parseInt(content[1])), 4);
            }
        }

        if ("RRC".equals(x)) {
            OpCode = genLeftPaddedString(octalToBinary(Integer.toString(32)), 6);
            codeValid = content.length == 4;
            if (codeValid) {
                R = genLeftPaddedString(Integer.toBinaryString(Integer.parseInt(content[0])), 2);
                // Combining the a/l and a/r into IX, assuming they are single digit binary.
                IX = Integer.toBinaryString(Integer.parseInt(content[3])) + Integer.toBinaryString(Integer.parseInt(content[2]));
                I = "0";
                // Address needs only 4 bits, hence the substring to ensure it does not exceed the length.
                Address = "0" + genLeftPaddedString(Integer.toBinaryString(Integer.parseInt(content[1])), 4);
            }
        }

        if ("IN".equals(x)) {
            OpCode = genLeftPaddedString(octalToBinary(Integer.toString(61)), 6);
            codeValid = content.length == 2;
            if (codeValid) {
                R = genLeftPaddedString(Integer.toBinaryString(Integer.parseInt(content[0])), 2);
                IX = "00";
                I = "0";
                Address = genLeftPaddedString(Integer.toBinaryString(Integer.parseInt(content[1])), 5); // Device ID assumed to be 5 bits
            }
        }

        if ("OUT".equals(x)) {
            OpCode = genLeftPaddedString(octalToBinary(Integer.toString(62)), 6);
            codeValid = content.length == 2;
            if (codeValid) {
                R = genLeftPaddedString(Integer.toBinaryString(Integer.parseInt(content[0])), 2);
                IX = "00";
                I = "0";
                Address = genLeftPaddedString(Integer.toBinaryString(Integer.parseInt(content[1])), 5); // Device ID assumed to be 5 bits
            }
        }

        if(codeValid){
            Instruction = OpCode + R + IX + I + Address;
        }

    }


    //ENCODE INSTRUCTION BY TRAVERSING ROW BY ROW THROUGH THE INPUT
    public void encodeInstructions() {

        for (FileData r : inputRows) {
            System.out.println(r.getRow1() + (r.getRow2() != null ? " " + r.getRow2() : ""));

            if (r.getRow2() != null) {
                boolean shouldBreak = false;
                // System.out.print("\n");
                System.out.println();
                if ("LOC".equals(r.getRow1())) {
                    LOC = Integer.parseInt(r.getRow2());
                    if (LOC == 1024) {
                        outputData.add(new FileData("0400", "0"));
                        shouldBreak = true;
                    }
                } else if (r.getRow1().contains("End")) {
                    outputData.add(new FileData("0400", "0"));
                } else if ("Data".equals(r.getRow1())) {
                    outputData.add(new FileData(
                            genLeftPaddedString(Integer.toHexString(LOC), 4),
                            genLeftPaddedString(Integer.toHexString(Integer.parseInt(r.getRow2())), 4)
                    ));
                } else {
                    codeValid = true;
                    generateInstruction(r.getRow1(), r.getRow2());
                    if (codeValid) {
                        outputData.add(new FileData(
                                genLeftPaddedString(Integer.toHexString(LOC), 4),
                                genLeftPaddedString(binaryToHex(Instruction), 4)
                        ));
                    } else {
                        System.out.println("Invalid Input. It must match with syntax of the Class Project PDF");
                        shouldBreak = true;
                    }
                }

                LOC++;

                if (shouldBreak) {
                    break;
                }
            }
        }
    }


    //UTILITY FUNCTION TO CONVERT OCTAL TO BINARY
    public String octalToBinary(String i) {
        return Integer.toBinaryString(Integer.parseInt(i, 8));
    }

    //UTILITY FUNCTION TO CONVERT BINARY TO HEXADECIMAL
    public String binaryToHex(String bin) {
        return Integer.toHexString(Integer.parseInt(bin, 2));
    }


    //UTILITY FUNCTION TO LEFT PADDED STRING OF ZEROES OF SPECIFIED LENGTH
    public String genLeftPaddedString(String str, int length) {
        String format = "%"+length+"s";
        return String.format(format, str).replace(" ", "0");
    }

}
