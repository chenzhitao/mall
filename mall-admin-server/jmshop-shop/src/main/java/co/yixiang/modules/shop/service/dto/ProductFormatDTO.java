package co.yixiang.modules.shop.service.dto;

import lombok.Data;

import java.util.Map;

/**
 * @ClassName ProductFormatDTO
 * @Author hupeng <610796224@qq.com>
 * @Date 2019/10/12
 **/

@Data
public class ProductFormatDTO {


    private Double price;

    private Double wholesale;

    private Double cost;

    private Integer sales;

    private String pic;

    private String barCode;

    private String packaging;

    private Map<String, String> detail;

    private Boolean check;

    private Integer stock;

    private Double otPrice;


}
