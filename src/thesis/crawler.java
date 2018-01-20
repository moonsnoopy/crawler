package thesis;
import java.io.PrintWriter;
import java.net.URL;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;
import org.jsoup.nodes.Element;
public class crawler {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			Parsing();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void Parsing() throws Exception {
		int maxVolume = 10,fail = 0, success = 0;
		for (int volume =9;volume <=maxVolume;volume++){
			int issueIndex=0;
			for(int issue = 1;issue<=8;issue++){
				//if (volume ==70 ) volume++;4-5				
				issueIndex++;
				String issueStr= String.valueOf(issue);
				if(volume==8 && issue==2){
					issueStr = String.valueOf(issue)+"-"+String.valueOf(issue+1);
					issue++;
				}else if(volume==17 && issue==2){
					issueStr = String.valueOf(issue)+"-"+String.valueOf(issue+2);
					issue+=2;
				}
				System.out.println(String.valueOf(volume));
				URL url = new URL("http://www.sciencedirect.com/science/journal/09507051/"+String.valueOf(volume)+"/"+issueStr+"?sdc=1"); 
				Document doc =  Jsoup.parse(url, 300000000); 
				//Document doc = Jsoup.connect("http://www.sciencedirect.com/science?_ob=ArticleListURL&_method=list&_ArticleListID=-1203095891&_sort=r&_st=13&view=c&md5=a6bfd5a2f8f0775baf4c92a566ac81d1&searchtype=a").get();
				//String title = doc.title();
				Element content =  doc.getElementById("bodyMainResults");
				Elements articles =  content.getElementsByClass("article");
				PrintWriter writer = new PrintWriter("Knowledge_Based_Systems_volume"+volume+"_"+issueIndex+".txt", "UTF-8");
				Elements volumeHeader = doc.select("div.volumeHeader.skyScraper");
				String volumeText ="";			
				for(Element volumeElement : volumeHeader){volumeText+=volumeElement.text()+",";}
				System.out.println("volumeHeader :"+volumeText);
				writer.println("volumeHeader :"+volumeText);
				
				
				for (Element article : articles) {
					try {							
						Thread.sleep(3000);
						String title = article.select("li.title").select("a").text();        	 
						//String source = article.select("li.source").select("i").first().text();
						String author = article.select("li.authors").text();
						String link = article.select("li.title").select("a").attr("href");					
						writer.println("title :"+ title);
						writer.println("author :"+ author);
						writer.println("URL :"+link);
						if(!title.equals("Editorial Board")){						
							Document doc2 = Jsoup.connect(link).data("query", "Java")
		        			  .userAgent("Mozilla")
		        			  .cookie("auth", "token")
		        			  .timeout(300000000)
		        			  .get();
							Elements absElements = doc2.select("h2:containsOwn(Abstract)~*");
							String abs ="";						
							for (Element absElement : absElements){abs +=absElement.text();} 
							//System.out.println("abstract :"+ abs.length());
							Elements keywordElements = doc2.select("h2:containsOwn(Keywords)~*");
							String keywords ="";						
							for (Element keywordElement : keywordElements){
								keywords +=keywordElement.text()+";";
							}
							//System.out.println("keyword :"+ keywords);
							writer.println("abstract :"+ abs);
							writer.println("keyword :"+ keywords);
							writer.println("");
							
							}
						success ++;
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						fail++;
					}
					
	        	}
				writer.close();
			}
		}
        //Elements title = xmlDoc.select("div:title"); 
        //Elements happy = xmlDoc.select("h1#svTitle");  
             
        System.out.println("success:"+success+" fail:"+fail); 
         
 
    }
}
