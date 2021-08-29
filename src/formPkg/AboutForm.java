package formPkg;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Дмитрий on 23.11.2015.
 */
public class AboutForm extends JDialog {
    private JPanel pnl;
    private JTextArea txtAreaAbout;

    public AboutForm(Frame owner, String title){
        super(owner, title, true);
        setSize(585, 250);
        txtAreaAbout = new JTextArea("\n" +" Программа позволяет отслеживать появление новых сообщений в ветках форума сайта Onliner.by\n" +
                "\n" +
                " На вход программе подаётся значение URL стартовой страницы форума. При обнаружении\n"+
                " нового сообщения на форуме, программа оповещает пользователя и приостанавливает работу.\n"+
                " По завершении работы с программой, дата последнего сообщения сохраняется в файл Date.txt.\n"+
                "\n" +
                " Сообщение не будет отловлено, если дата и время его опубликования совпадают с предыдущим\n"+
                " сообщением, с точностью до минуты.\n"+
                "\n" +
                " author: Zhurik Alexey, 24.11.2015"
        );
        add(txtAreaAbout);
        setLocationRelativeTo(null);
    }
}
