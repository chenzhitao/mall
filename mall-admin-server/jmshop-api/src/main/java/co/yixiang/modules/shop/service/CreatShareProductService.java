package co.yixiang.modules.shop.service;

import co.yixiang.modules.activity.web.vo.YxStoreSeckillQueryVo;
import co.yixiang.modules.shop.entity.YxStoreProduct;

import java.awt.FontFormatException;
import java.io.IOException;

public interface CreatShareProductService {

    String creatProductPic(YxStoreProduct productDTO, String qrcode, String spreadPicName, String spreadPicPath, String apiUrl) throws IOException, FontFormatException;

    String creatSeckillPic(YxStoreSeckillQueryVo seckillDTO, String qrcode, String spreadPicName, String spreadPicPath, String apiUrl) throws IOException, FontFormatException;
}
