import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;

//一个读取缓存的登录信息的文件读写类

public class QuickLogin {
     
    private String fileName = "";
    public String AllData = "";
    
    public QuickLogin() {
    	this.fileName = System.getProperty("user.dir") + "\\Data\\data.txt";
		System.out.println(fileName);
    }
    
    //得到String类型变量存储着读取到的信息
    public String getAllData() {    
        //读取文件
        BufferedReader br = null;
        StringBuffer sb = null;
        try {
            br = new BufferedReader(new InputStreamReader(new FileInputStream(fileName),"GBK")); //这里可以控制编码
            sb = new StringBuffer();
            String line = null;
            while((line = br.readLine()) != null) {
                sb.append(line);
                sb.append("\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                br.close();
            } catch (Exception e) {
                e.printStackTrace();
            }   
        }
         
        AllData = new String(sb); 
        //System.out.println("" + AllData);
        return AllData;
             
    }
     
}