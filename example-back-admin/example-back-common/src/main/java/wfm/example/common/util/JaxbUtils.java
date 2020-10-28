package wfm.example.common.util;

import lombok.extern.slf4j.Slf4j;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamWriter;
import java.io.ByteArrayOutputStream;

/**
 * javabean与xml转换工具类
 * @author 吴福明
 */

@Slf4j
public class JaxbUtils {

    /**
     * JavaBean转换成xml
     * @param obj
     * @return
     */
    public static String convertToXml(Object obj) {
        try {
            JAXBContext context = JAXBContext.newInstance(obj.getClass());
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
            marshaller.setProperty(Marshaller.JAXB_FRAGMENT, true);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            //注意jdk版本
            XMLOutputFactory xmlOutputFactory = XMLOutputFactory.newInstance();
            XMLStreamWriter xmlStreamWriter = xmlOutputFactory.createXMLStreamWriter(baos, (String) marshaller.getProperty(Marshaller.JAXB_ENCODING));
            xmlStreamWriter.writeStartDocument((String) marshaller.getProperty(Marshaller.JAXB_ENCODING), "1.0");
            marshaller.marshal(obj, xmlStreamWriter);
            xmlStreamWriter.writeEndDocument();
            xmlStreamWriter.close();
            return new String(baos.toString("UTF-8"));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 字符左补零
     *
     * @param code
     * @param len
     * @return
     */
    public static String codeAddOne(String code, int len) {
        Integer intHao = Integer.parseInt(code);
        String strHao = intHao.toString();
        while (strHao.length() < len) {
            strHao = "0" + strHao;
        }
        return strHao;
    }

}
