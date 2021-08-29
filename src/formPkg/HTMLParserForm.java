package formPkg;

import mainPkg.Constants;
import mainPkg.Parser;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Алексей on 07.11.2015.
 */
public class HTMLParserForm extends JFrame {
    private JTextField tfldAdress;
    private JLabel lblAdress;
    private JProgressBar progressBar;
    private JButton btnStart;
    private JButton btnStop;
    private JMenuItem miAbout;

    private TrayIcon trayIcon;

    public void initUI(){
        setTitle("Onliner forums parser");
        setIconImage(Parser.sImgIcon.getImage());
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        setSize(500, 142);
        setLocation(500, 500);
        btnStart.setPreferredSize(new Dimension(64, 320));
        progressBar.setStringPainted(true);
        Container container = this.getContentPane();
        GroupLayout layout = new GroupLayout(container);
        container.setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);
        layout.setHorizontalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGroup(layout.createSequentialGroup()
                            .addComponent(lblAdress)
                            .addComponent(tfldAdress))
                    .addComponent(btnStart))
                .addGroup(layout.createSequentialGroup()
                    .addComponent(progressBar)
                    .addComponent(btnStop))
        );
        layout.setVerticalGroup(
            layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                            .addComponent(lblAdress)
                            .addComponent(tfldAdress))
                    .addComponent(btnStart))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(progressBar)
                    .addComponent(btnStop))
        );
        layout.linkSize(SwingConstants.CENTER, btnStart, btnStop);
        setLocationRelativeTo(null);
        JMenuBar mb = new JMenuBar();
        JMenu mHelp = new JMenu("Help");
        miAbout = new JMenuItem("About");
        mHelp.add(miAbout);
        mb.add(mHelp);
        setJMenuBar(mb);
    }

    public HTMLParserForm() {
        // TODO: иначе в JAR не отображаются иконки
        Parser.sImgIcon = new ImageIcon(getClass().getClassLoader().getResource(Constants.IMG_PATH));
        initUI();
        Parser.LoadDate();

        //checking for support
        if(!SystemTray.isSupported()){
            System.out.println("System tray is not supported !!! ");
            return ;
        }
        //get the systemTray of the system
        SystemTray systemTray = SystemTray.getSystemTray();
        //popupmenu
        PopupMenu trayPopupMenu = new PopupMenu();

        MenuItem close = new MenuItem("Exit");
        close.addActionListener(e -> System.exit(0));
        trayPopupMenu.add(close);
        //setting tray icon
        trayIcon = new TrayIcon(Parser.sImgIcon.getImage(), "Stopped", trayPopupMenu);
        //adjust to default size as per system recommendation
        trayIcon.setImageAutoSize(true);
        trayIcon.addActionListener(e -> {
            HTMLParserForm.this.setVisible(true);
            HTMLParserForm.this.setExtendedState(HTMLParserForm.NORMAL);
            systemTray.remove(trayIcon);
        });

        addWindowListener(new WindowAdapter() {

            @Override
            public void windowIconified(WindowEvent e) {
                HTMLParserForm.this.setVisible(false);
                try {
                    systemTray.add(trayIcon);
                } catch (AWTException e1) {
                    e1.printStackTrace();
                }
            }
        });

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (JOptionPane.showConfirmDialog(null, "exit", "Onliner forums parser", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION ){
                    Parser.SaveDate();
                    System.exit(0);
                }
            }
        });

        btnStart.addActionListener(e -> {
            trayIcon.setToolTip("Working...");
            String vstrURL;
            vstrURL = tfldAdress.getText();
            // check url
            URL vURL = null;
            try {
                vURL = new URL(vstrURL);
            } catch (MalformedURLException eUrl) {
                JOptionPane.showMessageDialog(null,eUrl.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            Parser.sIsWork = true;
            progressBar.setString(Parser.getForumTitle(vURL.toString()));
            btnStart.setEnabled(false);
            btnStop.setEnabled(true);
            Parser.DateTrap(vURL, progressBar, trayIcon);
        });

        btnStop.addActionListener(e -> {
            Parser.sIsWork = false;
            progressBar.setIndeterminate(false);
            trayIcon.setToolTip("Stopped");
            btnStart.setEnabled(true);
            btnStop.setEnabled(false);
        });

        miAbout.addActionListener(e -> {
            AboutForm af = new AboutForm(null,"About");
            af.setVisible(true);
        });
    }

}
