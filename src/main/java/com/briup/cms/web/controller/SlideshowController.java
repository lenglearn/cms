package com.briup.cms.web.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.briup.cms.bean.basic.Slideshow;
import com.briup.cms.service.ISlideshowService;
import com.briup.cms.util.Result;
import com.briup.cms.util.ResultUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author leng
 * @since 2024-09-13
 */
@Api(tags = "轮播图模块")
@Slf4j
@RestController //  @ResponseBody + @Controller
@RequestMapping("/slideshow")
public class SlideshowController {
    @Autowired
    private ISlideshowService slideshowService;

    //user端使用
    @ApiOperation(value = "查询所有可用的轮播图")
    @GetMapping("/queryAllEnable")
    public Result queryAllEnable() {
        List<Slideshow> list = slideshowService.queryAllEnable();
        return ResultUtil.success(list);
    }

    //admin使用
    //第几页 每页数量 status 描述
    @ApiOperation(value = "条件+分页查询轮播图")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "当前页", dataType = "int", required = true, defaultValue = "1", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "每页数量", dataType = "int", required = true, defaultValue = "4", paramType = "query"),
            @ApiImplicitParam(name = "status", value = "状态值", paramType = "query"),
            @ApiImplicitParam(name = "desc", value = "描述信息", paramType = "query")
    })
    @GetMapping("/query")
    public Result query(Integer pageNum, Integer pageSize, String status, String desc) {
        IPage<Slideshow> p = slideshowService.query(pageNum, pageSize, status, desc);
        return ResultUtil.success(p);
    }

    //admin使用
    @ApiOperation(value = "根据id查询轮播图信息",notes = "用于更新时的数据回显")
    @GetMapping("/queryById/{id}")
    public Result queryById(@PathVariable("id") Integer id) {
        return ResultUtil.success(slideshowService.queryById(id));
    }

    //admin使用
    @ApiOperation(value = "新增或更新轮播图", notes = "slideshow参数包含id值则为更新，不包含i为新增")
    @PostMapping("/saveOrUpdate")
    public Result saveOrUpdate(@RequestBody Slideshow slideshow) {
        slideshowService.saveOrUpdate(slideshow);
        return ResultUtil.success("操作成功");
    }

    //admin使用
    @ApiOperation(value = "批量删除轮播图", notes = "需要提供多个id值")
    @DeleteMapping("/deleteByBatch/{ids}")
    public Result deleteSlideshowInBatch(@PathVariable("ids") List<Integer> ids) {
        log.info("ids: {}",ids);
        slideshowService.deleteInBatch(ids);
        return ResultUtil.success("删除成功");
    }
}
