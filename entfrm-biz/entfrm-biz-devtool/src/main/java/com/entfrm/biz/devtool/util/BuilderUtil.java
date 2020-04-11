package com.entfrm.biz.devtool.util;

import com.entfrm.biz.devtool.entity.Column;
import com.entfrm.biz.devtool.entity.Table;
import com.entfrm.core.base.util.StrUtil;

/**
 * @author entfrm
 * @date 2020/4/11
 * @description sql 脚本构成器
 */
public class BuilderUtil {

    //创建表
    public static String createTable(Table table) {
        StringBuilder sb = new StringBuilder();
        sb.append("CREATE TABLE `" + table.getTableName() + "`  (");
        if (table.getColumns() != null && table.getColumns().size() > 0) {
            for (int i = 0; i < table.getColumns().size(); i++) {
                Column column = table.getColumns().get(i);
                if (i == table.getColumns().size() - 1) {
                    sb.append(fieldCreateScript(column));
                } else {
                    sb.append(fieldCreateScript(column) + ",");
                }
            }
        }
        sb.append(")ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci" + (StrUtil.isNotEmpty(table.getTableComment()) ? " COMMENT '" + table.getTableComment() + "' " : " ") + "ROW_FORMAT = Dynamic;");
        return sb.toString();
    }

    //字段信息转sql脚本(创建)
    private static String fieldCreateScript(Column column) {
        String script = column.getColumnName() + " " + column.getColumnType() + " " + (("1").equals(column.getIsRequired()) ? "NULL" : "NOT NULL");
        script += (StrUtil.isNotEmpty(column.getColumnComment()) ? " COMMENT '" + column.getColumnComment() + "'" : " ");
        script += (StrUtil.isNotEmpty(column.getDefValue()) ? " DEFAULT " + column.getDefValue() : " ");
        if ("id".equalsIgnoreCase(column.getColumnName()) && "1".equals(column.getIsPk())) {
            script += " primary key AUTO_INCREMENT";
        }
        return script;
    }

    //更新表
    public static String updateTable(Table table) {
        StringBuilder sb = new StringBuilder();
        if(StrUtil.isNotEmpty(table.getTableName())){
            sb.append("alter table " + table.getTableName() + " comment '" + table.getTableName() + "';");
        }
        if (table.getColumns() != null && table.getColumns().size() > 0) {
            for (Column column : table.getColumns()) {
                if (StrUtil.isBlankIfStr(column.getId())) {
                    sb.append("alter table " + table.getTableName() + " add " + column.getColumnName() + " " + column.getColumnType()
                            + (StrUtil.isNotEmpty(column.getDefValue()) ? " default '" + column.getDefValue() + "' " : " ") + (StrUtil.isNotEmpty(column.getColumnComment()) ? "comment '" + column.getColumnComment() + "';" : ";"));
                } else {
                    sb.append("alter table " + table.getTableName() + " modify " + column.getColumnName() + " " + column.getColumnType()
                            + (StrUtil.isNotEmpty(column.getDefValue()) ? " default '" + column.getDefValue() + "' " : " ") + (StrUtil.isNotEmpty(column.getColumnComment()) ? "comment '" + column.getColumnComment() + "';" : ";"));
                }
            }
        }
        return sb.toString();
    }

}
