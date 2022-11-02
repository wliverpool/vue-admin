package wfm.example.back.wx.model.qywx;


/**
 * 文件消息
 * @author 吴福明
 *
 */
public class FileMessage extends BaseMessage {

    private Media file;

    public FileMessage() {
        super.msgtype = "file";
    }

    public FileMessage(Receiver receiver, String sender,Media file) {
        super(receiver, sender, "file");
        this.file = file;
    }

    public Media getFile() {
        return file;
    }

    public void setFile(Media file) {
        this.file = file;
    }

}
