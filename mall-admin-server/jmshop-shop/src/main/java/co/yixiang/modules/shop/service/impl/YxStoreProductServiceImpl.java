package co.yixiang.modules.shop.service.impl;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import co.yixiang.exception.BadRequestException;
import co.yixiang.modules.activity.domain.YxStoreDiscount;
import co.yixiang.modules.activity.repository.YxStoreDiscountRepository;
import co.yixiang.modules.activity.service.dto.YxStoreDiscountDTO;
import co.yixiang.modules.activity.service.mapper.YxStoreDiscountMapper;
import co.yixiang.modules.shop.domain.*;
import co.yixiang.modules.shop.repository.*;
import co.yixiang.modules.shop.service.YxStoreProductService;
import co.yixiang.modules.shop.service.dto.*;
import co.yixiang.modules.shop.service.mapper.YxStoreProductMapper;
import co.yixiang.utils.*;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.Array;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import static co.yixiang.utils.OrderUtil.dateToTimestamp;
import static co.yixiang.utils.OrderUtil.orderStatus;

/**
 * @author hupeng
 * @date 2019-10-04
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class YxStoreProductServiceImpl implements YxStoreProductService {

    private final YxStoreProductRepository yxStoreProductRepository;
    private final YxStoreProductAttrRepository yxStoreProductAttrRepository;
    private final YxStoreProductAttrValueRepository yxStoreProductAttrValueRepository;
    private final YxStoreProductAttrResultRepository yxStoreProductAttrResultRepository;
    private final YxStoreCategoryRepository yxStoreCategoryRepository;
    private final YxSystemUserLevelRepository yxSystemUserLevelRepository;
    private final YxStoreDiscountRepository yxStoreDiscountRepository;
    private final YxStoreDiscountMapper yxStoreDiscountMapper;

    private final YxStoreProductMapper yxStoreProductMapper;

    public YxStoreProductServiceImpl(YxStoreProductRepository yxStoreProductRepository,
                                     YxStoreProductAttrRepository yxStoreProductAttrRepository, YxStoreProductAttrValueRepository yxStoreProductAttrValueRepository,
                                     YxStoreProductAttrResultRepository yxStoreProductAttrResultRepository, YxStoreProductMapper yxStoreProductMapper, YxStoreCategoryRepository yxStoreCategoryRepository,
                                     YxSystemUserLevelRepository yxSystemUserLevelRepository, YxStoreDiscountRepository yxStoreDiscountRepository, YxStoreDiscountMapper yxStoreDiscountMapper) {
        this.yxStoreProductRepository = yxStoreProductRepository;
        this.yxStoreProductAttrRepository = yxStoreProductAttrRepository;
        this.yxStoreProductAttrValueRepository = yxStoreProductAttrValueRepository;
        this.yxStoreProductAttrResultRepository = yxStoreProductAttrResultRepository;
        this.yxStoreProductMapper = yxStoreProductMapper;
        this.yxStoreCategoryRepository = yxStoreCategoryRepository;
        this.yxSystemUserLevelRepository = yxSystemUserLevelRepository;
        this.yxStoreDiscountRepository = yxStoreDiscountRepository;
        this.yxStoreDiscountMapper = yxStoreDiscountMapper;
    }

    @Override
    public Map<String, Object> queryAll(YxStoreProductQueryCriteria criteria, Pageable pageable) {
        //criteria.setIsDel(0);
        Page<YxStoreProduct> page = yxStoreProductRepository
                .findAll((root, criteriaQuery, criteriaBuilder)
                                -> QueryHelp.getPredicate(root, criteria, criteriaBuilder)
                        , pageable);
        yxStoreProductRepository.findAll();
        List<YxStoreProductDTO> storeProductDTOS = new ArrayList<>();
        for (YxStoreProduct product : page.getContent()) {

            YxStoreProductDTO yxStoreProductDTO = yxStoreProductMapper.toDto(product);
            //规格属性库存
            Integer newStock = yxStoreProductAttrValueRepository.sumStock(product.getId());
            if (newStock != null) {
                yxStoreProductDTO.setStock(newStock);
            }
            //设置商品最低价格
            BigDecimal lowestPrice = yxStoreProductAttrValueRepository.lowestPrice(product.getId());
            if (lowestPrice != null) {
                yxStoreProductDTO.setPrice(lowestPrice);
            }

            storeProductDTOS.add(yxStoreProductDTO);
        }
        Map<String, Object> map = new LinkedHashMap<>(2);
        map.put("content", storeProductDTOS);
        map.put("totalElements", page.getTotalElements());
        return map;
    }

    @Override
    public List<YxStoreProductDTO> queryAll(YxStoreProductQueryCriteria criteria) {
        return yxStoreProductMapper.toDto(yxStoreProductRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder)));
    }

    @Override
    public YxStoreProductDTO findById(Integer id) {
        Optional<YxStoreProduct> yxStoreProduct = yxStoreProductRepository.findById(id);
        //ValidationUtil.isNull(yxStoreProduct,"YxStoreProduct","id",id);
        if (yxStoreProduct.isPresent()) {
            return yxStoreProductMapper.toDto(yxStoreProduct.get());
        }
        return new YxStoreProductDTO();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public YxStoreProductDTO create(YxStoreProduct resources) {
        return yxStoreProductMapper.toDto(yxStoreProductRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(YxStoreProduct resources) {
        Optional<YxStoreProduct> optionalYxStoreProduct = yxStoreProductRepository.findById(resources.getId());
        ValidationUtil.isNull(optionalYxStoreProduct, "YxStoreProduct", "id", resources.getId());
        YxStoreProduct yxStoreProduct = optionalYxStoreProduct.get();
        yxStoreProduct.copy(resources);
        yxStoreProductRepository.save(yxStoreProduct);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Integer id) {
        yxStoreProductRepository.updateDel(1, id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void recovery(Integer id) {
        yxStoreProductRepository.updateDel(0, id);
        yxStoreProductRepository.updateOnsale(0, id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void onSale(Integer id, Integer status) {
        if (status == 1) {
            status = 0;
        } else {
            status = 1;
        }
        yxStoreProductRepository.updateOnsale(status, id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<ProductFormatDTO> isFormatAttr(Integer id, String jsonStr) {
        if (ObjectUtil.isNull(id)) {
            throw new BadRequestException("产品不存在");
        }

        DetailDTO detailDTO = attrFormat(jsonStr);

        List<ProductFormatDTO> newList = new ArrayList<>();
        for (Map<String, Map<String, String>> map : detailDTO.getRes()) {
            ProductFormatDTO productFormatDTO = new ProductFormatDTO();
            Map<String, String> detail = map.get("detail");
            productFormatDTO.setDetail(detail);
            //获取sku并用，分割开
            String[] detailArr = detail.values().toArray(new String[]{});
            Arrays.sort(detailArr);

            String sku = String.join(",", detailArr);
            productFormatDTO.setSales(0);
            productFormatDTO.setCost(0.0);
            productFormatDTO.setPrice(0.0);
            productFormatDTO.setWholesale(0.0);
            productFormatDTO.setStock(0);
            if (id > 0) {
                YxStoreProductAttrValue storeProductAttrValue = yxStoreProductAttrValueRepository.findByProductIdAndSuk(id, sku);
                if (storeProductAttrValue != null) {
                    productFormatDTO.setPic(storeProductAttrValue.getImage());
                    productFormatDTO.setSales(ObjectUtils.isEmpty(storeProductAttrValue.getStock()) ? new Integer(0) : storeProductAttrValue.getStock());
                    //productFormatDTO.setStock(ObjectUtils.isEmpty(storeProductAttrValue.getStock()) ? new Integer(0) : storeProductAttrValue.getStock());
                    productFormatDTO.setCost(ObjectUtils.isEmpty(storeProductAttrValue.getCost()) ? new Double(0) : storeProductAttrValue.getCost().doubleValue());
                    productFormatDTO.setPrice(ObjectUtils.isEmpty(storeProductAttrValue.getPrice()) ? new Double(0) : storeProductAttrValue.getPrice().doubleValue());
                    productFormatDTO.setWholesale(ObjectUtils.isEmpty(storeProductAttrValue.getWholesale()) ? new Double(0) : storeProductAttrValue.getWholesale().doubleValue());
                    productFormatDTO.setBarCode(storeProductAttrValue.getBarCode());
                    productFormatDTO.setPackaging(storeProductAttrValue.getPackaging());
                }
            }
            productFormatDTO.setCheck(false);
            newList.add(productFormatDTO);
        }
        return newList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createProductAttr(Integer id, String jsonStr) {
        JSONObject jsonObject = JSON.parseObject(jsonStr);
        String items = jsonObject.get("items") == null ? jsonObject.get("attr").toString() : JSON.toJSONString(jsonObject.get("items"));
        String attrs = jsonObject.get("attrs") == null ? jsonObject.get("value").toString() : JSON.toJSONString(jsonObject.get("attrs"));
        //System.out.println(jsonObject);
        List<FromatDetailDTO> attrList = JSON.parseArray(items,
                FromatDetailDTO.class);
        List<ProductFormatDTO> valueList = JSON.parseArray(attrs,
                ProductFormatDTO.class);

        List<YxStoreProductAttr> attrGroup = new ArrayList<>();
        for (FromatDetailDTO fromatDetailDTO : attrList) {
            YxStoreProductAttr yxStoreProductAttr = new YxStoreProductAttr();
            yxStoreProductAttr.setProductId(id);
            yxStoreProductAttr.setAttrName(fromatDetailDTO.getValue());
            yxStoreProductAttr.setAttrValues(StrUtil.
                    join(",", fromatDetailDTO.getDetail()));
            attrGroup.add(yxStoreProductAttr);
        }


        List<YxStoreProductAttrValue> valueGroup = new ArrayList<>();
        for (ProductFormatDTO productFormatDTO : valueList) {
            YxStoreProductAttrValue yxStoreProductAttrValue = new YxStoreProductAttrValue();
            yxStoreProductAttrValue.setProductId(id);
            //productFormatDTO.getDetail().values().stream().collect(Collectors.toList());
            List<String> stringList = productFormatDTO.getDetail().values()
                    .stream().collect(Collectors.toList());
            Collections.sort(stringList);
            yxStoreProductAttrValue.setSuk(StrUtil.
                    join(",", stringList));
            yxStoreProductAttrValue.setPrice(BigDecimal.valueOf(productFormatDTO.getPrice()));
            yxStoreProductAttrValue.setCost(BigDecimal.valueOf(productFormatDTO.getCost()));
            yxStoreProductAttrValue.setStock(productFormatDTO.getSales());
            yxStoreProductAttrValue.setUnique(IdUtil.simpleUUID());
            yxStoreProductAttrValue.setImage(productFormatDTO.getPic());
            yxStoreProductAttrValue.setWholesale(ObjectUtils.isEmpty(productFormatDTO.getWholesale()) ? new BigDecimal(0) : BigDecimal.valueOf(productFormatDTO.getWholesale()));
            yxStoreProductAttrValue.setBarCode(productFormatDTO.getBarCode());
            yxStoreProductAttrValue.setPackaging(productFormatDTO.getPackaging());
            if(ObjectUtil.isNotNull(productFormatDTO.getOtPrice())) {
                yxStoreProductAttrValue.setOtPrice(BigDecimal.valueOf(productFormatDTO.getOtPrice()));
            }else{
                yxStoreProductAttrValue.setOtPrice(new BigDecimal(0));
            }
            valueGroup.add(yxStoreProductAttrValue);
        }

        if (attrGroup.isEmpty() || valueGroup.isEmpty()) {
            throw new BadRequestException("请设置至少一个属性!");
        }

        //插入之前清空
        clearProductAttr(id, true);


        //保存属性
        yxStoreProductAttrRepository.saveAll(attrGroup);

        //保存值
        yxStoreProductAttrValueRepository.saveAll(valueGroup);

        //设置商品最低价格
        BigDecimal lowestPrice = yxStoreProductAttrValueRepository.lowestPrice(id);
        if (lowestPrice != null) {
            yxStoreProductRepository.updatePrice(lowestPrice, id);
        }

        //设置商品库存
        Integer stock = yxStoreProductAttrValueRepository.sumStock(id);
        if (stock != null) {
            yxStoreProductRepository.updateStock(stock, id);
        }

        Map<String, Object> map = new LinkedHashMap<>();
        Object attr = ObjectUtil.isNull(jsonObject.get("items")) ? jsonObject.get("attr") : jsonObject.get("items");
        Object value = ObjectUtil.isNull(jsonObject.get("attrs")) ? jsonObject.get("value") : jsonObject.get("attrs");
        map.put("attr", attr);
        map.put("value", value);

        //保存结果
        setResult(map, id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void setResult(Map<String, Object> map, Integer id) {
        YxStoreProductAttrResult yxStoreProductAttrResult = new YxStoreProductAttrResult();
        yxStoreProductAttrResult.setProductId(id);
        yxStoreProductAttrResult.setResult(JSON.toJSONString(map));
        yxStoreProductAttrResult.setChangeTime(OrderUtil.getSecondTimestampTwo());

        yxStoreProductAttrResultRepository.deleteByProductId(id);

        yxStoreProductAttrResultRepository.save(yxStoreProductAttrResult);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void clearProductAttr(Integer id, boolean isActice) {
        if (ObjectUtil.isNull(id)) {
            throw new BadRequestException("产品不存在");
        }

        yxStoreProductAttrRepository.deleteByProductId(id);
        yxStoreProductAttrValueRepository.deleteByProductId(id);

        if (isActice) {
            yxStoreProductAttrResultRepository.deleteByProductId(id);
        }
    }

    @Override
    public String getStoreProductAttrResult(Integer id) {
        YxStoreProductAttrResult yxStoreProductAttrResult = yxStoreProductAttrResultRepository
                .findByProductId(id);
        if (ObjectUtil.isNull(yxStoreProductAttrResult)) {
            return "";
        }
        return yxStoreProductAttrResult.getResult();
    }

    @Override
    public void download(List<YxStoreProductDTO> queryAll, HttpServletResponse response) throws IOException, ParseException {
        List<Map<String, Object>> list = new ArrayList<>();
        List<YxSystemUserLevel> yxSystemUserLevel = yxSystemUserLevelRepository.findAll();
        Integer time = OrderUtil.dateToTimestamp(new Date());

        if (queryAll != null && queryAll.size() > 0) {
            for (YxStoreProductDTO yxStoreProductDTO : queryAll) {
                String storeProductAttrResult = getStoreProductAttrResult(yxStoreProductDTO.getId());
                JSONObject jsonObject = JSON.parseObject(storeProductAttrResult);
                JSONArray jsonAttr = JSONArray.parseArray(jsonObject != null ? jsonObject.get("attr").toString() : "");
                JSONArray jsonValue = JSONArray.parseArray(jsonObject != null ? jsonObject.get("value").toString() : "");

                List nameArr = new ArrayList();
                List typeArr = new ArrayList();
                if (jsonAttr != null) {
                    for (Object json : jsonAttr) {
                        JSONObject jsonO = JSON.parseObject(json.toString());
                        nameArr.add(jsonO != null ? jsonO.get("value") : "");
                        List detail = JSON.parseArray(jsonO.get("detail").toString(), String.class);
                        typeArr.add(jsonO != null ? StringUtils.join(detail.toArray(), ",") : "");
                    }
                }

                List priceArr = new ArrayList();
                List costArr = new ArrayList();
                List stockArr = new ArrayList();
                List picArr = new ArrayList();
                List barCodeArr = new ArrayList();
                List packagingArr = new ArrayList();
                if (jsonValue != null) {
                    for (Object json : jsonValue) {
                        JSONObject jsonO = JSON.parseObject(json.toString());
                        priceArr.add(jsonO != null ? jsonO.get("price") : 0);
                        costArr.add(jsonO != null ? jsonO.get("cost") : 0);
                        stockArr.add(jsonO != null ? jsonO.get("sales") : 0);
                        picArr.add(jsonO != null ? jsonO.get("pic") : "@");
                        barCodeArr.add(jsonO != null ? jsonO.get("barCode") : "0");
                        packagingArr.add(jsonO != null ? jsonO.get("packaging") : "0");
                    }
                }

                Map<String, Object> map = new LinkedHashMap<>();
                map.put("商品ID", yxStoreProductDTO.getId());

                //查询
                List<YxStoreCategory> yxStoreCategoryList = yxStoreCategoryRepository.findParentsById(yxStoreProductDTO.getStoreCategory().getId());
                if (yxStoreCategoryList != null && yxStoreCategoryList.size() > 0) {
                    // 一级分类
                    map.put("栏目分类", yxStoreCategoryList.get(0).getCateName());
                    // 二级分类sss
                    map.put("商品分类", yxStoreCategoryList.size() >= 2 ? yxStoreCategoryList.get(1).getCateName() : "");
                    // 系列
                    map.put("商品系列", yxStoreCategoryList.size() >= 3 ? yxStoreCategoryList.get(2).getCateName() : "");
                }

                map.put("商品名称", yxStoreProductDTO.getStoreName());
                map.put("商品规格", StringUtils.join(nameArr.toArray(), "/"));
                map.put("商品型号", StringUtils.join(typeArr.toArray(), "/"));
                map.put("商品编码", StringUtils.join(barCodeArr.toArray(), "/"));
                map.put("包装规格", StringUtils.join(packagingArr.toArray(), "/"));

                map.put("关键字", yxStoreProductDTO.getKeyword());
                // map.put("商品包装", yxStoreProductDTO.getPackaging());
                map.put("单位名称", yxStoreProductDTO.getUnitName());
                map.put("商品简介", yxStoreProductDTO.getStoreInfo());
                map.put("产品描述", yxStoreProductDTO.getDescription());
                map.put("邮费", yxStoreProductDTO.getPostage());
                map.put("排序", yxStoreProductDTO.getSort());
                map.put("销量", yxStoreProductDTO.getSales());
                map.put("热卖单品", yxStoreProductDTO.getIsHot());
                map.put("促销单品", yxStoreProductDTO.getIsBenefit());
                map.put("精品推荐", yxStoreProductDTO.getIsBest());
                map.put("首发新品", yxStoreProductDTO.getIsNew());
                map.put("是否包邮", yxStoreProductDTO.getIsPostage());
                map.put("优品推荐", yxStoreProductDTO.getIsGood());
                map.put("获得积分", yxStoreProductDTO.getGiveIntegral());
                map.put("虚拟销量", yxStoreProductDTO.getFicti());
                map.put("是否上架", yxStoreProductDTO.getIsShow());

                // 商品成本价
                String costStr = costArr.size() > 0 ? StringUtils.join(costArr.toArray(), "/") : "";
                // 商品零售价
                Double[] numArr = new Double[priceArr.size()];
                for (int c = 0; c < costArr.size(); c++) {
                    Double num = 1.00;
                    if (priceArr.size() > 0 && costArr.size() == priceArr.size()) {
                        Double cost = Double.valueOf(costArr.get(c).toString());
                        Double price = Double.valueOf(priceArr.get(c).toString());
                        num = cost == 0 || price == 0 ? 1.00 : cost / price;
                        numArr[c] = (double) Math.round(num * 100) / 100;
                    } else {
                        throw new BadRequestException("商品成本价或商品零售价数据异常!");
                    }
                }
                map.put("商品成本价", costStr);
                map.put("商品零售价基数", numArr.length > 0 ? StringUtils.join(numArr, "/") : 1);
                map.put("商品库存", stockArr.size() > 0 ? StringUtils.join(stockArr.toArray(), "/") : "");
                map.put("商品图片", picArr.size() > 0 ? StringUtils.join(picArr.toArray(), "&*&") : "");

                for (int m = 0; m < yxSystemUserLevel.size(); m++) {
                    List<YxStoreDiscount> yxStoreDiscount = yxStoreDiscountRepository.findByProductIdAndGrade(yxStoreProductDTO.getId(), yxSystemUserLevel.get(m).getGrade(), time);
                    map.put(yxSystemUserLevel.get(m).getName() + "折扣(%)", yxStoreDiscount != null && yxStoreDiscount.size() > 0 ? yxStoreDiscount.get(0).getDiscount() : 100);
                }

                list.add(map);
            }
        } else {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("商品ID", "");
            map.put("栏目分类", "");
            map.put("商品分类", "");
            map.put("商品系列", "");
            map.put("商品名称", "");
            map.put("商品规格", "");
            map.put("商品型号", "");
            map.put("商品编码", "");
            map.put("包装规格", "");
            map.put("关键字", "");
            // map.put("商品包装", "");
            map.put("单位名称", "");
            map.put("商品简介", "");
            map.put("产品描述", "");
            map.put("邮费", 0);
            map.put("排序", 0);
            map.put("销量", 0);
            map.put("热卖单品", 0);
            map.put("促销单品", 0);
            map.put("精品推荐", 0);
            map.put("首发新品", 0);
            map.put("是否包邮", 0);
            map.put("优品推荐", 0);
            map.put("获得积分", 0);
            map.put("虚拟销量", 0);
            map.put("是否上架", 0);
            map.put("商品成本价", 0);
            map.put("商品零售价基数", 0);
            map.put("商品库存", 0);
            map.put("商品图片", "&*&");

            for (YxSystemUserLevel level : yxSystemUserLevel) {
                map.put(level.getName() + "折扣(%)", 100);
            }

            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    public int excelToList1(InputStream inputStream) throws InvalidFormatException, IOException {
        List<YxStoreProductDTO> gisList = new ArrayList<YxStoreProductDTO>();
        Workbook workbook = null;
        workbook = WorkbookFactory.create(inputStream);
        inputStream.close();
        //工作表对象
        Sheet sheet = workbook.getSheetAt(0);
        //总行数
        int rowLength = sheet.getLastRowNum() + 1;
        //工作表的列
        Row row = sheet.getRow(0);
        //总列数
        int colLength = row.getLastCellNum();

        for (int i = 1; i < rowLength; i++) {
            row = sheet.getRow(i);
            for (int j = 0; j < colLength; j++) {
                if (row.getCell(j) != null) {
                    row.getCell(j).setCellType(CellType.STRING);
                } else {
                    row.createCell(j).setCellValue("0");
                }
            }
            YxStoreProduct resources = new YxStoreProduct();
            int product_id = 0;
            boolean aNull = ObjectUtil.isNotEmpty(row.getCell(0).getStringCellValue());
            if (aNull) {
                try {
                    product_id = Integer.parseInt(row.getCell(0).getStringCellValue());
                } catch (Exception e) {
                    e.printStackTrace();
                    String msg = " 商品ID不能包含字母、汉字及特殊字符, " + " 请重新填写再次上传！ ";
                    throw new BadRequestException(msg);
                }
            } else {
                product_id = (int) (Math.random() * (352324 + 1));
            }
            // 商品ID
            resources.setId(product_id);
            // 商品分类 共三级分类，从最小一级获取，如没有填写则获取上一级
            String name = StringUtils.isNotEmpty(row.getCell(3).getStringCellValue()) ?
                    row.getCell(3).getStringCellValue() :
                    StringUtils.isNotEmpty(row.getCell(2).getStringCellValue()) ?
                            row.getCell(2).getStringCellValue() :
                            row.getCell(1).getStringCellValue();
            YxStoreCategory yxStoreCategory = null;
            try {
                yxStoreCategory = yxStoreCategoryRepository.findByName(name);
            } catch (Exception e) {
                e.printStackTrace();
                String msg = " 商品分类查询信息异常, " + " 请检查商品分类，重新填写再次上传！ ";
                throw new BadRequestException(msg);
            }

            if (ObjectUtil.isNull(yxStoreCategory)) {
                String msg = " 商品分类【" + name + "】不存在, " + " 请重新填写再次上传！ ";
                throw new BadRequestException(msg);
            }
            resources.setStoreCategory(yxStoreCategory);
            // 商品系列
            // resources.setStoreName(row.getCell(2).getStringCellValue());
            // 商品名称
            resources.setStoreName(row.getCell(4).getStringCellValue());
            // 商品规格
            // resources.setStoreName(row.getCell(5).getStringCellValue());
            // 商品型号
            // resources.setStoreName(row.getCell(6).getStringCellValue());
            // 商品编码
            // resources.setBarCode(row.getCell(7).getStringCellValue());
            // 包装规格
            // resources.setPackaging(row.getCell(8).getStringCellValue());
            // 关键字
            resources.setKeyword(row.getCell(9).getStringCellValue());
            // 商品包装
            resources.setPackaging("");
            // 单位名
            resources.setUnitName(row.getCell(10).getStringCellValue());
            // 商品简介
            resources.setStoreInfo(row.getCell(11).getStringCellValue());
            // 产品描述
            resources.setDescription(row.getCell(12).getStringCellValue());
            // 邮费
            resources.setPostage(new BigDecimal(Double.parseDouble(row.getCell(13).getStringCellValue())));
            // 排序
            resources.setSort(Integer.parseInt(row.getCell(14).getStringCellValue()));
            // 销量
            resources.setSales(Integer.parseInt(row.getCell(15).getStringCellValue()));
            // 是否热卖
            resources.setIsHot(Integer.parseInt(row.getCell(16).getStringCellValue()));
            // 是否优惠
            resources.setIsBenefit(Integer.parseInt(row.getCell(17).getStringCellValue()));
            // 是否精品
            resources.setIsBest(Integer.parseInt(row.getCell(18).getStringCellValue()));
            // 是否新品
            resources.setIsNew(Integer.parseInt(row.getCell(19).getStringCellValue()));
            // 是否包邮
            resources.setIsPostage(Integer.parseInt(row.getCell(20).getStringCellValue()));
            // 是否优选
            resources.setIsGood(Integer.parseInt(row.getCell(21).getStringCellValue()));
            // 获得积分
            resources.setGiveIntegral(new BigDecimal(Double.parseDouble(row.getCell(22).getStringCellValue())));

            resources.setFicti(Integer.parseInt(row.getCell(23).getStringCellValue()));
            // 是否上架
            resources.setIsShow(Integer.parseInt(row.getCell(24).getStringCellValue()));

            List<YxSystemUserLevel> yxSystemUserLevel = yxSystemUserLevelRepository.findAll();
            YxStoreDiscount yxStoreDiscount = new YxStoreDiscount();
            yxStoreDiscount.setStartTime(OrderUtil.
                    dateToTimestamp(new Date()));
            yxStoreDiscount.setStartTimeDate(new Date());
            try {
                Date parse = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2029-01-01 00:00:00");
                yxStoreDiscount.setStopTime(OrderUtil.dateToTimestamp(parse));
                yxStoreDiscount.setEndTimeDate(parse);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            yxStoreDiscount.setAddTime(OrderUtil.getSecondTimestampTwo());
            yxStoreDiscount.setStatus(1);
            yxStoreDiscount.setIsDel(0);
            yxStoreDiscount.setTitle(row.getCell(3).getStringCellValue());

            List discountList = new ArrayList();
            for (int m = 0; m < yxSystemUserLevel.size(); m++) {
                Map discountMap = new HashMap();
                Integer discount = Integer.parseInt(row.getCell(28 + m + 1).getStringCellValue());
                YxSystemUserLevel level = yxSystemUserLevel.get(m);
                discountMap.put("id", level.getId());
                discountMap.put("name", level.getName());
                discountMap.put("grade", level.getGrade());
                discountMap.put("discount", discount);
                discountList.add(discountMap);
            }

            for (int m = 0; m < yxSystemUserLevel.size(); m++) {
                Integer discount = Integer.parseInt(row.getCell(28 + m + 1).getStringCellValue());
                yxStoreDiscount.setId((int) (Math.random() * (352324 + 1)));
                yxStoreDiscount.setProductId(product_id);
                yxStoreDiscount.setGrade(yxSystemUserLevel.get(m).getGrade());
                yxStoreDiscount.setDiscount(discount);
                yxStoreDiscount.setJsonStr(JSON.toJSONString(discountList));
                yxStoreDiscountRepository.save(yxStoreDiscount);
            }

            String[] guige = row.getCell(5).getStringCellValue().split("/");
            String[] shuxing = row.getCell(6).getStringCellValue().split("/");
            // 商品编码
            String[] barCode = row.getCell(7).getStringCellValue().split("/");
            // 包装规格
            String[] packaging = row.getCell(8).getStringCellValue().split("/");
            // 成本价
            String[] cost = row.getCell(25).getStringCellValue().split("/");
            // 基价
            String[] num = row.getCell(26).getStringCellValue().split("/");
            String[] stock = row.getCell(27).getStringCellValue().split("/");
            String[] pic = row.getCell(28).getStringCellValue().split("\\&\\*\\&");

            Map map = new HashMap();
            List attrList = new ArrayList();
            for (int a = 0; a < guige.length; a++) {
                Map attrMap = new HashMap();
                attrMap.put("attrHidden", true);
                attrMap.put("detailValue", "");
                List shuxingList = Arrays.asList(shuxing[a].split(","));
                JSONArray jsonArray = new JSONArray();
                try {
                    jsonArray = JSONArray.parseArray(JSON.toJSONString(shuxingList));
                } catch (Exception e) {
                    e.printStackTrace();
                    String msg = " 商品属性不能包含特殊字符或不能为空 " + " 请检查后重新填写再次上传！ ";
                    throw new BadRequestException(msg);
                }
                attrMap.put("detail", shuxingList != null ? jsonArray : new ArrayList());
                attrMap.put("value", guige[a]);
                attrList.add(attrMap);
            }
            map.put("items", attrList);
            map.put("attrs", new ArrayList());
            DetailDTO detailDTO = attrFormat(JSON.toJSONString(map));

            List<ProductFormatDTO> newList = new ArrayList<>();
            for (int b = 0; b < detailDTO.getRes().size(); b++) {
                Map<String, Map<String, String>> valueMap = detailDTO.getRes().get(b);
                // Map<String, Map<String,String>> valueMap : detailDTO.getRes()
                ProductFormatDTO productFormatDTO = new ProductFormatDTO();
                productFormatDTO.setDetail(valueMap.get("detail"));

                try {
                    productFormatDTO.setPackaging(packaging[b].equals("") ? "0" : packaging[b]);
                } catch (Exception e) {
                    e.printStackTrace();
                    String msg = " 包装规格填写异常, " + " 请重新填写再次上传！ ";
                    throw new BadRequestException(msg);
                }

                try {
                    productFormatDTO.setBarCode(barCode[b].equals("") ? "0" : barCode[b]);
                } catch (Exception e) {
                    e.printStackTrace();
                    String msg = " 商品编码填写异常, " + " 请重新填写再次上传！ ";
                    throw new BadRequestException(msg);
                }

                try {
                    productFormatDTO.setCost(cost[b].equals("") ? new Double(0) : new Double(cost[b]));
                } catch (Exception e) {
                    e.printStackTrace();
                    String msg = " 商品成本价填写异常, " + " 请重新填写再次上传！ ";
                    throw new BadRequestException(msg);
                }

                try {
                    Double price = new Double(cost[b]) * new Double(num[b]);
                    productFormatDTO.setPrice(num[b].equals("1") ? new Double(0.00) : new Double(price));
                } catch (Exception e) {
                    e.printStackTrace();
                    String msg = " 商品零售价基数填写异常, " + " 请重新填写再次上传！ ";
                    throw new BadRequestException(msg);
                }

                try {
                    productFormatDTO.setSales(stock[b].equals("") ? new Integer(0) : Integer.valueOf(stock[b]));
                } catch (Exception e) {
                    e.printStackTrace();
                    String msg = " 商品库存填写异常, " + " 请重新填写再次上传！ ";
                    throw new BadRequestException(msg);
                }

                try {
                    productFormatDTO.setPic(pic[b].equals("") ? "" : pic[b]);
                } catch (Exception e) {
                    e.printStackTrace();
                    String msg = " 商品图片填写异常, " + " 请重新填写再次上传！ ";
                    throw new BadRequestException(msg);
                }

                productFormatDTO.setWholesale(new Double(0));
                productFormatDTO.setCheck(false);
                newList.add(productFormatDTO);
            }
            map.put("attrs", newList);

            // 添加时间
            resources.setAddTime(dateToTimestamp(new Date()));
            // 商品图片
            resources.setImage("@");
            // 商品轮播图
            resources.setSliderImage("@");
            resources.setCodePath("@");
            resources.setPrice(new BigDecimal(0));
            // 商品价格
            resources.setPrice(new BigDecimal(0));
            // 市场价
            resources.setOtPrice(new BigDecimal(0));
            // 成本价
            resources.setCost(new BigDecimal(0));
            // 库存
            resources.setStock(0);

            boolean existsById = yxStoreProductRepository.existsById(product_id);
            if (existsById) {
                yxStoreProductRepository.updatePrice(resources);
                List<YxStoreProductDTO> yxStoreProductDTOS = yxStoreProductMapper.toDto(yxStoreProductRepository.findAll());
                if (ObjectUtil.isNull(yxStoreProductDTOS)) {
                    String msg = " 商品信息修改失败! ";
                    throw new BadRequestException(msg);
                }
            } else {
                YxStoreProductDTO yxStoreProductDTO = yxStoreProductMapper.toDto(yxStoreProductRepository.save(resources));
                if (ObjectUtil.isNull(yxStoreProductDTO)) {
                    String msg = " 商品信息保存失败! ";
                    throw new BadRequestException(msg);
                }
            }
            createProductAttr(product_id, JSON.toJSONString(map));
        }
        return 1;
    }


    public String saveProductInfo(List<Row> list) {
        for (Row row : list) {
            YxStoreProduct resources = new YxStoreProduct();
            int product_id = 0;
            boolean aNull = ObjectUtil.isNotEmpty(row.getCell(0).getStringCellValue());
            if (aNull) {
                try {
                    product_id = Math.getExponent(Double.parseDouble(row.getCell(0).getStringCellValue()));
                } catch (Exception e) {
                    e.printStackTrace();
                    String msg = " 商品ID不能包含字母、汉字及特殊字符, " + " 请重新填写再次上传！ ";
                    throw new BadRequestException(msg);
                }
            } else {
                product_id = (int) (Math.random() * (352324 + 1));
            }
            // 商品ID
            resources.setId(product_id);
            // 商品分类 共三级分类，从最小一级获取，如没有填写则获取上一级
            String name = StringUtils.isNotEmpty(row.getCell(3).getStringCellValue()) ?
                    row.getCell(3).getStringCellValue() :
                    StringUtils.isNotEmpty(row.getCell(2).getStringCellValue()) ?
                            row.getCell(2).getStringCellValue() :
                            row.getCell(1).getStringCellValue();
            YxStoreCategory yxStoreCategory = null;
            try {
                yxStoreCategory = yxStoreCategoryRepository.findByName(name);
            } catch (Exception e) {
                e.printStackTrace();
                String msg = " 商品分类查询信息异常, " + " 请检查商品分类，重新填写再次上传！ ";
                throw new BadRequestException(msg);
            }

            if (ObjectUtil.isNull(yxStoreCategory)) {
                String msg = " 商品分类【" + name + "】不存在, " + " 请重新填写再次上传！ ";
                throw new BadRequestException(msg);
            }
            resources.setStoreCategory(yxStoreCategory);
            // 商品系列
            // resources.setStoreName(row.getCell(2).getStringCellValue());
            // 商品名称
            resources.setStoreName(row.getCell(4).getStringCellValue());
            // 商品规格
            // resources.setStoreName(row.getCell(5).getStringCellValue());
            // 商品型号
            // resources.setStoreName(row.getCell(6).getStringCellValue());
            // 商品编码
            // resources.setBarCode(row.getCell(7).getStringCellValue());
            // 包装规格
            // resources.setPackaging(row.getCell(8).getStringCellValue());
            // 关键字
            resources.setKeyword(row.getCell(9).getStringCellValue());
            // 商品包装
            resources.setPackaging("");
            // 单位名
            resources.setUnitName(row.getCell(10).getStringCellValue());
            // 商品简介
            resources.setStoreInfo(row.getCell(11).getStringCellValue());
            // 产品描述
            resources.setDescription(row.getCell(12).getStringCellValue());
            // 邮费
            resources.setPostage(new BigDecimal(Double.parseDouble(row.getCell(13).getStringCellValue())));
            // 排序
            resources.setSort(Integer.parseInt(row.getCell(14).getStringCellValue()));
            // 销量
            resources.setSales(Integer.parseInt(row.getCell(15).getStringCellValue()));
            // 是否热卖
            resources.setIsHot(Integer.parseInt(row.getCell(16).getStringCellValue()));
            // 是否优惠
            resources.setIsBenefit(Integer.parseInt(row.getCell(17).getStringCellValue()));
            // 是否精品
            resources.setIsBest(Integer.parseInt(row.getCell(18).getStringCellValue()));
            // 是否新品
            resources.setIsNew(Integer.parseInt(row.getCell(19).getStringCellValue()));
            // 是否包邮
            resources.setIsPostage(Integer.parseInt(row.getCell(20).getStringCellValue()));
            // 是否优选
            resources.setIsGood(Integer.parseInt(row.getCell(21).getStringCellValue()));
            // 获得积分
            resources.setGiveIntegral(new BigDecimal(Double.parseDouble(row.getCell(22).getStringCellValue())));

            resources.setFicti(Integer.parseInt(row.getCell(23).getStringCellValue()));
            // 是否上架
            resources.setIsShow(Integer.parseInt(row.getCell(24).getStringCellValue()));

            List<YxSystemUserLevel> yxSystemUserLevel = yxSystemUserLevelRepository.findAll();
            YxStoreDiscount yxStoreDiscount = new YxStoreDiscount();
            yxStoreDiscount.setStartTime(OrderUtil.
                    dateToTimestamp(new Date()));
            yxStoreDiscount.setStartTimeDate(new Date());
            try {
                Date parse = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2029-01-01 00:00:00");
                yxStoreDiscount.setStopTime(OrderUtil.dateToTimestamp(parse));
                yxStoreDiscount.setEndTimeDate(parse);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            yxStoreDiscount.setAddTime(OrderUtil.getSecondTimestampTwo());
            yxStoreDiscount.setStatus(1);
            yxStoreDiscount.setIsDel(0);
            yxStoreDiscount.setTitle(row.getCell(3).getStringCellValue());

            List discountList = new ArrayList();
            for (int m = 0; m < yxSystemUserLevel.size(); m++) {
                Map discountMap = new HashMap();
                Integer discount = Integer.parseInt(row.getCell(28 + m + 1).getStringCellValue());
                YxSystemUserLevel level = yxSystemUserLevel.get(m);
                discountMap.put("id", level.getId());
                discountMap.put("name", level.getName());
                discountMap.put("grade", level.getGrade());
                discountMap.put("discount", discount);
                discountList.add(discountMap);
            }

            for (int m = 0; m < yxSystemUserLevel.size(); m++) {
                Integer discount = Integer.parseInt(row.getCell(28 + m + 1).getStringCellValue());
                yxStoreDiscount.setId((int) (Math.random() * (352324 + 1)));
                yxStoreDiscount.setProductId(product_id);
                yxStoreDiscount.setGrade(yxSystemUserLevel.get(m).getGrade());
                yxStoreDiscount.setDiscount(discount);
                yxStoreDiscount.setJsonStr(JSON.toJSONString(discountList));
                yxStoreDiscountRepository.save(yxStoreDiscount);
            }

            // 获取相同商品信息的商品规格属性值
            // getSameShuxing(sheet, i);

            String[] guige = row.getCell(5).getStringCellValue().split("/");
            String[] shuxing = row.getCell(6).getStringCellValue().split("/");
            // 商品编码
            String[] barCode = row.getCell(7).getStringCellValue().split("/");
            // 包装规格
            String[] packaging = row.getCell(8).getStringCellValue().split("/");
            // 成本价
            String[] cost = row.getCell(25).getStringCellValue().split("/");
            // 基价
            String[] num = row.getCell(26).getStringCellValue().split("/");
            String[] stock = row.getCell(27).getStringCellValue().split("/");
            String[] pic = row.getCell(28).getStringCellValue().split("\\&\\*\\&");

            Map map = new HashMap();
            List attrList = new ArrayList();
            for (int a = 0; a < guige.length; a++) {
                Map attrMap = new HashMap();
                attrMap.put("attrHidden", true);
                attrMap.put("detailValue", "");
                List shuxingList = Arrays.asList(shuxing[a].split(","));
                JSONArray jsonArray = new JSONArray();
                try {
                    jsonArray = JSONArray.parseArray(JSON.toJSONString(shuxingList));
                } catch (Exception e) {
                    e.printStackTrace();
                    String msg = " 商品属性不能包含特殊字符或不能为空 " + " 请检查后重新填写再次上传！ ";
                    throw new BadRequestException(msg);
                }
                attrMap.put("detail", shuxingList != null ? jsonArray : new ArrayList());
                attrMap.put("value", guige[a]);
                attrList.add(attrMap);
            }
            map.put("items", attrList);
            map.put("attrs", new ArrayList());
            DetailDTO detailDTO = attrFormat(JSON.toJSONString(map));

            List<ProductFormatDTO> newList = new ArrayList<>();
            for (int b = 0; b < detailDTO.getRes().size(); b++) {
                Map<String, Map<String, String>> valueMap = detailDTO.getRes().get(b);
                // Map<String, Map<String,String>> valueMap : detailDTO.getRes()
                ProductFormatDTO productFormatDTO = new ProductFormatDTO();
                productFormatDTO.setDetail(valueMap.get("detail"));

                try {
                    productFormatDTO.setPackaging(packaging[b].equals("") ? "0" : packaging[b]);
                } catch (Exception e) {
                    e.printStackTrace();
                    String msg = " 包装规格填写异常, " + " 请重新填写再次上传！ ";
                    throw new BadRequestException(msg);
                }

                try {
                    productFormatDTO.setBarCode(barCode[b].equals("") ? "0" : barCode[b]);
                } catch (Exception e) {
                    e.printStackTrace();
                    String msg = " 商品编码填写异常, " + " 请重新填写再次上传！ ";
                    throw new BadRequestException(msg);
                }

                try {
                    productFormatDTO.setCost(cost[b].equals("") ? new Double(0) : new Double(cost[b]));
                } catch (Exception e) {
                    e.printStackTrace();
                    String msg = " 商品成本价填写异常, " + " 请重新填写再次上传！ ";
                    throw new BadRequestException(msg);
                }

                try {
                    Double price = new Double(cost[b]) * new Double(num[b]);
                    productFormatDTO.setPrice(num[b].equals("1") ? new Double(0.00) : new Double(price));
                } catch (Exception e) {
                    e.printStackTrace();
                    String msg = " 商品零售价基数填写异常, " + " 请重新填写再次上传！ ";
                    throw new BadRequestException(msg);
                }

                try {
                    productFormatDTO.setSales(stock[b].equals("") ? new Integer(0) : Integer.valueOf(stock[b]));
                } catch (Exception e) {
                    e.printStackTrace();
                    String msg = " 商品库存填写异常, " + " 请重新填写再次上传！ ";
                    throw new BadRequestException(msg);
                }

                try {
                    productFormatDTO.setPic(pic[b].equals("") ? "" : pic[b]);
                } catch (Exception e) {
                    e.printStackTrace();
                    String msg = " 商品图片填写异常, " + " 请重新填写再次上传！ ";
                    throw new BadRequestException(msg);
                }

                productFormatDTO.setWholesale(new Double(0));
                productFormatDTO.setCheck(false);
                newList.add(productFormatDTO);
            }
            map.put("attrs", newList);
            createProductAttr(product_id, JSON.toJSONString(map));

            // 添加时间
            resources.setAddTime(dateToTimestamp(new Date()));
            // 商品图片
            resources.setImage("@");
            // 商品轮播图
            resources.setSliderImage("@");
            resources.setCodePath("@");
            resources.setPrice(new BigDecimal(0));
            // 商品价格
            resources.setPrice(new BigDecimal(0));
            // 市场价
            resources.setOtPrice(new BigDecimal(0));
            // 成本价
            resources.setCost(new BigDecimal(0));
            // 库存
            resources.setStock(0);

            boolean existsById = yxStoreProductRepository.existsById(product_id);
            if (existsById) {
                yxStoreProductRepository.updatePrice(resources);
                List<YxStoreProductDTO> yxStoreProductDTOS = yxStoreProductMapper.toDto(yxStoreProductRepository.findAll());
                if (ObjectUtil.isNull(yxStoreProductDTOS)) {
                    String msg = " 商品信息修改失败! ";
                    throw new BadRequestException(msg);
                }
            } else {
                YxStoreProductDTO yxStoreProductDTO = yxStoreProductMapper.toDto(yxStoreProductRepository.save(resources));
                if (ObjectUtil.isNull(yxStoreProductDTO)) {
                    String msg = " 商品信息保存失败! ";
                    throw new BadRequestException(msg);
                }
            }
        }
        return "";
    }

    public Row doSameShuxing(List<Row> list, Sheet sheet) {
        //工作表的列
        Row rowList = sheet.getRow(0);
        int colLength = rowList.getLastCellNum();

        List guige = new ArrayList<Row>();
        List shuxing = new ArrayList<Row>();
        List barCode = new ArrayList<Row>();
        List packaging = new ArrayList<Row>();
        List cost = new ArrayList<Row>();
        List num = new ArrayList<Row>();
        List stock = new ArrayList<Row>();
        List pic = new ArrayList<Row>();

        List<Row> listNew = new ArrayList<Row>(new TreeSet<Row>(list));
        for (Row row : listNew) {
            for (int i = 0; i < colLength; i++) {
                Cell cell = rowList.createCell(i);
                cell.setCellValue(row.getCell(i).getStringCellValue()); //设置值
            }
            guige.add(row.getCell(5).getStringCellValue());
            shuxing.add(row.getCell(6).getStringCellValue());
            barCode.add(row.getCell(7).getStringCellValue());
            packaging.add(row.getCell(8).getStringCellValue());
            cost.add(row.getCell(25).getStringCellValue());
            num.add(row.getCell(26).getStringCellValue());
            stock.add(row.getCell(27).getStringCellValue());
            pic.add(row.getCell(28).getStringCellValue());
        }
        rowList.createCell(5).setCellValue(String.join("/", guige));
        rowList.createCell(6).setCellValue(String.join("/", shuxing));
        rowList.createCell(7).setCellValue(String.join("/", barCode));
        rowList.createCell(8).setCellValue(String.join("/", packaging));
        rowList.createCell(25).setCellValue(String.join("/", cost));
        rowList.createCell(26).setCellValue(String.join("/", num));
        rowList.createCell(27).setCellValue(String.join("/", stock));
        rowList.createCell(28).setCellValue(String.join("\\&\\*\\&", pic));

        return rowList;
    }

    public void getSameShuxing(Sheet sheet) {
        List<Row> list = new ArrayList<Row>();
        //总行数
        int rowLength = sheet.getLastRowNum() + 1;
        //工作表的列
        Row row = sheet.getRow(0);
        //总列数
        int colLength = row.getLastCellNum();

        List<Row> rowList = new ArrayList<Row>();
        for (int i = 1; i < rowLength; i++) {
            Row cells = sheet.getRow(0);
            // 获取当前一行的数据
            row = sheet.getRow(i);
            Row rowNext = i == rowLength - 1 ? sheet.getRow(i) : sheet.getRow(i + 1);
            // 转换单元格内容类型
            for (int j = 0; j < colLength; j++) {
                if (row.getCell(j) != null) {
                    row.getCell(j).setCellType(CellType.STRING);
                } else {
                    row.createCell(j).setCellValue("0");
                }
                if (rowNext.getCell(j) != null) {
                    rowNext.getCell(j).setCellType(CellType.STRING);
                } else {
                    rowNext.createCell(j).setCellValue("0");
                }
            }

            //当前属性
            String str1 = row.getCell(6).getStringCellValue();
            //下一个属性
            String str2 = rowNext.getCell(6).getStringCellValue();

            String[] arr1 = str1.split("");
            String[] arr2 = str2.split("");
            StringBuffer sb = new StringBuffer();
            for (int m = 0; m < arr2.length; m++) {
                for (int n = 0; n < arr1.length; n++) {
                    if (arr1[n].equals(arr2[m])) {
                        sb.append(arr1[n] + "");
                    }
                }
            }
            String[] sameStringArr = sb.toString().split("");
            // 判断当前的商品名称相同
            if (str1.indexOf(sameStringArr[0]) == 0 && str1.indexOf(sameStringArr[1]) == 1 && sameStringArr.length > 1) {
                rowList.add(row);
                rowList.add(rowNext);
            } else {
                if (rowList.size() > 0) {
                    cells = doSameShuxing(rowList, sheet);
                    list.add(cells);
                    rowList = new ArrayList<Row>();
                } else {
                    list.add(row);
                }
            }
            if (i == rowLength - 1) {
                if (rowList.size() > 0) {
                    list.add(doSameShuxing(rowList, sheet));
                    rowList = new ArrayList<Row>();
                } else {
                    list.add(row);
                }
            }
        }
        saveProductInfo(list);
    }

    @Override
    public int excelToList(InputStream inputStream) throws InvalidFormatException, IOException {
        List<YxStoreProductDTO> gisList = new ArrayList<YxStoreProductDTO>();
        Workbook workbook = null;
        workbook = WorkbookFactory.create(inputStream);
        inputStream.close();
        //工作表对象
        Sheet sheet = workbook.getSheetAt(0);
        //判断当前商品信息属性是否一致
        getSameShuxing(sheet);
        return 1;
    }

    @Override
    public List<YxStoreProductDTO> findByIds(List<String> idList) {
        return yxStoreProductMapper.toDto(yxStoreProductRepository.findByIds(idList));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void autoOfflineProduct() {
        System.out.println("销量为0的商品自动下架自动任务开始执行");
        YxStoreProductQueryCriteria criteria=new YxStoreProductQueryCriteria();
        criteria.setIsShow(1);
        List<YxStoreProduct> productList=  yxStoreProductRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder));
        productList.stream().forEach(product->{
            List<YxStoreProductAttrValue> attrValueList=  yxStoreProductAttrValueRepository.findByProductId(product.getId());
            int totalStock=attrValueList.stream().collect(Collectors.summingInt(YxStoreProductAttrValue::getStock));
            if(totalStock==0){
                yxStoreProductRepository.updateOnsale(0,product.getId());
            }
        });
    }

    /**
     * 组合规则属性算法
     *
     * @param jsonStr
     * @return
     */
    public DetailDTO attrFormat(String jsonStr) {
        // List<Object> returnList = new ArrayList<>();

        JSONObject jsonObject = JSON.parseObject(jsonStr);
        List<FromatDetailDTO> fromatDetailDTOList = JSON.parseArray(jsonObject.get("items").toString(),
                FromatDetailDTO.class);
        List<String> data = new ArrayList<>();
        //List<Map<String,List<Map<String,String>>>> res =new ArrayList<>();
        List<Map<String, Map<String, String>>> res = new ArrayList<>();

        if (fromatDetailDTOList.size() > 1) {
            for (int i = 0; i < fromatDetailDTOList.size() - 1; i++) {
                if (i == 0) {
                    data = fromatDetailDTOList.get(i).getDetail();
                }
                List<String> tmp = new LinkedList<>();
                for (String v : data) {
                    for (String g : fromatDetailDTOList.get(i + 1).getDetail()) {
                        String rep2 = "";
                        if (i == 0) {
                            rep2 = fromatDetailDTOList.get(i).getValue() + "_" + v + "-"
                                    + fromatDetailDTOList.get(i + 1).getValue() + "_" + g;
                        } else {
                            rep2 = v + "-"
                                    + fromatDetailDTOList.get(i + 1).getValue() + "_" + g;
                        }

                        tmp.add(rep2);

                        if (i == fromatDetailDTOList.size() - 2) {
                            // Map<String,List<Map<String,String>>> rep4 = new LinkedHashMap<>();
                            Map<String, Map<String, String>> rep4 = new LinkedHashMap<>();
                            //List<Map<String,String>> listMap = new ArrayList<>();
                            //Map<String,String> map1 = new LinkedHashMap<>();
                            Map<String, String> reptemp = new LinkedHashMap<>();
                            for (String h : Arrays.asList(rep2.split("-"))) {
                                List<String> rep3 = Arrays.asList(h.split("_"));

                                if (rep3.size() > 1) {
                                    reptemp.put(rep3.get(0), rep3.get(1));
                                } else {
                                    reptemp.put(rep3.get(0), "");
                                }
                                //listMap.add(reptemp);

                            }
                            rep4.put("detail", reptemp);

                            //rep4.put("detail",listMap);

                            res.add(rep4);
                        }
                    }

                }

                //System.out.println("tmp:"+tmp);
                if (!tmp.isEmpty()) {
                    data = tmp;
                }
            }
        } else {
            List<String> dataArr = new ArrayList<>();

            for (FromatDetailDTO fromatDetailDTO : fromatDetailDTOList) {

                for (String str : fromatDetailDTO.getDetail()) {
                    Map<String, Map<String, String>> map2 = new LinkedHashMap<>();
                    //List<Map<String,String>> list1 = new ArrayList<>();
                    dataArr.add(fromatDetailDTO.getValue() + "_" + str);
                    Map<String, String> map1 = new LinkedHashMap<>();
                    map1.put(fromatDetailDTO.getValue(), str);
                    //list1.add(map1);
                    map2.put("detail", map1);
                    res.add(map2);
                }
            }

            String s = StrUtil.join("-", dataArr);
            data.add(s);
        }


        DetailDTO detailDTO = new DetailDTO();
        detailDTO.setData(data);
        detailDTO.setRes(res);


        return detailDTO;
    }

}
