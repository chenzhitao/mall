package co.yixiang.mp.controller;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import co.yixiang.exception.BadRequestException;
import co.yixiang.mp.domain.YxWechatReply;
import co.yixiang.mp.service.YxWechatReplyService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
* @author hupeng
* @date 2019-10-10
*/
@Api(tags = "商城:微信回復管理")
@RestController
@RequestMapping("api")
public class WechatReplyController {

    private final YxWechatReplyService yxWechatReplyService;

    public WechatReplyController(YxWechatReplyService yxWechatReplyService) {
        this.yxWechatReplyService = yxWechatReplyService;
    }

    @ApiOperation(value = "查询")
    @GetMapping(value = "/yxWechatReply")
    @PreAuthorize("@el.check('admin','YXWECHATREPLY_ALL','YXWECHATREPLY_SELECT')")
    public ResponseEntity getYxWechatReplys(){
        return new ResponseEntity(yxWechatReplyService.isExist("subscribe"),HttpStatus.OK);
    }


    @ApiOperation(value = "新增自动回复")
    @PostMapping(value = "/yxWechatReply")
    @PreAuthorize("@el.check('admin','YXWECHATREPLY_ALL','YXWECHATREPLY_CREATE')")
    public ResponseEntity create(@RequestBody String jsonStr){
        //if(StrUtil.isNotEmpty("22")) throw new BadRequestException("演示环境禁止操作");
        JSONObject jsonObject = JSON.parseObject(jsonStr);
        YxWechatReply yxWechatReply = new YxWechatReply();
        YxWechatReply isExist = yxWechatReplyService.isExist(jsonObject.get("key").toString());
        yxWechatReply.setKey(jsonObject.get("key").toString());
        yxWechatReply.setStatus(Integer.valueOf(jsonObject.get("status").toString()));
        yxWechatReply.setData(jsonObject.get("data").toString());
        yxWechatReply.setType(jsonObject.get("type").toString());
        if(ObjectUtil.isNull(isExist)){
            yxWechatReplyService.create(yxWechatReply);
        }else{
            yxWechatReply.setId(isExist.getId());
            yxWechatReplyService.update(yxWechatReply);
        }

        return new ResponseEntity(HttpStatus.CREATED);
    }





}