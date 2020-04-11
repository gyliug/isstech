package com.entfrm.biz.devtool.controller;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.io.IoUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.entfrm.biz.devtool.entity.Table;
import com.entfrm.biz.devtool.service.TableService;
import com.entfrm.core.base.api.R;
import com.entfrm.core.base.util.StrUtil;
import com.entfrm.core.data.datasource.DSContextHolder;
import com.entfrm.core.data.enums.DataTypeEnum;
import com.entfrm.core.data.util.AliasUtil;
import com.entfrm.core.log.annotation.OperLog;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author yong
 * @date 2020/3/21
 * @description 数据表管理
 */
@RestController
@RequestMapping("/devtool/datatable")
@AllArgsConstructor
public class DatatableController {

    private final JdbcTemplate jdbcTemplate;
    private final TableService tableService;

    /**
     * **
     * 根据数据名称获取所有表名
     *
     * @param alias
     * @return
     */
    @GetMapping("/list")
    @ResponseBody
    public R list(@RequestParam String alias, @RequestParam(required = false) String tableName) {
        DSContextHolder.setDSType(AliasUtil.getDsId(alias));
        StringBuilder sql = new StringBuilder();
        if (DataTypeEnum.MYSQL.getType().equals(AliasUtil.getDsType(alias))) {
            sql.append("select table_name tableName, table_comment tableComment, create_time createTime from information_schema.tables where table_schema=?")
                    .append(" and table_type='base table'");
            if (StrUtil.isNotBlank(tableName)) {
                sql.append(" and table_name like '%" + tableName + "%'");
            }
        }

        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql.toString(), AliasUtil.getDsName(alias));
        list.stream().forEach(map -> {
            Table table = tableService.getOne(new QueryWrapper<Table>().eq("table_name", map.get("tableName")));
            if (table != null) {
                map.put("isConfig", "1");
            } else {
                map.put("isConfig", "0");
            }
        });
        return R.ok(list);
    }

    /**
     * 代码生成 配置
     */
    @PostMapping("/getGenTable")
    @ResponseBody
    public R getGenTable(@RequestBody Table table) {
        return R.ok(tableService.getGenTable(table.getTableName(), table.getTableComment()));
    }

    @PutMapping("/update")
    @ResponseBody
    public R update(@RequestBody Table table) {
        tableService.validateEdit(table);
        tableService.updateTable(table);
        return R.ok();
    }

    /**
     * 预览代码
     */
    @GetMapping("/preview/{tableId}")
    @ResponseBody
    public R preview(@PathVariable("tableId") Integer tableId) {
        Map<String, String> dataMap = tableService.previewCode(tableId);
        return R.ok(dataMap);
    }

    /**
     * 批量生成代码
     */
    @GetMapping("/batchGenCode")
    @ResponseBody
    public void batchGenCode(HttpServletResponse response, String tables) throws IOException {
        String[] tableNames = Convert.toStrArray(tables);
        byte[] data = tableService.generatorCode(tableNames);
        genCode(response, data);
    }

    /**
     * 生成zip文件
     */
    private void genCode(HttpServletResponse response, byte[] data) throws IOException {
        response.reset();
        response.setHeader("Content-Disposition", "attachment; filename=\"entfrm.zip\"");
        response.addHeader("Content-Length", "" + data.length);
        response.setContentType("application/octet-stream; charset=UTF-8");
        IoUtil.write(response.getOutputStream(),true,data);
    }

    @OperLog("表删除")
    @PreAuthorize("@ps.hasPerm('datatable_del')")
    @GetMapping("/remove")
    @ResponseBody
    @Transactional
    public R remove(@RequestParam String alias, @RequestParam String tableName) {
        Table table = tableService.getOne(new QueryWrapper<Table>().eq("table_name", tableName));
        if(table != null){
            //删除表信息
            tableService.removeById(table.getId());
            //删除表列信息
            tableService.deleteTableByIds(table.getId() + "");
        }
        //删除数据库表
        DSContextHolder.setDSType(AliasUtil.getDsId(alias));
        StringBuilder sql = new StringBuilder();
        if (DataTypeEnum.MYSQL.getType().equals(AliasUtil.getDsType(alias))) {
            sql.append("drop table ").append(tableName);
        }

        return R.ok();
    }

}
