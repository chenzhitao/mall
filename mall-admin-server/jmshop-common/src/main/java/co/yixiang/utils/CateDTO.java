package co.yixiang.utils;



import lombok.Data;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * <p>
 * 商城商品分类
 * </p>
 *
 * @author hupeng
 * @since 2019-09-08
 */
@Data
public class CateDTO implements Serializable {

    private static final long serialVersionUID = 1L;


    private Long id;
    /**
     * 上级分类编号
     */
    private Long pid;

    /**
     * 商品分类名称
     */
    private String cateName;

    /**
     * 缩略图url
     */
    private String pic;

    private List<CateDTO> children = new ArrayList<>();

}
