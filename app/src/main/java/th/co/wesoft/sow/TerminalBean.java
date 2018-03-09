package th.co.wesoft.sow;

/**
 * Created by USER275 on 8/18/2017.
 */

public class TerminalBean {
    /**
     * TermCode : T90
     * TermDesc : FAT Terminal 90
     * TermGroup : A
     * TermType : C
     * TermIP : 192.168.3.64
     * DispUse : N
     * DispIP :
     * PrinterUse : N
     * PrinterName :
     * ShopCode :
     * SwapShop : N
     * Mprinter : D
     * Mdisplay : D
     * TextXML :
     * Active : Y
     * TermCategory : F
     * ScrWelcome : Welcome Tablet
     * SlogOn : N
     * CompanyName : We Soft Co., Ltd
     * CardPolicy : F
     */

    private String TermCode;
    private String TermDesc;
    private String TermGroup;
    private String TermType;
    private String TermIP;
    private String DispUse;
    private String DispIP;
    private String PrinterUse;
    private String PrinterName;
    private String ShopCode;
    private String SwapShop;
    private String Mprinter;
    private String Mdisplay;
    private String TextXML;
    private String Active;
    private String TermCategory;
    private String ScrWelcome;
    private String SlogOn;
    private String CompanyName;
    private String CardPolicy;

    public final static String COLUMN_COMPANY_NAME = "CompanyName";
    public final static String COLUMN_TERM_DESC = "TermDesc";
    public final static String COLUMN_SRC_WELCOME = "ScrWelcome";
    public final static String COLUMN_TERM_TYPE = "TermType";
    public final static String COLUMN_TERM_CATEGORY = "TermCategory";
    public final static String COLUMN_ACTIVE = "Active";

    public String getTermCode() {
        return TermCode;
    }

    public void setTermCode(String TermCode) {
        this.TermCode = TermCode;
    }

    public String getTermDesc() {
        return TermDesc;
    }

    public void setTermDesc(String TermDesc) {
        this.TermDesc = TermDesc;
    }

    public String getTermGroup() {
        return TermGroup;
    }

    public void setTermGroup(String TermGroup) {
        this.TermGroup = TermGroup;
    }

    public String getTermType() {
        return TermType;
    }

    public void setTermType(String TermType) {
        this.TermType = TermType;
    }

    public String getTermIP() {
        return TermIP;
    }

    public void setTermIP(String TermIP) {
        this.TermIP = TermIP;
    }

    public String getDispUse() {
        return DispUse;
    }

    public void setDispUse(String DispUse) {
        this.DispUse = DispUse;
    }

    public String getDispIP() {
        return DispIP;
    }

    public void setDispIP(String DispIP) {
        this.DispIP = DispIP;
    }

    public String getPrinterUse() {
        return PrinterUse;
    }

    public void setPrinterUse(String PrinterUse) {
        this.PrinterUse = PrinterUse;
    }

    public String getPrinterName() {
        return PrinterName;
    }

    public void setPrinterName(String PrinterName) {
        this.PrinterName = PrinterName;
    }

    public String getShopCode() {
        return ShopCode;
    }

    public void setShopCode(String ShopCode) {
        this.ShopCode = ShopCode;
    }

    public String getSwapShop() {
        return SwapShop;
    }

    public void setSwapShop(String SwapShop) {
        this.SwapShop = SwapShop;
    }

    public String getMprinter() {
        return Mprinter;
    }

    public void setMprinter(String Mprinter) {
        this.Mprinter = Mprinter;
    }

    public String getMdisplay() {
        return Mdisplay;
    }

    public void setMdisplay(String Mdisplay) {
        this.Mdisplay = Mdisplay;
    }

    public String getTextXML() {
        return TextXML;
    }

    public void setTextXML(String TextXML) {
        this.TextXML = TextXML;
    }

    public String getActive() {
        return Active;
    }

    public void setActive(String Active) {
        this.Active = Active;
    }

    public String getTermCategory() {
        return TermCategory;
    }

    public void setTermCategory(String TermCategory) {
        this.TermCategory = TermCategory;
    }

    public String getScrWelcome() {
        return ScrWelcome;
    }

    public void setScrWelcome(String ScrWelcome) {
        this.ScrWelcome = ScrWelcome;
    }

    public String getSlogOn() {
        return SlogOn;
    }

    public void setSlogOn(String SlogOn) {
        this.SlogOn = SlogOn;
    }

    public String getCompanyName() {
        return CompanyName;
    }

    public void setCompanyName(String CompanyName) {
        this.CompanyName = CompanyName;
    }

    public String getCardPolicy() {
        return CardPolicy;
    }

    public void setCardPolicy(String CardPolicy) {
        this.CardPolicy = CardPolicy;
    }

}
