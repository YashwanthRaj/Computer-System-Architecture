import javax.swing.*;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
import java.util.HashMap;
import java.util.Map;

// then press Enter. You can now see whitespace characters in your code.
public class Simulator {


    //ALL REQUIRED VALUES ARE STRAIGHTFORWARD INT VARIABLES
    //
    private  UI ui;

    private int GPR0;    //Stores GPR0 value
    private int GPR1;    //Stores GPR1 value
    private int GPR2;    //Stores GPR2 value
    private int GPR3;    //Stores GPR3 value

    private int IX1;    //Stores IXR1 value
    private int IX2;    //Stores IXR2 value
    private int IX3;    //Stores IXR3 value

    private int PC;    //Stores PC value
    private int MAR;   //Stores MAR value
    private int MBR;   //Stores MBR value
    private int IR;    //Stores IR value
    //private int MFR;   //Stores MFR value

    private int CC0;    //Stores CC0-OverFlow value
    private int CC1;    //Stores CC1-UnderFlow value
    private int CC2;    //Stores CC2-DivZero value
    private int CC3;    //Stores CC3-EqualOrNot value
    private int InputVal; ////Stores Input Value
    private int InputSignal; //Stores Input Signal status
    private String printer;

    //MEMORY IS BEING EMULATED USING AN ARRAY OF LENGTH 2048
    public static int[] memory=new int[2048];
//    public static int[] fileAddress = new int[2048];
//    public static int[] fileInstruction = new int[2048];

    //INPUT INSTRUCTION IS STORED IN ARRAY OF LENGTH 16
    public int[] inputInstruction = new int[16];

    //STORES ALL VARIABLES IN A HASHMAP THAT IS ACCESSED BY THE UI CLASS THROUGH A SETTER
    HashMap uiValues = new HashMap<String, String>();

    //INIT ALL VARS TO ZERO
    public Simulator() {
        this.GPR0 = 0;
        this.GPR1 = 0;
        this.GPR2 = 0;
        this.GPR3 = 0;
        this.IX1 = 0;
        this.IX2 = 0;
        this.IX3 = 0;
        this.PC = 0;
        this.MAR = 0;
        this.MBR = 0;
        this.IR = 0;
        this.CC0 = 0;
        this.CC1 = 0;
        this.CC2 = 0;
        this.CC3 = 0;
        this.printer = "";

        //this.MFR = 0;
    }

    //UTILITY FUNCTION TO REVERSE AN ARRAY
    static int[] reverseArray(int[] a, int n) {
        int[] b = new int[n];
        int j = n;
        for (int i = 0; i < n; i++) {
            b[j - 1] = a[i];
            j = j - 1;
        }
        return b;
    }

    //RESETS ALL VARIABLES TO ZERO
    public void reset(){
        this.GPR0 = 0;
        this.GPR1 = 0;
        this.GPR2 = 0;
        this.GPR3 = 0;
        this.IX1 = 0;
        this.IX2 = 0;
        this.IX3 = 0;
        this.PC = 0;
        this.MAR = 0;
        this.MBR = 0;
        this.IR = 0;
        this.InputSignal = 0;
        this.InputVal = 0;
        //this.MFR = 0;
    }

    //INITIAL PROGRAM LOAD FUNCTION
    //RUNS ASSEMBLER WHICH READS FROM data.txt AND THE RESULTS OF WHICH ARE IN instruction.txt WHICH IS THE FINAL NEW INPUT
    public void loadIPL() {

        Assembler assembler = new Assembler();
        assembler.runAssembler();

        File file = new File("src/instructions.txt");
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(file));
        } catch (FileNotFoundException ex) {
            System.out.println("File not Found!! " + ex);
        }
        String row;
        try {
            short flag_PC = 0; //GENERIC FLAG VARIABLE TO STORE ADDRESS INTO THE PC FOR THE FIRST TIME ONLY
            while ((row = br.readLine()) != null)
            {
                String[] content = row.split(" ");
                int address = Integer.parseInt(content[0], 16);
                int value = Integer.parseInt(content[1], 16);
                memory[address] = value;
                System.out.println(value);
                if( flag_PC == 0){
                    flag_PC++;
                    PC = address;
                    genUIVals();
                }
            }
        } catch (IOException ex) {
            System.out.println("FILE READ ERROR!! " + ex);
        }

    }

    //STORE FUNCTION TO WRITE DATA TO MEMORY ARRAY
    public void store(){
        memory[MAR] = MBR;
    }

    //LOAD FUNCTION TO READ FROM MEMORY ARRAY AND UPDATES UI VALUES
    public void load(){
        MBR = memory[MAR];
        genUIVals();
    }

    //LD FOR MAR FUNCTION TO READ DATA FROM INPUT INSTRUCTION
    public String loadMAR(){
        MAR = 0;

        int [] reverseInput = reverseArray(inputInstruction, inputInstruction.length);

        for (int i = 0; i <= 11 ; i++) {
            if(reverseInput[i] == 1)
                MAR += Math.pow(2, i);
        }

        return genBinaryPaddedString(MAR, 12);
    }

    //LD FOR MBR FUNCTION TO READ DATA FROM INPUT INSTRUCTION
    public String loadMBR(){
        MBR = 0;
        System.out.println("MBR -> " + MAR);

        int [] reverseInput = reverseArray(inputInstruction, inputInstruction.length);

        for (int i = 0; i < reverseInput.length ; i++) {
            if(reverseInput[i] == 1)
                MBR += Math.pow(2, i);
        }
        return genBinaryPaddedString(MBR, 16);
    }

    //LD FOR PC TO READ DATA FROM INPUT INSTRUCTION
    public String loadPC(){
        this.PC = 0;
        int [] reverseInput = reverseArray(inputInstruction, inputInstruction.length);

        for (int i = 0; i <= 11 ; i++) {
            if(reverseInput[i] == 1)
                PC += Math.pow(2, i);
        }
        return genBinaryPaddedString(PC, 12);
    }

    //LD FOR IR TO READ DATA FROM INPUT INSTRUCTION
    public String loadIR(){
        this.IR = 0;

        int [] reverseInput = reverseArray(inputInstruction, inputInstruction.length);
        for (int i = 0; i < reverseInput.length ; i++) {
            if(reverseInput[i] == 1)
                IR += Math.pow(2, i);
        }
        return genBinaryPaddedString(IR, 16);
    }

    //LD FOR GPR0 TO READ DATA FROM INPUT INSTRUCTION
    public String loadGPR0() {
        this.GPR0 = 0;
        int [] reverseInput = reverseArray(inputInstruction, inputInstruction.length);

        for (int i = 0; i < reverseInput.length ; i++) {
            if(reverseInput[i] == 1){
                this.GPR0 += Math.pow(2, i);
            }
        }

        return genBinaryPaddedString(GPR0, 16);
    }

    //LD FOR GPR1 TO READ DATA FROM INPUT INSTRUCTION
    public  String loadGPR1() {
        this.GPR1 = 0;
        int [] reverseInput = reverseArray(inputInstruction, inputInstruction.length);

        for (int i = 0; i < reverseInput.length ; i++) {
            if(reverseInput[i] == 1)
                this.GPR1 += Math.pow(2, i);
        }
        return genBinaryPaddedString(GPR1, 16);
    }

    //LD FOR GPR2 TO READ DATA FROM INPUT INSTRUCTION
    public String loadGPR2() {
        this.GPR2 = 0;
        int [] reverseInput = reverseArray(inputInstruction, inputInstruction.length);

        for (int i = 0; i < reverseInput.length ; i++) {
            if(reverseInput[i] == 1)
                this.GPR2 += Math.pow(2, i);
        }

        return genBinaryPaddedString(GPR2, 16);
    }

    //LD FOR GPR3 TO READ DATA FROM INPUT INSTRUCTION
    public String loadGPR3() {
        this.GPR3 = 0;
        int [] reverseInput = reverseArray(inputInstruction, inputInstruction.length);

        for (int i = 0; i < reverseInput.length ; i++) {
            if(reverseInput[i] == 1)
                this.GPR3 += Math.pow(2, i);
        }
        return genBinaryPaddedString(GPR3, 16);
    }

    //LD FOR IX1 TO READ DATA FROM INPUT INSTRUCTION
    public String loadIX1() {
        this.IX1 = 0;
        int [] reverseInput = reverseArray(inputInstruction, inputInstruction.length);

        for (int i = 0; i < reverseInput.length ; i++) {
            if(reverseInput[i] == 1)
                this.IX1 += Math.pow(2, i);
        }
        return genBinaryPaddedString(IX1, 16);
    }

    //LD FOR IX2 TO READ DATA FROM INPUT INSTRUCTION
    public String loadIX2() {
        this.IX2 = 0;
        int [] reverseInput = reverseArray(inputInstruction, inputInstruction.length);

        for (int i = 0; i < reverseInput.length ; i++) {
            if(reverseInput[i] == 1)
                this.IX2 += Math.pow(2, i);
        }
        return genBinaryPaddedString(IX2, 16);
    }

    //LD FOR IX3 TO READ DATA FROM INPUT INSTRUCTION
    public String loadIX3() {
        this.IX3 = 0;
        int [] reverseInput = reverseArray(inputInstruction, inputInstruction.length);

        for (int i = 0; i < reverseInput.length ; i++) {
            if(reverseInput[i] == 1)
                this.IX3 += Math.pow(2, i);
        }
        return genBinaryPaddedString(IX3, 16);
    }

    public int[] getInputInstruction() {
        return inputInstruction;
    }

    //SETS INPUT INSTRUCTION VAL FROM UI
    public void setInputInstruction(int[] inputInstruction) {
        this.inputInstruction = inputInstruction;
    }

    //CHECKS IF GIVEN ADDRESS/INDEX IS WITHIN ARRAY LENGTH/LIMIT
    public boolean isMemoryOutOfBound(int memoryAddress){ //MFR
        if(memoryAddress > 2056) {
            PC = memory[1];
            System.out.println("Memory being tried to access beyond 2048\n");

        } else if(memoryAddress>0 && memoryAddress<=5){
            PC = memory[1];
            System.out.println("Restricted memory being tried to access\n");

        } else {
            return false;
        }
        return true;
    }

    //BINARY TO DECIMAL CONVERSION THAT'S USED IN CALCULATING EFFECTIVE ADDRESS
    public int getAddress(int x){
        String binX = Integer.toBinaryString(x);
        if (binX.length() == 32){
            binX=binX.substring(16,32);
        }
        int address = 0;

        String temp = String.valueOf(new StringBuilder(binX).reverse());

        for (int i = 0; i < 5; i++) {
            if(temp.charAt(i) == '1'){
                address += Math.pow(2, i);
            }
        }

        return address;
    }

    //GETTER FOR UI VALUES
    public HashMap getUiValues() {
        return uiValues;
    }

    //GETTER FOR IR VALUE
    public int getIR() {
        return IR;
    }

    //FETCH-DECODE-EXECUTE-STORE FUNCTION / SINGLE STEP FUNCTION
    private void executeInstruction(){

        IR = memory[PC];
        short selectGPR = 0, selectIXR = 0, doIndirectAddressing = 0;

        if(IR == 0)   //Halt
        {
            MAR = PC;

            PC++;
            return;
        }

        String binIR = Integer.toBinaryString(IR);
        System.out.println(IR + " --- " + binIR);
        System.out.println("LENGTH: " + binIR.length());
        if (binIR.length() == 32){
            binIR=binIR.substring(16,32);
        }

        if(binIR.length() <= 10)
        {
            MBR = IR;
            MAR = PC;
        }
        else
        {
            //CHOOSING WHICH GPR TO LOAD TO
            String gprBits = binIR.substring(binIR.length() - 10, binIR.length() - 8); //EXTRACT GPR BITS
            selectGPR = switch (gprBits) {
                case "00" -> 1;
                case "01" -> 2;
                case "10" -> 3;
                default -> 4;
            };

            //CHOOSING WHICH IXR TO LOAD TO
            String ixrBits = binIR.substring(binIR.length() - 8, binIR.length() - 6); //EXTRACT IX BITS
            selectIXR = switch (ixrBits) {
                case "01" -> 1;
                case "10" -> 2;
                case "11" -> 3;
                default -> 0;
            };

            //Checking "I" bit and deciding for indirect addressing
            if(binIR.charAt(binIR.length()-6)=='1')
            {
                doIndirectAddressing = 1;
            }

            //Extracting OPCODE Bits
            StringBuilder temp = new StringBuilder();
            temp.append(binIR);
            temp.reverse();
            temp = new StringBuilder(temp.substring(10)).reverse();

            String opcodeBits = String.valueOf(temp);

            System.out.println("selectGPR" + " " + selectGPR);
            System.out.println("selectIXR" + " " + selectIXR);
            System.out.println("opcodes" + " " + opcodeBits);
            System.out.println("doIndirectAddressing" + " " + doIndirectAddressing);

            //PERFORM LDR WHEN OPCODE IS 1
            if(opcodeBits.equals("1"))
            {
                System.out.println("PERFORMING LDR ");

                //CALCULATING EFFECTIVE ADDRESS
                if(doIndirectAddressing == 0)
                {
                    if(selectIXR == 0)
                    {
                        MAR = getAddress(IR);
                        if(isMemoryOutOfBound(MAR))
                            return;
                    }
                    else  //IX = 1..3
                    {
                        switch (selectIXR) {
                            case 1 -> {
                                System.out.println("IX1 I=0 ");
                                MAR = (short) (getAddress(IR) + IX1);
                                if(isMemoryOutOfBound(MAR))
                                    return;
                            }
                            case 2 -> {
                                System.out.println("IX2 I=0 ");
                                MAR = (short) (getAddress(IR) + IX2);
                                if(isMemoryOutOfBound(MAR))
                                    return;
                            }
                            case 3 -> {
                                System.out.println("IX3 I=0 ");
                                MAR = (short) (getAddress(IR) + IX3);
                                if(isMemoryOutOfBound(MAR))
                                    return;
                            }
                            default -> {
                            }
                        }
                    }

                    MBR = memory[MAR];

                    //FINALLY LOADING INTO GPR 0..3
                    switch(selectGPR)
                    {
                        case 1 -> {
                            System.out.println("GPR0\n");
                            GPR0 = memory[MAR];

                        }
                        case 2 -> {
                            System.out.println("GPR1\n");
                            GPR1 = memory[MAR];

                        }
                        case 3 -> {
                            System.out.println("GPR2\n");
                            GPR2 = memory[MAR];
                        }
                        case 4 -> {
                            System.out.println("GPR3\n");
                            GPR3 = memory[MAR];
                        }
                    }
                }
                else   //Indirect Addressing = 1
                {
                    //CALCULATING EFFECTIVE ADDRESS
                    if(selectIXR == 0)
                    {
                        System.out.println("no Indexing I=1 ");
                        MAR = memory[getAddress(IR)];
                        if(isMemoryOutOfBound(MAR)) return;
                    }
                    else
                    {
                        switch (selectIXR) {
                            case 1 : System.out.println("IX1 I=1 ");MAR = memory[(getAddress(IR) + IX1)];if(isMemoryOutOfBound(MAR)) return;break;
                            case 2 : System.out.println("IX2 I=1 ");MAR = memory[getAddress(IR) + IX2];if(isMemoryOutOfBound(MAR)) return;break;
                            case 3 : System.out.println("IX3 I=1 ");MAR = memory[getAddress(IR) + IX3];if(isMemoryOutOfBound(MAR)) return;break;
                        }
                    }
                    MBR = memory[MAR];

                    //FINALLY LOADING INTO GPR 0..3
                    switch(selectGPR)
                    {
                        case 1 -> {
                            GPR0 = memory[MAR];
                        }
                        case 2 -> {
                            GPR1 = memory[MAR];
                        }
                        case 3 -> {
                            GPR2 = memory[MAR];
                        }
                        case 4 -> {
                            GPR3 = memory[MAR];
                        }
                    }
                }
            }

            //PERFORM STR WHEN OPCODE IS 2
            else if(opcodeBits.equals("10"))
            {
                System.out.println("STR ");
                //I = 0
                if(doIndirectAddressing == 0)
                {
                    System.out.println("I=0 ");
                    //CALCULATING EFFECTIVE ADDRESS
                    if(selectIXR == 0)
                    {
                        System.out.println("No Indexing ");

                        MAR = getAddress(IR);
                        if(isMemoryOutOfBound(MAR)) return;
                    }
                    else  //Indexed
                    {
                        //WHEN IXR 1..3
                        switch (selectIXR) {
                            case 1 -> {
                                System.out.println("IX1 ");
                                MAR = (short) (getAddress(IR) + IX1);
                                if(isMemoryOutOfBound(MAR)) return;
                            }
                            case 2 -> {
                                System.out.println("IX2 ");
                                MAR = (short) (getAddress(IR) + IX2);
                                if(isMemoryOutOfBound(MAR)) return;
                            }
                            case 3 -> {
                                System.out.println("IX3 ");
                                MAR = (short) (getAddress(IR) + IX3);
                                if(isMemoryOutOfBound(MAR)) return;
                            }
                        }
                    }

                    //STORING INTO MEMORY FROM GPR 0..3
                    switch(selectGPR)
                    {
                        case 1 -> {
                            System.out.println("GPR0\n");
                            memory[MAR] = GPR0;

                        }
                        case 2 -> {
                            System.out.println("GPR1\n");
                            memory[MAR] = GPR1;

                        }
                        case 3 -> {
                            System.out.println("GPR2\n");
                            memory[MAR] = GPR2;

                        }
                        case 4 -> {
                            System.out.println("GPR3\n");
                            memory[MAR] = GPR3;

                        }
                    }
                    MBR = memory[MAR];

                }
                else   //I = 1
                {
                    System.out.println("I=1 ");
                    //CALCULATING EFFECTIVE ADDRESS
                    if(selectIXR == 0)
                    {
                        System.out.println("no indexing ");
                        MAR = memory[getAddress(IR)];
                        if(isMemoryOutOfBound(MAR)) return;
                    }
                    else  //Indexed
                    {
                        switch (selectIXR) {
                            case 1 : MAR = memory[getAddress(IR) + IX1];System.out.println("IX1 ");if(isMemoryOutOfBound(MAR)) return;
                            case 2 : MAR = memory[getAddress(IR) + IX2];System.out.println("IX2 ");if(isMemoryOutOfBound(MAR)) return;
                            case 3 : MAR = memory[getAddress(IR) + IX3];System.out.println("IX3 ");if(isMemoryOutOfBound(MAR)) return;
                            default : {
                            }
                        }
                    }

                    //WRITING INTO MEMORY FROM GPR 0..3
                    switch(selectGPR)
                    {
                        case 1 -> {
                            System.out.println("GPR0\n");
                            memory[MAR] = GPR0;
                        }
                        case 2 -> {
                            System.out.println("GPR1\n");
                            memory[MAR] = GPR1;
                        }
                        case 3 -> {
                            System.out.println("GPR2\n");
                            memory[MAR] = GPR2;
                        }
                        case 4 -> {
                            System.out.println("GPR3\n");
                            memory[MAR] = GPR3;
                        }
                    }

                    MBR = memory[MAR];
                }
            }

            //PERFORM LDA WHEN OPCODE IS 3
            else if(opcodeBits.equals("11"))
            {
                System.out.println("LDA ");
                if(doIndirectAddressing == 0)
                {

                    System.out.println("I=0 ");
                    //CALCULATING EFFECTIVE ADDRESS
                    if(selectIXR == 0)
                    {
                        System.out.println("no indexing ");
                        MAR = getAddress(IR);
                        if(isMemoryOutOfBound(MAR)) return;
                    }
                    else
                    {
                        switch (selectIXR) {
                            case 1 -> {
                                MAR = (short) (getAddress(IR) + IX1);
                                if(isMemoryOutOfBound(MAR))
                                    return;
                                System.out.println("IX1 ");
                            }
                            case 2 -> {
                                MAR = (short) (getAddress(IR) + IX2);
                                if(isMemoryOutOfBound(MAR))
                                    return;
                                System.out.println("IX2 ");
                            }
                            case 3 -> {
                                MAR = (short) (getAddress(IR) + IX3);
                                if(isMemoryOutOfBound(MAR))
                                    return;
                                System.out.println("IX3 ");
                            }
                            default -> {
                            }
                        }
                    }

                    MBR = memory[MAR];

                    //LOAD INTO GPR 0..3
                    switch(selectGPR)
                    {
                        case 1 -> {
                            GPR0 = MAR;
                            System.out.println("GPR0\n");
                        }
                        case 2 -> {
                            GPR1 = MAR;
                            System.out.println("GPR1\n");
                        }
                        case 3 -> {
                            GPR2 = MAR;
                            System.out.println("GPR2\n");
                        }
                        case 4 -> {
                            GPR3 = MAR;
                            System.out.println("GPR3\n");
                        }
                    }
                }
                else   //I = 1
                {
                    //CALCULATE EFFECTIVE ADDRESS
                    System.out.println("I=1 ");
                    //No Indexing
                    if(selectIXR == 0)
                    {
                        System.out.println("No Indexing ");
                        MAR = memory[getAddress(IR)];
                        if(isMemoryOutOfBound(MAR)) return;
                    }
                    else  //Indexed
                    {

                        switch (selectIXR) {
                            case 1 : MAR = memory[getAddress(IR) + IX1];System.out.println("IX1 ");if(isMemoryOutOfBound(MAR)) return;
                            case 2 : MAR = memory[getAddress(IR) + IX2];System.out.println("IX2 ");if(isMemoryOutOfBound(MAR)) return;
                            case 3 : MAR = memory[getAddress(IR) + IX3];System.out.println("IX3 ");if(isMemoryOutOfBound(MAR)) return;
                            default : {
                            }
                        }
                    }

                    MBR = memory[MAR];

                    //LOAD INTO GPR 0..3
                    switch(selectGPR)
                    {
                        case 1 -> {
                            GPR0 = MAR;
                            System.out.println("GPR0\n");
                        }
                        case 2 -> {
                            GPR1 = MAR;
                            System.out.println("GPR1\n");
                        }
                        case 3 -> {
                            GPR2 = MAR;
                            System.out.println("GPR2\n");
                        }
                        case 4 -> {
                            GPR3 = MAR;
                            System.out.println("GPR3\n");
                        }
                    }
                }
            }

            //PERFORM LDX WHEN OPCODE IS 41
            else if(opcodeBits.equals("100001"))
            {
                System.out.println("LDX ");

                if(doIndirectAddressing == 0)
                {
                    //CALCULATE EFFECTIVE ADDRESS
                    if(selectIXR == 0)
                    {
                        System.out.println("No Indexing ");
                        MAR = getAddress(IR);
                        if(isMemoryOutOfBound(MAR)) return;

                    }
                    else  //INDIRECT ADDRESSING = 1
                    {
                        switch (selectIXR) {
                            case 1 -> {
                                MAR = (short) (getAddress(IR));
                                if(isMemoryOutOfBound(MAR)) return;
                                System.out.println("IX1\n");
                            }
                            case 2 -> {
                                MAR = (short) (getAddress(IR));
                                if(isMemoryOutOfBound(MAR)) return;
                                System.out.println("IX2\n");
                            }
                            case 3 -> {
                                MAR = (short) (getAddress(IR));
                                if(isMemoryOutOfBound(MAR)) return;
                                System.out.println("IX3\n");
                            }
                            default -> {
                            }
                        }
                    }

                    MBR = memory[MAR];


                    //LOAD INTO IXR 1..3
                    switch(selectIXR)
                    {
                        case 1 -> {
                            IX1 = MBR;
                        }
                        case 2 -> {
                            IX2 = MBR;
                        }
                        case 3 -> {
                            IX3 = MBR;
                        }
                    }
                }
                else   //INDIRECT ADDRESSING
                {

                    System.out.println("I=1 ");
                    if(selectIXR == 0)
                    {
                        System.out.println("No Indexing ");
                        MAR = memory[getAddress(IR)];
                        if(isMemoryOutOfBound(MAR))
                            return;
                    }
                    else  //Indexed
                    {
                        switch (selectIXR) {
                            case 1 : MAR = memory[getAddress(IR)];System.out.println("IX1\n");if(isMemoryOutOfBound(MAR)) return;
                            case 2 : MAR = memory[getAddress(IR)];System.out.println("IX2\n");if(isMemoryOutOfBound(MAR)) return;
                            case 3 : MAR = memory[getAddress(IR)];System.out.println("IX3\n");if(isMemoryOutOfBound(MAR)) return;

                        }
                    }

                    MBR = memory[MAR];

                    //LOAD INTO IXR 1..3
                    switch(selectIXR)
                    {
                        case 1 -> {
                            IX1 = MAR;
                        }
                        case 2 -> {
                            IX2 = MAR;
                        }
                        case 3 -> {
                            IX3 = MAR;
                        }
                    }
                }
            }

            //PERFORM STX WHEN OPCODE IS 42
            else if(opcodeBits.equals("100010"))
            {
                System.out.println("STX ");

                if(doIndirectAddressing == 0)
                {
                    System.out.println("I=0 ");
                    //CALCULATE EFFECTIVE ADDRESS
                    if(selectIXR == 0)
                    {
                        System.out.println("No Indexing ");
                        MAR = getAddress(IR);
                        if(isMemoryOutOfBound(MAR)) return;
                    }
                    else  //Indexed
                    {
                        switch (selectIXR) {
                            case 1 -> {
                                MAR = (short) (getAddress(IR) + IX1);System.out.println("IX1\n");if(isMemoryOutOfBound(MAR)) return;
                            }
                            case 2 -> {
                                MAR = (short) (getAddress(IR) + IX2);System.out.println("IX2\n");if(isMemoryOutOfBound(MAR)) return;
                            }
                            case 3 -> {
                                MAR = (short) (getAddress(IR) + IX3);System.out.println("IX3\n");if(isMemoryOutOfBound(MAR)) return;
                            }
                            default -> {
                            }
                        }
                    }

                    //WRITE INTO MEMORY FROM IXR 1..3
                    switch(selectIXR)
                    {
                        case 1 -> {
                            memory[MAR] = IX1;
                        }
                        case 2 -> {
                            memory[MAR] = IX2;
                        }
                        case 3 -> {
                            memory[MAR] = IX3;
                        }
                    }

                    MBR = memory[MAR];
                }
                else   //I = 1
                {
                    System.out.println("I=1 ");
                    //CALCULATE EFFECTIVE ADDRESS
                    if(selectIXR == 0)
                    {
                        System.out.println("No Indexing ");
                        MAR = memory[getAddress(IR)];
                        if(isMemoryOutOfBound(MAR)) return;
                    }
                    else  //Indexed
                    {
                        switch (selectIXR) {
                            case 1 : MAR = memory[getAddress(IR) + IX1];System.out.println("IX1\n");if(isMemoryOutOfBound(MAR)) return;
                            case 2 : MAR = memory[getAddress(IR) + IX2];System.out.println("IX2\n");if(isMemoryOutOfBound(MAR)) return;
                            case 3 : MAR = memory[getAddress(IR) + IX3];System.out.println("IX3\n");if(isMemoryOutOfBound(MAR)) return;
                            default : {
                            }
                        }
                    }

                    //WRITE INTO MEMORY FROM IXR 1..3
                    switch(selectIXR)
                    {
                        case 1 -> {
                            memory[MAR] = IX1;
                        }
                        case 2 -> {
                            memory[MAR] = IX2;
                        }
                        case 3 -> {
                            memory[MAR] = IX3;
                        }
                    }
                    MBR = memory[MAR];
                }
            }

            //PERFORM JZ WHEN OPCODE IS 10
            else if(opcodeBits.equals("1000")){
                int Rx;

                Rx = switch (selectGPR){
                    case 1 -> GPR0;
                    case 2 -> GPR1;
                    case 3 -> GPR2;
                    case 4 -> GPR3;
                    default -> 0;
                };

                if(doIndirectAddressing == 0)
                {
                    //CALCULATE EFFECTIVE ADDRESS
                    if(selectIXR == 0)
                    {
                        System.out.println("No Indexing ");
                        MAR = getAddress(IR);
                        if(isMemoryOutOfBound(MAR)) return;

                    }
                    else  //INDIRECT ADDRESSING = 1
                    {
                        switch (selectIXR) {
                            case 1 -> {
                                MAR = (short) (getAddress(IR) + IX1);
                                if(isMemoryOutOfBound(MAR)) return;
                                System.out.println("IX1\n");
                            }
                            case 2 -> {
                                MAR = (short) (getAddress(IR) + IX2);
                                if(isMemoryOutOfBound(MAR)) return;
                                System.out.println("IX2\n");
                            }
                            case 3 -> {
                                MAR = (short) (getAddress(IR) + IX3);
                                if(isMemoryOutOfBound(MAR)) return;
                                System.out.println("IX3\n");
                            }
                            default -> {
                            }
                        }
                    }
                    MBR = memory[MAR];
                }
                else   //INDIRECT ADDRESSING
                {

                    System.out.println("I=1 ");
                    if(selectIXR == 0)
                    {
                        System.out.println("No Indexing ");
                        MAR = memory[getAddress(IR)];
                        if(isMemoryOutOfBound(MAR))
                            return;
                    }
                    else  //Indexed
                    {
                        switch (selectIXR) {
                            case 1 : MAR = memory[getAddress(IR) + IX1];System.out.println("IX1\n");if(isMemoryOutOfBound(MAR)) return;
                            case 2 : MAR = memory[getAddress(IR) + IX2];System.out.println("IX2\n");if(isMemoryOutOfBound(MAR)) return;
                            case 3 : MAR = memory[getAddress(IR) + IX3];System.out.println("IX3\n");if(isMemoryOutOfBound(MAR)) return;

                        }
                    }

                    MBR = memory[MAR];

                    if(Rx == 0){
                        PC = MAR - 1;
                    }
                }

            }

            //PERFORM JNE WHEN OPCODE IS 11
            else if(opcodeBits.equals("1001")){
                int Rx;

                Rx = switch (selectGPR){
                    case 1 -> GPR0;
                    case 2 -> GPR1;
                    case 3 -> GPR2;
                    case 4 -> GPR3;
                    default -> 99; //internal error indicator
                };

                if(doIndirectAddressing == 0)
                {
                    //CALCULATE EFFECTIVE ADDRESS
                    if(selectIXR == 0)
                    {
                        System.out.println("No Indexing ");
                        MAR = getAddress(IR);
                        if(isMemoryOutOfBound(MAR)) return;

                    }
                    else  //INDIRECT ADDRESSING = 1
                    {
                        switch (selectIXR) {
                            case 1 -> {
                                MAR = (short) (getAddress(IR) + IX1);
                                if(isMemoryOutOfBound(MAR)) return;
                                System.out.println("IX1\n");
                            }
                            case 2 -> {
                                MAR = (short) (getAddress(IR) + IX2);
                                if(isMemoryOutOfBound(MAR)) return;
                                System.out.println("IX2\n");
                            }
                            case 3 -> {
                                MAR = (short) (getAddress(IR) + IX3);
                                if(isMemoryOutOfBound(MAR)) return;
                                System.out.println("IX3\n");
                            }
                            default -> {
                            }
                        }
                    }
                    MBR = memory[MAR];
                }
                else   //INDIRECT ADDRESSING
                {

                    System.out.println("I=1 ");
                    if(selectIXR == 0)
                    {
                        System.out.println("No Indexing ");
                        MAR = memory[getAddress(IR)];
                        if(isMemoryOutOfBound(MAR))
                            return;
                    }
                    else  //Indexed
                    {
                        switch (selectIXR) {
                            case 1 : MAR = memory[getAddress(IR) + IX1];System.out.println("IX1\n");if(isMemoryOutOfBound(MAR)) return;
                            case 2 : MAR = memory[getAddress(IR) + IX2];System.out.println("IX2\n");if(isMemoryOutOfBound(MAR)) return;
                            case 3 : MAR = memory[getAddress(IR) + IX3];System.out.println("IX3\n");if(isMemoryOutOfBound(MAR)) return;

                        }
                    }

                    MBR = memory[MAR];

                    if(Rx != 0){
                        PC = MAR - 1;
                    }
                }

            }


            //PERFORM JCC WHEN OPCODE IS 12
            else if(opcodeBits.equals("1010")){
                int CC = 0;
                switch (selectGPR){
                    case 1 -> {
                        CC = CC0;
                    }
                    case 2 -> {
                        CC = CC1;
                    }
                    case 3 -> {
                        CC = CC2;

                    }
                    case 4 -> {
                        CC = CC3;

                    }
                    default -> {
                    }
                };

                if(CC == 1){
                    if(doIndirectAddressing == 0)
                    {
                        //CALCULATE EFFECTIVE ADDRESS
                        if(selectIXR == 0)
                        {
                            System.out.println("No Indexing ");
                            MAR = getAddress(IR);
                            if(isMemoryOutOfBound(MAR)) return;

                        }
                        else  //INDIRECT ADDRESSING = 1
                        {
                            switch (selectIXR) {
                                case 1 -> {
                                    MAR = (short) (getAddress(IR) + IX1);
                                    if(isMemoryOutOfBound(MAR)) return;
                                    System.out.println("IX1\n");
                                }
                                case 2 -> {
                                    MAR = (short) (getAddress(IR) + IX2);
                                    if(isMemoryOutOfBound(MAR)) return;
                                    System.out.println("IX2\n");
                                }
                                case 3 -> {
                                    MAR = (short) (getAddress(IR) + IX3);
                                    if(isMemoryOutOfBound(MAR)) return;
                                    System.out.println("IX3\n");
                                }
                                default -> {
                                }
                            }
                        }
                        MBR = memory[MAR];
                    }
                    else   //INDIRECT ADDRESSING
                    {

                        System.out.println("I=1 ");
                        if (selectIXR == 0) {
                            System.out.println("No Indexing ");
                            MAR = memory[getAddress(IR)];
                            if (isMemoryOutOfBound(MAR))
                                return;
                        } else  //Indexed
                        {
                            switch (selectIXR) {
                                case 1:
                                    MAR = memory[getAddress(IR) + IX1];
                                    System.out.println("IX1\n");
                                    if (isMemoryOutOfBound(MAR)) return;
                                case 2:
                                    MAR = memory[getAddress(IR) + IX2];
                                    System.out.println("IX2\n");
                                    if (isMemoryOutOfBound(MAR)) return;
                                case 3:
                                    MAR = memory[getAddress(IR) + IX3];
                                    System.out.println("IX3\n");
                                    if (isMemoryOutOfBound(MAR)) return;

                            }
                        }

                        MBR = memory[MAR];

                    }
                    PC= (MAR-1);
                }

            }

            //PERFORM JMA WHEN OPCODE IS 13
            else if(opcodeBits.equals("1011")){
                System.out.println("JMA");

                if(doIndirectAddressing == 0)
                {
                    //CALCULATE EFFECTIVE ADDRESS
                    if(selectIXR == 0)
                    {
                        System.out.println("No Indexing ");
                        MAR = getAddress(IR);
                        if(isMemoryOutOfBound(MAR)) return;

                    }
                    else  //INDIRECT ADDRESSING = 1
                    {
                        switch (selectIXR) {
                            case 1 -> {
                                MAR = (short) (getAddress(IR) + IX1);
                                if(isMemoryOutOfBound(MAR)) return;
                                System.out.println("IX1\n");
                            }
                            case 2 -> {
                                MAR = (short) (getAddress(IR) + IX2);
                                if(isMemoryOutOfBound(MAR)) return;
                                System.out.println("IX2\n");
                            }
                            case 3 -> {
                                MAR = (short) (getAddress(IR) + IX3);
                                if(isMemoryOutOfBound(MAR)) return;
                                System.out.println("IX3\n");
                            }
                            default -> {
                            }
                        }
                    }

                    MBR = memory[MAR];
                }
                else   //INDIRECT ADDRESSING
                {

                    System.out.println("I=1 ");
                    if(selectIXR == 0)
                    {
                        System.out.println("No Indexing ");
                        MAR = memory[getAddress(IR)];
                        if(isMemoryOutOfBound(MAR))
                            return;
                    }
                    else  //Indexed
                    {
                        switch (selectIXR) {
                            case 1 : MAR = memory[getAddress(IR) + IX1];System.out.println("IX1\n");if(isMemoryOutOfBound(MAR)) return;
                            case 2 : MAR = memory[getAddress(IR) + IX2];System.out.println("IX2\n");if(isMemoryOutOfBound(MAR)) return;
                            case 3 : MAR = memory[getAddress(IR) + IX3];System.out.println("IX3\n");if(isMemoryOutOfBound(MAR)) return;

                        }
                    }

                    MBR = memory[MAR];
                }
                PC = MAR - 1;
            }

            //PERFORM JSR WHEN OPCODE IS 14
            else if(opcodeBits.equals("1100")){

                System.out.println("JSR");
                GPR3 = PC + 1;

                if(doIndirectAddressing == 0)
                {
                    //CALCULATE EFFECTIVE ADDRESS
                    if(selectIXR == 0)
                    {
                        System.out.println("No Indexing ");
                        MAR = getAddress(IR);
                        if(isMemoryOutOfBound(MAR)) return;

                    }
                    else  //INDIRECT ADDRESSING = 1
                    {
                        switch (selectIXR) {
                            case 1 -> {
                                MAR = (short) (getAddress(IR) + IX1);
                                if(isMemoryOutOfBound(MAR)) return;
                                System.out.println("IX1\n");
                            }
                            case 2 -> {
                                MAR = (short) (getAddress(IR) + IX2);
                                if(isMemoryOutOfBound(MAR)) return;
                                System.out.println("IX2\n");
                            }
                            case 3 -> {
                                MAR = (short) (getAddress(IR) + IX3);
                                if(isMemoryOutOfBound(MAR)) return;
                                System.out.println("IX3\n");
                            }
                            default -> {
                            }
                        }
                    }

                    MBR = memory[MAR];
                }
                else   //INDIRECT ADDRESSING
                {

                    System.out.println("I=1 ");
                    if(selectIXR == 0)
                    {
                        System.out.println("No Indexing ");
                        MAR = memory[getAddress(IR)];
                        if(isMemoryOutOfBound(MAR))
                            return;
                    }
                    else  //Indexed
                    {
                        switch (selectIXR) {
                            case 1 : MAR = memory[getAddress(IR) + IX1];System.out.println("IX1\n");if(isMemoryOutOfBound(MAR)) return;
                            case 2 : MAR = memory[getAddress(IR) + IX2];System.out.println("IX2\n");if(isMemoryOutOfBound(MAR)) return;
                            case 3 : MAR = memory[getAddress(IR) + IX3];System.out.println("IX3\n");if(isMemoryOutOfBound(MAR)) return;

                        }
                    }

                    MBR = memory[MAR];
                }
                PC = MAR - 1;
            }

            //PERFORM RFS WHEN OPCODE IS 15
            else if(opcodeBits.equals("1101")){
                System.out.println("RFS");
                GPR0 = getAddress(IR);
                PC = GPR3 - 1;
            }

            //PERFORM SOB WHEN OPCODE IS 16
            else if(opcodeBits.equals("1110")){
                System.out.println("SOB");
                int Rx,x;

                switch (selectGPR){
                    case 1 -> {
                        GPR0 = GPR0 - 1;
                        Rx = GPR0;
                        x = 0;
                    }
                    case 2 -> {
                        GPR1 = GPR1 - 1;
                        Rx = GPR1;
                        x = 1;
                    }
                    case 3 -> {
                        GPR2 = GPR2 - 1;
                        Rx = GPR2;
                        x = 2;
                    }
                    case 4 -> {
                        GPR3 = GPR3 - 1;
                        Rx = GPR3;
                        x = 3;
                    }
                    default -> {
                        Rx = 0;
                    }
                };

                if(Rx > 0){
                    if(doIndirectAddressing == 0)
                    {
                        //CALCULATE EFFECTIVE ADDRESS
                        if(selectIXR == 0)
                        {
                            System.out.println("No Indexing ");
                            MAR = getAddress(IR);
                            if(isMemoryOutOfBound(MAR)) return;

                        }
                        else  //INDIRECT ADDRESSING = 1
                        {
                            switch (selectIXR) {
                                case 1 -> {
                                    MAR = (short) (getAddress(IR) + IX1);
                                    if(isMemoryOutOfBound(MAR)) return;
                                    System.out.println("IX1\n");
                                }
                                case 2 -> {
                                    MAR = (short) (getAddress(IR) + IX2);
                                    if(isMemoryOutOfBound(MAR)) return;
                                    System.out.println("IX2\n");
                                }
                                case 3 -> {
                                    MAR = (short) (getAddress(IR) + IX3);
                                    if(isMemoryOutOfBound(MAR)) return;
                                    System.out.println("IX3\n");
                                }
                                default -> {
                                }
                            }
                        }

                        MBR = memory[MAR];
                    }
                    else   //INDIRECT ADDRESSING
                    {

                        System.out.println("I=1 ");
                        if(selectIXR == 0)
                        {
                            System.out.println("No Indexing ");
                            MAR = memory[getAddress(IR)];
                            if(isMemoryOutOfBound(MAR))
                                return;
                        }
                        else  //Indexed
                        {
                            switch (selectIXR) {
                                case 1 : MAR = memory[getAddress(IR) + IX1];System.out.println("IX1\n");if(isMemoryOutOfBound(MAR)) return;
                                case 2 : MAR = memory[getAddress(IR) + IX2];System.out.println("IX2\n");if(isMemoryOutOfBound(MAR)) return;
                                case 3 : MAR = memory[getAddress(IR) + IX3];System.out.println("IX3\n");if(isMemoryOutOfBound(MAR)) return;

                            }
                        }

                        MBR = memory[MAR];
                    }
                    PC = MAR - 1;
                }

            }

            //PERFORM JGE WHEN OPCODE IS 17
            else if(opcodeBits.equals("1111")){
                int Rx;

                Rx = switch (selectGPR){
                    case 1 -> GPR0;
                    case 2 -> GPR1;
                    case 3 -> GPR2;
                    case 4 -> GPR3;
                    default -> 99; //internal error indicator
                };

                if(Rx >= 0){
                    if(doIndirectAddressing == 0)
                    {
                        //CALCULATE EFFECTIVE ADDRESS
                        if(selectIXR == 0)
                        {
                            System.out.println("No Indexing ");
                            MAR = getAddress(IR);
                            if(isMemoryOutOfBound(MAR)) return;

                        }
                        else  //INDIRECT ADDRESSING = 1
                        {
                            switch (selectIXR) {
                                case 1 -> {
                                    MAR = (short) (getAddress(IR) + IX1);
                                    if(isMemoryOutOfBound(MAR)) return;
                                    System.out.println("IX1\n");
                                }
                                case 2 -> {
                                    MAR = (short) (getAddress(IR) + IX2);
                                    if(isMemoryOutOfBound(MAR)) return;
                                    System.out.println("IX2\n");
                                }
                                case 3 -> {
                                    MAR = (short) (getAddress(IR) + IX3);
                                    if(isMemoryOutOfBound(MAR)) return;
                                    System.out.println("IX3\n");
                                }
                                default -> {
                                }
                            }
                        }
                        MBR = memory[MAR];
                    }
                    else   //INDIRECT ADDRESSING
                    {

                        System.out.println("I=1 ");
                        if(selectIXR == 0)
                        {
                            System.out.println("No Indexing ");
                            MAR = memory[getAddress(IR)];
                            if(isMemoryOutOfBound(MAR))
                                return;
                        }
                        else  //Indexed
                        {
                            switch (selectIXR) {
                                case 1 : MAR = memory[getAddress(IR) + IX1];System.out.println("IX1\n");if(isMemoryOutOfBound(MAR)) return;
                                case 2 : MAR = memory[getAddress(IR) + IX2];System.out.println("IX2\n");if(isMemoryOutOfBound(MAR)) return;
                                case 3 : MAR = memory[getAddress(IR) + IX3];System.out.println("IX3\n");if(isMemoryOutOfBound(MAR)) return;

                            }
                        }

                        MBR = memory[MAR];
                    }
                    PC = MAR - 1;
                }
            }

            //PERFORM AMR WHEN OPCODE IS 04
            else if (opcodeBits.equals("100")) {
                int Rx;

                if(doIndirectAddressing == 0)
                {
                    //CALCULATE EFFECTIVE ADDRESS
                    if(selectIXR == 0)
                    {
                        System.out.println("No Indexing ");
                        MAR = getAddress(IR);
                        if(isMemoryOutOfBound(MAR)) return;

                    }
                    else  //INDIRECT ADDRESSING = 1
                    {
                        switch (selectIXR) {
                            case 1 -> {
                                MAR = (short) (getAddress(IR) + IX1);
                                if(isMemoryOutOfBound(MAR)) return;
                                System.out.println("IX1\n");
                            }
                            case 2 -> {
                                MAR = (short) (getAddress(IR) + IX2);
                                if(isMemoryOutOfBound(MAR)) return;
                                System.out.println("IX2\n");
                            }
                            case 3 -> {
                                MAR = (short) (getAddress(IR) + IX3);
                                if(isMemoryOutOfBound(MAR)) return;
                                System.out.println("IX3\n");
                            }
                            default -> {
                            }
                        }
                    }
                    MBR = memory[MAR];
                }
                else   //INDIRECT ADDRESSING
                {

                    System.out.println("I=1 ");
                    if(selectIXR == 0)
                    {
                        System.out.println("No Indexing ");
                        MAR = memory[getAddress(IR)];
                        if(isMemoryOutOfBound(MAR))
                            return;
                    }
                    else  //Indexed
                    {
                        switch (selectIXR) {
                            case 1 : MAR = memory[getAddress(IR) + IX1];System.out.println("IX1\n");if(isMemoryOutOfBound(MAR)) return;
                            case 2 : MAR = memory[getAddress(IR) + IX2];System.out.println("IX2\n");if(isMemoryOutOfBound(MAR)) return;
                            case 3 : MAR = memory[getAddress(IR) + IX3];System.out.println("IX3\n");if(isMemoryOutOfBound(MAR)) return;

                        }
                    }
                    MBR = memory[MAR];
                }

                switch (selectGPR){
                    case 1 -> {
                        GPR0 = GPR0 + memory[MAR];
                        if(GPR0>=Math.pow(2,16)){
                            CC0=1;
                        }
                        else{
                            CC0=0;
                        }
                    }

                    case 2 -> {
                        GPR1 = GPR1 + memory[MAR];
                        if(GPR1>=Math.pow(2,16)){
                            CC0=1;
                        }
                        else{
                            CC0=0;
                        }
                    }
                    case 3 -> {
                        GPR2 = GPR2 + memory[MAR];
                        if(GPR2>=Math.pow(2,16)){
                            CC0=1;
                        }
                        else{
                            CC0=0;
                        }
                    }
                    case 4 -> {
                        GPR3 = GPR3 + memory[MAR];
                        if(GPR3>=Math.pow(2,16)){
                            CC0=1;
                        }
                        else{
                            CC0=0;
                        }
                    }
                    default -> {} //internal error indicator
                };

            }

            //PERFORM SMR WHEN OPCODE IS 05
            else if(opcodeBits.equals("101")){
                int Rx;

                if(doIndirectAddressing == 0)
                {
                    //CALCULATE EFFECTIVE ADDRESS
                    if(selectIXR == 0)
                    {
                        System.out.println("No Indexing ");
                        MAR = getAddress(IR);
                        if(isMemoryOutOfBound(MAR)) return;

                    }
                    else  //INDIRECT ADDRESSING = 1
                    {
                        switch (selectIXR) {
                            case 1 -> {
                                MAR = (short) (getAddress(IR) + IX1);
                                if(isMemoryOutOfBound(MAR)) return;
                                System.out.println("IX1\n");
                            }
                            case 2 -> {
                                MAR = (short) (getAddress(IR) + IX2);
                                if(isMemoryOutOfBound(MAR)) return;
                                System.out.println("IX2\n");
                            }
                            case 3 -> {
                                MAR = (short) (getAddress(IR) + IX3);
                                if(isMemoryOutOfBound(MAR)) return;
                                System.out.println("IX3\n");
                            }
                            default -> {
                            }
                        }
                    }
                    MBR = memory[MAR];
                }
                else   //INDIRECT ADDRESSING
                {

                    System.out.println("I=1 ");
                    if(selectIXR == 0)
                    {
                        System.out.println("No Indexing ");
                        MAR = memory[getAddress(IR)];
                        if(isMemoryOutOfBound(MAR))
                            return;
                    }
                    else  //Indexed
                    {
                        switch (selectIXR) {
                            case 1 : MAR = memory[getAddress(IR) + IX1];System.out.println("IX1\n");if(isMemoryOutOfBound(MAR)) return;
                            case 2 : MAR = memory[getAddress(IR) + IX2];System.out.println("IX2\n");if(isMemoryOutOfBound(MAR)) return;
                            case 3 : MAR = memory[getAddress(IR) + IX3];System.out.println("IX3\n");if(isMemoryOutOfBound(MAR)) return;

                        }
                    }
                    MBR = memory[MAR];
                }

                switch (selectGPR){
                    case 1 -> {
                        GPR0 = GPR0 - memory[MAR];
                    }

                    case 2 -> {
                        GPR1 = GPR1 - memory[MAR];
                    }
                    case 3 -> {
                        GPR2 = GPR2 - memory[MAR];
                    }
                    case 4 -> {
                        GPR3 = GPR3 - memory[MAR];
                    }
                    default -> {} //internal error indicator
                };
            }

            //PERFORM AIR WHEN OPCODE IS 06
            else if(opcodeBits.equals("110")){
                System.out.println("AIR");
                int Immed = getAddress(IR);

                if(Immed != 0){
                    switch (selectGPR){
                        case 1 -> {
                            GPR0 = GPR0 + Immed;
                        }

                        case 2 -> {
                            GPR1 = GPR1 + Immed;
                        }
                        case 3 -> {
                            GPR2 = GPR2 + Immed;
                        }
                        case 4 -> {
                            GPR3 = GPR3 + Immed;
                        }
                        default -> {} //internal error indicator
                    };
                }
            }

            //PERFORM SIR WHEN OPCODE IS 07
            else if(opcodeBits.equals("111")){
                System.out.println("AIR");
                int Immed = getAddress(IR);

                if(Immed != 0){
                    switch (selectGPR){
                        case 1 -> {
                            GPR0 = GPR0 - Immed;
                        }

                        case 2 -> {
                            GPR1 = GPR1 - Immed;
                        }
                        case 3 -> {
                            GPR2 = GPR2 - Immed;
                        }
                        case 4 -> {
                            GPR3 = GPR3  - Immed;
                        }
                        default -> {} //internal error indicator
                    };
                }
            }

            //PERFORM MLT WHEN OPCODE IS 20
            else if(opcodeBits.equals("10000")){
                int Rx=0, Ry=0, x=0, y=0;

                switch (selectGPR){
                    case 1 -> {
                        Rx = GPR0;
                        x = 0;
                    }
                    case 2 -> {
                        Rx = GPR1;
                        x = 1;
                    }
                    case 3 -> {
                        Rx = GPR2;
                        x = 2;
                    }
                    case 4 -> {
                        Rx = GPR3;
                        x = 3;
                    }
                    default -> {
                        Rx = 0;
                    }
                };

                switch (selectIXR){
                    case 1 -> {
                        Ry = GPR0;
                        y = 0;
                    }
                    case 2 -> {
                        Ry = GPR1;
                        y = 1;
                    }
                    case 3 -> {
                        Ry = GPR2;
                        y = 2;
                    }
                    case 4 -> {
                        Ry = GPR3;
                        y = 3;
                    }
                    default -> {
                        Rx = 0;
                        y = 0;
                    }
                };

                int MLT = Rx * Ry;
                String MlT_binary = genBinaryPaddedString(MLT, 16);

                if(MLT >= Math.pow(2, 16)){
                    CC0 = 1;
                }
                else {
                    CC0 = 0;
                }

                if(x == 0){
                    GPR0 = Integer.parseInt(MlT_binary.substring(0, 8), 2);
                    GPR1 = Integer.parseInt(MlT_binary.substring(8, 16), 2);
                }
                else if(x == 1){
                    GPR2 = Integer.parseInt(MlT_binary.substring(0, 8), 2);
                    GPR3 = Integer.parseInt(MlT_binary.substring(8, 16), 2);
                }
            }


            //PERFORM DVD WHEN OPCODE IS 21
            else if(opcodeBits.equals("10001")){
                System.out.println("DVD");

                int Rx=0, Ry=0, x=0, y=0;

                switch (selectGPR){
                    case 1 -> {
                        Rx = GPR0;
                        x = 0;
                    }
                    case 2 -> {
                        Rx = GPR1;
                        x = 1;
                    }
                    case 3 -> {
                        Rx = GPR2;
                        x = 2;
                    }
                    case 4 -> {
                        Rx = GPR3;
                        x = 3;
                    }
                    default -> {
                        Rx = 0;
                    }
                };

                switch (selectIXR){
                    case 1 -> {
                        Ry = GPR0;
                        y = 0;
                    }
                    case 2 -> {
                        Ry = GPR1;
                        y = 1;
                    }
                    case 3 -> {
                        Ry = GPR2;
                        y = 2;
                    }
                    case 4 -> {
                        Ry = GPR3;
                        y = 3;
                    }
                    default -> {
                        Rx = 0;
                        y = 0;
                    }
                };

                if(Ry == 0){
                    CC2=1;
                }
                else
                {
                    CC2=0;
                    int Quotient = (Rx / Ry);
                    int Remainder = (Rx % Ry);
                    if(x==0){
                        GPR0 = Quotient;
                        GPR1 = Remainder;
                    }
                    else if(x==2){
                        GPR2 = Quotient;
                        GPR3 = Remainder;
                    }
                }
            }

            //PERFORM TRR WHEN OPCODE IS 22
            else if(opcodeBits.equals("10010")){

                System.out.println("TRR");
                int Rx=0, Ry=0, x=0, y=0;

                switch (selectGPR){
                    case 1 -> {
                        Rx = GPR0;
                        x = 0;
                    }
                    case 2 -> {
                        Rx = GPR1;
                        x = 1;
                    }
                    case 3 -> {
                        Rx = GPR2;
                        x = 2;
                    }
                    case 4 -> {
                        Rx = GPR3;
                        x = 3;
                    }
                    default -> {
                        Rx = 0;
                    }
                };

                switch (selectIXR){
                    case 1 -> {
                        Ry = GPR0;
                        y = 0;
                    }
                    case 2 -> {
                        Ry = GPR1;
                        y = 1;
                    }
                    case 3 -> {
                        Ry = GPR2;
                        y = 2;
                    }
                    case 4 -> {
                        Ry = GPR3;
                        y = 3;
                    }
                    default -> {
                        Ry = 0;
                        y = 0;
                    }
                };
                System.out.println(Rx);
                System.out.println(Ry);

                if(Rx == Ry){
                    CC3=1;
                }
                else{
                    CC3=0;
                }
                System.out.println(CC3);
            }

            //PERFORM AND WHEN OPCODE IS 23
            else if(opcodeBits.equals("10011")){
                System.out.println("AND");
                int Rx=0, Ry=0, x=0, y=0;

                switch (selectGPR){
                    case 1 -> {
                        Rx = GPR0;
                        x = 0;
                    }
                    case 2 -> {
                        Rx = GPR1;
                        x = 1;
                    }
                    case 3 -> {
                        Rx = GPR2;
                        x = 2;
                    }
                    case 4 -> {
                        Rx = GPR3;
                        x = 3;
                    }
                    default -> {
                        Rx = 0;
                    }
                };

                switch (selectIXR){
                    case 1 -> {
                        Ry = GPR0;
                        y = 0;
                    }
                    case 2 -> {
                        Ry = GPR1;
                        y = 1;
                    }
                    case 3 -> {
                        Ry = GPR2;
                        y = 2;
                    }
                    case 4 -> {
                        Ry = GPR3;
                        y = 3;
                    }
                    default -> {
                        Rx = 0;
                        y = 0;
                    }
                };

                Rx = (Rx & Ry);
                if(x == 0){
                    GPR0=Rx;
                }
                else if(x == 1){
                    GPR1=Rx;

                }
                else if(x == 2){
                    GPR2=Rx;
                }
                else{
                    GPR3=Rx;
                }
            }

            //PERFORM ORR WHEN OPCODE IS 24
            else if(opcodeBits.equals("10100")){
                System.out.println("ORR");
                int Rx=0, Ry=0, x=0, y=0;

                switch (selectGPR){
                    case 1 -> {
                        Rx = GPR0;
                        x = 0;
                    }
                    case 2 -> {
                        Rx = GPR1;
                        x = 1;
                    }
                    case 3 -> {
                        Rx = GPR2;
                        x = 2;
                    }
                    case 4 -> {
                        Rx = GPR3;
                        x = 3;
                    }
                    default -> {
                        Rx = 0;
                    }
                };

                switch (selectIXR){
                    case 1 -> {
                        Ry = GPR0;
                        y = 0;
                    }
                    case 2 -> {
                        Ry = GPR1;
                        y = 1;
                    }
                    case 3 -> {
                        Ry = GPR2;
                        y = 2;
                    }
                    case 4 -> {
                        Ry = GPR3;
                        y = 3;
                    }
                    default -> {
                        Rx = 0;
                        y = 0;
                    }
                };

                Rx =  (Rx | Ry);
                if(x == 0){
                    GPR0=Rx;
                }
                else if(x == 1){
                    GPR1=Rx;
                }
                else if(x == 2){
                    GPR2=Rx;
                }
                else{
                    GPR3=Rx;
                }

            }

            //PERFORM NOT WHEN OPCODE IS 25
            else if(opcodeBits.equals("10101")){
                System.out.println("NOT");

                int Rx=0, x=0;

                switch (selectGPR){
                    case 1 -> {
                        Rx = GPR0;
                        x = 0;
                    }
                    case 2 -> {
                        Rx = GPR1;
                        x = 1;
                    }
                    case 3 -> {
                        Rx = GPR2;
                        x = 2;
                    }
                    case 4 -> {
                        Rx = GPR3;
                        x = 3;
                    }
                    default -> {
                        Rx = 0;
                    }
                };

                Rx =  ~(Rx);
                if(x == 0){
                    GPR0=Rx;
                }
                else if(x == 1){
                    GPR1=Rx;
                }
                else if(x == 2){
                    GPR2=Rx;
                }
                else{
                    GPR3=Rx;
                }
            }

            //PEFORM SRC WHEN OPCODE IS 31
            else if(opcodeBits.equals("11001")){

                System.out.println("SRC");
                int x=0, Count=getAddress(IR), LR=0, AL=0, Rx=0;

                switch (selectGPR){
                    case 1 -> {
                        Rx = GPR0;
                        x = 0;
                    }
                    case 2 -> {
                        Rx = GPR1;
                        x = 1;
                    }
                    case 3 -> {
                        Rx = GPR2;
                        x = 2;
                    }
                    case 4 -> {
                        Rx = GPR3;
                        x = 3;
                    }
                    default -> {
                        Rx = 0;
                    }
                };

                if(selectIXR == 1 || selectIXR == 3){
                    Rx = (Rx<< Count);
                }
                else if(selectIXR == 2){
                    String Rx_bin = genBinaryPaddedString(Rx&0xFFFF, 16);
                    if(Rx_bin.charAt(16-Count)=='1'){
                        CC1=1;
                    }
                    else
                    {
                        CC1=0;
                    }
                    Rx=(short) (Rx>>(short) Count);
                }
                else{
                    String Rx_bin = genBinaryPaddedString(Rx&0xFFFF, 16);
                    if(Rx_bin.charAt(16-Count)=='1'){
                        CC1=1;
                    }
                    else
                    {
                        CC1=0;
                    }
                    Rx=(short) (Rx>>>(short) Count);
                }

                switch (x) {
                    case 0 -> {
                        GPR0=Rx;
                    }
                    case 1 -> {
                        GPR1=Rx;
                    }
                    case 2 -> {
                        GPR2=Rx;
                    }
                    default -> {
                        GPR3=Rx;
                    }
                }
            }

            //PERFORM RRC WHEN OPCODE IS 32
            else if(opcodeBits.equals("11010")){
                System.out.println("RRC");
                int x=0, Count=getAddress(IR), Rx=0;

                switch (selectGPR){
                    case 1 -> {
                        Rx = GPR0;
                        x = 0;
                    }
                    case 2 -> {
                        Rx = GPR1;
                        x = 1;
                    }
                    case 3 -> {
                        Rx = GPR2;
                        x = 2;
                    }
                    case 4 -> {
                        Rx = GPR3;
                        x = 3;
                    }
                    default -> {
                        Rx = 0;
                    }
                };

                if(selectIXR == 1 || selectIXR == 3){
                    Rx= ((Rx << Count) | (Rx >> (16 - Count)));
                }
                else{
                    Rx= ((Rx >> Count) | (Rx << (16 - Count)));
                }

                if(Rx>=Math.pow(2,16)){
                    CC0=1;
                }
                else{
                    CC0=0;
                }

                switch (x) {
                    case 0 -> {
                        GPR0=Rx;
                    }
                    case 1 -> {
                        GPR1=Rx;
                    }
                    case 2 -> {
                        GPR2=Rx;
                    }
                    default -> {
                        GPR3=Rx;
                    }
                }
            }


            //PERFORM IN WHEN OPCODE IS 61
            else if(opcodeBits.equals("110001")){
                System.out.println("IN");
                int DevId = getAddress(IR);
                InputVal = Character.getNumericValue(InputVal);
                System.out.println(InputVal);

                switch (selectGPR){
                    case 1 -> {
                        GPR0 = InputVal;
                    }
                    case 2 -> {
                        GPR1 = InputVal;
                    }
                    case 3 -> {
                        GPR2 = InputVal;
                    }
                    case 4 -> {
                        GPR3 = InputVal;
                    }
                    default -> {
                    }
                };

                InputSignal = 0;
            }

            //PERFORM OUT WHEN OPCODE IS 62
            else if(opcodeBits.equals("110010")){
                System.out.println("OUT");

                int DevId = getAddress(IR);

                switch (selectGPR){
                    case 1 -> {
                        printer = String.valueOf(GPR0);
                    }
                    case 2 -> {
                        printer = String.valueOf(GPR1);
                    }
                    case 3 -> {
                        printer = String.valueOf( GPR2);
                    }
                    case 4 -> {
                        printer = String.valueOf(GPR3);
                    }
                    default -> {
                    }
                };

                System.out.println(printer);
            }

        }

        PC++;

//        System.out.println("MAR " + MAR);
//        System.out.println("MBR " + MBR);
//        System.out.println("IR " + IR);
//        System.out.println("PC " + PC);
//
//        System.out.println("GPR0 " + GPR0);
//        System.out.println("GPR1 " + GPR1);
//        System.out.println("GPR2 " + GPR2);
//        System.out.println("GPR3 " + GPR3);
//
//        System.out.println("IX1 " + IX1);
//        System.out.println("IX2 " + IX2);
//        System.out.println("IX3 " + IX3);

        genUIVals();
    }

    //SINGLE STEP BUTTON FUNCTION
    public void singleStep(){
        this.executeInstruction();
    }

    public void run(){
        while (true){
            executeInstruction();
            if(IR == 0){
                System.out.println("Machine Halted!!");
                break;
            }
        }
    }

    //GENERATE UI VALUES AND STORE INTO uiValues variable
    public void genUIVals(){
        uiValues.put("GPR0", genBinaryPaddedString(GPR0,16));
        uiValues.put("GPR1", genBinaryPaddedString(GPR1,16));
        uiValues.put("GPR2", genBinaryPaddedString(GPR2,16));
        uiValues.put("GPR3", genBinaryPaddedString(GPR3,16));

        uiValues.put("IX1", genBinaryPaddedString(IX1,16));
        uiValues.put("IX2", genBinaryPaddedString(IX2,16));
        uiValues.put("IX3", genBinaryPaddedString(IX3,16));

        uiValues.put("PC", genBinaryPaddedString(PC,12));
        uiValues.put("MAR",genBinaryPaddedString(MAR, 12));
        uiValues.put("MBR", genBinaryPaddedString(MBR, 16));
        uiValues.put("IR", genBinaryPaddedString(IR,16));

        uiValues.put("CC0", genBinaryPaddedString(CC0, 1));
        uiValues.put("CC1", genBinaryPaddedString(CC1, 1));
        uiValues.put("CC2", genBinaryPaddedString(CC2, 1));
        uiValues.put("CC3", genBinaryPaddedString(CC3, 1));

        uiValues.put("printer", printer);

    }

    //GENERATE BINARY STRING PADDED WITH ZEROS OF GIVEN LENGTH
    public String genBinaryPaddedString(int num, int length){
        String format = "%"+length+"s";
        return String.format(format, Integer.toBinaryString(num)).replace(" ", "0");
    }

    public void KeyBoardKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_KeyBoardKeyPressed

    }

    public void KeyBoardKeyReleased(java.awt.event.KeyEvent evt, JTextField KeyBoard) {//GEN-FIRST:event_KeyBoardKeyReleased
        String str = KeyBoard.getText();
        System.out.println(str);
        if(evt.getKeyCode()== KeyEvent.VK_CAPS_LOCK){
            return;
        }
        if(InputSignal==0){
            if(str.charAt(str.length()-1)!='\n')
            {
                InputVal=(short) str.charAt(str.length()-1);
            }
            else{
                InputVal=(short) ((char)10);
            }
            InputSignal = 1;
        }
    }

    //'main' function
    public static void main(String[] args) {

        //Create new ui object and call the 'init' function
        UI ui = new UI();
        ui.init();

    }
}