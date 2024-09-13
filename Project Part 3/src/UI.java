import javax.swing.*;
import javax.swing.border.Border;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;
import javax.swing.text.*;

//CLASS TO SET CHARACTER LIMIT IN JTEXTFIELD
class JTextFieldLimit extends PlainDocument {
    private int limit;
    JTextFieldLimit(int limit) {
        super();
        this.limit = limit;
    }
    JTextFieldLimit(int limit, boolean upper) {
        super();
        this.limit = limit;
    }
    public void insertString(int offset, String str, AttributeSet attr) throws BadLocationException {
        if (str == null)
            return;
        if ((getLength() + str.length()) <= limit) {
            super.insertString(offset, str, attr);
        }
    }
}

//MAIN UI
public class UI {

    public boolean inputInstructionValid;

    //SIMULATOR / CPU OBJECT
    Simulator simulator;
    Font f1 = new Font("Monotype Corsiva", Font.CENTER_BASELINE, 16);
    Font f2 = new Font("Serif", Font.BOLD, 24);
    Font f3 = new Font("Impact", Font.PLAIN, 20);
    Font f4 = new Font("Monotype Corsiva", Font.CENTER_BASELINE, 24);
    JFrame frame = new JFrame("Simulator Panel");

    /**--- FOLLOWING ARE THE VARIABLES OF REQUIRED ALL COMPONENTS/FUNCTIONALITIES---**/
    /** NAMING CONVENTION **/
    //example:- lblPC => "lbl" text label of PC
    //ldPC => "ld" load button of PC
    //PC => text field for PC register
    /**ALL components follow the above convention--**/

    JLabel lblInputInstruction = new JLabel("Input Instruction");
    JTextField inputInstruction = new JTextField();
    JLabel msg = new JLabel();
    JButton submit = new JButton("SUBMIT");
    JTextField PC = new JTextField();
    JLabel lblPC = new JLabel("PC");
    JButton ldPC = new JButton("LD");

    JTextField MAR = new JTextField();
    JLabel lblMAR = new JLabel("MAR");
    JButton ldMAR = new JButton("LD");

    JTextField MBR = new JTextField();
    JLabel lblMBR = new JLabel("MBR");
    JButton ldMBR = new JButton("LD");
    JTextField IR = new JTextField();
    JLabel lblIR = new JLabel("IR");
    JButton ldIR = new JButton("LD");
    JButton store = new JButton("STORE");
    JButton load = new JButton("LOAD");

    JButton ss = new JButton("SS");
    JButton run = new JButton("RUN");
    JButton ipl = new JButton("IPL");
    JTextField GPR0 = new JTextField();
    JLabel lblGPR0 = new JLabel("GPR0");
    JButton ldGPR0 = new JButton("LD");
    JTextField GPR1 = new JTextField();
    JLabel lblGPR1 = new JLabel("GPR1");
    JButton ldGPR1 = new JButton("LD");
    JTextField GPR2 = new JTextField();
    JLabel lblGPR2 = new JLabel("GPR2");
    JButton ldGPR2 = new JButton("LD");
    JTextField GPR3 = new JTextField();
    JLabel lblGPR3 = new JLabel("GPR3");
    JButton ldGPR3 = new JButton("LD");
    JTextField IX1 = new JTextField();
    JLabel lblIX1 = new JLabel("IX1");
    JButton ldIX1 = new JButton("LD");
    JTextField IX2 = new JTextField();
    JLabel lblIX2 = new JLabel("IX2");
    JButton ldIX2 = new JButton("LD");
    JTextField IX3 = new JTextField();
    JLabel lblIX3 = new JLabel("IX3");
    JButton ldIX3 = new JButton("LD");

    JLabel lblCC = new JLabel("CC");
    JTextField CC0 = new JTextField();
    JLabel lblCC0 = new JLabel("CC0");

    JTextField CC1 = new JTextField();
    JLabel lblCC1 = new JLabel("CC1");

    JTextField CC2 = new JTextField();
    JLabel lblCC2 = new JLabel("CC2");

    JTextField CC3 = new JTextField();
    JLabel lblCC3 = new JLabel("CC3");

    JTextField printer = new JTextField();
    JLabel lblPrinter = new JLabel("Printer");

    JTextField KeyBoard = new JTextField();
    JLabel lblKeyBoard = new JLabel("Keyboard");



    JButton fullReset = new JButton("FULL RESET");

    private void styleButton(JButton button, Color bgColor, Color fgColor, Font font) {
        button.setBackground(bgColor);
        button.setForeground(fgColor);
        button.setFont(font);
        button.setOpaque(true);
        button.setBorderPainted(false);
    }
    
    private void styleTextField(JTextField textField, Font font, Color bgColor, Color fgColor, Border border) {
        textField.setFont(font);
        textField.setBackground(bgColor);
        textField.setForeground(fgColor);
        textField.setBorder(border);
        textField.setHorizontalAlignment(JTextField.CENTER); // For center alignment
    }
    
    //STARTS UI (INCLUDES THE CPU OBJECT)
    public void init(){

        this.simulator = new Simulator();


        /*-----FOLLOWING SETS THE DESIGN OF EACH COMPONENT OF THE UI AS WELL AS BINDS SIMULATOR CLASS FUNCTIONS--------*/

        frame.setSize(1500, 950);
        frame.getContentPane().setBackground(new Color(155, 209, 84));
        frame.setLayout(new BorderLayout(10, 10));
        

        lblInputInstruction.setBounds(700, 420, 300, 30);
        lblInputInstruction.setFont(f4);

        inputInstruction.setDocument(new JTextFieldLimit(16));
        inputInstruction.setBounds(590, 380, 380, 40);
        inputInstruction.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        inputInstruction.setColumns(16);
        styleTextField(inputInstruction, f2, new Color(255, 127, 80), Color.white, BorderFactory.createLineBorder(new Color(0, 191, 255), 2));

        msg.setVisible(false);
        
        styleButton(submit, new Color(196, 161, 240), new Color(43, 133, 34), f3);
        submit.setBounds(1240, 340, 100, 40);
        
        //SETS INPUT INSTRUCTION DATA IN CPU ONLY WHEN IT IS BINARY OF LENGTH 16
        submit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                inputInstructionValid = Pattern.matches("[01]{16}", inputInstruction.getText());
                if(inputInstructionValid){

                    char[] chars = inputInstruction.getText().toCharArray();
                    int [] temp = new int[chars.length];

                    for (int i = 0; i < temp.length; i++) {
                        temp[i] = Integer.parseInt(String.valueOf(chars[i]));
                    }

                    simulator.setInputInstruction(temp);

                    msg.setText("SUBMITTED!");
                    msg.setBounds(1190, 345, 400, 90 );
                    msg.setFont(f1);
                    msg.setForeground(new Color(56, 110, 176));
                    msg.setVisible(true);

                }
                else {
                    msg.setText("Enter Input which is binary of length 16");
                    msg.setBounds(1130, 345, 400, 90 );
                    msg.setFont(f1);
                    msg.setForeground(Color.red);
                    msg.setVisible(true);
                }
            }
        });

        PC.setDocument(new JTextFieldLimit(12));
        PC.setEditable(false);
        PC.setBounds(1090, 550, 240, 40);
        PC.setColumns(12);
        styleTextField(PC, f2, new Color(255, 127, 80), Color.white, BorderFactory.createLineBorder(new Color(0, 191, 255), 2));

        lblPC.setBounds(1060, 550, 20, 40);
        lblPC.setFont(f1);

        styleButton(ldPC, new Color(128, 93, 194), Color.white, f3);
        ldPC.setBounds(1340, 550, 60, 40);
        ldPC.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PC.setText(simulator.loadPC());
                resetInputInstruction();
            }
        });

        MAR.setDocument(new JTextFieldLimit(12));
        MAR.setEditable(false);
        MAR.setBounds(1090, 600, 240, 40);
        MAR.setColumns(12);
        styleTextField(MAR, f2, new Color(255, 127, 80), Color.white, BorderFactory.createLineBorder(new Color(0, 191, 255), 2));


        lblMAR.setBounds(1045, 600, 40, 40);
        lblMAR.setFont(f1);
        
        ldMAR.setBounds(1340, 600, 60, 40);
        styleButton(ldMAR, new Color(128, 93, 194), Color.white, f3);
        ldMAR.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MAR.setText(simulator.loadMAR());
                resetInputInstruction();
            }
        });

        MBR.setDocument(new JTextFieldLimit(16));
        MBR.setEditable(false);
        MBR.setBounds(1090, 650, 240, 40);
        MBR.setColumns(16);
        styleTextField(MBR, f2, new Color(255, 127, 80), Color.white, BorderFactory.createLineBorder(new Color(0, 191, 255), 2));


        lblMBR.setBounds(1045, 650, 40, 40);
        lblMBR.setFont(f1);
        
        ldMBR.setBounds(1340, 650, 60, 40);
        styleButton(ldMBR, new Color(128, 93, 194), Color.white, f3);
        ldMBR.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MBR.setText(simulator.loadMBR());
                resetInputInstruction();
            }
        });

        IR.setDocument(new JTextFieldLimit(16));
        IR.setEditable(false);
        IR.setBounds(1090, 700, 240, 40);
        IR.setColumns(16);
        styleTextField(IR, f2, new Color(255, 127, 80), Color.white, BorderFactory.createLineBorder(new Color(0, 191, 255), 2));


        lblIR.setBounds(1060, 700, 40, 40);
        lblIR.setFont(f1);
        
        ldIR.setBounds(1340, 700, 60, 40);
        styleButton(ldIR, new Color(128, 93, 194), Color.white, f3);
        ldIR.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                IR.setText(simulator.loadIR());
                resetInputInstruction();
            }
        });

        JFrame operationsPanel = new JFrame();
        operationsPanel.setLayout(new FlowLayout());

        store.setBounds(190, 340, 100, 40);
        styleButton(store, new Color(196, 161, 240), new Color(204, 65, 183), f3);
        store.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                simulator.store();
                clearUI();
            }
        });

        load.setBounds(310, 340, 80, 40);
        styleButton(load, new Color(196, 161, 240), new Color(204, 65, 183), f3);
        load.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                simulator.load();
                updateUI(simulator.getUiValues());
            }
        });

        ss.setBounds(200, 400, 80, 40);
        styleButton(ss, new Color(196, 161, 240), new Color(204, 65, 183), f3);
        ss.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                simulator.singleStep();
                updateUI(simulator.getUiValues());
            }
        });

        run.setBounds(300, 400, 80, 40);
        styleButton(run, new Color(196, 161, 240), new Color(204, 65, 183), f3);
        run.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                while (true){
                    simulator.singleStep();
                    updateUI(simulator.getUiValues());
                    if(simulator.getIR() == 0){
                        System.out.println("Machine Halted!!");
                        break;
                    }
                }

            }
        });

        ipl.setBounds(1140, 340, 80, 40);
        styleButton(ipl, new Color(196, 161, 240), new Color(204, 65, 183), f3);
        ipl.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                simulator.loadIPL();
                updateUI(simulator.getUiValues());
                resetInputInstruction();
            }
        });


        GPR0.setDocument(new JTextFieldLimit(16));
        GPR0.setEditable(false);
        GPR0.setBounds(110, 550, 240, 40);
        GPR0.setColumns(16);
        styleTextField(GPR0, f2, new Color(255, 127, 80), Color.white, BorderFactory.createLineBorder(new Color(0, 191, 255), 2));


        lblGPR0.setBounds(60, 550, 40, 40);
        lblGPR0.setFont(f1);
        
        ldGPR0.setBounds(360, 550, 60, 40);
        styleButton(ldGPR0, new Color(128, 93, 194), Color.white, f3);
        ldGPR0.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GPR0.setText(simulator.loadGPR0());
                resetInputInstruction();
            }
        });


        GPR1.setDocument(new JTextFieldLimit(16));
        GPR1.setEditable(false);
        GPR1.setBounds(110, 600, 240, 40);
        GPR1.setColumns(16);
        styleTextField(GPR1, f2, new Color(255, 127, 80), Color.white, BorderFactory.createLineBorder(new Color(0, 191, 255), 2));

        lblGPR1.setBounds(60, 600, 40, 40);
        lblGPR1.setFont(f1);
        
        ldGPR1.setBounds(360, 600, 60, 40);
        styleButton(ldGPR1, new Color(128, 93, 194), Color.white, f3);
        ldGPR1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GPR1.setText(simulator.loadGPR1());
                resetInputInstruction();
            }
        });

        GPR2.setDocument(new JTextFieldLimit(16));
        GPR2.setEditable(false);
        GPR2.setBounds(110, 650, 240, 40);
        GPR2.setColumns(16);
        styleTextField(GPR2, f2, new Color(255, 127, 80), Color.white, BorderFactory.createLineBorder(new Color(0, 191, 255), 2));


        lblGPR2.setBounds(60, 650, 40, 40);
        lblGPR2.setFont(f1);
        
        ldGPR2.setBounds(360, 650, 60, 40);
        styleButton(ldGPR2, new Color(128, 93, 194), Color.white, f3);
        ldGPR2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GPR2.setText(simulator.loadGPR2());
                resetInputInstruction();
            }
        });

        GPR3.setDocument(new JTextFieldLimit(16));
        GPR3.setEditable(false);
        GPR3.setBounds(110, 700, 240, 40);
        GPR3.setColumns(16);
        styleTextField(GPR3, f2, new Color(255, 127, 80), Color.white, BorderFactory.createLineBorder(new Color(0, 191, 255), 2));


        lblGPR3.setBounds(60, 700, 40, 40);
        lblGPR3.setFont(f1);
        
        ldGPR3.setBounds(360, 700, 60, 40);
        styleButton(ldGPR3, new Color(128, 93, 194), Color.white, f3);
        ldGPR3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GPR3.setText(simulator.loadGPR3());
                resetInputInstruction();
            }
        });

        IX1.setDocument(new JTextFieldLimit(16));
        IX1.setEditable(false);
        IX1.setBounds(595, 600, 240, 40);
        IX1.setColumns(16);
        styleTextField(IX1, f2, new Color(255, 127, 80), Color.white, BorderFactory.createLineBorder(new Color(0, 191, 255), 2));

        lblIX1.setBounds(560, 600, 40, 40);
        lblIX1.setFont(f1);
        
        ldIX1.setBounds(845, 600, 60, 40);
        styleButton(ldIX1, new Color(128, 93, 194), Color.white, f3);
        ldIX1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                IX1.setText(simulator.loadIX1());
                resetInputInstruction();
            }
        });

        IX2.setDocument(new JTextFieldLimit(16));
        IX2.setEditable(false);
        IX2.setBounds(595, 650, 240, 40);
        IX2.setColumns(16);
        styleTextField(IX2, f2, new Color(255, 127, 80), Color.white, BorderFactory.createLineBorder(new Color(0, 191, 255), 2));


        lblIX2.setBounds(560, 650, 40, 40);
        lblIX2.setFont(f1);
        
        ldIX2.setBounds(845, 650, 60, 40);
        styleButton(ldIX2, new Color(128, 93, 194), Color.white, f3);
        ldIX2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                IX2.setText(simulator.loadIX2());
                resetInputInstruction();
            }
        });

        IX3.setDocument(new JTextFieldLimit(16));
        IX3.setEditable(false);
        IX3.setBounds(595, 700, 240, 40);
        IX3.setColumns(16);
        styleTextField(IX3, f2, new Color(255, 127, 80), Color.white, BorderFactory.createLineBorder(new Color(0, 191, 255), 2));


        lblIX3.setBounds(560, 700, 40, 40);
        lblIX3.setFont(f1);
        
        ldIX3.setBounds(845, 700, 60, 40);
        styleButton(ldIX3, new Color(128, 93, 194), Color.white, f3);
        ldIX3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                IX3.setText(simulator.loadIX3());
                resetInputInstruction();
            }
        });

        lblCC.setBounds(670, 110, 40, 40);
        lblCC.setFont(f1);
        

        CC0.setDocument(new JTextFieldLimit(1));
        CC0.setEditable(false);
        CC0.setBounds(700, 110, 40, 40);
        CC0.setColumns(3);
        styleTextField(CC0, f2, new Color(255, 127, 80), Color.white, BorderFactory.createLineBorder(new Color(0, 191, 255), 2));
        lblCC0.setBounds(705, 92, 30, 12);
        lblCC0.setFont(f1);
        
        CC1.setDocument(new JTextFieldLimit(1));
        CC1.setEditable(false);
        CC1.setBounds(750, 110, 40, 40);
        CC1.setColumns(3);
        styleTextField(CC1, f2, new Color(255, 127, 80), Color.white, BorderFactory.createLineBorder(new Color(0, 191, 255), 2));
        lblCC1.setBounds(755, 92, 30, 12);
        lblCC1.setFont(f1);
        
        CC2.setDocument(new JTextFieldLimit(1));
        CC2.setEditable(false);
        CC2.setBounds(800, 110, 40, 40);
        CC2.setColumns(3);
        styleTextField(CC2, f2, new Color(255, 127, 80), Color.white, BorderFactory.createLineBorder(new Color(0, 191, 255), 2));
        lblCC2.setBounds(805, 92, 30, 12);
        lblCC2.setFont(f1);
        
        CC3.setDocument(new JTextFieldLimit(1));
        CC3.setEditable(false);
        CC3.setBounds(850, 110, 40, 40);
        CC3.setColumns(3);
        styleTextField(CC3, f2, new Color(255, 127, 80), Color.white, BorderFactory.createLineBorder(new Color(0, 191, 255), 2));
        lblCC3.setBounds(855, 92, 30, 12);
        lblCC3.setFont(f1);
        
        printer.setDocument(new JTextFieldLimit(40));
        printer.setEditable(false);
        printer.setBounds(1000, 50, 400, 200);
        printer.setColumns(10);
        styleTextField(printer, f2, new Color(255, 127, 80), Color.white, BorderFactory.createLineBorder(new Color(0, 191, 255), 2));
        lblPrinter.setBounds(1180, 240, 90, 60);
        lblPrinter.setFont(f4);
        
        KeyBoard.setDocument(new JTextFieldLimit(100));
        printer.setEditable(true);
        KeyBoard.setBounds(150, 50, 400, 200);
        KeyBoard.setColumns(10);
        styleTextField(KeyBoard, f2, new Color(255, 127, 80), Color.white, BorderFactory.createLineBorder(new Color(0, 191, 255), 2));
        lblKeyBoard.setBounds(320, 240, 90, 60);
        lblKeyBoard.setFont(f4);
        
        KeyBoard.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                simulator.KeyBoardKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                simulator.KeyBoardKeyReleased(evt, KeyBoard);
            }
        });


        fullReset.setBounds(1180, 400, 120, 40);
        styleButton(fullReset, new Color(196, 161, 240), new Color(204, 65, 183), f3);
        fullReset.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fullReset();
            }
        });


        frame.add(inputInstruction); frame.add(lblInputInstruction); frame.add(submit); frame.add(msg);

        frame.add(PC); frame.add(lblPC); frame.add(ldPC);
        frame.add(MAR); frame.add(lblMAR); frame.add(ldMAR);
        frame.add(MBR); frame.add(lblMBR); frame.add(ldMBR);
        frame.add(IR); frame.add(lblIR); frame.add(ldIR);

        frame.add(store); frame.add(load); frame.add(ss); frame.add(run); frame.add(ipl); frame.add(fullReset);

        frame.add(GPR0); frame.add(lblGPR0); frame.add(ldGPR0);
        frame.add(GPR1); frame.add(lblGPR1); frame.add(ldGPR1);
        frame.add(GPR2); frame.add(lblGPR2); frame.add(ldGPR2);
        frame.add(GPR3); frame.add(lblGPR3); frame.add(ldGPR3);

        frame.add(IX1); frame.add(lblIX1); frame.add(ldIX1);
        frame.add(IX2); frame.add(lblIX2); frame.add(ldIX2);
        frame.add(IX3); frame.add(lblIX3); frame.add(ldIX3);

        frame.add(lblCC);
        frame.add(CC0); frame.add(lblCC0);
        frame.add(CC1); frame.add(lblCC1);
        frame.add(CC2); frame.add(lblCC2);
        frame.add(CC3); frame.add(lblCC3);

        frame.add(printer);
        frame.add(lblPrinter);

        frame.add(KeyBoard);
        frame.add(lblKeyBoard);


        frame.setLayout(null);
        frame.setVisible(true);
    }


    //FULL RESET OF BOTH UI AND CPU VALUES
    public void fullReset(){
        clearUI();
        this.simulator.reset();
        System.out.println("===========ALL CLEARED!==========");
    }

    //CLEARS ONLY TEXTFIELD VALUE
    public  void clearUI(){
        PC.setText("");
        MAR.setText("");
        MBR.setText("");
        IR.setText("");

        GPR0.setText("");
        GPR1.setText("");
        GPR2.setText("");
        GPR3.setText("");

        IX1.setText("");
        IX2.setText("");
        IX3.setText("");
    }

    //UPDATES TEXTFIELDS USING VALUES RETRIEVED FROM SIMULATOR
    public void updateUI(HashMap uiValues){

        System.out.println(uiValues.get("PC"));
        PC.setText((String) uiValues.get("PC"));
        MAR.setText((String) uiValues.get("MAR"));
        MBR.setText((String) uiValues.get("MBR"));
        IR.setText((String) uiValues.get("IR"));

        GPR0.setText((String) uiValues.get("GPR0"));
        GPR1.setText((String) uiValues.get("GPR1"));
        GPR2.setText((String) uiValues.get("GPR2"));
        GPR3.setText((String) uiValues.get("GPR3"));

        IX1.setText((String) uiValues.get("IX1"));
        IX2.setText((String) uiValues.get("IX2"));
        IX3.setText((String) uiValues.get("IX3"));

        CC0.setText((String) uiValues.get("CC0"));
        CC1.setText((String) uiValues.get("CC1"));
        CC2.setText((String) uiValues.get("CC2"));
        CC3.setText((String) uiValues.get("CC3"));

        printer.setText((String) uiValues.get("printer"));

    }

    //RESETS INPUT INSTRUCTION AS WELL AS VALIDATION MESSAGE
    private void resetInputInstruction(){
        inputInstruction.setText("");
        msg.setVisible(false);
    }



}
