package wfm.example.common.dto;

import lombok.Data;

/**
 * fastdfs文件dto类
 * @author 吴福明
 */

@Data
public class FastDFSFileDto {

    /**
     * 文件名称
     */
    private String name;

    /**
     * 内容
     */
    private byte[] content;

    /**
     * 文件类型
     */
    private String ext;

    /**
     * md5值
     */
    private String md5;

    /**
     * 作者
     */
    private String author;

    public FastDFSFileDto(String name, byte[] content, String ext, String height,
                          String width, String author) {
        super();
        this.name = name;
        this.content = content;
        this.ext = ext;
        this.author = author;
    }
    public FastDFSFileDto(String name, byte[] content, String ext) {
        super();
        this.name = name;
        this.content = content;
        this.ext = ext;
    }

}