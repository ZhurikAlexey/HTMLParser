package mainPkg;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Алексей on 07.11.2015.
 */
public class Parser {

    static String strNewDate;
    static Date sOldDate = new Date();
    public static Boolean sIsWork = true;
    public static ImageIcon sImgIcon = null;

    public static String getLastMsgDate(String url){
        try {
            Document doc = Jsoup.connect(url).get();
            Elements msgpostdates = doc.getElementsByClass("msgpost-date");
            msgpostdates = msgpostdates.select("span[title]");
            return msgpostdates.last().text();
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public static String getForumTitle(String url){
        String title = " ";
        try {
            Document doc = Jsoup.connect(url).get();
            title = doc.select("meta[property=og:title]").first().attr("content");
        } catch (IOException e){
            JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        return title;
    }

    public static String getLastPageLink(String url) {
        Integer count, index;
        String span = "";
        try {
            Document doc = Jsoup.connect(url).get();
            Elements span1 = doc.select("ul.pages-fastnav");
            if (span1.size() > 0) {
                Element span4 = span1.first();
                Elements span2 = span4.select("li");
                count = span2.size();
                if (count >= 2)
                    index = count - 2;
                else
                    index = 0;
                Element span3 = span2.get(index);
                span = span3.select("a").attr("href");
                span = "http://forum.onliner.by/" + span;
            } else
                span = url;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return span;
    }

    public static Date ConvertDate(String aDate) {
        Map<String, String> dmap = new HashMap<String, String>();
        dmap.put("января", "01");
        dmap.put("февраля", "02");
        dmap.put("марта", "03");
        dmap.put("апреля", "04");
        dmap.put("мая", "05");
        dmap.put("июня", "06");
        dmap.put("июля", "07");
        dmap.put("августа", "08");
        dmap.put("сентября", "09");
        dmap.put("октября", "10");
        dmap.put("ноября", "11");
        dmap.put("декабря", "12");

        String[] datestr = aDate.split(" ");
        String month = " ";
        for (Map.Entry<String, String> entry : dmap.entrySet())
            if (datestr[1].equals(entry.getKey())) {
                month = entry.getValue();
                break;
            }

        String outDate = datestr[0] + " " + month + " " + datestr[2] + " " + datestr[3];

        Date resDate = new Date();
        try {
            resDate = new SimpleDateFormat("dd MM yyyy HH:mm").parse(outDate);
        } catch (ParseException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

        return resDate;
    }

    public static void DateTrap(URL aURL, JProgressBar aPrgBar, TrayIcon aTrayIcon){
        Thread myThread = new Thread(() -> {
            aPrgBar.setIndeterminate(true);
            while (sIsWork)
            {
                String vLastPageLink = getLastPageLink(aURL.toString());
                strNewDate = getLastMsgDate(vLastPageLink);
                Date newDate = Parser.ConvertDate(strNewDate);
                if (sOldDate.before(newDate)) {
                    //System.out.println("Yes!");
                    aPrgBar.setIndeterminate(false);
                    aTrayIcon.setToolTip("Stopped");
                    aTrayIcon.displayMessage("New Message!", strNewDate, TrayIcon.MessageType.INFO);
                    JOptionPane pane = new JOptionPane(strNewDate + System.getProperty("line.separator") + vLastPageLink + System.getProperty("line.separator") + "View page?",
                            JOptionPane.INFORMATION_MESSAGE, JOptionPane.YES_NO_OPTION, sImgIcon);
                    JDialog dialog = pane.createDialog(null);
                    dialog.setAlwaysOnTop(true); // причина извращений
                    dialog.setIconImage(sImgIcon.getImage());
                    dialog.setVisible(true);
                    if (Integer.valueOf(pane.getValue().toString()) == JOptionPane.YES_OPTION){
                        try {
                            Desktop.getDesktop().browse(new URI(vLastPageLink));
                        } catch (URISyntaxException | IOException ex) {
                            //It looks like there's a problem
                            ex.printStackTrace();
                        }
                    }
                    //System.out.println("Dialog was closed with button " + pane.getValue() + "JOptionPane.YES_OPTION" + JOptionPane.YES_OPTION);
                    /*if (JOptionPane.showConfirmDialog(null, strNewDate + System.getProperty("line.separator") + vLastPageLink + System.getProperty("line.separator") + "View page?", "New Message",
                                JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE) == JOptionPane.YES_OPTION){
                            try {
                                Desktop.getDesktop().browse(new URI(vLastPageLink));
                            } catch (URISyntaxException | IOException ex) {
                                //It looks like there's a problem
                            }
                        }*/
                    sOldDate = newDate;
                    return;
                }
            }
        });
        myThread.start();
    }

    public static void SaveDate(){
        String errTitle = "Error in SaveDate() method!";
        try{
            if (strNewDate != null) {
                BufferedWriter fw = new BufferedWriter(new FileWriter("Date.txt"));
                fw.write(strNewDate);
                fw.close();
            }
        } catch (FileNotFoundException e1){
            JOptionPane.showMessageDialog(null, e1.getMessage(), errTitle, JOptionPane.ERROR_MESSAGE);
        } catch (IOException e2){
            JOptionPane.showMessageDialog(null, e2.getMessage(), errTitle, JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void LoadDate(){
        String strOldDate;
        try {
            BufferedReader fr = new BufferedReader(new FileReader("Date.txt"));
            strOldDate = fr.readLine();
            fr.close();
            sOldDate = ConvertDate(strOldDate);
        } catch (FileNotFoundException e1){
            /* strOldDate by default equals currDate*/
        } catch (IOException e2){
            e2.printStackTrace();
        }
    }
}
