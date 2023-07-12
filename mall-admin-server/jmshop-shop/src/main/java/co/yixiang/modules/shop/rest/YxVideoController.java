package co.yixiang.modules.shop.rest;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import co.yixiang.aop.log.Log;
import co.yixiang.modules.shop.domain.YxProductTemplate;
import co.yixiang.modules.shop.domain.YxVideo;
import co.yixiang.modules.shop.service.YxVideoProductService;
import co.yixiang.modules.shop.service.YxVideoService;
import co.yixiang.modules.shop.service.YxVideoTypeService;
import co.yixiang.modules.shop.service.dto.*;
import co.yixiang.utils.OrderUtil;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.*;
import java.io.IOException;
import java.util.List;
import javax.servlet.http.HttpServletResponse;

/**
* @author yushen
* @date 2023-06-08
*/
@Api(tags = "api管理")
@RestController
@RequestMapping("/api/yxVideo")
public class YxVideoController {

    private final YxVideoService yxVideoService;

    private final YxVideoTypeService yxVideoTypeService;

    private final YxVideoProductService yxVideoProductService;

    public YxVideoController(YxVideoService yxVideoService,YxVideoTypeService yxVideoTypeService,YxVideoProductService yxVideoProductService) {
        this.yxVideoService = yxVideoService;
        this.yxVideoTypeService = yxVideoTypeService;
        this.yxVideoProductService = yxVideoProductService;
    }

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('yxVideo:list')")
    public void download(HttpServletResponse response, YxVideoQueryCriteria criteria) throws IOException {
        yxVideoService.download(yxVideoService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询api")
    @ApiOperation("查询api")
    @PreAuthorize("@el.check('yxVideo:list')")
    public ResponseEntity<Object> getYxVideos(YxVideoQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(yxVideoService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增api")
    @ApiOperation("新增api")
    @PreAuthorize("@el.check('yxVideo:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody YxVideo resources){
        YxVideoTypeDto type= yxVideoTypeService.findById(resources.getTypeId());
        resources.setTypeName(type.getTypeName());
        resources.setCreateTime(OrderUtil.dateToTimestamp(DateUtil.date()));
        return new ResponseEntity<>(yxVideoService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改api")
    @ApiOperation("修改api")
    @PreAuthorize("@el.check('yxVideo:edit')")
    public ResponseEntity<Object> update(@Validated @RequestBody YxVideo resources){
        YxVideoTypeDto type= yxVideoTypeService.findById(resources.getTypeId());
        resources.setTypeName(type.getTypeName());
        yxVideoService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除api")
    @ApiOperation("删除api")
    @PreAuthorize("@el.check('yxVideo:del')")
    @DeleteMapping
    public ResponseEntity<Object> deleteAll(@RequestBody Integer[] ids) {
        yxVideoService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(value = "/addVideoItems")
    @ApiOperation(value = "模板增加商品",notes = "模板增加商品")
    public ResponseEntity<Object> addVideoItems(@RequestBody YxVideo yxVideo){
        if(ObjectUtil.isNull(yxVideo.getId())){
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        if(ObjectUtil.isNull(yxVideo.getItemList())){
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        yxVideo.getItemList().stream().forEach(item->{
            item.setVideoId(yxVideo.getId().intValue());
            Long time=System.currentTimeMillis()/1000;
            item.setAddTime(time.intValue());
            YxVideoProductQueryCriteria param=new YxVideoProductQueryCriteria();
            param.setVideoId(yxVideo.getId());
            param.setProductId(item.getProductId());
            List<YxVideoProductDto> dtoList=yxVideoProductService.queryAll(param);
            if(dtoList.size()==0) {
                yxVideoProductService.create(item);
            }
        });

        return ResponseEntity.ok("ok");
    }

    @PostMapping(value = "/editVideoItems")
    @ApiOperation(value = "编辑增加商品",notes = "模板编辑商品")
    public ResponseEntity<Object> editVideoItems(@RequestBody YxVideo yxVideo){
        if(ObjectUtil.isNull(yxVideo.getItemList())){
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        yxVideo.getItemList().stream().forEach(item->{
            yxVideoProductService.update(item);
        });

        return ResponseEntity.ok("ok");
    }
}
