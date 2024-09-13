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
    Boolean isValid = true;

    //LIST TO STORE INPUT ROWS FROM INPUT FILE
    ArrayList<FileData> inputRows = new ArrayList<FileData>();

    //LIST TO STORE OUTPUT ROWS AND PRINT TO OUTPUT FILE
    ArrayList<FileData> outputData = new ArrayList<FileData>();

    //INITIAL FUNCTION TO PARSE THE INPUT FILE AND STORE IN INPUT ROWS
    public void readInit() {

        File file = new File("src/data.txt");
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

    public void runAssembler() {

        this.readInit();
        this.encodeInstructions();

        //GENERATING OUTPUT FILE, ONLY GIVEN INPUT IS VALID
        if(this.isValid){

            try{
                //NAME OF OUTPUT FILE IS "output.txt"
                FileWriter fileWriter = new FileWriter("src/instructions.txt");
                for (FileData r :
                        this.outputData) {
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


//    public static void main(String[] args) {
//
//        Assembler assembler = new Assembler();
//        System.out.println(assembler.genLeftPaddedString(Integer.toHexString(13), 4));
//        assembler.readInit();
//        assembler.encodeInstructions();
//
//        //GENERATING OUTPUT FILE, ONLY GIVEN INPUT IS VALID
//        if(assembler.isValid){
//
//            try{
//                //NAME OF OUTPUT FILE IS "output.txt"
//                FileWriter fileWriter = new FileWriter("src/instructions.txt");
//                for (FileData r :
//                        assembler.outputData) {
//                    String line = r.getRow1() + " ";
//                    System.out.print(r.getRow1() + " ");
//                    if(r.getRow2() != null){
//                        System.out.println(r.getRow2());
//                        line += r.getRow2();
//                    }
//                    line += "\n";
//                    fileWriter.write(line);
//                }
//                fileWriter.close();
//            } catch (Exception e){
//                System.out.println("File Writing Error "  + e.getMessage());
//            }
//        }
//    }

    //BUILD FINAL INSTRUCTION USING R, IX, I & ADDRESS AS PER THE GIVEN OP CODE TO TRANSLATE HEXADECIMAL
    public void generateInstruction(String row1, String row2){

        String[] content = row2.split(",");

        if(row1.equals("LDX")){
            OpCode = genLeftPaddedString(octalToBinary(Integer.toString(41)), 6);
            if(content.length == 2){
                IX = genLeftPaddedString(Integer.toBinaryString(Integer.parseInt(content[0])), 2);
                Address = genLeftPaddedString(Integer.toBinaryString(Integer.parseInt(content[1])), 5);
                R = "00";
                I = "0";
            } else if (content.length == 3) {
                IX = genLeftPaddedString(Integer.toBinaryString(Integer.parseInt(content[0])), 2);
                Address = genLeftPaddedString(Integer.toBinaryString(Integer.parseInt(content[1])), 5);
                R = "00";
                I = Integer.toBinaryString(Integer.parseInt(content[2]));
            }
            else {
                isValid = false;
            }

        }
        if(row1.equals("STX")){
            OpCode = genLeftPaddedString(octalToBinary(Integer.toString(42)), 6);
            if(content.length == 2){
                IX = genLeftPaddedString(Integer.toBinaryString(Integer.parseInt(content[0])), 2);
                Address = genLeftPaddedString(Integer.toBinaryString(Integer.parseInt(content[1])), 5);
                R = "00";
                I = "0";
            } else if (content.length == 3) {
                IX = genLeftPaddedString(Integer.toBinaryString(Integer.parseInt(content[0])), 2);
                Address = genLeftPaddedString(Integer.toBinaryString(Integer.parseInt(content[1])), 5);
                R = "00";
                I = Integer.toBinaryString(Integer.parseInt(content[2]));
            }
            else {
                isValid = false;
            }

        }
        if(row1.equals("LDR")){
            OpCode = genLeftPaddedString(octalToBinary(Integer.toString(1)), 6);
            if(content.length == 3){
                R = genLeftPaddedString(Integer.toBinaryString(Integer.parseInt(content[0])), 2);
                IX = genLeftPaddedString(Integer.toBinaryString(Integer.parseInt(content[1])), 2);
                Address = genLeftPaddedString(Integer.toBinaryString(Integer.parseInt(content[2])), 5);
                I = "0";
            } else if (content.length == 4) {
                R = genLeftPaddedString(Integer.toBinaryString(Integer.parseInt(content[0])), 2);
                IX = genLeftPaddedString(Integer.toBinaryString(Integer.parseInt(content[1])), 2);
                Address = genLeftPaddedString(Integer.toBinaryString(Integer.parseInt(content[2])), 5);
                I = Integer.toBinaryString(Integer.parseInt(content[3]));
            }
            else {
                isValid = false;
            }
        }
        if(row1.equals("STR")){
            OpCode = genLeftPaddedString(octalToBinary(Integer.toString(2)), 6);
            if(content.length == 3){
                R = genLeftPaddedString(Integer.toBinaryString(Integer.parseInt(content[0])), 2);
                IX = genLeftPaddedString(Integer.toBinaryString(Integer.parseInt(content[1])), 2);
                Address = genLeftPaddedString(Integer.toBinaryString(Integer.parseInt(content[2])), 5);
                I = "0";
            } else if (content.length == 4) {
                R = genLeftPaddedString(Integer.toBinaryString(Integer.parseInt(content[0])), 2);
                IX = genLeftPaddedString(Integer.toBinaryString(Integer.parseInt(content[1])), 2);
                Address = genLeftPaddedString(Integer.toBinaryString(Integer.parseInt(content[2])), 5);
                I = Integer.toBinaryString(Integer.parseInt(content[3]));
            }
            else {
                isValid = false;
            }
        }
        if(row1.equals("LDA")){
            OpCode = genLeftPaddedString(octalToBinary(Integer.toString(3)), 6);
            if(content.length == 3){
                R = genLeftPaddedString(Integer.toBinaryString(Integer.parseInt(content[0])), 2);
                IX = genLeftPaddedString(Integer.toBinaryString(Integer.parseInt(content[1])), 2);
                Address = genLeftPaddedString(Integer.toBinaryString(Integer.parseInt(content[2])), 5);
                I = "0";
            }
            else if (content.length == 4) {
                R = genLeftPaddedString(Integer.toBinaryString(Integer.parseInt(content[0])), 2);
                IX = genLeftPaddedString(Integer.toBinaryString(Integer.parseInt(content[1])), 2);
                Address = genLeftPaddedString(Integer.toBinaryString(Integer.parseInt(content[2])), 5);
                I = Integer.toBinaryString(Integer.parseInt(content[3]));
            }
            else {
                isValid = false;
            }
        }
        if(row1.equals("JZ")){
            OpCode = genLeftPaddedString(octalToBinary(Integer.toString(10)), 6);
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
            }
            else {
                isValid = false;
            }
        }
        if(row1.equals("JCC")){
            OpCode = genLeftPaddedString(octalToBinary(Integer.toString(12)), 6);
            if(content.length == 3){
                R = genLeftPaddedString(Integer.toBinaryString(Integer.parseInt(content[0])), 2); //cc bit is stored 'R'
                IX = genLeftPaddedString(Integer.toBinaryString(Integer.parseInt(content[1])), 2);
                Address = genLeftPaddedString(Integer.toBinaryString(Integer.parseInt(content[2])), 5);
                I = "0";
            } else if (content.length == 4){
                R = genLeftPaddedString(Integer.toBinaryString(Integer.parseInt(content[0])), 2);
                IX = genLeftPaddedString(Integer.toBinaryString(Integer.parseInt(content[1])), 2);
                Address = genLeftPaddedString(Integer.toBinaryString(Integer.parseInt(content[2])), 5);
                I = Integer.toBinaryString(Integer.parseInt(content[3]));
            }
            else {
                isValid = false;
            }
        }
        if(row1.equals("JNE")){
            OpCode = genLeftPaddedString(octalToBinary(Integer.toString(11)), 6);
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
            }
            else {
                isValid = false;
            }
        }
        if(row1.equals("JMA")){
            OpCode = genLeftPaddedString(octalToBinary(Integer.toString(13)), 6);
            if(content.length == 2){
                IX = genLeftPaddedString(Integer.toBinaryString(Integer.parseInt(content[0])), 2);
                Address = genLeftPaddedString(Integer.toBinaryString(Integer.parseInt(content[1])), 5);
                R = "00";
                I = "0";
            } else if (content.length == 3) {
                IX = genLeftPaddedString(Integer.toBinaryString(Integer.parseInt(content[0])), 2);
                Address = genLeftPaddedString(Integer.toBinaryString(Integer.parseInt(content[1])), 5);
                R = "00";
                I = Integer.toBinaryString(Integer.parseInt(content[2]));
            }
            else {
                isValid = false;
            }
        }
        if(row1.equals("JSR")){
            OpCode = genLeftPaddedString(octalToBinary(Integer.toString(14)), 6);
            if(content.length == 2){
                IX = genLeftPaddedString(Integer.toBinaryString(Integer.parseInt(content[0])), 2);
                Address = genLeftPaddedString(Integer.toBinaryString(Integer.parseInt(content[1])), 5);
                R = "00";
                I = "0";
            } else if (content.length == 3) {
                IX = genLeftPaddedString(Integer.toBinaryString(Integer.parseInt(content[0])), 2);
                Address = genLeftPaddedString(Integer.toBinaryString(Integer.parseInt(content[1])), 5);
                R = "00";
                I = Integer.toBinaryString(Integer.parseInt(content[2]));
            } else {
                isValid = false;
            }
        }
        if(row1.equals("SOB")){
            OpCode = genLeftPaddedString(octalToBinary(Integer.toString(16)), 6);
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
                isValid = false;
            }
        }
        if(row1.equals("JGE")){
            OpCode = genLeftPaddedString(octalToBinary(Integer.toString(17)), 6);
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
                isValid = false;
            }
        }
        if(row1.equals("RFS")){
            OpCode = genLeftPaddedString(octalToBinary(Integer.toString(15)), 6);
            if(content.length == 1){
                R = "01";
                IX = "00";
                Address = genLeftPaddedString(Integer.toBinaryString(Integer.parseInt(content[0])), 5);
                I = "0";
            } else {
                isValid = false;
            }
        }
        if(row1.equals("AMR")){
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
                isValid = false;
            }
        }
        if(row1.equals("SMR")){
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
                isValid = false;
            }
        }
        if(row1.equals("AIR")){
            OpCode = genLeftPaddedString(octalToBinary(Integer.toString(6)), 6);
            if(content.length == 2){
                R = genLeftPaddedString(Integer.toBinaryString(Integer.parseInt(content[0])), 2);
                IX = "00";
                Address = genLeftPaddedString(Integer.toBinaryString(Integer.parseInt(content[1])), 5);
                I = "0";
            }  else {
                isValid = false;
            }
        }
        if(row1.equals("SIR")){
            OpCode = genLeftPaddedString(octalToBinary(Integer.toString(7)), 6);
            if(content.length == 2){
                R = genLeftPaddedString(Integer.toBinaryString(Integer.parseInt(content[0])), 2);
                IX = "00";
                Address = genLeftPaddedString(Integer.toBinaryString(Integer.parseInt(content[1])), 5);
                I = "0";
            }  else {
                isValid = false;
            }
        }
        if(row1.equals("MLT")){
            OpCode = genLeftPaddedString(octalToBinary(Integer.toString(20)), 6);
            if(content.length == 2){
                R = genLeftPaddedString(Integer.toBinaryString(Integer.parseInt(content[0])), 2); //rx
                IX = genLeftPaddedString(Integer.toBinaryString(Integer.parseInt(content[1])), 2); //ry
                Address = "00000";
                I = "0";
            } else {
                isValid = false;
            }
        }
        if(row1.equals("DVD")){
            OpCode = genLeftPaddedString(octalToBinary(Integer.toString(21)), 6);
            if(content.length == 2){
                R = genLeftPaddedString(Integer.toBinaryString(Integer.parseInt(content[0])), 2); //rx
                IX = genLeftPaddedString(Integer.toBinaryString(Integer.parseInt(content[1])), 2); //ry
                Address = "00000";
                I = "0";
            } else {
                isValid = false;
            }
        }
        if(row1.equals("TRR")){
            OpCode = genLeftPaddedString(octalToBinary(Integer.toString(22)), 6);
            if(content.length == 2){
                R = genLeftPaddedString(Integer.toBinaryString(Integer.parseInt(content[0])), 2); //rx
                IX = genLeftPaddedString(Integer.toBinaryString(Integer.parseInt(content[1])), 2); //ry
                Address = "00000";
                I = "0";
            } else {
                isValid = false;
            }
        }
        if(row1.equals("AND")){
            OpCode = genLeftPaddedString(octalToBinary(Integer.toString(23)), 6);
            if(content.length == 2){
                R = genLeftPaddedString(Integer.toBinaryString(Integer.parseInt(content[0])), 2); //rx
                IX = genLeftPaddedString(Integer.toBinaryString(Integer.parseInt(content[1])), 2); //ry
                Address = "00000";
                I = "0";
            } else {
                isValid = false;
            }
        }
        if(row1.equals("ORR")){
            OpCode = genLeftPaddedString(octalToBinary(Integer.toString(24)), 6);
            if(content.length == 2){
                R = genLeftPaddedString(Integer.toBinaryString(Integer.parseInt(content[0])), 2); //rx
                IX = genLeftPaddedString(Integer.toBinaryString(Integer.parseInt(content[1])), 2); //ry
                Address = "00000";
                I = "0";
            } else {
                isValid = false;
            }
        }
        if(row1.equals("NOT")){
            OpCode = genLeftPaddedString(octalToBinary(Integer.toString(25)), 6);
            if(content.length == 1){
                R = genLeftPaddedString(Integer.toBinaryString(Integer.parseInt(content[0])), 2); //rx
                IX = "00"; //ry
                Address = "00000";
                I = "0";
            } else {
                isValid = false;
            }
        }
        if(row1.equals("SRC")){
            OpCode = genLeftPaddedString(octalToBinary(Integer.toString(31)), 6);
            if(content.length == 4){
                R = genLeftPaddedString(Integer.toBinaryString(Integer.parseInt(content[0])), 2); //r
                IX = Integer.toBinaryString(Integer.parseInt(content[3])) + Integer.toBinaryString(Integer.parseInt(content[2])); // a/l + a/r
                I = "0";
                Address = "0" + genLeftPaddedString(Integer.toBinaryString(Integer.parseInt(content[1])), 4); //
            } else {
                isValid = false;
            }
        }
        if(row1.equals("RRC")){
            OpCode = genLeftPaddedString(octalToBinary(Integer.toString(32)), 6);
            if(content.length == 4){
                R = genLeftPaddedString(Integer.toBinaryString(Integer.parseInt(content[0])), 2); //r
                IX = Integer.toBinaryString(Integer.parseInt(content[3])) + Integer.toBinaryString(Integer.parseInt(content[2])); // a/l + a/r
                I = "0";
                Address = "0" + genLeftPaddedString(Integer.toBinaryString(Integer.parseInt(content[1])), 4); //
            } else {
                isValid = false;
            }
        }
        if(row1.equals("IN")){
            OpCode = genLeftPaddedString(octalToBinary(Integer.toString(61)), 6);
            if(content.length == 2){
                R = genLeftPaddedString(Integer.toBinaryString(Integer.parseInt(content[0])), 2); //r
                IX = "00";
                I = "0";
                Address = genLeftPaddedString(Integer.toBinaryString(Integer.parseInt(content[1])), 5); //Devid
            } else {
                isValid = false;
            }
        }
        if(row1.equals("OUT")){
            OpCode = genLeftPaddedString(octalToBinary(Integer.toString(62)), 6);
            if(content.length == 2){
                R = genLeftPaddedString(Integer.toBinaryString(Integer.parseInt(content[0])), 2); //r
                IX = "00";
                I = "0";
                Address = genLeftPaddedString(Integer.toBinaryString(Integer.parseInt(content[1])), 5); //Devid
            } else {
                isValid = false;
            }
        }

        if(isValid){
            Instruction = OpCode + R + IX + I + Address;
        }

    }


    //ENCODE INSTRUCTION BY TRAVERSING ROW BY ROW THROUGH THE INPUT
    public void encodeInstructions() {

        for (FileData r : inputRows) {
            System.out.print(r.getRow1() + " ");
            if(r.getRow2() != null){
                System.out.println(r.getRow2());
            }
            System.out.print("\n");
            System.out.println();

            if(r.getRow2() != null ) {

                //UPDATE LOC VALUE IF PRESENT
                if(Objects.equals(r.getRow1(), "LOC")){
                    LOC = Integer.parseInt(r.getRow2());
                    //HALT IF LOC IS 1024
                    if(LOC == 1024){
                        FileData output = new FileData(
                                "0400",
                                "0"
                        );
                        outputData.add(output);
                        break;
                    }
                }//EXECUTE END INSTRUCTION
                else if(r.getRow1().contains("End")){
                    FileData output = new FileData(
                            "0400",
                            "0"
                    );
                    outputData.add(output);
                }
                //EXECUTE DATA INSTRUCTIONS
                else if(Objects.equals(r.getRow1(), "Data")){
                    FileData output = new FileData(
                            genLeftPaddedString(Integer.toHexString(LOC), 4),
                            genLeftPaddedString(Integer.toHexString(Integer.parseInt(r.getRow2())), 4)
                    );
                    outputData.add(output);
                }//EXECUTE ALL OTHERS
                else {
                    isValid = true;
                    generateInstruction(r.getRow1(), r.getRow2());
                    if(isValid){
                        FileData output = new FileData(
                                genLeftPaddedString(Integer.toHexString(LOC), 4),
                                genLeftPaddedString(binaryToHex(Instruction), 4)
                        );
                        outputData.add(output);
                    }
                    if(!isValid){
                        System.out.println("Invalid Input. It must match with syntax of the Class Project PDF");
                        break;
                    }
                }
                LOC++;
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
