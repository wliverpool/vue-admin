package wfm.example.back.wx.exception;

/**
 * 微信异常信息
 * @author 吴福明
 *
 */
public class WexinReqException extends Exception {

    private static final long serialVersionUID = 1L;

    public WexinReqException(String message){
        super(message);
    }

    public WexinReqException(Throwable cause)
    {
        super(cause);
    }

    public WexinReqException(String message,Throwable cause)
    {
        super(message,cause);
    }
}