package mainPkg;

import formPkg.HTMLParserForm;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Alexey
 * Date: 01.11.2015
 * Time: 12:27:03
 * To change this template use File | Settings | File Templates.
 */
public class Main {

    public static void main(String args[]) {

        HTMLParserForm frm = new HTMLParserForm();
        frm.setVisible(true);

       /* Elements msgpostdate = new Elements();
        Elements span = new Elements();
        try {
            Document doc = Jsoup.connect("http://forum.onliner.by/viewtopic.php?t=5828475").get();
            msgpostdate = doc.getElementsByClass("msgpost-date");
        } catch (IOException e) {
            e.printStackTrace();
        }
        span = msgpostdate.select("span[title]");
        for (Element el : span) {
            //for (int i=0; i<content.size(); i++){
            System.out.println(el.html());
        }
        System.out.println(span.last().html());
        System.out.println("--------------------------------------------------------");
        for (String str : ParseFastNav("http://forum.onliner.by/viewtopic.php?t=11827806&start=40")) {
            System.out.println(str);
        }
        System.out.println("-------------------LastPageLink-------------------------------------");
        System.out.println(getLastPageLink("http://forum.onliner.by/viewtopic.php?t=5828475"));
        System.out.println("-------------------LastMsgDate-------------------------------------");
        System.out.println(getLastMsgDate(getLastPageLink("http://forum.onliner.by/viewtopic.php?t=5828475")));
        System.out.println("-------------------FormatLastMsgDate-------------------------------------");
        System.out.println(ConvertDate(getLastMsgDate(getLastPageLink("http://forum.onliner.by/viewtopic.php?t=5828475"))));*/
    }

    public static List<String> ParseFastNav(String url) {
        List<String> RefList = new ArrayList<String>(); // = new List<String>();
        //Elements span = new Elements();
        try {
            Document doc = Jsoup.connect(url).get();
            Element span1 = doc.select("ul.pages-fastnav").first();
            Elements span = span1.select("li  > a");//.first();
            for (Element el : span) {
                RefList.add(el.attr("href"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return RefList;
    }

}
