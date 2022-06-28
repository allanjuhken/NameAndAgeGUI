import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class NameAndAgeGUI extends JFrame {
    /**
     * Põhi paneel
     */
    private JPanel pnlMain;
    /**
     * Nime küsimise paneel
     */
    private JPanel pnlName;
    /**
     * Vanuse küsimise paneel
     */
    private JPanel pnlAge;
    /**
     * Nime tekstiväli
     */
    private JTextField txtName;
    /**
     * Vanuse tekstiväli
     */
    private JTextField txtAge;
    /**
     * Nime välja silt
     */
    private JLabel lblName;
    /**
     * Vanuse välja silt
     */
    private JLabel lblAge;
    /**
     * Faili lisamise nupp
     */
    private JButton btnAddToFile;
    /**
     * Faili kuvamise nupp
     */
    private JButton btnFileContent;
    /**
     * Faili lisamise paneel
     */
    private JPanel pnlAdd;
    /**
     * Faili kuvamise paneel
     */
    private JPanel pnlShow;
    private String name;
    private int age;
    private String filename = "Personsdata.txt";
    private List<NameAndAgeGUI> personsInfo;



    private NameAndAgeGUI() {
        this.setTitle("'Nimi ja vanus' tekstifaili loomiseks ja vaatamiseks");
        this.setPreferredSize(new Dimension(450, 250));
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        createPanelName();
        createPanelAge();
        createPanelAdd();
        createPanelShow();
        createPanelMain();
        createTextFieldName();
        createTextFieldAge();
        setupLabelName();
        setupLabelAge();
        setupButtonAddToFile();
        setupButtonFileContent();
        clearFields();
        addToFile();

        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    public NameAndAgeGUI(String name, int age) throws HeadlessException {
        this.name = name;
        this.age = age;
    }

    private void createPanelName() {
        pnlName = new JPanel();
        pnlName.setBackground(Color.YELLOW);
    }

    private void createPanelAge() {
        pnlAge = new JPanel();
        pnlAge.setBackground(Color.GREEN);
    }

    private void createPanelAdd() {
        pnlAdd = new JPanel();
        pnlAdd.setBackground(Color.MAGENTA);
    }

    private void createPanelShow() {
        pnlShow = new JPanel();
        pnlShow.setBackground(Color.ORANGE);
    }

    private void createPanelMain() {
        pnlMain = new JPanel(new FlowLayout());
        pnlMain.setBackground(Color.CYAN);
        pnlMain.setBorder(new EmptyBorder(5, 5, 5, 5));
        pnlMain.add(pnlName);
        pnlMain.add(pnlAge);
        pnlMain.add(pnlAdd);
        pnlMain.add(pnlShow);
        this.add(pnlMain);
    }

    private void setupLabelName() {
        lblName = new JLabel("Sisesta nimi(min 2 tähemärki)");
        pnlName.add(lblName);
    }

    private void setupLabelAge() {
        lblAge = new JLabel("Sisesta vanus(max 99 aastat)");
        pnlAge.add(lblAge);
    }

    private void createTextFieldName() {
        txtName = new JTextField("", 20);
        txtName.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (txtName.getText().length() >= 20)
                    e.consume();
            }
        });
        txtName.setToolTipText("Nime pikkus max 20 tähemärki");
        pnlName.add(txtName);
    }

    private void createTextFieldAge() {
        txtAge = new JTextField(20);
        txtAge.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (txtAge.getText().length() >= 2)
                    e.consume();
                char c = e.getKeyChar();
                if (((c < '0') || (c > '9')) && (c != KeyEvent.VK_BACK_SPACE)) {
                    e.consume();
                }
            }
        });
        txtAge.setToolTipText("Sisestada saad ainult numbreid, max vanus 99 aastat");
        pnlAge.add(txtAge);
    }

    public JTextField getTxtName() {
        return txtName;
    }

    public JTextField getTxtAge() {
        return txtAge;
    }

    private void clearFields() {
        txtName.setText(null);
        txtAge.setText(null);
    }

    private void setupButtonAddToFile() {
        btnAddToFile = new JButton("Lisa faili");
        btnAddToFile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //JOptionPane.showMessageDialog(null,"klikiti Lisa faili nuppu"); // Test
                addToFile();
                clearFields();
            }
        });
        pnlAdd.add(btnAddToFile);
    }

    private void setupButtonFileContent() {
        btnFileContent = new JButton("Näita faili sisu");
        btnFileContent.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //JOptionPane.showMessageDialog(null,"klikiti Näita faili nuppu"); // Test
                try{
                    readFromFile();
                    JDialog dialog = new JDialog();
                    createPersonsTable(dialog);
                    dialog.setModal(true);
                    dialog.pack();
                    dialog.setLocationRelativeTo(null);
                    dialog.setVisible(true);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        pnlShow.add(btnFileContent);
    }

    private void addToFile() { // Kirjuta JTextfield sisestused tekstifaili "Parsonsdata.txt"
        String txtName = getTxtName().getText();
        String txtAge = getTxtAge().getText();
        String defaultAge = "0";
        if (txtName != null && !txtName.isEmpty() && txtName.length() >= 2 && txtAge.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vanust ei ole sisestatud, vanuseks määratakse 0(null)");
            writeToFile(txtName, defaultAge);
        } else if (txtAge != null && !txtAge.isEmpty() && txtName.length() >= 2 && !txtAge.isEmpty()) {
            writeToFile(txtName, txtAge);
        }
    }
    private void writeToFile(String txtName, String txtAge) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("Personsdata.txt", true))) {
            String line = txtName + ";" + txtAge;
            bw.write(line);
            bw.newLine();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Personsdata faili ei leitud");
        }
    }

    private boolean readFromFile() throws IOException {
        personsInfo = new ArrayList<>();
        File f = new File(filename);
        if (f.exists()) {
            if (f.length() > 0) {
                try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
                    for (String line; (line = br.readLine()) != null; ) {
                        String name = line.split(";")[0];
                        int age = Integer.parseInt(line.split(";")[1]);
                        personsInfo.add(new NameAndAgeGUI(name, age));
                    }
                    return true;
                }
            }
        } else {
            f.createNewFile(); // kui faili pole siis see luuakse
            JOptionPane.showMessageDialog(this, "Loodi Personsdata fail");
        }
        return false;
    }

    @Override
    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public void createPersonsTable(JDialog dialog) { // Tabel tekstifaili sisu jaoks
            String[] columName = new String[]{"Nimi", "Vanus"};
            String[][] data = new String[personsInfo.size()][2];
            for(int x = 0; x < personsInfo.size(); x++) {
                data[x][0] = personsInfo.get(x).getName();
                data[x][1] = String.valueOf(personsInfo.get(x).getAge());
            }
            JTable table = new JTable(data, columName);
            dialog.add(new JScrollPane(table));
            dialog.setTitle("Persons Info");
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new NameAndAgeGUI();
            }
        });
    }
}




