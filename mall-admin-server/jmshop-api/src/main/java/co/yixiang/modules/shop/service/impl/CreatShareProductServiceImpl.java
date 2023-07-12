package co.yixiang.modules.shop.service.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ObjectUtil;
import co.yixiang.modules.activity.web.vo.YxStoreSeckillQueryVo;
import co.yixiang.modules.shop.entity.YxStoreProduct;
import co.yixiang.modules.shop.service.CreatShareProductService;
import co.yixiang.modules.user.entity.YxSystemAttachment;
import co.yixiang.modules.user.service.YxSystemAttachmentService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static co.yixiang.utils.FileUtil.transformStyle;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CreatShareProductServiceImpl implements CreatShareProductService {

    private final YxSystemAttachmentService systemAttachmentService;

    @Override
    public String creatProductPic(YxStoreProduct productDTO, String shareCode, String spreadPicName, String spreadPicPath, String apiUrl) throws IOException, FontFormatException {
        YxSystemAttachment attachmentT = systemAttachmentService.getInfo(spreadPicName);
        String spreadUrl = "";
//        if (ObjectUtil.isNull(attachmentT)) {
        //创建图片
        BufferedImage img = new BufferedImage(750, 1334 + 160, BufferedImage.TYPE_INT_RGB);
        //开启画图
        Graphics g = img.getGraphics();
        //背景 -- 读取互联网图片
        InputStream stream = getClass().getClassLoader().getResourceAsStream("background.png");
        ImageInputStream background = ImageIO.createImageInputStream(stream);
        BufferedImage back = ImageIO.read(background);

        g.drawImage(back.getScaledInstance(750, 1334 + 160, Image.SCALE_DEFAULT), 0, 0, null); // 绘制缩小后的图
        //商品  banner图
        //读取互联网图片
        BufferedImage priductUrl;
        String transformStyle = transformStyle(productDTO.getImage());
        priductUrl = ImageIO.read(new URL(transformStyle));
           /* try {
                String transformStyle = transformStyle(productDTO.getImage());
                boolean isurl = false;
                String regex = "(((https|http)?://)?([a-z0-9]+[.])|(www.))"
                        + "\\w+[.|\\/]([a-z0-9]{0,})?[[.]([a-z0-9]{0,})]+((/[\\S&&[^,;\u4E00-\u9FA5]]+)+)?([.][a-z0-9]{0,}+|/?)";//设置正则表达式

                Pattern pat = Pattern.compile(regex.trim());//比对
                Matcher mat = pat.matcher(transformStyle.trim());
                isurl = mat.matches();//判断是否匹配
                if (isurl) {
                    priductUrl = ImageIO.read(new URL(transformStyle));
                }else {
                    stream =  getClass().getClassLoader().getResourceAsStream("background.png");
                    background = ImageIO.createImageInputStream(stream);
                    priductUrl = ImageIO.read(background);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }*/
        g.drawImage(priductUrl.getScaledInstance(750, 590 + 160, Image.SCALE_DEFAULT), 0, 0, null);
        InputStream streamT = getClass().getClassLoader()
                .getResourceAsStream("Alibaba-PuHuiTi-Regular.otf");
        File newFileT = new File("Alibaba-PuHuiTi-Regular.otf");
        FileUtils.copyInputStreamToFile(streamT, newFileT);
        //Font font = new Font("黑体", Font.BOLD, 60);
        Font font = Font.createFont(Font.TRUETYPE_FONT, newFileT);
        //文案标题
        g.setFont(font.deriveFont(Font.BOLD, 34));
        g.setColor(new Color(29, 29, 29));
        int fontlenb = getWatermarkLength(productDTO.getStoreName(), g);
        //文字长度相对于图片宽度应该有多少行
        int lineb = fontlenb / (back.getWidth() + 200);
        //高度
        int yb = back.getHeight() - (lineb + 1) * 30 + 100;
        //文字叠加,自动换行叠加
        int tempXb = 32;
        int tempYb = yb + 160;
        //单字符长度
        int tempCharLenb = 0;
        //单行字符总长度临时计算
        int tempLineLenb = 0;
        StringBuffer sbb = new StringBuffer();
        for (int i = 0; i < productDTO.getStoreName().length(); i++) {
            char tempChar = productDTO.getStoreName().charAt(i);
            tempCharLenb = getCharLen(tempChar, g);
            tempLineLenb += tempCharLenb;
            if (tempLineLenb >= (back.getWidth() + 220)) {
                //长度已经满一行,进行文字叠加
                g.drawString(sbb.toString(), tempXb, tempYb + 50);
                //清空内容,重新追加
                sbb.delete(0, sbb.length());
                //每行文字间距50
                tempYb += 50;
                tempLineLenb = 0;
            }
            //追加字符
            sbb.append(tempChar);
        }
        g.drawString(sbb.toString(), tempXb, tempYb + 50);

        //------------------------------------------------文案-----------------------
        //文案
        g.setFont(font.deriveFont(Font.PLAIN, 30));
        g.setColor(new Color(47, 47, 47));
        int fontlen = getWatermarkLength(productDTO.getStoreInfo(), g);
        //文字长度相对于图片宽度应该有多少行
        int line = fontlen / (back.getWidth() - 90);
        //高度
        int y = tempYb + 50 - (line + 1) * 30 + 100;
        //文字叠加,自动换行叠加
        int tempX = 32;
        int tempY = y + 50;
        //单字符长度
        int tempCharLen = 0;
        //单行字符总长度临时计算
        int tempLineLen = 0;
        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < productDTO.getStoreInfo().length(); i++) {
            char tempChar = productDTO.getStoreInfo().charAt(i);
            tempCharLen = getCharLen(tempChar, g);
            tempLineLen += tempCharLen;
            if (tempLineLen >= (back.getWidth() - 90)) {
                //长度已经满一行,进行文字叠加
                g.drawString(sb.toString(), tempX, tempY + 50);
                //清空内容,重新追加
                sb.delete(0, sb.length());
                //每行文字间距50
                tempY += 50;
                tempLineLen = 0;
            }
            //追加字符
            sb.append(tempChar);
        }
        //最后叠加余下的文字
        g.drawString(sb.toString(), tempX, tempY + 50);

        //价格背景
        //读取互联网图片
        BufferedImage bground = null;//
        InputStream redStream = getClass().getClassLoader().getResourceAsStream("red.jpg");
        try {
            ImageInputStream red = ImageIO.createImageInputStream(redStream);
            bground = ImageIO.read(red);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 绘制缩小后的图
        g.drawImage(bground.getScaledInstance(160, 40, Image.SCALE_DEFAULT), 30, 1053 + 160, null);

        g.setFont(font.deriveFont(Font.PLAIN, 26));
        g.setColor(new Color(47, 47, 47));
        g.drawString("销量："+(productDTO.getSales()+productDTO.getFicti())+productDTO.getUnitName(), 50, 1080+100);
        //限时促销价
        g.setFont(font.deriveFont(Font.PLAIN, 24));
        g.setColor(new Color(255, 255, 255));
        g.drawString("会 员 价", 50, 1080 + 160);

        //价格
        g.setFont(font.deriveFont(Font.PLAIN, 50));
        g.setColor(new Color(249, 64, 64));
        g.drawString("¥" + productDTO.getOtPrice(), 29, 1162 + 160);

        //原价
        g.setFont(font.deriveFont(Font.PLAIN, 36));
        g.setColor(new Color(171, 171, 171));
        String price = "¥" + productDTO.getPrice();
        g.drawString(price, 260, 1160 + 160);
        g.drawLine(250, 1148, 260 + 150, 1148 + 160);

//            //商品名称
//            g.setFont(font.deriveFont(Font.PLAIN,32));
//            g.setColor(new Color(29,29,29));
//            g.drawString(productDTO.getStoreName(), 30, 1229);

        //生成二维码返回链接
        String url = shareCode;
        //读取互联网图片
        BufferedImage qrCode = null;
        try {
            qrCode = ImageIO.read(new URL(url));
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 绘制缩小后的图
        g.drawImage(qrCode.getScaledInstance(174, 174, Image.SCALE_DEFAULT), 536, 1057 + 160, null);

        //二维码字体
        g.setFont(font.deriveFont(Font.PLAIN, 25));
        g.setColor(new Color(171, 171, 171));
        //绘制文字
        g.drawString("扫描或长按小程序码", 515, 1260 + 160);

        g.dispose();
        //先将画好的海报写到本地
        File file = new File(spreadPicPath);
        try {
            ImageIO.write(img, "jpg", file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        systemAttachmentService.attachmentAdd(spreadPicName,
                String.valueOf(FileUtil.size(new File(spreadPicPath))),
                spreadPicPath, "qrcode/" + spreadPicName);
        spreadUrl = apiUrl + "/api/file/qrcode/" + spreadPicName;
        //保存到本地 生成文件名字
//        } else {
//            spreadUrl = apiUrl + "/api/file/" + attachmentT.getSattDir();
//        }
        return spreadUrl;
    }


    @Override
    public String creatSeckillPic(YxStoreSeckillQueryVo seckillDTO, String shareCode, String spreadPicName, String spreadPicPath, String apiUrl) throws IOException, FontFormatException {
        YxSystemAttachment attachmentT = systemAttachmentService.getInfo(spreadPicName);
        String spreadUrl = "";
        if (ObjectUtil.isNull(attachmentT)) {
            //创建图片
            BufferedImage img = new BufferedImage(750, 1334, BufferedImage.TYPE_INT_RGB);
            //开启画图
            Graphics g = img.getGraphics();
            //背景 -- 读取互联网图片
            InputStream stream = getClass().getClassLoader().getResourceAsStream("background.png");
            ImageInputStream background = ImageIO.createImageInputStream(stream);
            BufferedImage back = ImageIO.read(background);

            g.drawImage(back.getScaledInstance(750, 1334, Image.SCALE_DEFAULT), 0, 0, null); // 绘制缩小后的图
            //商品  banner图
            //读取互联网图片
            BufferedImage priductUrl;

            String transformStyle = transformStyle(seckillDTO.getImage());
            priductUrl = ImageIO.read(new URL(transformStyle));

            /*try {
                String transformStyle = transformStyle(seckillDTO.getImage());
                boolean isurl;
                String regex = "(((https|http)?://)?([a-z0-9]+[.])|(www.))"
                        + "\\w+[.|\\/]([a-z0-9]{0,})?[[.]([a-z0-9]{0,})]+((/[\\S&&[^,;\u4E00-\u9FA5]]+)+)?([.][a-z0-9]{0,}+|/?)";//设置正则表达式

                Pattern pat = Pattern.compile(regex.trim());//比对
                Matcher mat = pat.matcher(transformStyle.trim());
                isurl = mat.matches();//判断是否匹配
                if (isurl) {
                    priductUrl = ImageIO.read(new URL(transformStyle));
                }else {
                    stream =  getClass().getClassLoader().getResourceAsStream("background.png");
                    background = ImageIO.createImageInputStream(stream);
                    priductUrl = ImageIO.read(background);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }*/
            g.drawImage(priductUrl.getScaledInstance(750, 590, Image.SCALE_DEFAULT), 0, 0, null);
            InputStream streamT = getClass().getClassLoader()
                    .getResourceAsStream("Alibaba-PuHuiTi-Regular.otf");
            File newFileT = new File("Alibaba-PuHuiTi-Regular.otf");
            FileUtils.copyInputStreamToFile(streamT, newFileT);
            //Font font = new Font("黑体", Font.BOLD, 60);
            Font font = Font.createFont(Font.TRUETYPE_FONT, newFileT);
            //文案标题
            g.setFont(font.deriveFont(Font.BOLD, 34));
            g.setColor(new Color(29, 29, 29));
            int fontlenb = getWatermarkLength(seckillDTO.getTitle(), g);
            //文字长度相对于图片宽度应该有多少行
            int lineb = fontlenb / (back.getWidth() + 200);
            //高度
            int yb = back.getHeight() - (lineb + 1) * 30 + 100;
            //文字叠加,自动换行叠加
            int tempXb = 32;
            int tempYb = yb;
            //单字符长度
            int tempCharLenb = 0;
            //单行字符总长度临时计算
            int tempLineLenb = 0;
            StringBuffer sbb = new StringBuffer();
            for (int i = 0; i < seckillDTO.getTitle().length(); i++) {
                char tempChar = seckillDTO.getTitle().charAt(i);
                tempCharLenb = getCharLen(tempChar, g);
                tempLineLenb += tempCharLenb;
                if (tempLineLenb >= (back.getWidth() + 220)) {
                    //长度已经满一行,进行文字叠加
                    g.drawString(sbb.toString(), tempXb, tempYb + 50);
                    //清空内容,重新追加
                    sbb.delete(0, sbb.length());
                    //每行文字间距50
                    tempYb += 50;
                    tempLineLenb = 0;
                }
                //追加字符
                sbb.append(tempChar);
            }
            g.drawString(sbb.toString(), tempXb, tempYb + 50);

            //------------------------------------------------文案-----------------------
            //文案
            g.setFont(font.deriveFont(Font.PLAIN, 30));
            g.setColor(new Color(47, 47, 47));
            int fontlen = getWatermarkLength(seckillDTO.getInfo(), g);
            //文字长度相对于图片宽度应该有多少行
            int line = fontlen / (back.getWidth() - 90);
            //高度
            int y = tempYb + 50 - (line + 1) * 30 + 100;
            //文字叠加,自动换行叠加
            int tempX = 32;
            int tempY = y;
            //单字符长度
            int tempCharLen = 0;
            //单行字符总长度临时计算
            int tempLineLen = 0;
            StringBuffer sb = new StringBuffer();

            for (int i = 0; i < seckillDTO.getInfo().length(); i++) {
                char tempChar = seckillDTO.getInfo().charAt(i);
                tempCharLen = getCharLen(tempChar, g);
                tempLineLen += tempCharLen;
                if (tempLineLen >= (back.getWidth() - 90)) {
                    //长度已经满一行,进行文字叠加
                    g.drawString(sb.toString(), tempX, tempY + 50);
                    //清空内容,重新追加
                    sb.delete(0, sb.length());
                    //每行文字间距50
                    tempY += 50;
                    tempLineLen = 0;
                }
                //追加字符
                sb.append(tempChar);
            }
            //最后叠加余下的文字
            g.drawString(sb.toString(), tempX, tempY + 50);

            //价格背景
            //读取互联网图片
            BufferedImage bground = null;//
            InputStream redStream = getClass().getClassLoader().getResourceAsStream("red.jpg");
            try {
                ImageInputStream red = ImageIO.createImageInputStream(redStream);
                bground = ImageIO.read(red);
            } catch (IOException e) {
                e.printStackTrace();
            }
            // 绘制缩小后的图
            g.drawImage(bground.getScaledInstance(160, 40, Image.SCALE_DEFAULT), 30, 1053, null);

            //限时促销价
            g.setFont(font.deriveFont(Font.PLAIN, 24));
            g.setColor(new Color(255, 255, 255));
            g.drawString("限时促销价", 50, 1080);

            //价格
            g.setFont(font.deriveFont(Font.PLAIN, 50));
            g.setColor(new Color(249, 64, 64));
            g.drawString("¥" + seckillDTO.getPrice(), 29, 1162);

            //原价
            g.setFont(font.deriveFont(Font.PLAIN, 36));
            g.setColor(new Color(171, 171, 171));
            String price = "¥" + seckillDTO.getOtPrice();
            g.drawString(price, 260, 1160);
            g.drawLine(250, 1148, 260 + 150, 1148);

//            //商品名称
//            g.setFont(font.deriveFont(Font.PLAIN,32));
//            g.setColor(new Color(29,29,29));
//            g.drawString(productDTO.getStoreName(), 30, 1229);

            //生成二维码返回链接
            String url = shareCode;
            //读取互联网图片
            BufferedImage qrCode = null;
            try {
                qrCode = ImageIO.read(new URL(url));
            } catch (IOException e) {
                e.printStackTrace();
            }
            // 绘制缩小后的图
            g.drawImage(qrCode.getScaledInstance(174, 174, Image.SCALE_DEFAULT), 536, 1057, null);

            //二维码字体
            g.setFont(font.deriveFont(Font.PLAIN, 25));
            g.setColor(new Color(171, 171, 171));
            //绘制文字
            g.drawString("扫描或长按小程序码", 515, 1260);

            g.dispose();
            //先将画好的海报写到本地
            File file = new File(spreadPicPath);
            try {
                ImageIO.write(img, "jpg", file);
            } catch (IOException e) {
                e.printStackTrace();
            }
            systemAttachmentService.attachmentAdd(spreadPicName,
                    String.valueOf(FileUtil.size(new File(spreadPicPath))),
                    spreadPicPath, "qrcode/" + spreadPicName);
            spreadUrl = apiUrl + "/api/file/qrcode/" + spreadPicName;
            //保存到本地 生成文件名字
        } else {
            spreadUrl = apiUrl + "/api/file/" + attachmentT.getSattDir();
        }

        return spreadUrl;
    }


    /**
     * 获取水印文字总长度
     *
     * @paramwaterMarkContent水印的文字
     * @paramg
     * @return水印文字总长度
     */
    public static int getWatermarkLength(String waterMarkContent, Graphics g) {
        return g.getFontMetrics(g.getFont()).charsWidth(waterMarkContent.toCharArray(), 0, waterMarkContent.length());
    }

    public static int getCharLen(char c, Graphics g) {
        return g.getFontMetrics(g.getFont()).charWidth(c);
    }
}
