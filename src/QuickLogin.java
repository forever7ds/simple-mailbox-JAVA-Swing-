import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;

//һ����ȡ����ĵ�¼��Ϣ���ļ���д��

public class QuickLogin {
     
    private String fileName = "";
    public String AllData = "";
    
    public QuickLogin() {
    	this.fileName = System.getProperty("user.dir") + "\\Data\\data.txt";
		System.out.println(fileName);
    }
    
    //�õ�String���ͱ����洢�Ŷ�ȡ������Ϣ
    public String getAllData() {    
        //��ȡ�ļ�
        BufferedReader br = null;
        StringBuffer sb = null;
        try {
            br = new BufferedReader(new InputStreamReader(new FileInputStream(fileName),"GBK")); //������Կ��Ʊ���
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