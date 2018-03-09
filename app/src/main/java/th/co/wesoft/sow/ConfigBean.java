package th.co.wesoft.sow;

/**
 * Created by USER275 on 14/7/2560.
 */

public class ConfigBean {
    String lang;
    String smb_host;
    String smb_user;
    String smb_pass;
    boolean is_use_video;
    String program_folder;
    String marquee_folder;
    String video_file;
    String image_file;
    String image_loop;
    String marquee_file;
    String marquee_loop;
    String prod_type;
    String wcf_host;
    String wcf_port;
    String delimiter;
    String delimiter_text;
    String encode_pwd;
    String socket_port;
    String theme;
    String pwd_touchlock;
    int client;
    String app_version;
    String app_code;
    long app_size;



    public final static String TABLE_NAME = "config";
    public final static String COLUMN_LANGUAGE = "LANGUAGE";
    public final static String COLUMN_SMB_HOST = "SMB_HOST";
    public final static String COLUMN_SMB_USER = "SMB_USER";
    public final static String COLUMN_SMB_PASS = "SMB_PASS";
    public final static String COLUMN_IS_USE_VDO = "IS_USE_VDO";
    public final static String COLUMN_FOLDER = "FOLDER";
    public final static String COLUMN_PROGRAM_FOLDER = "PROGRAM_FOLDER";
    public final static String COLUMN_MARQUEE_FOLDER = "MARQUEE_FOLDER";
    public final static String COLUMN_VDO_FILE = "VDO_FILE";
    public final static String COLUMN_IMAGE_FILE = "IMAGE_FILE";
    public final static String COLUMN_IMAGE_LOOP = "IMAGE_LOOP";
    public final static String COLUMN_MARQUEE_FILE = "MARQUEE_FILE";
    public final static String COLUMN_MARQUEE_LOOP = "MARQUEE_LOOP";
    public final static String COLUMN_MARQUEE_SPEED = "COLUMN_MARQUEE_SPEED";
    public final static String COLUMN_PROD_TYPE = "PROD_TYPE";
    public final static String COLUMN_WCF_HOST = "WCF_HOST";
    public final static String COLUMN_WCF_PORT = "WCF_PORT";
    public final static String COLUMN_DELIMITER = "DELIMITER";
    public final static String COLUMN_DELIMITER_TEXT = "DELIMITER_TEXT";
    public final static String COLUMN_ENCODE_PWD = "ENCODE_PWD";
    public final static String COLUMN_SOCKET_PORT = "SOCKET_PORT";
    public final static String COLUMN_THEME = "THEME";
    public final static String COLUMN_PWD_TOUCHLOCK = "PWD_TOUCHLOCK";
    public final static String COLUMN_CLIENT = "client";
    public final static String COLUMN_APP_VERSION = "app_version";
    public final static String COLUMN_APP_CODE = "app_code";
    public final static String COLUMN_APP_SIZE = "app_size";

    public ConfigBean(int client, String app_version, String app_code, long app_size) {
        this.client = client;
        this.app_version = app_version;
        this.app_code = app_code;
        this.app_size = app_size;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getSmb_host() {
        return smb_host;
    }

    public void setSmb_host(String smb_host) {
        this.smb_host = smb_host;
    }

    public String getSmb_user() {
        return smb_user;
    }

    public void setSmb_user(String smb_user) {
        this.smb_user = smb_user;
    }

    public String getSmb_pass() {
        return smb_pass;
    }

    public void setSmb_pass(String smb_pass) {
        this.smb_pass = smb_pass;
    }

    public boolean getIs_use_video() {
        return is_use_video;
    }

    public void setIs_use_video(boolean is_use_video) {
        this.is_use_video = is_use_video;
    }

    public boolean is_use_video() {
        return is_use_video;
    }

    public String getProgram_folder() {
        return program_folder;
    }

    public void setProgram_folder(String program_folder) {
        this.program_folder = program_folder;
    }

    public String getMarquee_folder() {
        return marquee_folder;
    }

    public void setMarquee_folder(String marquee_folder) {
        this.marquee_folder = marquee_folder;
    }

    public String getVideo_file() {
        return video_file;
    }

    public void setVideo_file(String video_file) {
        this.video_file = video_file;
    }

    public String getImage_file() {
        return image_file;
    }

    public void setImage_file(String image_file) {
        this.image_file = image_file;
    }

    public String getImage_loop() {
        return image_loop;
    }

    public void setImage_loop(String image_loop) {
        this.image_loop = image_loop;
    }

    public String getMarquee_file() {
        return marquee_file;
    }

    public void setMarquee_file(String marquee_file) {
        this.marquee_file = marquee_file;
    }

    public String getMarquee_loop() {
        return marquee_loop;
    }

    public void setMarquee_loop(String marquee_loop) {
        this.marquee_loop = marquee_loop;
    }

    public String getProd_type() {
        return prod_type;
    }

    public void setProd_type(String prod_type) {
        this.prod_type = prod_type;
    }

    public String getWcf_host() {
        return wcf_host;
    }

    public void setWcf_host(String wcf_host) {
        this.wcf_host = wcf_host;
    }

    public String getWcf_port() {
        return wcf_port;
    }

    public void setWcf_port(String wcf_port) {
        this.wcf_port = wcf_port;
    }

    public String getDelimiter() {
        return delimiter;
    }

    public void setDelimiter(String delimiter) {
        this.delimiter = delimiter;
    }

    public String getDelimiter_text() {
        return delimiter_text;
    }

    public void setDelimiter_text(String delimiter_text) {
        this.delimiter_text = delimiter_text;
    }

    public String getSocket_port() {
        return socket_port;
    }

    public String getEncode_pwd() {
        return encode_pwd;
    }

    public void setEncode_pwd(String encode_pwd) {
        this.encode_pwd = encode_pwd;
    }

    public void setSocket_port(String socket_port) {
        this.socket_port = socket_port;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public String getPwd_touchlock() {
        return pwd_touchlock;
    }

    public void setPwd_touchlock(String pwd_touchlock) {
        this.pwd_touchlock = pwd_touchlock;
    }

    public String getApp_version() {
        return app_version;
    }

    public void setApp_version(String app_version) {
        this.app_version = app_version;
    }

    public String getApp_code() {
        return app_code;
    }

    public void setApp_code(String app_code) {
        this.app_code = app_code;
    }

    public long getApp_size() {
        return app_size;
    }

    public void setApp_size(long app_size) {
        this.app_size = app_size;
    }

    public int getClient() {
        return client;
    }

    public void setClient(int client) {
        this.client = client;
    }
}
