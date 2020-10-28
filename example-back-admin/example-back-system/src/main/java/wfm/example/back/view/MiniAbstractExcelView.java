package wfm.example.back.view;

import org.springframework.web.servlet.view.AbstractView;

import javax.servlet.http.HttpServletRequest;

/**
 * 抽象view类,用于导出excel操作
 * @author 吴福明
 */

public abstract class MiniAbstractExcelView extends AbstractView {

    private static final String CONTENT_TYPE = "application/vnd.ms-excel";

    protected static final String HSSF = ".xls";
    protected static final String XSSF = ".xlsx";

    public MiniAbstractExcelView()
    {
        setContentType("application/vnd.ms-excel");
    }

    protected boolean isIE(HttpServletRequest request)
    {
        return (request.getHeader("USER-AGENT").toLowerCase().indexOf("msie") > 0) || (request.getHeader("USER-AGENT").toLowerCase().indexOf("rv:11.0") > 0);
    }
}