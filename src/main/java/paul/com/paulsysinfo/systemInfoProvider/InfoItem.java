package paul.com.paulsysinfo.systemInfoProvider;

/**
 * Created by cheolgyoon on 2016. 5. 11..
 */
public class InfoItem {
    public InfoItem() {
    }

    public InfoItem(String name, String value) {
        this.name = name;
        if(value == null){
            this.value = "";
        }else {
            this.value = value;
        }
    }

   public String name;
   public String value;
}
